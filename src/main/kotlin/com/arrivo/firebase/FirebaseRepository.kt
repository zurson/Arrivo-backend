package com.arrivo.firebase

import com.arrivo.utilities.Settings.Companion.ERROR_ID_NOT_FOUND_MESSAGE
import com.arrivo.utilities.Settings.Companion.ERROR_NO_EMAIL_ASSOCIATED
import com.arrivo.utilities.generateRandomPassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserRecord
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class FirebaseRepository {

    fun createFirebaseUser(email: String): String {
        val password = generateRandomPassword(Random().nextInt(20, 40))

        val userRecord = UserRecord.CreateRequest()
            .setEmail(email)
            .setPassword(password)

        val createdUser = FirebaseAuth.getInstance().createUser(userRecord)
        return createdUser.uid
    }

    fun changeUserEmail(uid: String, email: String): String {
        val userRecord =
            FirebaseAuth.getInstance().getUser(uid) ?: throw IllegalArgumentException(ERROR_ID_NOT_FOUND_MESSAGE)
        val currentEmail = userRecord.email ?: throw IllegalStateException(ERROR_NO_EMAIL_ASSOCIATED)
        val request = UserRecord.UpdateRequest(uid).setEmail(email)
        FirebaseAuth.getInstance().updateUser(request)

        return currentEmail
    }

}