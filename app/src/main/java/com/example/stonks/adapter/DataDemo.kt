package com.example.stonks.adapter

import com.example.stonks.model.Stock
import com.example.stonks.model.Wallet

class DataDemo {
    val demowallet = Wallet(mutableListOf<Stock>())

    fun set() {
        demowallet.deposit(10000.0)
        demowallet.purchaseStock("AAPL", 10.0, 2)
        demowallet.purchaseStock("TSLA", 20.0, 2)
        demowallet.purchaseStock("NFLX", 15.0, 2)
        demowallet.purchaseStock("GOOGL", 12.0, 2)
    }

    fun size() :Int {
        return demowallet.assetCount()
    }
}