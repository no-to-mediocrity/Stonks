@file:Suppress("PropertyName", "PropertyName", "PropertyName", "PropertyName", "PropertyName")

package com.example.stonks.model

    @Suppress("PropertyName", "PropertyName", "PropertyName", "PropertyName", "PropertyName",
        "PropertyName"
    )
    class AssetData(var qty: Double, var purchasePrice: Double, currentPrice: Double) {
        var percentROI = 0.0 //todo change initial value
        var ROI = 0.0 //todo change initial value
        var totalPurchasePrice = purchasePrice * qty
        var totalCurrentPrice = currentPrice * qty

        fun getPercentROI(){
            percentROI = calculateROI(totalPurchasePrice, totalCurrentPrice)
        }

        fun getROI(){
            ROI =  totalCurrentPrice - totalPurchasePrice
        }
    }
