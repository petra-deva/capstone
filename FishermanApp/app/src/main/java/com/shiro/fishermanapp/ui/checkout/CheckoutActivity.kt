package com.shiro.fishermanapp.ui.checkout

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.shiro.fishermanapp.R
import com.shiro.fishermanapp.databinding.ActivityCheckoutBinding
import com.shiro.fishermanapp.models.Checkout
import com.shiro.fishermanapp.models.Product
import com.shiro.fishermanapp.ui.MainActivity
import com.shiro.fishermanapp.utils.CurrencyFormatter
import com.shiro.fishermanapp.utils.GONE
import com.shiro.fishermanapp.utils.VISIBLE
import com.squareup.picasso.Picasso

class CheckoutActivity : AppCompatActivity() {
    companion object {
        const val DATA = "data"
        fun getIntent(context: Context) = Intent(context, CheckoutActivity::class.java)
    }

    private lateinit var binding: ActivityCheckoutBinding
    private lateinit var viewModel: CheckOutViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        viewModel = ViewModelProvider(this).get(CheckOutViewModel::class.java)

        val extra = intent.extras
        val product = extra?.getParcelable<Product>(DATA)

        binding.apply {
            toolbarBack.setOnClickListener { finish() }
            product?.let { product ->
                Picasso.get().load(product.image).into(detailImage)
                checkoutName.text = product.name
                checkoutPrice.text = CurrencyFormatter.convertAndFormat(product.price).dropLast(3)
                checkoutPlus.setOnClickListener { plusCount() }
                checkoutMinus.setOnClickListener { minusCount() }
                checkout.setOnClickListener {
                    if (checkoutRecipientAddress.text.isNotEmpty())
                        if (checkoutRecipientAddress.text.length > 6) {
                            doCheckOut(
                                checkoutCount.text.toString().toLong(),
                                product,
                                checkoutRecipientAddress.text.toString()
                            )
                        } else {
                            Toast.makeText(
                                this@CheckoutActivity,
                                getString(R.string.fill_valid_form),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    else
                        Toast.makeText(
                            this@CheckoutActivity,
                            getString(R.string.fill_all_form),
                            Toast.LENGTH_SHORT
                        ).show()
                }
            }
        }

        viewModel.response.observe(this) {
            if (it == "Success") {
                val i = MainActivity.getIntent(this@CheckoutActivity)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                i.putExtra(MainActivity.DATA, true)
                startActivity(i)
            } else {
                binding.apply {
                    checkoutProgress.GONE()
                    checkout.isEnabled = true
                }

                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun doCheckOut(quantity: Long, product: Product, address: String) {
        binding.apply {
            checkoutProgress.VISIBLE()
            checkout.isEnabled = false
        }

        val checkout = Checkout(quantity, product.name, product.id, product.price, address)
        viewModel.checkout(product.id.toString(), checkout)
    }

    private fun plusCount() {
        val countStr = binding.checkoutCount.text.toString()
        var count = countStr.toLong()

        if (count < 999) {
            count += 1
            binding.checkoutCount.text = count.toString()
        }
    }

    private fun minusCount() {
        val countStr = binding.checkoutCount.text.toString()
        var count = countStr.toLong()

        if (count > 1) {
            count -= 1
            binding.checkoutCount.text = count.toString()
        }
    }
}