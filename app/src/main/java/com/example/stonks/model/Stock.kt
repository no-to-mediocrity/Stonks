package com.example.stonks.model

const val tinkoffImageSource = "https://invest-brands.cdn-tinkoff.ru/"
const val tinkoffImageSize = "x160.png"


class Stock (var ticker:String, var isin:String, var name: String) {

    var purchasePrice = 0.0
    var qty = 0L
    var image = ""

    fun purchase(price: Double, buy_qty: Long) {
        if (price <= 0.0) {
            return // to do error
        } else if ((purchasePrice == 0.0) || (qty == 0L)) { //first purchase
            purchasePrice = price
            qty += buy_qty
        } else if ((purchasePrice > 0.0) && (qty > 0L)) { //other purchases
            purchasePrice = ((purchasePrice * qty) + (price * buy_qty)) / (qty + buy_qty)
            qty += buy_qty
        }
        if (isin != "") {
          setImage()
        }
    }

    fun sell(price: Double, sell_qty: Long) {
        if ((price <= 0.0) || (qty == 0L) || (sell_qty > qty)) {
            return
        } else {
            qty -= sell_qty
        }
    }

    fun setImage() {
        image = tinkoffImageSource + isin + tinkoffImageSize
    }

}