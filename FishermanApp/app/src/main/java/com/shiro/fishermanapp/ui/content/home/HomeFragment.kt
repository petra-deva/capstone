package com.shiro.fishermanapp.ui.content.home

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.getValue
import com.shiro.fishermanapp.databinding.HomeFragmentBinding
import com.shiro.fishermanapp.models.Product
import com.shiro.fishermanapp.ui.detail.DetailActivity
import com.shiro.fishermanapp.ui.login.LoginActivity
import com.shiro.fishermanapp.utils.AutoFitGridLayoutManager
import com.shiro.fishermanapp.utils.DisplayMetricUtil
import com.shiro.fishermanapp.utils.GONE
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var binding: HomeFragmentBinding
    private lateinit var viewModel: HomeViewModel

    private var dataList = arrayListOf<Product>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = HomeAdapter {
            val i = DetailActivity.getIntent(requireContext())
            i.putExtra(DetailActivity.DATA, it)
            startActivity(i)
        }

        viewModel.response.observe(viewLifecycleOwner) {
            if (it != "Success") {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.getProductList().observe(viewLifecycleOwner) { snapshot ->
            binding.homeProgress.GONE()

            val value = snapshot?.getValue<Map<String, Product>>()
            val valueList = arrayListOf<Product>()
            value?.values?.let { valueList.addAll(it) }

            dataList = valueList
            adapter.submitList(valueList)
        }

        val displayMetrics = activity?.let { DisplayMetricUtil(it) }
        var width = displayMetrics?.getWidth()

        width = if (width == null) 250
        else (width / 2) - 16

        val layoutManager = AutoFitGridLayoutManager(requireContext(), width)
        var filteredData = dataList

        binding.apply {
            homeProductRv.adapter = adapter
            homeProductRv.layoutManager = layoutManager
            homeSearchEt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s != null) {
                        filteredData = if (s.isNotEmpty()) {
                            dataList.filter { it.name.contains(s) } as ArrayList<Product>
                        } else {
                            dataList
                        }

                        adapter.submitList(filteredData)
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }
}