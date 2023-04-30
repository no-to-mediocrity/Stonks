package com.example.stonks.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Model (private val wallet: Wallet) : ViewModel() {

    private val _selectedStock = MutableLiveData<String>()
    val selectedStock: LiveData<String> = _selectedStock

    private val _ifActionisBuy = MutableLiveData<Boolean>()
    val ifActionisBuy : LiveData<Boolean> = _ifActionisBuy


    fun getStockGraphData(asset: String){

    }

    fun getStockPrice(asset: String) {

    }

    fun updateWallet(wallet: Wallet){
        
    }
}
