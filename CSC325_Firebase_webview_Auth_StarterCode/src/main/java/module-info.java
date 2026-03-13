module com.example.csc325_firebase_webview_auth {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.web;
    requires jdk.jsobject;
    requires java.xml;
    requires java.logging;
    requires java.net.http;

    requires com.google.auth.oauth2;
    requires com.google.auth;
    requires com.google.api.apicommon;
    requires google.cloud.core;
    requires google.cloud.firestore;
    requires google.cloud.storage;
    requires firebase.admin;

    opens com.example.csc325_firebase_webview_auth.view to javafx.fxml;
    opens com.example.csc325_firebase_webview_auth.model to javafx.base;
    opens com.example.csc325_firebase_webview_auth.viewmodel to javafx.base;

    exports com.example.csc325_firebase_webview_auth.view;
    exports com.example.csc325_firebase_webview_auth.model;
    exports com.example.csc325_firebase_webview_auth.viewmodel;
}