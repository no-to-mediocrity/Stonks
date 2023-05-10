package com.example.stonks.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.finnhub.api.apis.DefaultApi
import io.finnhub.api.infrastructure.ApiClient
import io.finnhub.api.models.Quote

const val apikey = "chbat19r01qmso50r3vgchbat19r01qmso50r400"
class Model(): ViewModel() {

    var ifActionIsBuy = MutableLiveData<Boolean>()
    var selectedStock = MutableLiveData("")
    var _selectedStock = ""
    var selectedStockIsin = ""
    var selectedStockName = ""
    var selectedStockPrice = 0.0
    var selectedStockPurchasePrice = 0.0
    var portfolioValue = 0.0
    var investmentValue = 0.0
    var ROI = 0.0
    var portfolioEvaluated = false
    lateinit var portfolioPrices:Map<String, Double>
    lateinit var wallet:Wallet
    //lateinit var stockShelf:
    val apiClient = DefaultApi()
    var demoLoaded = false
    var shop = mutableListOf<Stock>()

    fun purchaseStock(qty:Long){
        if (selectedStockPrice != 0.0) {
            println(wallet.balance())
            println(selectedStock.value.toString())
            wallet.purchaseStock(selectedStock.value.toString(), selectedStockPrice, qty, selectedStockIsin,selectedStockName)
            evalutatePortfolio()
            println(wallet.balance())
        }
    }

    fun sellStock(qty:Long){
        if (selectedStockPrice != 0.0) {
            println(selectedStock.value.toString())
            wallet.sellStock(selectedStock.value.toString(), selectedStockPrice, qty)
            evalutatePortfolio()
        }
    }

    fun loadState(walletData:Wallet) {
        ApiClient.apiKey["token"] = apikey
        wallet = walletData
        wallet.init()
        wallet.deduct(200.00)
    }

    fun selectStock(stock: String, isin: String, name:String){
        selectedStock = MutableLiveData(stock)
        _selectedStock = stock
        selectedStockIsin = isin
        selectedStockName = name
        selectedStockPurchasePrice = wallet.specificAssetPurchasePrice(stock)
    }
    fun setActionBuy(boolean: Boolean) {
        ifActionIsBuy.value = boolean
    }

    fun getSelectedStockPrice() {
       // var err = Exception()
        var response = Quote()
        try {
            response = apiClient.quote(_selectedStock)
            selectedStockPrice = response.c!!.toDouble()
        } catch (e:Exception) {
           println(e)
            return
        }
    }

    fun evalutatePortfolio() {
            val stockPrices = mutableMapOf<String, Double>()
            var sum = 0.0
            wallet.assets().forEach { stock ->
                if (stock.ticker != "USD") {
                    try {
                        val response = apiClient.quote(stock.ticker)
                        stockPrices[stock.ticker] = response.c!!.toDouble()
                        sum += response.c!!.toDouble() * stock.qty
                    } catch (e:Exception) {
                        println(e)
                        return
                    }
                }
            }
            investmentValue = sum
            ROI = calculateROI(wallet.assetsPurchasePrice(),sum)
            sum += wallet.balance()
            portfolioPrices = stockPrices
            portfolioValue = sum
            portfolioEvaluated = true
        }

    fun calculateROI(initialInvestment: Double, currentInvestment: Double): Double {
        return ((currentInvestment - initialInvestment) / initialInvestment) * 100
    }



}

