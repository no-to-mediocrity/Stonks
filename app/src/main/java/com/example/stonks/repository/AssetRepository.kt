
package com.example.stonks.repository


import com.example.stonks.model.AssetDao
import javax.inject.Inject

class AssetRepository @Inject constructor(private val assetDao: AssetDao) {
    fun getAll() = assetDao.getAll()
    fun purchaseAsset(ticker: String, price: Double, qty: Double, isin: String, name: String) =
        assetDao.purchaseAsset(ticker, price, qty, isin, name)
    fun sellAsset(ticker: String, price: Double, qty: Double) = assetDao.sellAsset(ticker, price, qty)
    fun balance() = assetDao.balance()
    fun assetsPurchasePrice() = assetDao.assetsPurchasePrice()
}

