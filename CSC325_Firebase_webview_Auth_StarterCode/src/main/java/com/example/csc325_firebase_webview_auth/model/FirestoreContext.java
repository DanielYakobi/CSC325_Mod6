package com.example.csc325_firebase_webview_auth.model;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;

public class FirestoreContext {

    public Firestore firebase() throws IOException {

        FileInputStream serviceAccount =
                new FileInputStream("src/main/resources/files/key.json");

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setProjectId("csc3251234")
                .setStorageBucket("csc3251234.firebasestorage.app")
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
            System.out.println("Firebase is initialized");
        }

        return FirestoreClient.getFirestore();
    }
}