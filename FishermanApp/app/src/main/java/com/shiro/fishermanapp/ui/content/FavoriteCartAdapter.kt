package com.shiro.fishermanapp.ui.content

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shiro.fishermanapp.databinding.ProductCartFavItemBinding
import com.shiro.fishermanapp.models.Product
import com.shiro.fishermanapp.utils.CurrencyFormatter
import com.squareup.picasso.Picasso

class FavoriteCartAdapter(
    private val onItemClick: (Product, Boolean) -> Unit,
) : ListAdapter<Product, FavoriteCartAdapter.HomeHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Product> =
            object : DiffUtil.ItemCallback<Product>() {
                override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
                    oldItem == newItem
            }
    }

    inner class HomeHolder(private val binding: ProductCartFavItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                productName.text = product.name
                productPrice.text = CurrencyFormatter.convertAndFormat(product.price).dropLast(3)
                Picasso.get().load(product.image).into(productImage)
                productDelete.setOnClickListener { onItemClick(product, true) }
            }

            itemView.setOnClickListener { onItemClick(product, false) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HomeHolder(ProductCartFavItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: HomeHolder, position: Int) =
        holder.bind(getItem(position))
}