package com.aljebrastudio.islamicstory.core.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aljebrastudio.islamicstory.core.utils.vo.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class RemoteDataSource(
    private val firebaseAuth: FirebaseAuth,
) {
    fun register(email: String, password: String): LiveData<Resource<AuthResult>> {
        val auth = MutableLiveData<Resource<AuthResult>>()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    auth.postValue(Resource.success(task.result))
                } else {
                    auth.postValue(Resource.error(task.exception?.message))
                }
            }
            .addOnFailureListener { error ->
                auth.postValue(Resource.error(error.message.toString()))
            }
        return auth
    }
    fun login(email: String, password: String): LiveData<Resource<AuthResult>>{
        val credential = EmailAuthProvider.getCredential(email, password)
        val auth = MutableLiveData<Resource<AuthResult>>()
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    auth.postValue(Resource.success(task.result))
                }else{
                    auth.postValue(Resource.error(task.exception?.message))
                }
            }
            .addOnFailureListener {
                auth.postValue(Resource.error(it.message.toString()))
            }
        return auth
    }
}