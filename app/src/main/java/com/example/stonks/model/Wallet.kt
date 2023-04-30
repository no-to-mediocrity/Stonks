package com.example.stonks.model

class Wallet (private val assets: MutableList<Stock>) {
   private var money = 0.0

    fun deposit(amount: Double) {
        money += amount
    }

    fun deduct(amount: Double) {
        money -= amount
    }

    fun balance(): Double {
        return money
    }

    fun assets(): MutableList<Stock> {
        return assets
    }

    fun purchaseStock(ticker: String, price: Double, qty: Int) {
        if (price * qty < money) {
            val stockIndex = assets.indexOfFirst { it.ticker == ticker }
            if (stockIndex != -1) {
                assets[stockIndex].purchase(price, qty)
            } else {
                val newStock = Stock(ticker)
                newStock.purchase(price, qty)
                assets.add(newStock)
            }
            deduct(qty * price)
        }
    }

    fun sellStock(ticker: String, price: Double, qty: Int) {
        val stockIndex = assets.indexOfFirst { it.ticker == ticker }
        if (stockIndex != -1) {
            val stock = assets[stockIndex]
            if (stock.qty >= qty) {
                stock.sell(price, qty)
                deposit(qty * price)
            } else {
                // TODO: error handling 
            }
        }
    }

    fun assetCount(): Int{
        return assets.size
    }
}