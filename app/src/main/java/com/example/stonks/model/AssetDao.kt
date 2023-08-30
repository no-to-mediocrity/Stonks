package com.example.stonks.model

import androidx.room.*

@Dao
interface AssetDao {
    @Query("SELECT * FROM Asset")
    fun getAll(): MutableList<Asset>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(asset: MutableList<Asset>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(asset: Asset)

    @Query("SELECT purchasePrice FROM Asset")
    fun getAllPurchasePrices(): List<Double>

    @Query("SELECT qty FROM Asset")
    fun getAllQty(): List<Double>

    @Delete
    fun delete(Asset: Asset)

    @Query("SELECT * FROM Asset WHERE ticker = :ticker")
    fun findByTicker(ticker: String): Asset?

    @Query("UPDATE Asset SET purchasePrice = :purchasePrice WHERE ticker = :ticker")
    fun updatePurchasePrice(ticker: String, purchasePrice: Double)

    @Query("SELECT purchasePrice FROM Asset WHERE ticker = :ticker")
    fun getPurchasePrice(ticker: String): Double?

    @Query("SELECT COUNT(*) FROM Asset")
    fun getEntryCount(): Int

    //Transaction
    @Transaction
    fun addUSD(){
        var usd = Asset("USD", "0", "USD", 1.0, 0.0)
        insert(usd)
    }

    @Transaction
    fun depositMoney(amount: Double) {
        if (amount <= 0.0) {
            return
            println("error") //TODO proper error handling
        }
        var asset = findByTicker("USD")
        if (asset == null) {
            var usd = Asset("USD", "0", "USD", 1.0, amount)
            insert(usd)
            return
        }
        asset.qty += amount
        insert(asset)
        return
    }

    @Transaction
    fun deductMoney(amount: Double) {
        if (amount <= 0.0) {
            return
            println("error") //TODO proper error handling
        }
        var asset = findByTicker("USD")
        if (asset == null) {
            addUSD()
            println("error") //TODO proper error handling
            return
        }
        if (amount > asset.qty) {
            println("error") //TODO proper error handling
            return
        }
        asset.qty -= amount
        insert(asset)
        return
    }

    @Transaction
    fun balance(): Double {
        var asset = findByTicker("USD")
        if (asset == null) {
            addUSD()
            return 0.0
        }
        return asset.qty
    }

    @Transaction
    fun assetsPurchasePrice() : Double {
        var assetsPurchasePrice = 0.0
        val assetsPurchaseAllPrices = getAllPurchasePrices()
        val assetsQty = getAllQty() // cutting corners here, less safe, but faster
        val multipliedAssets = assetsPurchaseAllPrices.zip(assetsQty) { price, qty ->
            price * qty
        }
        if (multipliedAssets != null) {
            assetsPurchasePrice += multipliedAssets.sum()
        }
        return assetsPurchasePrice
    }

    @Transaction
    fun purchaseAsset(ticker: String, price: Double, qty: Double, isin:String, name: String) {
        if ((price * qty < balance()) || (ticker == "USD")) {
            var asset = findByTicker(ticker)
            if (asset == null) {
                asset = Asset(ticker, isin, name, price, qty)
            } else {
                asset.purchasePrice = ((asset.purchasePrice * asset.qty) + (price * qty)) / (asset.qty + qty)
                asset.qty += qty
            }
            insert(asset)
            deductMoney(qty * price)
        } else {
            //TODO error handling
            println("error")
        }
    }

    @Transaction
    fun sellAsset(ticker: String, price: Double, qty: Double) {
        var asset = findByTicker(ticker)
        if ((asset == null) || (asset.qty == 0.0) || (price <= 0.0) || (ticker == "USD")) {
            println("error, no assets to sell") // TODO proper error handling
            return
        }
        if (qty > asset.qty) {
            println("selling more than available") // TODO proper error handling
            return
        }
        asset.qty -= qty
        depositMoney(qty * price)
        if (asset.qty == 0.0) {
            delete(asset)
        } else {
            insert(asset)
        }
    }
}


