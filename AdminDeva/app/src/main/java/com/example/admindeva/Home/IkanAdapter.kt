package com.example.admindeva.Home

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.admindeva.R
import com.example.admindeva.data.Ikan
import com.google.firebase.database.FirebaseDatabase


class IkanAdapter(val mCtx: Context, val LayoutResId : Int, val ikanList: List<Ikan>): ArrayAdapter<Ikan> (
    mCtx,LayoutResId,ikanList){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)

        val view: View = layoutInflater.inflate(LayoutResId,null)

        val tvNama : TextView = view.findViewById(R.id.tv_name)
        val tvStok : TextView = view.findViewById(R.id.tv_stock)
        val tvEdit : TextView = view.findViewById(R.id.tv_edit)

        val ikan: Ikan = ikanList[position]

        tvEdit.setOnClickListener{
            showUpdateDialog(ikan)
        }

        tvNama.text = ikan.nama
        tvStok.text = ikan.stock

        return view
    }

    private fun showUpdateDialog(ikan: Ikan) {
        val builder = AlertDialog.Builder(mCtx)
        builder.setTitle("Edit Ikan")

        val inflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view = inflater.inflate(R.layout.update_dialog, null)

        val etNama = view.findViewById<EditText>(R.id.et_nama)
        val etStok = view.findViewById<EditText>(R.id.et_stok)

        etNama.setText(ikan.nama)
        etStok.setText(ikan.stock)

        builder.setView(view)

        builder.setPositiveButton("Update") {p0,p1 ->
            val dbikan = FirebaseDatabase.getInstance().getReference("DataStok")

            val nama = etNama.text.toString().trim()
            val stok = etStok.text.toString().trim()
            if (nama.isEmpty()){
                etNama.error = "Mohon diisi"
                etNama.requestFocus()
                return@setPositiveButton
            }
            if (stok.isEmpty()) {
                etStok.error = "Mohon diisi"
                etStok.requestFocus()
                return@setPositiveButton
            }
            val ikan = Ikan(ikan.id,nama,stok)
            dbikan.child(ikan.id!!).setValue(ikan)

            Toast.makeText(mCtx, "Data Berhasil Diupdate", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){p0,p1 ->

        }

        val alert = builder.create()
        alert.show()
    }
}