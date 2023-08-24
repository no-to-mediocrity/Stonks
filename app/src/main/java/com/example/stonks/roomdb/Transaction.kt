package com.example.stonks.roomdb

import androidx.room.*
import java.util.*

@Entity(primaryKeys = ["ticker", "isin", "exchange", "name", "date", "qty", "price"])
class Transaction  (val ticker: String, val isin: String, val exchange: String, val name: String, val date: Date, val qty: Long, val price: Long) {
}

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transaction")
    fun getAll(): MutableList<Transaction>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(stocks: MutableList<Transaction>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(transaction: Transaction)

    @Delete
    fun delete(transaction: Transaction)
}
