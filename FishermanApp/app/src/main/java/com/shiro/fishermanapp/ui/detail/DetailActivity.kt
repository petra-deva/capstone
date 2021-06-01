package com.shiro.fishermanapp.ui.detail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.ktx.getValue
import com.shiro.fishermanapp.R
import com.shiro.fishermanapp.databinding.ActivityDetailBinding
import com.shiro.fishermanapp.models.Product
import com.shiro.fishermanapp.ui.checkout.CheckoutActivity
import com.shiro.fishermanapp.utils.CurrencyFormatter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val DATA = "data"
        fun getIntent(context: Context) = Intent(context, DetailActivity::class.java)
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel

    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

        val extra = intent.extras
        val product = extra?.getParcelable<Product>(DATA)

        binding.apply {
            toolbarBack.setOnClickListener { finish() }
            product?.let {
                Picasso.get().load(it.image).into(detailImage)
                detailName.text = it.name
                detailPrice.text = CurrencyFormatter.convertAndFormat(it.price).dropLast(3)
                detailDesc.text = it.description

                detailFavorite.setOnClickListener {
                    if(!isFavorite) {
                        viewModel.saveToFavorite(product)
                    }
                    else {
                        viewModel.deleteFromFavorite("${product.id}")
                    }
                }

                detailToCart.setOnClickListener { _ ->
                    viewModel.saveToCart(product.id.toString(), it)
                }

                detailCheckOut.setOnClickListener {
                    val i = CheckoutActivity.getIntent(this@DetailActivity)
                    i.putExtra(CheckoutActivity.DATA, product)
                    startActivity(i)
                }
            }
        }

        viewModel.getFavoriteList().observe(this) { snapshot ->
            val value = snapshot?.getValue<Map<String, Product>>()
            isFavorite = if (value != null && value.keys.contains("fav${product?.id}")) {
                binding.detailFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
                true
            }
            else {
                binding.detailFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                false
            }
        }

        viewModel.response.observe(this) {
            if (it != "Success") { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }
    }

}