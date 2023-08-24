package com.example.stonks.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.stonks.adapter.shop.ShopItemAdapter
import com.example.stonks.adapter.shop.shopSetter
import com.example.stonks.databinding.ShopViewBinding
import com.example.stonks.model.Model

class ShopFragment : Fragment() {
    private var binding: ShopViewBinding? = null
    private val model: Model by activityViewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = ShopViewBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        model.shop = shopSetter()
        super.onViewCreated(view, savedInstanceState)
        binding?.shopFragment = this
        val recyclerView = binding?.recycleView
        if (recyclerView != null) {
            recyclerView.adapter = binding?.let {
                ShopItemAdapter(this@ShopFragment, model, findNavController())
            }
        }
    }
}