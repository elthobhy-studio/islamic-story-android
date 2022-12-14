package com.elthobhy.islamicstory.core.data.remote

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elthobhy.islamicstory.core.domain.model.ListDomain
import com.elthobhy.islamicstory.core.domain.model.User
import com.elthobhy.islamicstory.core.utils.vo.Resource
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RemoteDataSource(
    private val firebaseAuth: FirebaseAuth,
    private val userDatabase: DatabaseReference,
    private val nabiDatabase: DatabaseReference,
    private val storageReference: StorageReference
) {
    fun register(name: String, email: String, password: String): LiveData<Resource<AuthResult>> {
        val auth = MutableLiveData<Resource<AuthResult>>()
        auth.postValue(Resource.loading())
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
        auth.postValue(Resource.loading())
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
        auth.postValue(Resource.loading())
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
        dataUser.postValue(Resource.loading())
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
        auth.postValue(Resource.loading())
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
        auth.postValue(Resource.loading())
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

    fun getData(): LiveData<Resource<List<ListDomain>>> {
        val data = MutableLiveData<Resource<List<ListDomain>>>()
        data.postValue(Resource.loading())
        val listener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                    val dataList = snapshot.children.toList()
                    val dataNabi = dataList.sortedWith(compareBy{
                        it.getValue(ListDomain::class.java)?.keyId
                    })
                    val newData = ArrayList<ListDomain>()
                    for(i in dataNabi.indices){
                        val dataKu = dataNabi[i].getValue(ListDomain::class.java)
                        if (dataKu != null) {
                            newData.add(dataKu)
                        }
                    }
                    data.postValue(Resource.success(newData))
            }

            override fun onCancelled(error: DatabaseError) {
                data.postValue(Resource.error(error.message))
            }


        }
        nabiDatabase.addValueEventListener(listener)
        return data
    }
    fun postDataNabi(
        nama: String,
        umur: String,
        tempatDiutus: String,
        kisah: String,
        keyId: String,
        profile: File,
        display: File,
    ): LiveData<Resource<ListDomain>>{
        val dataNabi = MutableLiveData<Resource<ListDomain>>()
        val profilePath = Uri.fromFile(File(profile.toString()))
        val displayPath = Uri.fromFile(File(display.toString()))
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_SS", Locale.getDefault())
        val date = Date()
        val fileName = formatter.format(date)
        getStorage(
            name = nama,
            umur = umur,
            umat = tempatDiutus,
            detail = kisah,
            keyId = keyId,
            dataNabi = dataNabi,
            profilePath = profilePath,
            displayPath = displayPath,
            fileName = fileName
        )
        return dataNabi
    }

    private fun getStorage(
        name: String,
        umur: String,
        umat: String,
        detail: String,
        keyId: String,
        dataNabi: MutableLiveData<Resource<ListDomain>>,
        profilePath: Uri,
        displayPath: Uri,
        fileName: String
    ) {
        storageReference.child(fileName).putFile(profilePath)
        storageReference.child(fileName+"_display").putFile(displayPath)
            .addOnSuccessListener {
                storageReference.child(fileName+"_display").downloadUrl.addOnSuccessListener { display ->
                    storageReference.child(fileName).downloadUrl.addOnSuccessListener { profile ->
                        getDatabase(
                            name = name,
                            umur = umur,
                            umat = umat,
                            detail = detail,
                            keyId = keyId,
                            profile = profile.toString(),
                            display = display.toString(),
                            dataNabi = dataNabi,
                        )
                    }.addOnFailureListener {
                        dataNabi.postValue(Resource.error(it.message))
                        Log.e("error", "getStorage: ${it.message}" )
                    }
                }.addOnFailureListener {
                    dataNabi.postValue(Resource.error(it.message))
                    Log.e("error", "getStorage: ${it.message}" )
                }
            }


    }

    private fun getDatabase(
        name: String,
        umur: String,
        umat: String,
        detail: String,
        keyId: String,
        profile: String,
        display: String,
        dataNabi: MutableLiveData<Resource<ListDomain>>
    ) {
        val data = ListDomain(
            name = name,
            umur = umur,
            umat = umat,
            detail = detail,
            keyId = keyId,
            profile = profile,
            display = display,
        )
        dataNabi.postValue(Resource.loading())
        nabiDatabase.child(keyId)
            .setValue(data)
            .addOnSuccessListener {
                dataNabi.postValue(Resource.success(data))
            }
            .addOnFailureListener {
                dataNabi.postValue(Resource.error(it.message))
            }
    }

    fun removeData(keyId: String): LiveData<Resource<String>> {
        val message = MutableLiveData<Resource<String>>()
        nabiDatabase.child(keyId).removeValue()
            .addOnSuccessListener {
                message.postValue(Resource.success("deleted"))
            }.addOnFailureListener {
                message.postValue(Resource.error(it.message))
            }
        return message
    }
}