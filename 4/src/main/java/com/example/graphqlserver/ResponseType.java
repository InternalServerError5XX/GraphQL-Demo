package com.example.graphqlserver;

public class ResponseType<T> {

    private String status;
    private String message;
    private T data;

    // Конструктор для успіху
    public ResponseType(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // Конструктор для помилки
    public ResponseType(String status, String message) {
        this.status = status;
        this.message = message;
        this.data = null;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

