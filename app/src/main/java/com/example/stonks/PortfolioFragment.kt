/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.stonks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.stonks.adapter.DataDemo
import com.example.stonks.adapter.ItemAdapter
import com.example.stonks.databinding.PortfolioViewBinding
import com.example.stonks.model.Model

/**
 * This is the first screen of the Cupcake app. The user can choose how many cupcakes to order.
 */
class PortfolioFragment : Fragment() {
    // Binding object instance corresponding to the fragment_start.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment.
    private var binding: PortfolioViewBinding? = null
    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    private val sharedViewModel: Model by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = PortfolioViewBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.portfolioFragment = this
        val walletdemo = DataDemo()
        walletdemo.set()
        val recyclerView = binding?.recycleView
        if (recyclerView != null) {
            recyclerView.adapter = binding?.let {
                ItemAdapter(this@PortfolioFragment, walletdemo.demowallet, findNavController(),
                    it
                )
            }
        }
    }

    /**
     * Start an order with the desired quantity of cupcakes and navigate to the next screen.
     */
    fun viewStock() {
        Toast.makeText(context, "Its a toast!", Toast.LENGTH_SHORT).show()
       // findNavController().navigate(R.id.action_portfolioFragment_to_stockViewFragment)
    }

    /**
     * This fragment lifecycle method is called when the view hierarchy associated with the fragment
     * is being removed. As a result, clear out the binding object.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}