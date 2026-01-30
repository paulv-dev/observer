package com.coder.observer.viewModel.dto;

public class ViewModelEventDTO {

    private final String header;
    private final String title;
    private final String content;

    public ViewModelEventDTO(String header, String title, String content) {
        this.header = header;
        this.title = title;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getHeader() {
        return header;
    }


    String getTitle() {
        return this.title;
    }
}
