package com.example.firebaseauth.manager

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {
    private val db = FirebaseFirestore.getInstance()

    fun saveUserToFirestore(user: FirebaseUser, name: String? = null, onResult: (Boolean, String?) -> Unit) {
        val userData = mapOf(
            "uid" to user.uid,
            "name" to (name ?: user.displayName ?: "No Name"),
            "email" to (user.email ?: "No Email"),
            "profilePic" to (user.photoUrl?.toString() ?: "")
        )

        db.collection("users").document(user.uid)
            .set(userData)
            .addOnSuccessListener { onResult(true, "User saved successfully") }
            .addOnFailureListener { onResult(false, it.message) }
    }


    fun getUserDetails(uid: String, callback: (Map<String, Any>?, String?) -> Unit) {
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    callback(document.data, null)
                } else {
                    callback(null, "User not found")
                }
            }
            .addOnFailureListener {
                callback(null, it.message)
            }
    }
}
