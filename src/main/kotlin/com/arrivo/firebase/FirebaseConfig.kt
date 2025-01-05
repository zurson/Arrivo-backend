package com.arrivo.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn


@Configuration
class FirebaseConfig {

    @Bean
    fun firebaseInit(): FirebaseApp {
        return if (FirebaseApp.getApps().isEmpty()) {
            val serviceAccount = javaClass.getResourceAsStream("/serviceAccountKey.json")

            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build()

            FirebaseApp.initializeApp(options)
        } else {
            FirebaseApp.getInstance()
        }
    }

    @Bean
    @DependsOn(value = ["firebaseInit"])
    fun createFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
}