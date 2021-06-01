package com.example.admindeva

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.admindeva.Home.HomeActivity
import com.example.admindeva.data.Nelayan
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_form.*

class FormActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var etNama: EditText
    private lateinit var etAlamat: EditText
    private lateinit var etTelepon: EditText
    private lateinit var etJenis: EditText
    private lateinit var btnSave : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        btn_kembali.setOnClickListener {
            Intent(this@FormActivity, HomeActivity::class.java).also {
                startActivity(it)
            }
        }

        etNama = findViewById(R.id.et_nama)
        etAlamat = findViewById(R.id.et_alamat)
        etTelepon = findViewById(R.id.et_telepon)
        etJenis = findViewById(R.id.et_jenis)
        btnSave = findViewById(R.id.btn_simpan)

        btnSave.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        saveData()
    }

    private fun saveData() {
        val nama: String = etNama.text.toString().trim()
        val alamat: String = etAlamat.text.toString().trim()
        val telepon: String = etTelepon.text.toString().trim()
        val jenis: String = etJenis.text.toString().trim()

        if (nama.isEmpty()){
            etNama.error = "Nama Harus Diisi"
            return
        }
        if(alamat.isEmpty()){
            etAlamat.error = "Alamat Harus Diisi"
            return
        }
        if (telepon.isEmpty()){
            etTelepon.error = "Telepon Harus Diisi"
            return
        }
        if (jenis.isEmpty()){
            etTelepon.error = "Jenis Telepon"
            return
        }

        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference("DataNelayan")

        val nlnId: String? = ref.push().key

        val nmn = Nelayan(nlnId!!,nama,alamat,telepon, jenis)

        if (nlnId != null) {
            ref.child(nlnId).setValue(nmn).addOnCompleteListener {
                Toast.makeText(applicationContext,"Data berhasil ditambahkan, Silahkan Kembali Ke Menu Utama", Toast.LENGTH_LONG).show()
            }
        }

    }
}