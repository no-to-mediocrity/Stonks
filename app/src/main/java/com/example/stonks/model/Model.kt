@file:Suppress("LocalVariableName", "LocalVariableName", "ObjectPropertyName")

package com.example.stonks.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.finnhub.api.apis.DefaultApi
import io.finnhub.api.infrastructure.ApiClient
import io.finnhub.api.models.Quote
import kotlinx.coroutines.*


const val _apikey = "cicbigpr01ql0uqkt0igcicbigpr01ql0uqkt0j0"

@Suppress("LocalVariableName", "LocalVariableName")
class Model : ViewModel() {
    var ifActionIsBuy = MutableLiveData<Boolean>()
    lateinit var selectedAsset: Asset
    var selectedAssetPrice = 0.0
    var portfolioValue = 0.0
    var _portfolioValue = MutableLiveData<Double>()
    var investmentValue = 0.0
    var _investmentValue = MutableLiveData<Double>()
    var ROI = 0.0
    var _ROI = MutableLiveData<Double>()
    var portfolioEvaluated = false
    lateinit var portfolioPrices: Map<String, Double>
    var _portfolioPrices = MutableLiveData<Map<String, AssetData>>()
    val apiClient = DefaultApi()
    var shop = mutableListOf<Stock>()
    var allAssets = MutableLiveData<List<Asset>>()
    var balance = MutableLiveData<Double>()
    lateinit var assetDao : AssetDao

    fun returnPortfolioPrices() : Map<String, Double> {
        if (portfolioEvaluated) {
            return portfolioPrices
        } else {
            return mapOf(Pair(" ", 0.0))
        }
    }

    fun purchaseAsset(qty: Double){
        viewModelScope.launch(Dispatchers.IO) {
            if (selectedAssetPrice != 0.0) {
                assetDao.purchaseAsset(
                    selectedAsset.ticker,
                    selectedAssetPrice,
                    qty,
                    selectedAsset.isin,
                    selectedAsset.name
                )
                evalutatePortfolio()
            }
        }
    }

    fun balanceDirectly() : Double {
        return assetDao.balance()
    }

    fun balance() {
        viewModelScope.launch(Dispatchers.IO) {
            val tempBalance = assetDao.balance()
            withContext(Dispatchers.Main) {
                balance.value = tempBalance
            }
        }
    }
    fun allAssets() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Default) {
                allAssets.value = assetDao.getAll()
            }
        }
    }

    fun allAssetsDirectly () : MutableList<Asset>{
       return assetDao.getAll()
    }

    fun sellAsset(qty:Double){
            viewModelScope.launch(Dispatchers.IO) {
                if (selectedAssetPrice != 0.0) {
                assetDao.sellAsset(selectedAsset.ticker, selectedAssetPrice, qty)
                evalutatePortfolio()
            }
        }
    }


    fun loadState(_assetDao: AssetDao) {
        ApiClient.apiKey["token"] = _apikey
        assetDao = _assetDao
        viewModelScope.launch(Dispatchers.IO) {
            if (assetDao.getAllQty().isEmpty()) {
                assetDao.depositMoney(10000.0)
            }
        }
    }

    fun selectAsset(asset: Asset){
        selectedAsset = asset
    }

    fun setActionBuy(boolean: Boolean) {
        ifActionIsBuy.value = boolean
    }

    fun getSelectedAssetPrice() {
        var response: Quote
        try {
            response = apiClient.quote(selectedAsset.ticker)
            selectedAssetPrice = response.c!!.toDouble()
        } catch (e:Exception) {
            println(e) //TODO proper error handling
            return
        }
    }

    fun evalutatePortfolio() {
        val AssetPrices = mutableMapOf<String, Double>()
        val _AssetPrices = mutableMapOf<String, AssetData>()
        var sum = 0.0
        val assets = assetDao.getAll()
        assets.forEach { Asset ->
            if (Asset.ticker != "USD") {
                try {
                    val response = apiClient.quote(Asset.ticker)
                    AssetPrices[Asset.ticker] = response.c!!.toDouble()
                    val evaluatedAsset = AssetData(Asset.qty, Asset.purchasePrice, response.c!!.toDouble())
                    evaluatedAsset.getROI()
                    evaluatedAsset.getPercentROI()
                    _AssetPrices[Asset.ticker] = evaluatedAsset
                    sum += response.c!!.toDouble() * Asset.qty
                } catch (e: Exception) {
                    println(e) //TODO proper exception handling
                    return
                }
            }
        }
        investmentValue = sum
        _investmentValue.postValue(sum)
        sum += assetDao.balance()
        ROI = calculateROI(assetDao.assetsPurchasePrice(), sum)
        _ROI.postValue(ROI)
        portfolioPrices = AssetPrices
        _portfolioPrices.postValue(_AssetPrices)
        portfolioValue = sum
        _portfolioValue.postValue(sum)
        portfolioEvaluated = true
    }

    override fun onCleared() {
        super.onCleared()
        CoroutineScope(Dispatchers.IO).cancel()  // Cancel any pending tasks when the ViewModel is cleared
    }
}