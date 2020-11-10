package com.hy.iot.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    private static Dotenv env = Dotenv.configure().ignoreIfMissing().load();

    public FirebaseConfig() {
        try {
            val serviceAccount = new FileInputStream(env.get("GOOGLE_APPLICATION_CREDENTIALS"));
            val options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(env.get("GOOGLE_FIREBASE_DATABASE_URL"))
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Bean
    public FirebaseMessaging messaging(){
        return FirebaseMessaging.getInstance();
    }
}

