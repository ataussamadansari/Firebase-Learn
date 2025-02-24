package com.example.firebaseauth.viewmodel

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseauth.manager.AuthManager
import com.example.firebaseauth.manager.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authManager: AuthManager, private val userRepository: UserRepository) : ViewModel() {

    private val _userData = MutableStateFlow<Map<String, Any>?>(null)
    val userData: StateFlow<Map<String, Any>?> = _userData

    init {
        getUserData()
        isUserLoggedIn()
    }

    // 🔹 Fix: This function should return Boolean
    fun isUserLoggedIn(): Boolean {
        return authManager.loggedIn()
    }

    fun getUserData() {
        if (_userData.value != null) return  // 🔥 पहले से Data है तो दुबारा मत Load करो

        val currentUser = authManager.getCurrentUser()
        if (currentUser != null) {
            userRepository.getUserDetails(currentUser.uid) { userData, error ->
                if (userData != null) {
                    viewModelScope.launch {
                        _userData.value = userData  // 🔥 केवल एक बार Data Set होगा
                    }
                }
            }
        }
    }


    /*fun getUserData() {
        val currentUser = authManager.getCurrentUser()
        if (currentUser != null) {
            userRepository.getUserDetails(currentUser.uid) { userData, error ->
                if (userData != null) {
                    viewModelScope.launch {
                        _userData.value = userData
                    }
                } else {
                    Log.e("AuthViewModel", "Error fetching user data: $error")
                }
            }
        } else {
            Log.e("AuthViewModel", "No user is logged in.")
        }
    }*/

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        authManager.loginWithEmail(email, password, onResult)
    }

    fun signUp(name: String, email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        authManager.registerUser(name, email, password, onResult)
    }

    fun googleSignIn(data: Intent?, onResult: (Boolean, String?) -> Unit) {
        authManager.handleGoogleSignInResult(data, onResult)
    }

    fun getGoogleSignInIntent(): Intent {
        return authManager.getSignInIntent()
    }


    fun logout(onComplete: () -> Unit) {
        authManager.logout {
            viewModelScope.launch {
                _userData.value = null
            }
            onComplete()
        }
//        authManager.logout(onComplete)
    }
}
