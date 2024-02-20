package com.nakibul.android.androidwebrtc.repository

import com.nakibul.android.androidwebrtc.firebaseClient.FirebaseClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val firebaseClient: FirebaseClient
) {
    fun login(userName: String, password: String, isDone: (Boolean, String?) -> Unit) {
        firebaseClient.login(userName, password, isDone)
    }

    fun observeUsersStatus(status: (List<Pair<String, String>>) -> Unit) {
        firebaseClient.observeUsersStatus(status)
    }

}