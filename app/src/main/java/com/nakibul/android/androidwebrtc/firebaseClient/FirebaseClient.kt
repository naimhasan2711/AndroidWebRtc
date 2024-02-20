package com.nakibul.android.androidwebrtc.firebaseClient

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.gson.Gson
import com.nakibul.android.androidwebrtc.FirebaseFieldsName.PASSWORD
import com.nakibul.android.androidwebrtc.FirebaseFieldsName.STATUS
import com.nakibul.android.androidwebrtc.MyEventListener
import com.nakibul.android.androidwebrtc.UserStatus
import javax.inject.Inject

class FirebaseClient @Inject constructor(
    private val dbRef: DatabaseReference,
    private val gson: Gson
) {
    private var currentUserName: String? = null
    private fun setUserName(userName: String) {
        this.currentUserName = userName
    }

    fun login(userName: String, password: String, isDone: (Boolean, String?) -> Unit) {
        dbRef.addListenerForSingleValueEvent(object : MyEventListener() {
            override fun onDataChange(snapshot: DataSnapshot) {
                //if current user exists
                if (snapshot.hasChild(userName)) {
                    //user exists, time to check password
                    val userPassword = snapshot.child(userName).child(PASSWORD).value
                    if (password == userPassword) {
                        //password is correct and sign in
                        dbRef.child(userName).child(STATUS).setValue(UserStatus.ONLINE)
                            .addOnCompleteListener {
                                setUserName(userName)
                                isDone(true, null)
                            }.addOnFailureListener {
                                isDone(false, "${it.message}")
                            }
                    } else {
                        //password is incorrect, notify user
                        isDone(false, "Password is Wrong")
                    }
                } else {
                    //user doesn't exists, create user
                    dbRef.child(userName).child(PASSWORD).setValue(password).addOnCompleteListener {
                        dbRef.child(userName).child(STATUS).setValue(UserStatus.ONLINE)
                            .addOnCompleteListener {
                                setUserName(userName)
                                isDone(true, null)
                            }
                            .addOnFailureListener {
                                isDone(false, it.message)
                            }
                    }.addOnFailureListener {
                        isDone(false, it.message)
                    }
                }
            }
        })
    }

    fun observeUsersStatus(status: (List<Pair<String, String>>) -> Unit) {
        dbRef.addValueEventListener(object : MyEventListener() {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = snapshot.children.filter { it.key != currentUserName }.map {
                    it.key!! to it.child(STATUS).value.toString()
                }
                status(userList)
            }
        })
    }
}