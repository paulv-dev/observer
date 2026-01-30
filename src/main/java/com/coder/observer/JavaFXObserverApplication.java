package com.coder.observer;

import com.coder.observer.view.MainView;
import com.coder.observer.viewModel.MainViewModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;

public class JavaFXObserverApplication extends Application {

    //TODO Future features:
    //  File filtering by:
    //  name (pattern)
    //  extension
    //  is read-only
    //  is hidden
    //  date (before, in range, after)
    //  time (before, in range, after)
    //  -----
    //  Save selected file
    //  Copy file to clipboard
    //  Open/run selected file
    //  -----
    //  Favorite paths / constant path list to search
    //  -----
    //  Languages support

    public static void start(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            MainViewModel mainViewModel = ApplicationBootstrap.createMainViewModel();

            MainView mainView = new MainView(mainViewModel, this);
            Scene mainScene = new Scene(mainView);

            stage.setOnCloseRequest(event -> {
                Platform.exit();
                System.exit(0);
            });
            stage.minHeightProperty().set(700);
            stage.minWidthProperty().set(1200);
            stage.setTitle("Observer");
            stage.setScene(mainScene);
            String iconPath = "/icons/eye.png";
            try (InputStream inputStream = this.getClass().getResourceAsStream(iconPath)) {
                if (inputStream != null) {
                    stage.getIcons().add(new Image(inputStream));
                } else {
                    System.err.printf("%s not found\n", iconPath);
                }
            }
            stage.show();
        } catch (Exception ex) {
            System.out.println("ERROR");
            ex.printStackTrace();
        }

    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.exit(0);
    }
}