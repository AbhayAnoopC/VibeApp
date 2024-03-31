package com.chatapp.vibeapp.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(private val auth: FirebaseAuth,
                     private val firestore: FirebaseFirestore
) {
    suspend fun signUp(email: String, password: String, firstName: String, lastName: String, phoneNumber: String): Result<Boolean> =
        try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user

            firebaseUser?.let {
                val userId =
                    it.uid  // This is the automatically generated user ID by Firebase Authentication

                // Create a User object including the userId
                val user = User(userId, firstName, lastName, email, phoneNumber)

                // Save the user to Firestore using the userId as the document ID
                saveUserToFirestore(user)
            } ?: throw Exception("Failed to create user account.")

            //add user to firestore
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }


    private suspend fun saveUserToFirestore(user: User) {
        firestore.collection("users").document(user.userID).set(user).await()
    }

    suspend fun login(email: String, password: String): Result<Boolean> =
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }
}

