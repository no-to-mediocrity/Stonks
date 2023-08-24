package com.example.stonks.model

import androidx.room.Entity

const val tinkoffImageSource = "https://invest-brands.cdn-tinkoff.ru/"
const val tinkoffImageSize = "x160.png"

@Entity(primaryKeys = ["ticker", "purchasePrice", "qty", "image"])
class Stock (var ticker: String, var isin: String, var name: String) {
    var purchasePrice = 0.0
    var qty = 0L
    var image = ""

    fun setImage() {
        image = tinkoffImageSource + isin + tinkoffImageSize
    }

}
