package com.example.stonks.model

fun calculateROI(initialInvestment: Double, currentInvestment: Double): Double {
    return ((currentInvestment - initialInvestment) / initialInvestment) * 100
}

