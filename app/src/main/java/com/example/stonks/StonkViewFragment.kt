package com.example.stonks

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.stonks.databinding.StonkViewBinding

class StonkViewFragment : Fragment() {
    private var binding: StonkViewBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = StonkViewBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        val lineChart = binding?.lineChart
        lineChart?.setNoDataText("")
        return fragmentBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
     //   binding?.buyButton?.setOnClickListener { buyStock() }

        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            buyButton.setOnClickListener { buyStock() }
        }
    }

    fun buyStock() {
        Log.d("StonkViewFragment", "buyStock() called")
        findNavController().navigate(R.id.action_stonkViewFragment_to_assetActionFragment)
    }
}