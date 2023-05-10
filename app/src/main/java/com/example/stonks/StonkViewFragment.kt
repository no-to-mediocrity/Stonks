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
import com.example.stonks.databinding.StonkViewBinding
import com.example.stonks.model.Model


class StonkViewFragment : Fragment() {
    private var binding: StonkViewBinding? = null
    private val model: Model by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = StonkViewBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        // val lineChart = binding?.lineChart
       // lineChart?.setNoDataText("")
        //val label = getString(R.string.current_stock, model.selectedStock.value)
      //  println(model.selectedStock.value.toString())

      //  findNavController().currentDestination?.label = model.selectedStock.value.toString()
        return fragmentBinding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
     //   binding?.buyButton?.setOnClickListener { buyStock() }
        super.onViewCreated(view, savedInstanceState)
       binding?.apply {
            buyButton.setOnClickListener {buyStock()}
           model.selectedStock.observe(viewLifecycleOwner
           ) { value ->
              // sellButton.setOnClickListener { sellStock() }
               val stockIndex = model.wallet.assets().indexOfFirst { it.ticker == value }
               if (stockIndex == -1) {
                   binding?.sellButton?.isActivated = false
                   binding?.sellButton?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray))
               } else {
                   binding?.sellButton?.setOnClickListener{sellStock()}
               }
           }
       }


        val webView = binding?.webView
        val webSettings = webView?.settings
        webView?.settings?.javaScriptEnabled
        model.selectedStock.observe(viewLifecycleOwner
        ) { value ->
            webView?.loadUrl("https://finance.yahoo.com/chart/$value")
        }

       // findNavController().currentDestination.
        //currentDestination?.label = model.selectedStockName
        webSettings?.userAgentString = "Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.5672.76 Mobile Safari/537.36"
        webSettings?.javaScriptEnabled = true
        webSettings?.allowFileAccess = true
        webSettings?.allowContentAccess = true
    }

    private fun buyStock() {
     //   model.purchaseStock(3)
     //   model.selectStock()
        model.setActionBuy(true)
        findNavController().navigate(R.id.action_stonkViewFragment_to_assetActionFragment)
    }

   private fun sellStock() {
        model.setActionBuy(false)
        findNavController().navigate(R.id.action_stonkViewFragment_to_assetActionFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}