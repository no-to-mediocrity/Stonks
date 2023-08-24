package com.example.stonks.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "Asset", primaryKeys = ["ticker", "isin"])
data class Asset(
    @ColumnInfo(name = "ticker") var ticker: String,
    @ColumnInfo(name = "isin") val isin: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "purchasePrice") var purchasePrice: Double,
    @ColumnInfo(name = "qty") var qty: Double
)


