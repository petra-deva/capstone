package com.example.admindeva.data

data class Nelayan (
    var id: String,
    var nama: String,
    var alamat: String,
    var telepon: String,
    var jenis: String
){
    constructor(): this("","","","",""){

    }
}
