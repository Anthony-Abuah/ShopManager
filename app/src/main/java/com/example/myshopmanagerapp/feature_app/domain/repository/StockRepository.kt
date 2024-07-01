package com.example.myshopmanagerapp.feature_app.domain.repository

import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.core.StockEntities
import com.example.myshopmanagerapp.feature_app.data.local.entities.stock.StockEntity
import com.example.myshopmanagerapp.feature_app.domain.model.AddStockInfo
import kotlinx.coroutines.flow.Flow


interface StockRepository {

    fun getAllStocks(): Flow<Resource<StockEntities?>>

    suspend fun addStock(stock: StockEntity): Flow<Resource<String>>

    suspend fun getShopExpectedRevenueReturn(addStockInfo: AddStockInfo): Flow<Resource<String>>

    suspend fun getMostInventoryItems(addStockInfo: AddStockInfo): Flow<Resource<String>>

    suspend fun getLeastInventoryItems(addStockInfo: AddStockInfo): Flow<Resource<String>>

    suspend fun addStocks(stocks: StockEntities)

    suspend fun getStock(uniqueStockId: String): StockEntity?

    suspend fun updateStock(stock: StockEntity): Flow<Resource<String>>

    suspend fun deleteStock(stockId: Int)

    suspend fun deleteStock(uniqueStockId: String): Flow<Resource<String>>

    suspend fun deleteAllStocks()

}
