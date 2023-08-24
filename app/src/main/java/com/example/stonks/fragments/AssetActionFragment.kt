package com.example.stonks.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.stonks.R
import com.example.stonks.databinding.AssetActionBinding
import com.example.stonks.model.Model
import com.example.stonks.repository.AssetRepository
import com.example.stonks.model.Asset
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Math.floor
import java.util.*
import kotlin.concurrent.fixedRateTimer


class AssetActionFragment: Fragment() {
    private var binding: AssetActionBinding? = null
    private val model: Model by activityViewModels()
    private var availableAssets: Long = 0
    private var availableFunds = 0.0
    private var _purchasePrice = 0.0
    private var actionIsBuy = false
    private var debounceTimer: Timer? = null
    private lateinit var assetRepository: AssetRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = AssetActionBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        binding?.buySellButton?.setOnClickListener { buySellAction() }
        binding?.minusButton?.setOnClickListener { decrement() }
        binding?.plusButton?.setOnClickListener { increment() }
        binding?.quantityEdittext?.doAfterTextChanged { refreshPriceDelayed() }
        return fragmentBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model.ifActionIsBuy.observe(viewLifecycleOwner) { boolean ->
            run {
                if (boolean) {
                    actionIsBuy = true
                    setUIBuy()
                } else {
                    actionIsBuy = false
                    setUISell()
                }
                updatePrice()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun setUIBuy() {
        binding?.currentAction?.text = "Buy ${model.selectedAsset.name}"
        model.balance()
        model.balance.observe(viewLifecycleOwner) { balance ->
            binding?.availableFunds?.text = "Available funds: " +
                    String.format("%.2f", balance) + "$"
            availableFunds = balance.toDouble()
        }

        binding?.buySellButton?.setText(R.string.asset_action_buy)
        binding?.sellingPrice?.setVisibility(View.INVISIBLE)
    }


    @SuppressLint("SetTextI18n")
    fun setUISell() {
        lateinit var allAssets : MutableList<Asset>
        Observable.fromCallable {
            allAssets = model.allAssetsDirectly()
            allAssets
        }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                val stockIndex = allAssets.indexOfFirst { it.ticker == model.selectedAsset.ticker }
                if (stockIndex != -1) {
                    val assets = allAssets
                    val stockData = assets[stockIndex]
                    binding?.currentAction?.text = "Sell ${model.selectedAsset.ticker}"
                    binding?.availableFunds?.text =
                        "Available assets: ${stockData.qty.toInt()}"
                    availableAssets = stockData.qty.toLong()
                    _purchasePrice = stockData.purchasePrice
                    val purchasePrice = String.format("%.2f", stockData.purchasePrice)
                    binding?.purchasePrice?.text = "Purchase price: $purchasePrice$"
                    //binding?.purchasePrice?.setVisibility(View.VISIBLE)
                    binding?.sellingPrice?.setVisibility(View.VISIBLE)
                } else {
                    binding?.availableFunds?.text = "Available assets: N/A"
                    // TODO: No stock error handling
                    binding?.buySellButton?.isEnabled = false
                    binding?.warningText?.text = "No assets are available"
                }
                binding?.buySellButton?.setText(R.string.asset_action_sell)
                binding?.currentAction?.text = "Sell ${model.selectedAsset.ticker}"
            }
            .doOnError { error ->
                Log.e("Observable", "Error: ${error.message}", error)
            }
            .subscribe()
    }

    @SuppressLint("SetTextI18n")
    fun refreshPrice() {
        val input = binding?.quantityEdittext?.text
        if (input.isNullOrEmpty()) {
            return
        }
        var quantity = input.toString().toLong()
        if (actionIsBuy) {
            if (model.selectedAssetPrice*(quantity+1) > availableFunds) {
                quantity = floor((availableFunds / model.selectedAssetPrice)).toLong()
            }
            binding?.purchasePrice?.text =
                "Transaction price:" + String.format(
                    "%.2f",
                    model.selectedAssetPrice * quantity
                ) + "$"
        } else {
            if (availableAssets < quantity) {
                quantity = availableAssets
            }
            binding?.sellingPrice?.text =
                "Selling price:" + String.format(
                    "%.2f",
                    model.selectedAssetPrice * quantity
                ) + "$"
        }
    }

    fun refreshPriceDelayed() {
        debounceTimer?.cancel()
        debounceTimer = Timer()
        debounceTimer?.schedule(object : TimerTask() {
            override fun run() {
                refreshPrice()
            }
        }, 1000) // Set a delay of 500 milliseconds
    }


    fun increment() {
        val input =  binding?.quantityEdittext?.text
        if (input.isNullOrEmpty()) {
            return
        }
        var quantity = input.toString().toLong()
        println(quantity)
        if (!actionIsBuy) {
            if (availableAssets > quantity) {
                quantity++
              //  binding?.sellingPrice?.text = "Selling price: " + String.format("%.2f", model.selectedStockPrice*quantity) + "$"
            }
        } else {
            if (model.selectedAssetPrice*(quantity+1) <= availableFunds) {
                quantity++
            }
        }
        refreshPriceDelayed()
        binding?.quantityEdittext?.setText(quantity.toString())
    }

    fun decrement() {
        val input =  binding?.quantityEdittext?.text
        if (input.isNullOrEmpty()) {
            return
        }
        var quantity = input.toString().toLong()
        if (quantity > 0) {
            quantity--
        }
        refreshPriceDelayed()
        binding?.quantityEdittext?.setText(quantity.toString())
     }

    @SuppressLint("SetTextI18n")
    fun updatePrice(){
        fixedRateTimer("timer", true, 0L, 2000L) {
            CoroutineScope(Dispatchers.IO).launch {
                model.getSelectedAssetPrice()
                binding?.currentPrice?.text =
                    "Current price: " + String.format("%.2f", model.selectedAssetPrice) + "$"
            }
        }
    }

    private fun buySellAction(){
        val input =  binding?.quantityEdittext?.text
        if (input.isNullOrEmpty()) {
            Toast.makeText(context, "Wrong input", Toast.LENGTH_SHORT).show()
            return
        }
        if (actionIsBuy) {
            if (model.selectedAssetPrice * input.toString().toLong() <= availableFunds) {
               if (input.toString().toLong() != 0L) {
                   model.purchaseAsset(input.toString().toDouble())
                   Toast.makeText(context, "Successful purchase", Toast.LENGTH_SHORT).show()
                   findNavController().navigate(R.id.action_assetActionFragment_to_PortfolioFragment)
                   } else {
                   Toast.makeText(context, "Wrong input", Toast.LENGTH_SHORT).show()
                   }
               } else {
                Toast.makeText(context, "Not enough funds!", Toast.LENGTH_SHORT).show()
            }
        } else {
            if (input.toString().toLong() != 0L) {
                model.sellAsset(input.toString().toDouble())
                if (_purchasePrice > 0.0) {
                    findNavController().navigate(R.id.action_assetActionFragment_to_PortfolioFragment)
                }
            } else {
                Toast.makeText(context, "Wrong input", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}
