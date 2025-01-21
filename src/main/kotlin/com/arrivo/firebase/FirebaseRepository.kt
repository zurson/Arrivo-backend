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

    fun findUserById(uid: String): UserRecord {
        return FirebaseAuth.getInstance().getUser(uid) ?: throw IllegalArgumentException(ERROR_ID_NOT_FOUND_MESSAGE)
    }


    fun createFirebaseUser(email: String): String {
        val password = generateRandomPassword(Random().nextInt(20, 40))

        val userRecord = UserRecord.CreateRequest()
            .setEmail(email)
            .setPassword(password)

        val createdUser = FirebaseAuth.getInstance().createUser(userRecord)
        return createdUser.uid
    }


    fun changeUserEmail(uid: String, email: String): String {
        val userRecord = findUserById(uid)
        val currentEmail = userRecord.email ?: throw IllegalStateException(ERROR_NO_EMAIL_ASSOCIATED)
        val request = UserRecord.UpdateRequest(uid).setEmail(email)
        FirebaseAuth.getInstance().updateUser(request)

        return currentEmail
    }


    fun blockUserAccount(uid: String) {
        val userRecord = findUserById(uid)
        val updateRequest = userRecord.updateRequest().setDisabled(true)

        val auth = FirebaseAuth.getInstance()

        auth.updateUser(updateRequest)
        revokeRefreshTokens(uid)
    }


    fun unlockUserAccount(uid: String) {
        val userRecord = findUserById(uid)
        val updateRequest = userRecord.updateRequest().setDisabled(false)

        val auth = FirebaseAuth.getInstance()

        auth.updateUser(updateRequest)
    }


    fun revokeRefreshTokens(uid: String) {
        FirebaseAuth.getInstance().revokeRefreshTokens(uid)
    }

}