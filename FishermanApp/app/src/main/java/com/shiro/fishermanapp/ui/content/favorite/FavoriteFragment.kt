package com.shiro.fishermanapp.ui.content.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.ktx.getValue
import com.shiro.fishermanapp.R
import com.shiro.fishermanapp.databinding.CartAndFavFragmentBinding
import com.shiro.fishermanapp.models.Product
import com.shiro.fishermanapp.ui.content.FavoriteCartAdapter
import com.shiro.fishermanapp.ui.detail.DetailActivity
import com.shiro.fishermanapp.utils.GONE
import com.shiro.fishermanapp.utils.VISIBLE

class FavoriteFragment : Fragment() {

    companion object {
        fun newInstance() = FavoriteFragment()
    }

    private lateinit var binding: CartAndFavFragmentBinding
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CartAndFavFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.catfFavTitle.text = getString(R.string.favorite)
        val adapter = FavoriteCartAdapter { product, isDelete ->
            if (isDelete) viewModel.delete(product.id.toString())
            else {
                val i = DetailActivity.getIntent(requireContext())
                i.putExtra(DetailActivity.DATA, product)
                startActivity(i)
            }
        }

        viewModel.response.observe(viewLifecycleOwner) {
            if (it != "Success") {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.getFavoriteList().observe(viewLifecycleOwner) { snapshot ->
            binding.cartFavProgress.GONE()

            val value = snapshot?.getValue<Map<String, Product>>()
            val valueList = arrayListOf<Product>()
            value?.values?.let { valueList.addAll(it) }
            adapter.submitList(valueList)

            if (valueList.isEmpty())
                binding.noData.VISIBLE()
        }

        binding.cartFavProductRv.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

}