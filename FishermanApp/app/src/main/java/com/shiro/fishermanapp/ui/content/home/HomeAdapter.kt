package com.shiro.fishermanapp.ui.content.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shiro.fishermanapp.databinding.ProductItemBinding
import com.shiro.fishermanapp.models.Product
import com.shiro.fishermanapp.utils.CurrencyFormatter
import com.squareup.picasso.Picasso

class HomeAdapter(
    private val onItemClick: (Product) -> Unit
) : ListAdapter<Product, HomeAdapter.HomeHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Product> =
            object : DiffUtil.ItemCallback<Product>() {
                override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
                    oldItem == newItem
            }
    }

    inner class HomeHolder(private val binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                productName.text = product.name
                productPrice.text = CurrencyFormatter.convertAndFormat(product.price).dropLast(3)
                Picasso.get().load(product.image).into(productImage)
            }

            itemView.setOnClickListener { onItemClick(product) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HomeHolder(ProductItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: HomeHolder, position: Int) =
        holder.bind(getItem(position))
}