package com.coder.observer.view;

import com.coder.observer.util.FileTimeFormatter;
import com.coder.observer.viewModel.MainViewModel;
import com.coder.observer.viewModel.dto.FileDTO;
import com.coder.observer.viewModel.events.ViewModelEvent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.Objects;

public class MainView extends VBox {
    private final MainViewModel viewModel;
    private final Application application;
    private final ObservableList<FileDTO> tableData;
    private final SortedList<FileDTO> sortedTableData;
    private TableView<FileDTO> table;
    private Button searchButton;
    private Button quitButton;
    private ScrollPane tablePane;
    private HBox controlsPane;
    private DirectoryChooser directoryChooser;
    private Button selectDirButton;
    private TextField searchPathTextField;
    private HBox controlsButtonsPane;
    private HBox controlsPathPane;
    private Label searchPathLabel;
    private HBox statusPane;
    private Label statusLabel;
    private DatePicker datePicker;
    private ImageView loadingImagePlaceholder;
    private Button cancelButton;
    private ContextMenu pathContextMenu;
    private MenuItem openInExplorerContextMenuItem;
    private MenuItem copyPathContextMenuItem;
    private String searchTaskId;
    private Alert alert;

    public MainView(MainViewModel viewModel, Application application) {
        this.viewModel = viewModel;
        this.application = application;
        tableData = FXCollections.observableArrayList();
        sortedTableData = new SortedList<>(tableData);
        createGUI();
        init();
    }

    void init() {
        datePicker.setValue(LocalDate.now());
        searchPathTextField.setText(System.getProperty("user.home"));
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    }

    private void createGUI() {
        createLayout();
        createTable();
        createControls();
        createCellContextMenu();
        addNodesToLayout();
        setBindings();
    }

    private void createLayout() {
        tablePane = new ScrollPane();
        tablePane.setFitToWidth(true);
        tablePane.setFitToHeight(true);
        controlsPane = new HBox();
        controlsPane.setSpacing(5);
        controlsPane.setSpacing(5);
        controlsButtonsPane = new HBox();
        controlsButtonsPane.setSpacing(5);
        controlsButtonsPane.prefWidthProperty().bind(this.controlsPane.widthProperty().multiply(0.2));
        controlsButtonsPane.setAlignment(Pos.CENTER_RIGHT);
        controlsPathPane = new HBox();
        controlsPathPane.prefWidthProperty().bind(this.controlsPane.widthProperty().multiply(0.8));
        controlsPathPane.setAlignment(Pos.CENTER_LEFT);
        controlsPathPane.setSpacing(5);
        statusPane = new HBox();
        this.setPadding(new Insets(5, 5, 5, 5));
        this.setSpacing(5);
    }

    private void createTable() {
        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.prefHeightProperty().bind(super.heightProperty());

        TableColumn<FileDTO, LocalDateTime> modifiedColumn = new TableColumn<>("Last modified time");
        modifiedColumn.prefWidthProperty().bind(this.table.widthProperty().multiply(0.12));
        modifiedColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFileTime()));
        modifiedColumn.setComparator(Comparator.comparing((ldt) -> ldt.toInstant(ZoneOffset.UTC)));

        modifiedColumn.setCellFactory((_) -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(FileTimeFormatter.format(item));
                }
            }
        });
        modifiedColumn.setSortType(TableColumn.SortType.DESCENDING);
        table.getSortOrder().add(modifiedColumn);


        TableColumn<FileDTO, Path> pathColumn = new TableColumn<>("Relative path");
        pathColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.88));
        pathColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPath()));
        pathColumn.setCellFactory((_) -> {
            TableCell<FileDTO, Path> tableCell = new TableCell<>() {
                @Override
                protected void updateItem(Path path, boolean empty) {
                    super.updateItem(path, empty);
                    if (empty || path == null) {
                        setText(null);
                    } else {
                        try {
                            Path relPath = Paths.get(searchPathTextField.getText()).relativize(path);
                            setText(relPath.toString());
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            tableCell.setOnMouseClicked((event) -> {
                if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                    Path filePath = table.getSelectionModel().getSelectedItem().getPath();
                    if (filePath != null) {
                        viewModel.openInOSFileManager(filePath);
                    }
                }
            });
            tableCell.setContextMenu(pathContextMenu);
            return tableCell;
        });
        table.getColumns().add(modifiedColumn);
        table.getColumns().add(pathColumn);

        loadingImagePlaceholder = new ImageView();
        loadingImagePlaceholder.setImage(new Image(Objects.requireNonNull(getClass().getResource("/images/loading" +
                ".gif")).toExternalForm()));
        table.setPlaceholder(loadingImagePlaceholder);
        loadingImagePlaceholder.setFitWidth(50);
        loadingImagePlaceholder.setPreserveRatio(true);
        loadingImagePlaceholder.setVisible(false);
    }

    private void createControls() {
        searchPathLabel = new Label();
        statusLabel = new Label();
        searchPathTextField = new TextField();
        searchPathTextField.prefWidthProperty().bind(controlsPathPane.widthProperty().multiply(0.5));
        searchPathTextField.setEditable(false);
        searchPathLabel.setText("Current directory:");
        directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select directory to search");
        selectDirButton = new Button("Select directory");
        searchButton = new Button("Search");
        quitButton = new Button("Quit");
        datePicker = new DatePicker();
        datePicker.setEditable(false);
        datePicker.prefWidthProperty().set(100);
        cancelButton = new Button("Cancel");
        alert = new Alert(Alert.AlertType.NONE);
    }

    private void createCellContextMenu() {
        pathContextMenu = new ContextMenu();
        openInExplorerContextMenuItem = new MenuItem("Open in explorer");
        copyPathContextMenuItem = new MenuItem("Copy path");
        pathContextMenu.getItems().addAll(openInExplorerContextMenuItem, copyPathContextMenuItem);
    }

    private void addNodesToLayout() {
        getChildren().addAll(tablePane, controlsPane, statusPane);
        controlsPane.getChildren().addAll(controlsPathPane, controlsButtonsPane);
        statusPane.getChildren().addAll(statusLabel);
        controlsButtonsPane.getChildren().addAll(searchButton, cancelButton, quitButton);
        tablePane.setContent(table);
        controlsPathPane.getChildren().addAll(searchPathLabel, searchPathTextField, selectDirButton, datePicker);
    }

    private void setBindings() {
        viewModel.getSearchFilesResultProperty().addListener(
                (_,
                 _,
                 newVal) -> Platform.runLater(() -> {
                            tableData.setAll(newVal);
                            table.setItems(sortedTableData);
                        }
                ));

        viewModel.getSearchFilesIsRunningProperty().addListener(
                (_,
                 _,
                 newVal) -> Platform.runLater(() -> {
                    searchButton.setDisable(newVal);
                    datePicker.setDisable(newVal);
                    selectDirButton.setDisable(newVal);
                    loadingImagePlaceholder.setVisible(newVal);
                    cancelButton.setDisable(!newVal);
                }));

        viewModel.getStatusProperty().addListener(
                (_,
                 _,
                 newWal) ->
                        Platform.runLater(() -> statusLabel.setText(newWal)));

        viewModel.getMessageDispatcher().addListener(ViewModelEvent.Type.ERROR, (ev) -> {
            Platform.runLater(() -> {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("");
                alert.setContentText(ev.getPayload());
                alert.show();
            });
        });

        viewModel.getMessageDispatcher().addListener(ViewModelEvent.Type.INFO, (ev) -> {
            Platform.runLater(() -> {
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setTitle("Info");
                alert.setHeaderText("");
                alert.setContentText(ev.getPayload());
                alert.show();
            });
        });

        sortedTableData.comparatorProperty().bind(table.comparatorProperty());

        selectDirButton.setOnAction(event -> {
            File selectedDirectory = directoryChooser.showDialog(getScene().getWindow());
            if (selectedDirectory != null) searchPathTextField.setText(selectedDirectory.toString());
        });

        searchButton.setOnAction(event -> {
            searchTaskId = viewModel.searchFiles(datePicker.getValue(), Paths.get(searchPathTextField.getText()),
                    "",
                    "", 0, Integer.MAX_VALUE);
        });

        cancelButton.setOnAction(event -> {
            viewModel.cancelTask(searchTaskId);
        });

        quitButton.setOnAction(event -> {
            try {
                viewModel.quitApplication();
                application.stop();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        openInExplorerContextMenuItem.setOnAction((event) -> {
            FileDTO item = table.getSelectionModel().getSelectedItem();
            viewModel.openInOSFileManager(item.getPath());
        });

        copyPathContextMenuItem.setOnAction((event) -> {
            FileDTO item = table.getSelectionModel().getSelectedItem();
            viewModel.copyToClipboard(item.getPath().toString());
        });
    }

}