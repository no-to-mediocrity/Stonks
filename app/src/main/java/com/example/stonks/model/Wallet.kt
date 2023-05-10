package com.example.stonks.model

class Wallet (private val assets: MutableList<Stock>) {
    private var money = 0.0
    private var assetsPurchasePrice = 0.0

    fun deposit(amount: Double) {
        println(money)
        money += amount
        println(money)
    }

    fun deduct(amount: Double) {
        println("deduct")
        println(amount)
        money -= amount
        println(money)
    }

    fun balance(): Double {
        return money
    }

    fun assets(): MutableList<Stock> {
        return assets
    }

    fun init(){
        calculateAssetsPurchasePrice()
    }

    fun assetsPurchasePrice() : Double{
        return assetsPurchasePrice
    }

    fun specificAssetPurchasePrice(ticker: String): Double {
        val stockIndex = assets.indexOfFirst { it.ticker == ticker }
        if (stockIndex != -1) {
            return assets[stockIndex].purchasePrice
        } else {
            return 0.0
        }
    }

    fun calculateAssetsPurchasePrice() {
        var sum = 0.0
        assets.forEach { stock ->
            if (stock.ticker != "USD") {
                sum += stock.purchasePrice * stock.qty
            }
        }
        assetsPurchasePrice = sum
    }


    fun purchaseStock(ticker: String, price: Double, qty: Long, isin:String, name: String) {
       // println("purchase")
       // println(ticker)
        if (price * qty < money) {
            val stockIndex = assets.indexOfFirst { it.ticker == ticker }
            if (stockIndex != -1) {
                assets[stockIndex].purchase(price, qty)
            } else {
                val newStock = Stock(ticker, isin, name)
                newStock.purchase(price, qty)
                assets.add(newStock)
            }
            deduct(qty * price)
        }
        calculateAssetsPurchasePrice()
    }

    fun sellStock(ticker: String, price: Double, qty: Long) {
        val stockIndex = assets.indexOfFirst { it.ticker == ticker }
        if (stockIndex != -1) {
            val stock = assets[stockIndex]
            if (stock.qty >= qty) {
                stock.sell(price, qty)
                deposit(qty * price)
                println(qty * price)
                if (stock.qty == 0L) {
                    assets.remove(stock)
                }
            } else {
                // TODO: error handling 
            }
        }
        calculateAssetsPurchasePrice()
    }

    fun assetCount(): Int{
        return assets.size
    }
}