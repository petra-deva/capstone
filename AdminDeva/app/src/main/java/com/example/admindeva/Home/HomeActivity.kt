package com.example.admindeva.Home

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import com.example.admindeva.FormActivity
import com.example.admindeva.auth.LoginActivity
import com.example.admindeva.R
import com.example.admindeva.StokActivity
import com.example.admindeva.data.Ikan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var ref: DatabaseReference
    private lateinit var ikanList: MutableList<Ikan>
    private lateinit var listIkan: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()

        btnLogout.setOnClickListener {
            auth.signOut()
            Intent(this@HomeActivity, LoginActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }

        btndiri.setOnClickListener{
            Intent(this@HomeActivity, FormActivity::class.java).also {
                startActivity(it)
            }
        }

        btntambah.setOnClickListener{
            Intent(this@HomeActivity, StokActivity::class.java).also {
                startActivity(it)
            }
        }

        ref = FirebaseDatabase.getInstance().getReference("DataStok")

        ikanList = mutableListOf()
        listIkan = findViewById(R.id.lv_ikan)
        ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (h in snapshot.children) {
                        val ikan: Ikan? = h.getValue(Ikan::class.java)
                        if (ikan != null) {
                            ikanList.add(ikan)
                        }
                    }
                    val adapter = IkanAdapter(this@HomeActivity, R.layout.list_item, ikanList)
                    listIkan.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

}