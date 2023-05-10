package com.example.stonks.adapter

import com.example.stonks.model.Stock
import com.example.stonks.model.Wallet

class DataDemo {
    val demowallet = Wallet(mutableListOf<Stock>())

    fun set() {
        demowallet.deposit(10000.0)
        demowallet.purchaseStock("USD", 0.0, 0,"","")
        demowallet.purchaseStock("AAPL", 100.0, 2, "US0378331005", "Apple")
        demowallet.purchaseStock("TSLA", 400.0, 3,"US88160R1014","Tesla Motors")
        demowallet.purchaseStock("NFLX", 150.0, 4,"US64110L1061", "Netflix")
        demowallet.purchaseStock("GOOGL", 120.0, 2,"US02079K3059", "Alphabet Class A")
        demowallet.purchaseStock("DIS", 100.0, 3,"US2546871060", "Walt Disney")
        demowallet.purchaseStock("MCD", 102.0, 2,"US5801351017", "McDonald's")
    }

    fun size() :Int {
        return demowallet.assetCount()
    }
}