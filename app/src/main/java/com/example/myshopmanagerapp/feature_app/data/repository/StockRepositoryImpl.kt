package com.example.myshopmanagerapp.feature_app.data.repository

import com.example.myshopmanagerapp.core.*
import com.example.myshopmanagerapp.core.Constants.ZERO
import com.example.myshopmanagerapp.core.Functions.addItemQuantities
import com.example.myshopmanagerapp.core.Functions.getTotalNumberOfUnits
import com.example.myshopmanagerapp.core.Functions.subtractItemQuantities
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Functions.toTimestamp
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelEntity
import com.example.myshopmanagerapp.core.TypeConverters.toUniqueIds
import com.example.myshopmanagerapp.core.TypeConverters.toUniqueIdsJson
import com.example.myshopmanagerapp.feature_app.MyShopManagerApp
import com.example.myshopmanagerapp.feature_app.data.local.AppDatabase
import com.example.myshopmanagerapp.feature_app.data.local.entities.stock.StockEntity
import com.example.myshopmanagerapp.feature_app.domain.model.AddStockInfo
import com.example.myshopmanagerapp.feature_app.domain.model.ItemValue
import com.example.myshopmanagerapp.feature_app.domain.model.PeriodDropDownItem
import com.example.myshopmanagerapp.feature_app.domain.model.UniqueId
import com.example.myshopmanagerapp.feature_app.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.util.*

class StockRepositoryImpl(
    private val appDatabase: AppDatabase
): StockRepository{
    override fun getAllStocks(): Flow<Resource<StockEntities?>> = flow{
        emit(Resource.Loading())
        val allStocks: List<StockEntity>?
        try {
            allStocks = appDatabase.stockDao.getAllStocks()?.sortedByDescending { it.date }
            emit(Resource.Success(allStocks))
        }catch (e: Exception){
            emit(Resource.Error(
                message = "Couldn't load all Stocks from Database",
                data = emptyList()
            ))
        }
    }

    override suspend fun addStock(stock: StockEntity):Flow<Resource<String>> = flow{
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val uniquePersonnelId = userPreferences.getPersonnelInfo.first()?.toPersonnelEntity()?.uniquePersonnelId.toNotNull()
            val personnel = appDatabase.personnelDao.getPersonnel(uniquePersonnelId)

            val importantFieldsAreMissing = stock.stockQuantityInfo.isEmpty()
            val dateNow = Date().time
            val inventoryItem = appDatabase.stockDao.getInventoryItem(stock.uniqueInventoryItemId)
            val itemStocks = appDatabase.stockDao.getItemStocks(stock.uniqueInventoryItemId) ?: emptyList()
            val lastStock = itemStocks.maxByOrNull{ it.date }
            val lastStockIsLesser = (inventoryItem?.totalNumberOfUnits ?: ZERO) < stock.stockQuantityInfo.getTotalNumberOfUnits()

            when(true) {
                importantFieldsAreMissing -> {
                    emit(Resource.Error("Unable to add stock.\nYou need to add a valid quantity of items"))
                }
                (inventoryItem == null) ->{
                    emit(Resource.Error("Unable to add stock.\nCould not find the associated inventory item"))
                }
                (personnel == null) ->{
                    emit(Resource.Error("Unable to add stock.\nCould not load the personnel info"))
                }
                (stock.date > dateNow) -> {
                    emit(Resource.Error("Unable to add stock.\nSelected date hasn't come yet"))
                }
                (stock.date < (lastStock?.date ?: ZERO.toLong())) -> {
                    emit(Resource.Error("Unable to add stock.\nOther stock(s) have been taken before this particular date"))
                }
                (lastStockIsLesser) -> {
                    emit(Resource.Error("Unable to add stock.\nCould not add stock because the last stock taken for this item is lesser than current quantity" +
                            "\nThere wouldn't be enough stock to subtract from if current stock quantity is more than previous available stock" +
                            "\nIn order to add stock, please add it as inventory")
                    )
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to add stock." +
                            "\nPlease log in into an account to save info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to add stock." +
                            "\nPlease log in into a personnel account to save info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is entering the information"))
                }
                else -> {
                    val changeInNumberOfUnits = ZERO.minus(stock.totalNumberOfUnits)
                    val latestInventoryStock = itemStocks.filter { it.isInventoryStock }.maxByOrNull { it.date }

                    val thisStock = stock.copy(
                        uniquePersonnelId = uniquePersonnelId,
                        totalNumberOfUnits = stock.stockQuantityInfo.getTotalNumberOfUnits(),
                        dateOfLastStock = lastStock?.date,
                        changeInNumberOfUnits = changeInNumberOfUnits,
                        unitCostPrice = latestInventoryStock?.unitCostPrice.toNotNull(),
                        totalCostPrice = stock.stockQuantityInfo.getTotalNumberOfUnits().times(latestInventoryStock?.unitCostPrice.toNotNull()),
                        isInventoryStock = false,
                    )
                    val stockInfo = itemStocks.sortedByDescending { it.date }.plus(thisStock)

                    val updatedInventoryItem = inventoryItem.copy(
                        stockInfo = stockInfo,
                        itemQuantityInfo = stock.stockQuantityInfo,
                        totalNumberOfUnits = stock.stockQuantityInfo.getTotalNumberOfUnits(),
                    )
                    appDatabase.stockDao.addStockWithInventoryItemUpdate(thisStock, updatedInventoryItem)

                    val addedStockIdsJson = AdditionEntityMarkers(context).getAddedStockIds.first().toNotNull()
                    val addedStockIds = addedStockIdsJson.toUniqueIds().plus(UniqueId(stock.uniqueStockId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedStockIds(addedStockIds.toUniqueIdsJson())

                    val addedInventoryItemIdsJson = AdditionEntityMarkers(context).getAddedInventoryItemIds.first().toNotNull()
                    val addedInventoryItemIds = addedInventoryItemIdsJson.toUniqueIds().plus(UniqueId(inventoryItem.uniqueInventoryItemId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedInventoryItemIds(addedInventoryItemIds.toUniqueIdsJson())

                    val updatedInventoryItemIdsJson = ChangesEntityMarkers(context).getChangedInventoryItemIds.first().toNotNull()
                    val updatedInventoryItemIds = updatedInventoryItemIdsJson.toUniqueIds().plus(UniqueId(inventoryItem.uniqueInventoryItemId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedInventoryItemIds(updatedInventoryItemIds.toUniqueIdsJson())

                    emit(Resource.Success("Stock added successfully"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Couldn't add stock\nError message: ${e.message}"))
        }
    }

    override suspend fun getShopExpectedRevenueReturn(addStockInfo: AddStockInfo): Flow<Resource<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMostInventoryItems(addStockInfo: AddStockInfo): Flow<Resource<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun getLeastInventoryItems(addStockInfo: AddStockInfo): Flow<Resource<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun addStocks(stocks: StockEntities) {
        try {
            val allStocks = appDatabase.stockDao.getAllStocks() ?: emptyList()
            val allUniqueStockIds = allStocks.map { it.uniqueStockId }
            val newStocks = stocks.filter { !allUniqueStockIds.contains(it.uniqueStockId) }
            appDatabase.stockDao.addStocks(newStocks)
        }catch (_: Exception){}
    }

    override suspend fun getStock(uniqueStockId: String): StockEntity? {
        return appDatabase.stockDao.getStock(uniqueStockId)
    }

    override suspend fun updateStock(stock: StockEntity):Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val uniquePersonnelId = userPreferences.getPersonnelInfo.first()?.toPersonnelEntity()?.uniquePersonnelId.toNotNull()
            val personnel = appDatabase.personnelDao.getPersonnel(uniquePersonnelId)

            val importantFieldsAreMissing = stock.stockQuantityInfo.isEmpty()
            val dateNow = Date().time
            val oldStock = appDatabase.stockDao.getStock(stock.uniqueStockId)
            val inventoryItem = appDatabase.stockDao.getInventoryItem(stock.uniqueInventoryItemId)
            val itemStocks = appDatabase.stockDao.getItemStocks(stock.uniqueInventoryItemId) ?: emptyList()
            val lastStock = itemStocks.maxByOrNull{ it.date }

            val oldItemQuantity = inventoryItem?.totalNumberOfUnits ?: ZERO
            val currentItemQuantity = oldItemQuantity.plus(oldStock?.totalNumberOfUnits ?: ZERO)
            val lastStockIsLesser = currentItemQuantity < stock.stockQuantityInfo.getTotalNumberOfUnits()

            when(true) {
                importantFieldsAreMissing -> {
                    emit(Resource.Error("Unable to update stock.\nYou need to add a valid quantity of items"))
                }
                (oldStock == null) ->{
                    emit(Resource.Error("Unable to update stock.\nCould not find the stock details you want to update"))
                }
                (personnel == null) ->{
                    emit(Resource.Error("Unable to update stock.\nCould not find the personnel"))
                }
                (inventoryItem == null) ->{
                    emit(Resource.Error("Unable to update stock.\nCould not find the associated inventory item"))
                }
                (stock.date > dateNow) -> {
                    emit(Resource.Error("Unable to update stock.\nSelected date hasn't come yet"))
                }
                (stock.date < (lastStock?.date ?: ZERO.toLong())) -> {
                    emit(Resource.Error("Unable to update stock.\nOther stock(s) have been taken before this particular date"))
                }
                (oldStock != lastStock) -> {
                    emit(Resource.Error(
                        "Unable to update stock.\nThis is because this stock that you are trying to update is not the latest stock of this item." +
                                "\nOther stocks depend on the values of this stock for calculations hence updating this will cause some inconsistencies" +
                                "\nYou need to delete all the inventories and stocks made for this item after ${stock.date.toDateString()}"))
                }
                (lastStockIsLesser) -> {
                    emit(Resource.Error("Unable to update stock.\nCould not add stock because the last stock taken for this item is lesser than current quantity" +
                            "\nThere wouldn't be enough stock to subtract from if current stock quantity is more than previous available stock" +
                            "\nIn order to add stock, please add it as inventory")
                    )
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to update stock." +
                            "\nPlease log in into an account to update info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to update stock." +
                            "\nPlease log in into a personnel account to update info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is entering the information"))
                }
                else -> {
                    val changeInNumberOfUnits = ZERO.minus(stock.totalNumberOfUnits)

                    val thisStock = stock.copy(
                        uniquePersonnelId = uniquePersonnelId,
                        changeInNumberOfUnits = changeInNumberOfUnits,
                    )
                    val oldQuantityInfo = inventoryItem.itemQuantityInfo ?: emptyList()
                    val unUpdatedQuantityInfo = oldQuantityInfo.subtractItemQuantities(oldStock.stockQuantityInfo)
                    val updatedQuantityInfo = unUpdatedQuantityInfo.addItemQuantities(thisStock.stockQuantityInfo)

                    val oldStockInfo = inventoryItem.stockInfo ?: emptyList()
                    val stockInfo = oldStockInfo.minus(oldStock).plus(thisStock)
                    val updatedInventoryItem = inventoryItem.copy(
                        stockInfo = stockInfo,
                        itemQuantityInfo = updatedQuantityInfo,
                        totalNumberOfUnits = updatedQuantityInfo.getTotalNumberOfUnits(),
                    )
                    appDatabase.stockDao.updateStockWithInventoryItemUpdate(thisStock, updatedInventoryItem)

                    val addedStockIdsJson = AdditionEntityMarkers(context).getAddedStockIds.first().toNotNull()
                    val addedStockIds = addedStockIdsJson.toUniqueIds().plus(UniqueId(stock.uniqueStockId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedStockIds(addedStockIds.toUniqueIdsJson())

                    val updatedStockIdsJson = ChangesEntityMarkers(context).getChangedStockIds.first().toNotNull()
                    val updatedStockIds = updatedStockIdsJson.toUniqueIds().plus(UniqueId(stock.uniqueStockId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedStockIds(updatedStockIds.toUniqueIdsJson())

                    val addedInventoryItemIdsJson = AdditionEntityMarkers(context).getAddedInventoryItemIds.first().toNotNull()
                    val addedInventoryItemIds = addedInventoryItemIdsJson.toUniqueIds().plus(UniqueId(inventoryItem.uniqueInventoryItemId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedInventoryItemIds(addedInventoryItemIds.toUniqueIdsJson())

                    val updatedInventoryItemIdsJson = ChangesEntityMarkers(context).getChangedInventoryItemIds.first().toNotNull()
                    val updatedInventoryItemIds = updatedInventoryItemIdsJson.toUniqueIds().plus(UniqueId(inventoryItem.uniqueInventoryItemId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedInventoryItemIds(updatedInventoryItemIds.toUniqueIdsJson())

                    emit(Resource.Success("Stock updated successfully"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Unknown error! Unable to update stock"))
        }
    }
    
    override suspend fun deleteStock(stockId: Int) {
        appDatabase.stockDao.deleteStock(stockId)
    }

    override suspend fun deleteStock(uniqueStockId: String):Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val personnelIsAdmin = userPreferences.getPersonnelInfo.first()?.toPersonnelEntity()?.hasAdminRights?: false

            val stock = appDatabase.stockDao.getStock(uniqueStockId)
            val inventoryItem = appDatabase.stockDao.getInventoryItem(stock?.uniqueInventoryItemId.toNotNull())
            val itemStocks = appDatabase.stockDao.getItemStocks(stock?.uniqueInventoryItemId.toNotNull()) ?: emptyList()
            val lastStock = itemStocks.maxByOrNull{ it.date }
            val penultimateStock = itemStocks.minus(lastStock).maxByOrNull { it?.date ?: ZERO.toLong() }

            when(true) {

                (stock == null) -> {
                    emit(Resource.Error("Unable to delete stock.\nCould not find the stock details you want to delete"))
                }
                (inventoryItem == null) -> {
                    emit(Resource.Error("Unable to delete stock.\nCould not find the associated inventory item"))
                }
                (stock != lastStock) -> {
                    emit(
                        Resource.Error(
                        "Unable to delete stock.\nThis is because this stock that you are trying to delete is not the latest stock of this item." +
                                "\nOther stocks depend on the values of this stock for calculations hence updating this will cause some inconsistencies" +
                                "\nYou need to delete all the inventories and stocks made for this item after ${stock.date.toDateString()}"
                        )
                    )
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to delete stock." +
                            "\nPlease log in into an account to delete info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to delete stock." +
                            "\nPlease log in into a personnel account to delete info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is deleting the information"))
                }
                !personnelIsAdmin->{
                    emit(Resource.Error("Unable to delete stock." +
                            "\nThis is because this personnel has not been given privileges to delete information" +
                            "\nTo be able to delete any information, you must have admin rights" +
                            "\nNB: Only an admin can bestow admin rights"))
                }
                else -> {
                    val updatedInventoryItem = inventoryItem.copy(
                        stockInfo = itemStocks.minus(stock),
                        itemQuantityInfo = penultimateStock?.stockQuantityInfo,
                        totalNumberOfUnits = penultimateStock?.totalNumberOfUnits ?: ZERO,
                    )
                    appDatabase.stockDao.deleteStockWithItemUpdate(uniqueStockId, updatedInventoryItem)

                    val addedStockIdsJson = AdditionEntityMarkers(context).getAddedStockIds.first().toNotNull()
                    val addedStockIds = addedStockIdsJson.toUniqueIds().filter { it.uniqueId != uniqueStockId }.toSet().toList()
                    AdditionEntityMarkers(context).saveAddedStockIds(addedStockIds.toUniqueIdsJson())

                    val deletedStockIdsJson = ChangesEntityMarkers(context).getChangedStockIds.first().toNotNull()
                    val deletedStockIds = deletedStockIdsJson.toUniqueIds().plus(UniqueId(stock.uniqueStockId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedStockIds(deletedStockIds.toUniqueIdsJson())

                    val addedInventoryItemIdsJson = AdditionEntityMarkers(context).getAddedInventoryItemIds.first().toNotNull()
                    val addedInventoryItemIds = addedInventoryItemIdsJson.toUniqueIds().plus(UniqueId(inventoryItem.uniqueInventoryItemId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedInventoryItemIds(addedInventoryItemIds.toUniqueIdsJson())

                    val updatedInventoryItemIdsJson = ChangesEntityMarkers(context).getChangedInventoryItemIds.first().toNotNull()
                    val updatedInventoryItemIds = updatedInventoryItemIdsJson.toUniqueIds().plus(UniqueId(inventoryItem.uniqueInventoryItemId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedInventoryItemIds(updatedInventoryItemIds.toUniqueIdsJson())

                    emit(Resource.Success("Stock deleted successfully"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Unknown error! Couldn't delete stock"))
        }
    }


    override suspend fun getShopValue(period: PeriodDropDownItem): Flow<Resource<ItemValue>> = flow{
        emit(Resource.Loading())
        try {
            val allStocks = appDatabase.stockDao.getAllStocks()?.sortedBy { it.date } ?: emptyList()
            if (period.isAllTime) {
                val lastStocks = allStocks.groupBy { it.uniqueInventoryItemId }.mapValues { it.value.maxByOrNull { _stock-> _stock.date } }.values.filterNotNull()
                val totalCost = lastStocks.sumOf { it.totalCostPrice.toNotNull() }
                emit(Resource.Success(ItemValue("Total Inventory Cost", totalCost)))
            }else{
                val firstDate = period.firstDate.toTimestamp()
                val lastDate = period.lastDate.toTimestamp()
                val allFilteredStocks = allStocks.filter { it.date in firstDate .. lastDate }
                val lastFilteredStocks = allFilteredStocks.groupBy { it.uniqueInventoryItemId }.mapValues { it.value.maxByOrNull { _stock-> _stock.date } }.values.filterNotNull()
                val totalCost = lastFilteredStocks.sumOf { it.totalCostPrice.toNotNull() }
                emit(Resource.Success(ItemValue("Total Inventory Cost", totalCost)))
            }
        }catch (e:Exception){
            emit(Resource.Error("Could not get value"))
        }
    }

    override suspend fun getExpectedSalesAmount(period: PeriodDropDownItem): Flow<Resource<ItemValue>> = flow{
        emit(Resource.Loading())
        try {
            val allInventoryItems = appDatabase.inventoryItemDao.getAllInventoryItems() ?: emptyList()
            val allStocks = appDatabase.stockDao.getAllStocks()?.sortedBy { it.date } ?: emptyList()
            if (period.isAllTime) {
                val lastStocks = allStocks.groupBy { it.uniqueInventoryItemId }.mapValues { it.value.maxByOrNull { _stock-> _stock.date } }.values.filterNotNull()
                val totalSalesAmount = lastStocks.sumOf { stock->
                    val inventory = allInventoryItems.firstOrNull { it.uniqueInventoryItemId == stock.uniqueInventoryItemId }
                    val sellingPrice = inventory?.currentSellingPrice.toNotNull()
                    stock.totalNumberOfUnits.times(sellingPrice)
                }
                emit(Resource.Success(ItemValue("Expected Sales Amount", totalSalesAmount)))
            }else{
                val firstDate = period.firstDate.toTimestamp()
                val lastDate = period.lastDate.toTimestamp()
                val allFilteredStocks = allStocks.filter { it.date in firstDate .. lastDate }
                val lastStocks = allFilteredStocks.groupBy { it.uniqueInventoryItemId }.mapValues { it.value.maxByOrNull { _stock-> _stock.date } }.values.filterNotNull()
                val totalSalesAmount = lastStocks.sumOf { stock->
                    val inventory = allInventoryItems.firstOrNull { it.uniqueInventoryItemId == stock.uniqueInventoryItemId }
                    val sellingPrice = inventory?.currentSellingPrice.toNotNull()
                    stock.totalNumberOfUnits.times(sellingPrice)
                }
                emit(Resource.Success(ItemValue("Expected Sales Amount", totalSalesAmount)))
            }
        }catch (e: Exception){
            emit(Resource.Error(
                message = "Unknown error",
                data = null
            ))
        }
    }

    override suspend fun deleteAllStocks() {
        appDatabase.stockDao.deleteAllStocks()
    }


}