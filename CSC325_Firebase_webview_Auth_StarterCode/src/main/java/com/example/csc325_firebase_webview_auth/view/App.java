package com.example.csc325_firebase_webview_auth.view;

import com.example.csc325_firebase_webview_auth.model.FirestoreContext;
import com.google.cloud.firestore.Firestore;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class App extends Application {

    public static Firestore fstore;
    public static Scene scene;

    private final FirestoreContext contextFirebase = new FirestoreContext();

    @Override
    public void start(Stage stage) throws Exception {
        fstore = contextFirebase.firebase();

        scene = new Scene(loadFXML("/files/SplashView.fxml"), 1000, 650);
        scene.getStylesheets().add(
                getClass().getResource("/addColor.css").toExternalForm()
        );

        stage.setTitle("Student Firebase App");
        stage.setScene(scene);
        stage.show();

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> {
            try {
                setRoot("/files/LoginView.fxml");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        pause.play();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml));
        return loader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}