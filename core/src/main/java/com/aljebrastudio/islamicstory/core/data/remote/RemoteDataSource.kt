package com.aljebrastudio.islamicstory.core.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aljebrastudio.islamicstory.core.domain.model.User
import com.aljebrastudio.islamicstory.core.utils.vo.Resource
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class RemoteDataSource(
    private val firebaseAuth: FirebaseAuth,
    private val userDatabase: DatabaseReference
) {
    fun register(name: String, email: String, password: String): LiveData<Resource<AuthResult>> {
        val auth = MutableLiveData<Resource<AuthResult>>()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = firebaseAuth.uid
                    val imageUser = "https://ui-avatars.com/api/?background=218B5E&color=fff&size=100&rounded=true&name=$name"
                    val user = User(
                        nameUser = name,
                        avatarUser = imageUser,
                        emailUser = email,
                        uidUser = uid
                    )
                    userDatabase.child(uid.toString())
                        .setValue(user)
                        .addOnSuccessListener {
                            auth.postValue(Resource.success(task.result))
                        }
                        .addOnFailureListener {
                            auth.postValue(Resource.error(task.exception?.message))
                        }
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
    fun loginWithGoogle(name: String, email: String, credential: AuthCredential): LiveData<Resource<AuthResult>>{
        val auth = MutableLiveData<Resource<AuthResult>>()
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    auth.postValue(Resource.success(task.result))
                    val uid = firebaseAuth.uid
                    val imageUser = "https://ui-avatars.com/api/?background=218B5E&color=fff&size=100&rounded=true&name=$name"
                    val user = User(
                        nameUser = name,
                        avatarUser = imageUser,
                        emailUser = email,
                        uidUser = uid
                    )
                    userDatabase.child(uid.toString())
                        .setValue(user)
                        .addOnSuccessListener {
                            auth.postValue(Resource.success(task.result))
                        }
                        .addOnFailureListener {
                            auth.postValue(Resource.error(task.exception?.message))
                        }
                }else{
                    auth.postValue(Resource.error(task.exception?.message))
                }
            }
            .addOnFailureListener {
                auth.postValue(Resource.error(it.message.toString()))
            }
        return auth
    }
    fun getDataUser(uid: String): LiveData<Resource<User>>{
        val dataUser = MutableLiveData<Resource<User>>()
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                dataUser.postValue(Resource.success(user))
            }

            override fun onCancelled(error: DatabaseError) {
                dataUser.postValue(Resource.error(error.message))
            }
        }
        userDatabase.child(uid)
                .addValueEventListener(listener)


        return dataUser
    }
    fun changePassword(newPass: String, credential: AuthCredential): LiveData<Resource<Void>>{
        val auth = MutableLiveData<Resource<Void>>()
        val currentUser = firebaseAuth.currentUser
        currentUser?.reauthenticate(credential)
            ?.addOnCompleteListener { task->
                if(task.isSuccessful){
                    currentUser.updatePassword(newPass)
                    auth.postValue(Resource.success(task.result))
                }
            }
            ?.addOnFailureListener {
                auth.postValue(Resource.error(it.message))
            }
        return auth
    }
    fun forgotPassword(email: String): LiveData<Resource<Void>>{
        val auth = MutableLiveData<Resource<Void>>()
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    auth.postValue(Resource.success(it.result))
                }
            }
            .addOnFailureListener {
                auth.postValue(Resource.error(it.message))
            }
        return auth
    }
}