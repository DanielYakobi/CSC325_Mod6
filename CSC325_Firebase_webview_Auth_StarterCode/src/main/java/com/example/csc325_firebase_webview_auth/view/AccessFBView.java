package com.example.csc325_firebase_webview_auth.view;

import com.example.csc325_firebase_webview_auth.model.Person;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.StorageClient;
import com.google.cloud.storage.Bucket;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AccessFBView implements Initializable {

    @FXML private TextField nameField;
    @FXML private TextField majorField;
    @FXML private TextField ageField;

    @FXML private TextField emailField;
    @FXML private TextField passwordField;

    @FXML private TextArea outputField;

    @FXML private TableView<Person> tableView;
    @FXML private TableColumn<Person, String> nameColumn;
    @FXML private TableColumn<Person, String> majorColumn;
    @FXML private TableColumn<Person, Integer> ageColumn;

    @FXML private ImageView profileImageView;

    private final ObservableList<Person> peopleList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        majorColumn.setCellValueFactory(new PropertyValueFactory<>("major"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

        tableView.setItems(peopleList);
        outputField.setText("Ready.");
    }

    @FXML
    private void addRecord(ActionEvent event) {
        String name = nameField.getText().trim();
        String major = majorField.getText().trim();
        String ageText = ageField.getText().trim();

        if (name.isEmpty() || major.isEmpty() || ageText.isEmpty()) {
            outputField.setText("Please fill in name, major, and age.");
            return;
        }

        try {
            int age = Integer.parseInt(ageText);

            Person person = new Person(name, major, age);
            peopleList.add(person);

            Map<String, Object> data = new HashMap<>();
            data.put("name", name);
            data.put("major", major);
            data.put("age", age);

            CollectionReference students = App.fstore.collection("students");
            students.add(data);

            outputField.setText("Student written to Firestore and added to table.");
            clearStudentFields();

        } catch (NumberFormatException ex) {
            outputField.setText("Age must be a number.");
        } catch (Exception ex) {
            outputField.setText("Write failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void readRecord(ActionEvent event) {
        try {
            peopleList.clear();

            ApiFuture<QuerySnapshot> future = App.fstore.collection("students").get();
            QuerySnapshot querySnapshot = future.get();

            for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                String name = doc.getString("name");
                String major = doc.getString("major");
                Long ageLong = doc.getLong("age");

                int age = ageLong != null ? ageLong.intValue() : 0;
                peopleList.add(new Person(name, major, age));
            }

            outputField.setText("Loaded " + peopleList.size() + " student record(s) from Firestore.");

        } catch (Exception ex) {
            outputField.setText("Read failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void uploadProfilePicture(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose Profile Picture");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File file = chooser.showOpenDialog(profileImageView.getScene().getWindow());
        if (file == null) {
            outputField.setText("Upload cancelled.");
            return;
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            String fileName = "profile-pictures/" + System.currentTimeMillis() + "_" + file.getName();

            Bucket bucket = StorageClient.getInstance().bucket();
            bucket.create(fileName, fis, Files.probeContentType(file.toPath()));

            profileImageView.setImage(new Image(file.toURI().toString()));
            outputField.setText("Profile picture uploaded to Firebase Storage.");

        } catch (Exception ex) {
            outputField.setText("Upload failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void clearForm(ActionEvent event) {
        nameField.clear();
        majorField.clear();
        ageField.clear();

        if (emailField != null) emailField.clear();
        if (passwordField != null) passwordField.clear();

        outputField.setText("");
    }

    @FXML
    private void closeApp(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void showAbout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Student Firebase App");
        alert.setContentText("JavaFX + Firebase Auth + Firestore + Storage");
        alert.showAndWait();
    }

    @FXML
    private void switchToSecondary(ActionEvent event) {
        try {
            App.setRoot("/files/WebContainer.fxml");
        } catch (IOException ex) {
            outputField.setText("Could not open web view.");
            ex.printStackTrace();
        }
    }

    @FXML
    private void regRecord(ActionEvent event) {
        outputField.setText("Already logged in.");
    }

    @FXML
    private void signInRecord(ActionEvent event) {
        outputField.setText("Already logged in.");
    }

    private void clearStudentFields() {
        nameField.clear();
        majorField.clear();
        ageField.clear();
    }
}