package com.example.stonks.model

class Stock (val ticker:String) {

    var purchasePrice = 0.0
    var qty = 0
    var image = ""

    fun purchase(price: Double, buy_qty: Int) {
        if (price <= 0.0) {
            return // to do error
        } else if ((purchasePrice == 0.0) || (qty == 0)) { //first purchase
            purchasePrice = price
            qty += buy_qty
        } else if ((purchasePrice > 0.0) && (qty > 0)) { //other purchases
            purchasePrice = ((purchasePrice * qty) + (price * buy_qty)) / (qty + buy_qty)
            qty += buy_qty
        }
    }

    fun sell(price: Double, sell_qty: Int) {
        if ((price <= 0.0) || (qty == 0) || (sell_qty > qty)) {
            return
        } else {
            qty -= sell_qty
        }
    }
}