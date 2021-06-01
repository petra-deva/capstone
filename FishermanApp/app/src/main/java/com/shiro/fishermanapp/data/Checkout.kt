package com.shiro.fishermanapp.models

data class Checkout(
    val quantity: Long,
    val productName: String,
    val productId: Int,
    val productPrice: Long,
    val recipientAddress: String
)