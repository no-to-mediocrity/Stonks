package com.example.stonks.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.stonks.R
import com.example.stonks.databinding.StonkViewBinding
import com.example.stonks.model.Model
import com.example.stonks.model.Asset
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel


class StonkViewFragment : Fragment() {
    private var binding: StonkViewBinding? = null
    private val model: Model by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = StonkViewBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       binding?.apply {
           buyButton.setOnClickListener {buyStock()}

           lateinit var allAssets : MutableList<Asset>
           Observable.fromCallable {
               allAssets = model.allAssetsDirectly()
               allAssets
           }
               .subscribeOn(Schedulers.computation())
               .observeOn(AndroidSchedulers.mainThread())
               .doOnComplete {
                   val stockIndex = allAssets.indexOfFirst { it.ticker == model.selectedAsset.ticker }
                   if (stockIndex == -1) {
                       binding?.sellButton?.isActivated = false
                       binding?.sellButton?.setBackgroundColor(
                           ContextCompat.getColor(
                               requireContext(),
                               R.color.gray
                           )
                       )
                       binding?.sellButton?.setOnClickListener(null)
                   } else {
                       binding?.sellButton?.isActivated = true
                       binding?.sellButton?.setOnClickListener { sellStock() }
                   }
               }
               .doOnError { error ->
                   Log.e("Observable", "Error: ${error.message}", error)
               }
               .subscribe()


           model.allAssets.observe(viewLifecycleOwner) { allAssets ->
               val stockIndex = allAssets.indexOfFirst { it.ticker == model.selectedAsset.ticker }
               if (stockIndex == -1) {
                   binding?.sellButton?.isActivated = false
                   binding?.sellButton?.setBackgroundColor(
                       ContextCompat.getColor(
                           requireContext(),
                           R.color.gray
                       )
                   )
                   binding?.sellButton?.setOnClickListener(null)
               } else {
                   binding?.sellButton?.isActivated = true
                   binding?.sellButton?.setOnClickListener { sellStock() }
               }
           }

       }

        val webView = binding?.webView
        val webSettings = webView?.settings
        webView?.settings?.javaScriptEnabled
        webView?.loadUrl("https://www.tradingview.com/chart/?symbol=${model.selectedAsset.ticker}")
        webSettings?.userAgentString = "Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.5672.76 Mobile Safari/537.36"
        webSettings?.javaScriptEnabled = true
        webSettings?.allowFileAccess = true
        webSettings?.allowContentAccess = true
    }

    private fun buyStock() {
        model.setActionBuy(true)
        findNavController().navigate(R.id.action_stonkViewFragment_to_assetActionFragment)
    }

   private fun sellStock() {
        model.setActionBuy(false)
        findNavController().navigate(R.id.action_stonkViewFragment_to_assetActionFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        CoroutineScope(Dispatchers.IO).cancel()
        binding = null
    }
}