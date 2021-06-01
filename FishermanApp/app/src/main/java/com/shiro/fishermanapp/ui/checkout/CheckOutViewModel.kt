package com.shiro.fishermanapp.ui.checkout

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shiro.fishermanapp.models.Checkout

class CheckOutViewModel : ViewModel() {
    private val database =
        Firebase.database("https://qwiklabs-gcp-03-e1473d8b-4f6b2-default-rtdb.firebaseio.com/")

    val response = MutableLiveData<String>()
    private val cartReference = database.getReference("checkoutList")
    fun checkout(id: String, checkout: Checkout) =
        cartReference.push()
            .setValue(checkout)
            .addOnSuccessListener { response.postValue("Success") }
            .addOnFailureListener { response.postValue(it.message) }
}