package com.udacity.project4.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {
    enum class AuthenticationState {
        Success, Failed
    }

    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.Success
        } else {
            AuthenticationState.Failed
        }
    }
}
