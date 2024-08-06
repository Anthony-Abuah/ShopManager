package com.example.myshopmanagerapp.feature_app.data.remote.dto.stock

import com.example.myshopmanagerapp.feature_app.domain.model.UniqueId


data class SmartStocks(
    val stocks: List<StockInfoDto>,
    val uniqueStockIds: List<UniqueId>
)