package com.example.admindeva.data

data class Ikan(
    val id: String,
    val nama: String,
    var stock: String
){
    constructor(): this("","",""){

    }
}