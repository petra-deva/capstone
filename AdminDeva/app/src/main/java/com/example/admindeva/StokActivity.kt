package com.example.admindeva

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.admindeva.Home.HomeActivity
import com.example.admindeva.data.Ikan
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_stok.*

class StokActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var etNama: EditText
    private lateinit var etStok: EditText
    private lateinit var btnSave : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stok)

        btn_kembali.setOnClickListener {
            Intent(this@StokActivity, HomeActivity::class.java).also {
                startActivity(it)
            }
        }

        etNama = findViewById(R.id.th_nama)
        etStok = findViewById(R.id.th_stok)
        btnSave = findViewById(R.id.btn_simpan)


        btnSave.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        saveData()
    }

    private fun saveData() {
        val nama: String = etNama.text.toString().trim()
        val stok: String = etStok.text.toString().trim()

        if (nama.isEmpty()){
            etNama.error = "Nama Harus Diisi"
            return
        }
        if(stok.isEmpty()){
            etStok.error = "Stok Harus Diisi"
            return
        }

        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference("DataStok")
        val ikanId: String? = ref.push().key
        val ikan = Ikan(ikanId!!,nama,stok)

        if (ikanId != null) {
            ref.child(ikanId).setValue(ikan).addOnCompleteListener{
                Toast.makeText(applicationContext,"Data berhasil ditambahkan, Silahkan Kembali Ke Menu Utama", Toast.LENGTH_LONG).show()
            }
        }
    }
}