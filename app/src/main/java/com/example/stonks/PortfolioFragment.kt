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

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.stonks.adapter.DataDemo
import com.example.stonks.adapter.ItemAdapter
import com.example.stonks.databinding.PortfolioViewBinding
import com.example.stonks.model.Model
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class PortfolioFragment : Fragment() {
    // Binding object instance corresponding to the fragment_start.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment.
    private var binding: PortfolioViewBinding? = null
    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    private val model: Model by activityViewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = PortfolioViewBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        // replace with real data
        if (model.demoLoaded == false) {
            val walletdemo = DataDemo()
            walletdemo.set()
            model.loadState(walletdemo.demowallet)
            model.demoLoaded = true
        }
        //
       // println(model.wallet.balance())
        if (model.portfolioEvaluated) {
            binding?.portfolioValue?.text = String.format("%.2f", model.portfolioValue) + "$"
            displayROI()
        }
        return fragmentBinding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.portfolioFragment = this
        val recyclerView = binding?.recycleView
        val fab = binding?.shopButton
        if (recyclerView != null) {
            recyclerView.adapter = binding?.let {
                ItemAdapter(this@PortfolioFragment, findNavController(), model)
            }
        }
        fab?.setOnClickListener { findNavController().navigate(R.id.action_portfolioFragment_to_shopFragment) }
        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // Check if the user has scrolled to the bottom of the RecyclerView
                if (!recyclerView.canScrollVertically(1)) {
                    // If the user has scrolled to the bottom, hide the FloatingActionButton
                    fab?.hide()
                } else {
                    // If the user is not at the bottom, show the FloatingActionButton
                    fab?.show()
                }
            }
        })

        binding?.swipeRefreshLayout?.setOnRefreshListener {
            refreshPortfolio(recyclerView)
            binding?.swipeRefreshLayout?.isRefreshing = false
        }


   //     binding?.refreshButton?.setOnClickListener { refreshPortfolio(recyclerView) }
        refreshPortfolio(recyclerView)
    }

    fun refreshPortfolio(recyclerView: RecyclerView?) {
        CoroutineScope(Dispatchers.Main).launch {
            val portfolioValueDeferred = async(Dispatchers.IO) {
                model.evalutatePortfolio()
            }
            println(portfolioValueDeferred.await().toString())
            val portfolioValue = if (model.portfolioValue is Double) {
                String.format("%.2f", model.portfolioValue) + "$"
            } else {
                "N/A"
            }
            binding?.portfolioValue?.text = portfolioValue
            displayROI()
            recyclerView?.adapter?.notifyDataSetChanged()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayROI() {
            binding?.roi?.text = String.format("%.2f", model.ROI) + "%"

            if (model.ROI > 0.0) {
                binding?.roiImage?.setImageResource(R.drawable.baseline_arrow_upward_24)
                binding?.roi?.setTextColor(ContextCompat.getColor(requireContext(), R.color.profit_green));
            } else {
                binding?.roiImage?.setImageResource(R.drawable.baseline_arrow_downward_24)
                binding?.roi?.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_red));
            }
            if (model.ROI != 0.0) {
                binding?.roi?.setVisibility(View.VISIBLE)
                binding?.roiImage?.setVisibility(View.VISIBLE)
                binding?.roiLabel?.setVisibility(View.VISIBLE)
            }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}