package com.example.stonks

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.stonks.databinding.AssetActionBinding
import com.example.stonks.model.Model
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
    private var actionIsBuy = false
    private var debounceTimer: Timer? = null
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

    fun setUIBuy() {
        model.selectedStock.observe(viewLifecycleOwner) { stock ->
            binding?.currentAction?.text = "Buy ${stock}"
        }
        binding?.availableFunds?.text = "Available funds: " +
                String.format("%.2f", model.wallet.balance()) + "$"
        availableFunds = model.wallet.balance()
        binding?.buySellButton?.setText(R.string.asset_action_buy)
        binding?.sellingPrice?.setVisibility(View.INVISIBLE)
    }


    @SuppressLint("SetTextI18n")
    fun setUISell() {
        model.selectedStock.observe(viewLifecycleOwner) { stock ->
            val stockIndex = model.wallet.assets().indexOfFirst { it.ticker == stock }
            if (stockIndex != -1) {
                val assets = model.wallet.assets()
                val stockData = assets[stockIndex]
                binding?.currentAction?.text = "Sell ${stock}"
                binding?.availableFunds?.text =
                    "Available assets: ${stockData.qty}"
                availableAssets = stockData.qty
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
            binding?.currentAction?.text = "Sell ${stock}"
        }
    }

    fun refreshPrice() {
        val input = binding?.quantityEdittext?.text
        if (input.isNullOrEmpty()) {
            return
        }
        var quantity = input.toString().toLong()
        println(quantity)
        if (actionIsBuy) {
            if (model.selectedStockPrice*(quantity+1) > availableFunds) {
                quantity = floor((availableFunds / model.selectedStockPrice)).toLong()
            }
            binding?.purchasePrice?.text =
                "Purchase price:" + String.format(
                    "%.2f",
                    model.selectedStockPrice * quantity
                ) + "$"
           // println(quantity)
          //  binding?.quantityEdittext?.setText(quantity.toString())
        } else {
            if (availableAssets < quantity) {
                quantity = availableAssets
            }
            binding?.sellingPrice?.text =
                "Selling price:" + String.format(
                    "%.2f",
                    model.selectedStockPrice * quantity
                ) + "$"
        }
        println("quantity, maybe corrected")
        println(quantity)
       // binding?.quantityEdittext?.notify()
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
            if (model.selectedStockPrice*(quantity+1) <= availableFunds) {
                quantity++
              //  binding?.purchasePrice?.text = "Purchase price: " +  String.format("%.2f", model.selectedStockPrice*quantity) + "$"
           //     println(quantity)
           //     println(quantity*model.selectedStockPrice)
            }
        }
        refreshPriceDelayed()
        binding?.quantityEdittext?.setText(quantity.toString())
        //println(input.toString().toDouble())
    }

    fun decrement() {
        val input =  binding?.quantityEdittext?.text
        if (input.isNullOrEmpty()) {
            return
        }
        var quantity = input.toString().toLong()
        if (quantity > 0) {
            quantity--
         //   if (actionIsBuy) {
           //     println(quantity)
             //   println(quantity*model.selectedStockPrice)
             //   binding?.purchasePrice?.text = "Purchase price:" +  String.format("%.2f", model.selectedStockPrice*quantity) + "$"
            //} else {

            //}
        }
        refreshPriceDelayed()
        binding?.quantityEdittext?.setText(quantity.toString())
       // println(input.toString().toDouble())
    }

    fun updatePrice(){
        fixedRateTimer("timer", true, 0L, 2000L) {
        CoroutineScope(Dispatchers.IO).launch {
            model.getSelectedStockPrice()
            binding?.currentPrice?.text =
                "Current price: " + String.format("%.2f", model.selectedStockPrice) + "$"
            if (actionIsBuy) {
            //    binding?.purchasePrice?.text = "Purchase price:" + model.selectedStockPrice * qty
            }
        }
         //   if (input != null) {
           //                   binding?.buySellButton?.isEnabled =
             //                   !(actionIsBuy && model.selectedStockPrice * input.toString().toInt() <= availableFunds)
                // }
            }
    }

    fun buySellAction(){
        val input =  binding?.quantityEdittext?.text
        if (input.isNullOrEmpty()) {
            Toast.makeText(context, "Wrong input", Toast.LENGTH_SHORT).show()
            return
        }
        if (actionIsBuy) {
            if (model.selectedStockPrice * input.toString().toLong() <= availableFunds) {
               if (input.toString().toLong() != 0L) {
                   model.purchaseStock(input.toString().toLong())
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
                model.sellStock(input.toString().toLong())
            } else {
                Toast.makeText(context, "Wrong input", Toast.LENGTH_SHORT).show()
            }
            findNavController().navigate(R.id.action_assetActionFragment_to_PortfolioFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}
