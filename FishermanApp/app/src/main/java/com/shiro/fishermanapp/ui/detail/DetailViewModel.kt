package com.shiro.fishermanapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shiro.fishermanapp.models.Product

class DetailViewModel : ViewModel() {

    private val database =
        Firebase.database("https://qwiklabs-gcp-03-e1473d8b-4f6b2-default-rtdb.firebaseio.com/")

    val response = MutableLiveData<String>()
    private val cartReference = database.getReference("cartList")
    fun saveToCart(id: String, product: Product) = cartReference.child("cart$id")
        .setValue(product)
        .addOnSuccessListener { response.postValue("Success") }
        .addOnFailureListener { response.postValue(it.message) }

    private val favoriteReference = database.getReference("favoriteList")
    private var liveData = MutableLiveData<DataSnapshot>()
    fun saveToFavorite(product: Product) = favoriteReference.child("fav${product.id}")
        .setValue(product)
        .addOnSuccessListener { response.postValue("Success") }
        .addOnFailureListener { response.postValue(it.message) }

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

    fun deleteFromFavorite(id: String) = favoriteReference.child("fav$id").removeValue()

}