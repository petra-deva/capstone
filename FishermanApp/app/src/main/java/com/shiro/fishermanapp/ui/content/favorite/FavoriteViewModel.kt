package com.shiro.fishermanapp.ui.content.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FavoriteViewModel : ViewModel() {
    private val database =
        Firebase.database("https://qwiklabs-gcp-03-e1473d8b-4f6b2-default-rtdb.firebaseio.com/")

    private val favoriteReference = database.getReference("favoriteList")

    private var liveData = MutableLiveData<DataSnapshot>()
    val response = MutableLiveData<String>()
    fun getFavoriteList(): LiveData<DataSnapshot?> {
        favoriteReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                liveData.postValue(snapshot)
                response.postValue("Success")
            }

            override fun onCancelled(error: DatabaseError) {
                response.postValue(error.message)
            }

        })
        return liveData
    }

    fun delete(id: String) = favoriteReference.child("fav$id").removeValue()
}