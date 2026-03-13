package com.example.csc325_firebase_webview_auth.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class LoginController {

    private static final String FIREBASE_WEB_API_KEY = "AIzaSyCVt8N6X0Vj4HxSZSFTi0sxxXPZ28qwPLA";

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    private final HttpClient client = HttpClient.newHttpClient();

    @FXML
    private void handleRegister(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Enter email and password.");
            return;
        }

        try {
            String endpoint = "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + FIREBASE_WEB_API_KEY;
            String json = "{\"email\":\"" + escape(email) + "\",\"password\":\"" + escape(password) + "\",\"returnSecureToken\":true}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                messageLabel.setText("Registration successful. Opening app...");
                App.setRoot("/files/AccessFBView.fxml");
            } else {
                messageLabel.setText("Registration failed: " + extractFirebaseError(response.body()));
            }

        } catch (Exception ex) {
            messageLabel.setText("Registration failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleSignIn(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Enter email and password.");
            return;
        }

        try {
            String endpoint = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + FIREBASE_WEB_API_KEY;
            String json = "{\"email\":\"" + escape(email) + "\",\"password\":\"" + escape(password) + "\",\"returnSecureToken\":true}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                messageLabel.setText("Sign in successful. Opening app...");
                App.setRoot("/files/AccessFBView.fxml");
            } else {
                messageLabel.setText("Sign in failed: " + extractFirebaseError(response.body()));
            }

        } catch (Exception ex) {
            messageLabel.setText("Sign in failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleGuest(ActionEvent event) {
        try {
            App.setRoot("/files/AccessFBView.fxml");
        } catch (IOException ex) {
            messageLabel.setText("Could not open main screen.");
            ex.printStackTrace();
        }
    }

    private String escape(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String extractFirebaseError(String responseBody) {
        int index = responseBody.indexOf("\"message\"");
        if (index == -1) {
            return "Unknown error";
        }

        int colon = responseBody.indexOf(":", index);
        int firstQuote = responseBody.indexOf("\"", colon + 1);
        int secondQuote = responseBody.indexOf("\"", firstQuote + 1);

        if (firstQuote != -1 && secondQuote != -1) {
            return responseBody.substring(firstQuote + 1, secondQuote);
        }

        return "Unknown error";
    }

}