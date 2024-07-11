package com.example.myshopmanagerapp.feature_app.data.repository

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.*
import com.example.myshopmanagerapp.core.Constants.Unit
import com.example.myshopmanagerapp.core.Constants.ZERO
import com.example.myshopmanagerapp.core.Functions.addItemQuantities
import com.example.myshopmanagerapp.core.Functions.dateToTimestamp
import com.example.myshopmanagerapp.core.Functions.generateUniqueStockId
import com.example.myshopmanagerapp.core.Functions.getTotalNumberOfUnits
import com.example.myshopmanagerapp.core.Functions.shortened
import com.example.myshopmanagerapp.core.Functions.subtractItemQuantities
import com.example.myshopmanagerapp.core.Functions.toCompanyEntity
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.Functions.toEllipses
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Functions.toTimestamp
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelEntity
import com.example.myshopmanagerapp.core.TypeConverters.toUniqueIds
import com.example.myshopmanagerapp.core.TypeConverters.toUniqueIdsJson
import com.example.myshopmanagerapp.feature_app.MyShopManagerApp
import com.example.myshopmanagerapp.feature_app.data.local.AppDatabase
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory.InventoryEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_items.InventoryItemEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_stock.InventoryStockEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.stock.StockEntity
import com.example.myshopmanagerapp.feature_app.domain.model.*
import com.example.myshopmanagerapp.feature_app.domain.repository.InventoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate

class InventoryRepositoryImpl(
    private val appDatabase: AppDatabase
): InventoryRepository{
    override fun getAllInventories(): Flow<Resource<InventoryEntities?>> = flow{
        emit(Resource.Loading())
        val allInventories: List<InventoryEntity>?
        try {
            allInventories = appDatabase.inventoryDao.getAllInventories()?.sortedByDescending { it.date }
            emit(Resource.Success(allInventories))
        }catch (e: Exception){
            emit(Resource.Error(
                message = "Couldn't load all Inventories from Database",
                data = emptyList()
            ))
        }
    }

    override suspend fun addInventoryWithStock(
        inventory: InventoryEntity,
    ) : Flow<Resource<String?>> = flow{
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val uniquePersonnelId = userPreferences.getPersonnelInfo.first()?.toPersonnelEntity()?.uniquePersonnelId.toNotNull()
            val personnel = appDatabase.personnelDao.getPersonnel(uniquePersonnelId)

            val importantFieldsAreMissing = (inventory.totalNumberOfUnits < 1) || inventory.quantityInfo.isEmpty()
            val inventoryItem = appDatabase.inventoryItemDao.getInventoryItem(inventory.uniqueInventoryItemId)
            val dateNow = LocalDate.now().toDate().time
            val itemStocks = appDatabase.stockDao.getItemStocks(inventory.uniqueInventoryItemId) ?: emptyList()
            val lastStock = itemStocks.maxByOrNull{ it.date }

            when(true){
                importantFieldsAreMissing ->{
                    emit(Resource.Error("Unable to add inventory.\nYou need to add a valid quantity of items"))
                }
                (inventoryItem == null) ->{
                    emit(Resource.Error("Unable to add inventory.\nCould not find the associated inventory item"))
                }
                (inventory.date > dateNow) ->{
                    emit(Resource.Error("Unable to add inventory.\nThe selected date hasn't come yet"))
                }
                ((inventory.date <= (lastStock?.date ?: ZERO.toLong()))) ->{
                    emit(Resource.Error("Unable to add inventory.\nOther stocks of this item has been taken before this chosen date"))
                }
                (personnel == null)->{
                    emit(Resource.Error("Unable to add revenue\nCould not get personnel details"))
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to add revenue." +
                            "\nPlease log in into an account to save info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to revenue." +
                            "\nPlease log in into a personnel account to save info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is entering the information"))
                }
                else->{
                    val thisInventoryQuantity = inventory.quantityInfo
                    val dateString = inventory.date.toDateString()
                    val uniqueStockId = generateUniqueStockId("${inventoryItem.inventoryItemName}-${dateString}_")
                    val lastStockQuantityInfo = lastStock?.stockQuantityInfo ?: emptyList()
                    val quantityInfo = thisInventoryQuantity.addItemQuantities(lastStockQuantityInfo)
                    val stock = StockEntity(
                        0,
                        uniqueStockId = uniqueStockId,
                        date = inventory.date,
                        dayOfWeek = inventory.dayOfWeek,
                        uniquePersonnelId = uniquePersonnelId,
                        uniqueInventoryItemId = inventory.uniqueInventoryItemId,
                        stockQuantityInfo = quantityInfo,
                        totalNumberOfUnits = quantityInfo.getTotalNumberOfUnits(),
                        unitCostPrice = inventory.unitCostPrice,
                        totalCostPrice = inventory.unitCostPrice.times(quantityInfo.getTotalNumberOfUnits()),
                        dateOfLastStock = lastStock?.date,
                        changeInNumberOfUnits = inventory.totalNumberOfUnits,
                        isInventoryStock = true,
                        otherInfo = inventory.otherInfo
                    )

                    val uniqueInventoryStockId = generateUniqueStockId(uniqueStockId + inventory.uniqueInventoryId)
                    val inventoryStock = InventoryStockEntity(
                        0,
                        uniqueInventoryStockId,
                        inventory.uniqueInventoryId,
                        uniqueStockId
                    )
                    val stockInfo = itemStocks.plus(stock)
                    val itemPrices = inventoryItem.costPrices ?: emptyList()
                    val costPrices = itemPrices.plus(Price(inventory.date, Unit, inventory.unitCostPrice))

                    val thisInventoryItemQuantityInfo = inventoryItem.itemQuantityInfo ?: emptyList()

                    val thisInventoryItem = inventoryItem.copy(
                        stockInfo = stockInfo,
                        itemQuantityInfo = thisInventoryItemQuantityInfo.addItemQuantities(thisInventoryQuantity),
                        totalNumberOfUnits = thisInventoryItemQuantityInfo.addItemQuantities(thisInventoryQuantity).getTotalNumberOfUnits(),
                        costPrices = costPrices,
                        currentCostPrice = inventory.unitCostPrice
                    )
                    appDatabase.inventoryDao.addInventoryWithStock(
                        stock,
                        inventory.copy(uniquePersonnelId = uniquePersonnelId),
                        inventoryStock,
                        thisInventoryItem
                    )

                    val addedStockIdsJson = AdditionEntityMarkers(context).getAddedStockIds.first().toNotNull()
                    val addedStockIds = addedStockIdsJson.toUniqueIds().plus(UniqueId(stock.uniqueStockId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedStockIds(addedStockIds.toUniqueIdsJson())

                    val addedInventoryIdsJson = AdditionEntityMarkers(context).getAddedInventoryIds.first().toNotNull()
                    val addedInventoryIds = addedInventoryIdsJson.toUniqueIds().plus(UniqueId(inventory.uniqueInventoryId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedInventoryIds(addedInventoryIds.toUniqueIdsJson())

                    val addedInventoryStockIdsJson = AdditionEntityMarkers(context).getAddedInventoryStockIds.first().toNotNull()
                    val addedInventoryStockIds = addedInventoryStockIdsJson.toUniqueIds().plus(UniqueId(inventoryStock.uniqueInventoryStockId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedInventoryStockIds(addedInventoryStockIds.toUniqueIdsJson())

                    val addedInventoryItemIdsJson = AdditionEntityMarkers(context).getAddedInventoryItemIds.first().toNotNull()
                    val addedInventoryItemIds = addedInventoryItemIdsJson.toUniqueIds().plus(UniqueId(inventoryItem.uniqueInventoryItemId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedInventoryItemIds(addedInventoryItemIds.toUniqueIdsJson())

                    val updatedInventoryItemIdsJson = ChangesEntityMarkers(context).getChangedInventoryItemIds.first().toNotNull()
                    val updatedInventoryItemIds = updatedInventoryItemIdsJson.toUniqueIds().plus(UniqueId(inventoryItem.uniqueInventoryItemId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedInventoryItemIds(updatedInventoryItemIds.toUniqueIdsJson())

                    emit(Resource.Success("Inventory successfully added"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Unable to add inventory\nError Message: ${e.message}"))
        }
    }

    override suspend fun addInventory(inventory: InventoryEntity) {
        appDatabase.inventoryDao.addInventory(inventory)
    }

    override suspend fun addInventories(inventories: InventoryEntities) {
        try {
            val allInventories = appDatabase.inventoryDao.getAllInventories() ?: emptyList()
            val allUniqueInventoryIds = allInventories.map { it.uniqueInventoryId }
            val newInventories = inventories.filter { !allUniqueInventoryIds.contains(it.uniqueInventoryId) }
            appDatabase.inventoryDao.addInventories(newInventories)
        }catch (_: Exception){}
    }

    override suspend fun getInventory(uniqueInventoryId: String): InventoryEntity? {
        return appDatabase.inventoryDao.getInventory(uniqueInventoryId)
    }


    override suspend fun updateInventory(inventory: InventoryEntity) {
        appDatabase.inventoryDao.updateInventory(inventory)
    }

    override suspend fun updateInventoryWithStock(
        inventory: InventoryEntity
    ) : Flow<Resource<String?>> = flow{
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val uniquePersonnelId = userPreferences.getPersonnelInfo.first()?.toPersonnelEntity()?.uniquePersonnelId.toNotNull()
            val personnel = appDatabase.personnelDao.getPersonnel(uniquePersonnelId)

            val importantFieldsAreMissing =
                (inventory.totalNumberOfUnits < 1) || inventory.quantityInfo.isEmpty()
            val inventoryItem = appDatabase.inventoryItemDao.getInventoryItem(inventory.uniqueInventoryItemId)
            val oldInventory = appDatabase.inventoryDao.getInventory(inventory.uniqueInventoryId)
            val inventoryStockEntity = appDatabase.inventoryStockDao.getInventoryStockByInventoryId(inventory.uniqueInventoryId)
            val oldStock = appDatabase.stockDao.getStock(inventoryStockEntity?.uniqueStockId.toNotNull())

            val dateNow = LocalDate.now().toDate().time
            val itemStocks = appDatabase.stockDao.getItemStocks(inventory.uniqueInventoryItemId) ?: emptyList()
            val lastStock = itemStocks.maxByOrNull { it.date }
            val penultimateStock = itemStocks.minus(lastStock).maxByOrNull { it?.date ?: ZERO.toLong() }

            when (true) {
                importantFieldsAreMissing -> {
                    emit(Resource.Error("Unable to update inventory.\nYou need to add a valid quantity of items"))
                }
                (oldInventory == null) -> {
                    emit(Resource.Error("Unable to update inventory.\nCould not find this inventory's details"))
                }
                (inventoryItem == null) -> {
                    emit(Resource.Error("Unable to update inventory.\nCould not find the associated inventory item"))
                }
                (inventoryStockEntity == null) -> {
                    emit(Resource.Error("Unable to update inventory.\nCould not find the associated inventory stock"))
                }
                (oldStock == null) -> {
                    emit(Resource.Error("Unable to update inventory.\nCould not find the associated stock"))
                }
                (oldStock != lastStock) -> {
                    emit(Resource.Error(
                    "Unable to update inventory.\nThis inventory is associated stock has other associated stocks." +
                            "\nYou need to delete all the inventories and stocks made for this item after ${inventory.date.toDateString()}"))
                }
                (inventory.date > dateNow) -> {
                    emit(Resource.Error("Unable to update inventory.\nThe selected date hasn't come yet"))
                }
                ((inventory.date <= (penultimateStock?.date ?: ZERO.toLong()))) -> {
                    emit(Resource.Error("Unable to update inventory.\nOther stocks of this item has been taken before this chosen date"))
                }
                (personnel == null) -> {
                    emit(Resource.Error("Unable to update inventory\nCould not get personnel details"))
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to update inventory." +
                            "\nPlease log in into an account to update info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to update inventory." +
                            "\nPlease log in into a personnel account to update info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is entering the information"))
                }
                else -> {
                    val thisInventoryQuantity = inventory.quantityInfo
                    val lastStockQuantityInfo = penultimateStock?.stockQuantityInfo ?: emptyList()
                    val quantityInfo = thisInventoryQuantity.addItemQuantities(lastStockQuantityInfo)

                    val updatedStock = oldStock.copy(
                        date = inventory.date,
                        uniquePersonnelId = uniquePersonnelId,
                        dayOfWeek = inventory.dayOfWeek,
                        stockQuantityInfo = quantityInfo,
                        totalNumberOfUnits = quantityInfo.getTotalNumberOfUnits(),
                        unitCostPrice = inventory.unitCostPrice,
                        totalCostPrice = quantityInfo.getTotalNumberOfUnits().times(inventory.unitCostPrice),
                        changeInNumberOfUnits = inventory.totalNumberOfUnits,
                        otherInfo = inventory.otherInfo
                    )

                    val stockInfo = itemStocks.minus(oldStock).plus(updatedStock)
                    val itemPrices = inventoryItem.costPrices ?: emptyList()
                    val oldCostPrice = itemPrices.maxByOrNull { it.date }
                    val thisPrices = itemPrices.minus(oldCostPrice).filterNotNull()
                    val prices = thisPrices.plus(Price(inventory.date, Unit, inventory.unitCostPrice))

                    val inventoryQuantityInfo = inventoryItem.itemQuantityInfo ?: emptyList()
                    val updatedQuantityInfo = inventoryQuantityInfo.subtractItemQuantities(oldInventory.quantityInfo).addItemQuantities(inventory.quantityInfo)

                    val updatedInventoryItem = inventoryItem.copy(
                        stockInfo = stockInfo,
                        itemQuantityInfo = updatedQuantityInfo,
                        totalNumberOfUnits = updatedQuantityInfo.getTotalNumberOfUnits(),
                        costPrices = prices,
                        currentCostPrice = inventory.unitCostPrice
                    )
                    appDatabase.inventoryDao.updateInventoryWithStock(
                        updatedStock,
                        inventory.copy(uniquePersonnelId = uniquePersonnelId),
                        updatedInventoryItem
                    )

                    val addedStockIdsJson = AdditionEntityMarkers(context).getAddedStockIds.first().toNotNull()
                    val addedStockIds = addedStockIdsJson.toUniqueIds().plus(UniqueId(updatedStock.uniqueStockId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedStockIds(addedStockIds.toUniqueIdsJson())

                    val updatedStockIdsJson = ChangesEntityMarkers(context).getChangedStockIds.first().toNotNull()
                    val updatedStockIds = updatedStockIdsJson.toUniqueIds().plus(UniqueId(updatedStock.uniqueStockId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedStockIds(updatedStockIds.toUniqueIdsJson())

                    val addedInventoryIdsJson = AdditionEntityMarkers(context).getAddedInventoryIds.first().toNotNull()
                    val addedInventoryIds = addedInventoryIdsJson.toUniqueIds().plus(UniqueId(inventory.uniqueInventoryId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedInventoryIds(addedInventoryIds.toUniqueIdsJson())

                    val updatedInventoryIdsJson = ChangesEntityMarkers(context).getChangedInventoryIds.first().toNotNull()
                    val updatedInventoryIds = updatedInventoryIdsJson.toUniqueIds().plus(UniqueId(inventory.uniqueInventoryId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedInventoryIds(updatedInventoryIds.toUniqueIdsJson())

                    val addedInventoryItemIdsJson = AdditionEntityMarkers(context).getAddedInventoryItemIds.first().toNotNull()
                    val addedInventoryItemIds = addedInventoryItemIdsJson.toUniqueIds().plus(UniqueId(inventoryItem.uniqueInventoryItemId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedInventoryItemIds(addedInventoryItemIds.toUniqueIdsJson())

                    val updatedInventoryItemIdsJson = ChangesEntityMarkers(context).getChangedInventoryItemIds.first().toNotNull()
                    val updatedInventoryItemIds = updatedInventoryItemIdsJson.toUniqueIds().plus(UniqueId(inventoryItem.uniqueInventoryItemId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedInventoryItemIds(updatedInventoryItemIds.toUniqueIdsJson())

                    emit(Resource.Success("Inventory successfully updated"))
                }
            }
        } catch (e: Exception){
            emit(Resource.Error("Unable to update inventory\nError Message: ${e.message}"))
        }
    }

    override suspend fun deleteInventory(inventoryId: Int) {
        appDatabase.inventoryDao.deleteInventory(inventoryId)
    }

    override suspend fun deleteInventory(uniqueInventoryId: String) {
        appDatabase.inventoryDao.deleteInventory(uniqueInventoryId)
    }

    override suspend fun deleteInventoryWithStock(
        uniqueInventoryId: String
    ): Flow<Resource<String?>> = flow{
        emit(Resource.Loading())
        try {
            val personnel = appDatabase.revenueDao.getRevenue(uniqueInventoryId)
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val personnelIsAdmin = userPreferences.getPersonnelInfo.first()?.toPersonnelEntity()?.hasAdminRights?: false

            val inventory = appDatabase.inventoryDao.getInventory(uniqueInventoryId)
            val inventoryItem = appDatabase.inventoryItemDao.getInventoryItem(inventory?.uniqueInventoryItemId.toNotNull())
            val inventoryStockEntity = appDatabase.inventoryStockDao.getInventoryStockByInventoryId(inventory?.uniqueInventoryId.toNotNull())
            val oldStock = appDatabase.stockDao.getStock(inventoryStockEntity?.uniqueStockId.toNotNull())

            val itemStocks = appDatabase.stockDao.getItemStocks(inventory?.uniqueInventoryItemId.toNotNull()) ?: emptyList()
            val lastStock = itemStocks.maxByOrNull { it.date }

            when (true) {
                (personnel == null) -> {
                    emit(Resource.Error("Unable to delete inventory.\nCould not find this personnel info"))
                }
                (inventory == null) -> {
                    emit(Resource.Error("Unable to delete inventory.\nCould not find this inventory's details"))
                }
                (inventoryItem == null) -> {
                    emit(Resource.Error("Unable to delete inventory.\nCould not find the associated inventory item"))
                }
                (inventoryStockEntity == null) -> {
                    emit(Resource.Error("Unable to delete inventory.\nCould not find the associated inventory stock"))
                }
                (oldStock == null) -> {
                    emit(Resource.Error("Unable to delete inventory.\nCould not find the associated stock"))
                }
                (oldStock != lastStock) -> {
                    emit(
                        Resource.Error(
                            "Unable to delete inventory.\nThis inventory's associated stock has other associated stocks." +
                                    "\nYou need to delete all the inventories and stocks made for this item after ${inventory.date.toDateString()}"
                        )
                    )
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to delete inventory." +
                            "\nPlease log in into an account to delete info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to delete inventory." +
                            "\nPlease log in into a personnel account to delete info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is deleting the information"))
                }
                !personnelIsAdmin->{
                    emit(Resource.Error("Unable to delete inventory." +
                            "\nThis is because this personnel has not been given privileges to delete information" +
                            "\nTo be able to delete any information, you must have admin rights" +
                            "\nNB: Only an admin can bestow admin rights"))
                }
                else-> {
                    val oldStockInfo = inventoryItem.stockInfo ?: emptyList()
                    val updatedStockInfo = oldStockInfo.minus(oldStock)
                    val remainingItemQuantity = inventoryItem.itemQuantityInfo?.subtractItemQuantities(inventory.quantityInfo)
                    val costPrices = inventoryItem.costPrices ?: emptyList()
                    val deletedCostPrice = costPrices.maxByOrNull { it.date }
                    val updatedCostPrices = costPrices.minus(deletedCostPrice).filterNotNull().sortedByDescending {(it.date) }
                    val currentCostPrice = updatedCostPrices.maxByOrNull { it.date }?.price
                    val updatedInventoryItem = inventoryItem.copy(
                        stockInfo = updatedStockInfo,
                        itemQuantityInfo = remainingItemQuantity,
                        totalNumberOfUnits = remainingItemQuantity?.getTotalNumberOfUnits(),
                        costPrices = updatedCostPrices,
                        currentCostPrice = currentCostPrice
                    )
                    val uniqueInventoryStockId = inventoryStockEntity.uniqueInventoryStockId
                    appDatabase.inventoryDao.deleteInventoryWithStock(
                        oldStock.uniqueStockId,
                        uniqueInventoryId,
                        uniqueInventoryStockId,
                        updatedInventoryItem
                    )

                    val addedInventoryStockIdsJson = AdditionEntityMarkers(context).getAddedInventoryStockIds.first().toNotNull()
                    val addedInventoryStockIds = addedInventoryStockIdsJson.toUniqueIds().filter { it.uniqueId != uniqueInventoryStockId }.toSet().toList()
                    AdditionEntityMarkers(context).saveAddedInventoryStockIds(addedInventoryStockIds.toUniqueIdsJson())

                    val deletedInventoryStockIdsJson = ChangesEntityMarkers(context).getChangedInventoryStockIds.first().toNotNull()
                    val deletedInventoryStockIds = deletedInventoryStockIdsJson.toUniqueIds().plus(UniqueId(uniqueInventoryStockId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedInventoryStockIds(deletedInventoryStockIds.toUniqueIdsJson())

                    val addedStockIdsJson = AdditionEntityMarkers(context).getAddedStockIds.first().toNotNull()
                    val addedStockIds = addedStockIdsJson.toUniqueIds().filter { it.uniqueId != oldStock.uniqueStockId }.toSet().toList()
                    AdditionEntityMarkers(context).saveAddedStockIds(addedStockIds.toUniqueIdsJson())

                    val deletedStockIdsJson = ChangesEntityMarkers(context).getChangedStockIds.first().toNotNull()
                    val deletedStockIds = deletedStockIdsJson.toUniqueIds().plus(UniqueId(oldStock.uniqueStockId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedStockIds(deletedStockIds.toUniqueIdsJson())

                    val addedInventoryIdsJson = AdditionEntityMarkers(context).getAddedInventoryIds.first().toNotNull()
                    val addedInventoryIds = addedInventoryIdsJson.toUniqueIds().filter { it.uniqueId != inventory.uniqueInventoryId }.toSet().toList()
                    AdditionEntityMarkers(context).saveAddedInventoryIds(addedInventoryIds.toUniqueIdsJson())

                    val deletedInventoryIdsJson = ChangesEntityMarkers(context).getChangedInventoryIds.first().toNotNull()
                    val deletedInventoryIds = deletedInventoryIdsJson.toUniqueIds().plus(UniqueId(inventory.uniqueInventoryId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedInventoryIds(deletedInventoryIds.toUniqueIdsJson())

                    val addedInventoryItemIdsJson = AdditionEntityMarkers(context).getAddedInventoryItemIds.first().toNotNull()
                    val addedInventoryItemIds = addedInventoryItemIdsJson.toUniqueIds().plus(UniqueId(inventoryItem.uniqueInventoryItemId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedInventoryItemIds(addedInventoryItemIds.toUniqueIdsJson())

                    val updatedInventoryItemIdsJson = ChangesEntityMarkers(context).getChangedInventoryItemIds.first().toNotNull()
                    val updatedInventoryItemIds = updatedInventoryItemIdsJson.toUniqueIds().plus(UniqueId(inventoryItem.uniqueInventoryItemId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedInventoryItemIds(updatedInventoryItemIds.toUniqueIdsJson())

                    emit(Resource.Success("Inventory successfully deleted"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Unable to delete inventory\nError Message: ${e.message}"))
        }
    }

    override suspend fun deleteAllInventories() {
        appDatabase.inventoryDao.deleteAllInventories()
    }

    override suspend fun getItemsAndTheirQuantities(period: PeriodDropDownItem): Flow<Resource<List<ItemValue>>> = flow{
        emit(Resource.Loading())
        try {
            val allInventoryItems = appDatabase.inventoryItemDao.getAllInventoryItems() ?: emptyList()
            val getInventoryItemName = mutableMapOf<String, String>()
            allInventoryItems.forEach { item->
                getInventoryItemName[item.uniqueInventoryItemId] = item.inventoryItemName
            }
            if (period.isAllTime){
                val itemValues = allInventoryItems.map {item->
                    val quantity = item.totalNumberOfUnits.toNotNull()
                    ItemValue(item.inventoryItemName, quantity.toDouble())
                }
                emit(Resource.Success(itemValues))
            }
            else{
                val firstDate = dateToTimestamp(period.firstDate.toDate())
                val lastDate = dateToTimestamp(period.lastDate.toDate())
                val allInventories = appDatabase.inventoryDao.getAllInventories()?.filter { it.date in firstDate  .. lastDate } ?: emptyList()
                val itemValues = allInventories.map { item->
                    val quantity = item.quantityInfo.getTotalNumberOfUnits().toDouble()
                    ItemValue(getInventoryItemName[item.uniqueInventoryItemId].toNotNull(), quantity)
                }
                emit(Resource.Success(itemValues))
            }
        }catch (e: Exception){
            emit(Resource.Error(
                message = "Unknown error",
                data = null
            ))
        }

    }

    override suspend fun getTotalNumberOfItems(period: PeriodDropDownItem): Flow<Resource<ItemValue>> = flow{
        emit(Resource.Loading())
        try {
            if (period.isAllTime){
                val allInventoryItems = appDatabase.inventoryItemDao.getAllInventoryItems() ?: emptyList()
                val itemValue = ItemValue("Total number of items", allInventoryItems.size.toDouble())
                emit(Resource.Success(itemValue))
            }
            else{
                val firstDate = dateToTimestamp(period.firstDate.toDate())
                val lastDate = dateToTimestamp(period.lastDate.toDate())
                val allInventories = appDatabase.inventoryDao.getAllInventories()?.filter { it.date in firstDate  .. lastDate } ?: emptyList()
                val totalInventoryItems = allInventories.map { it.uniqueInventoryItemId }.toSet().toList()
                val itemValue = ItemValue("Total number of items", totalInventoryItems.size.toDouble())
                emit(Resource.Success(itemValue))
            }
        }catch (e: Exception){
            emit(Resource.Error(
                message = "Unknown error",
                data = null
            ))
        }
    }

    override suspend fun getShopValue(period: PeriodDropDownItem): Flow<Resource<ItemValue>> = flow{
        emit(Resource.Loading())
        try {
            val latestInventoryStocks = mutableListOf<ItemValue>()
            val allInventories = appDatabase.inventoryDao.getAllInventories()?.sortedBy { it.date } ?: emptyList()
            val allStocks = appDatabase.stockDao.getAllStocks()?.sortedBy { it.date } ?: emptyList()
            val latestStocks = allStocks.groupBy { it.uniqueInventoryItemId }.mapValues { it.value.maxByOrNull { _stock-> _stock.date } }
            val latestInventories = allInventories.groupBy { it.uniqueInventoryItemId }.mapValues { it.value.maxByOrNull { _inventory-> _inventory.date } }

            latestInventories.keys.forEach { uniqueInventoryItemId->
                latestInventoryStocks.add(
                    ItemValue(uniqueInventoryItemId,
                        latestInventories[uniqueInventoryItemId]?.totalCostPrice.toNotNull()
                    )
                )
            }
            latestStocks.keys.forEach { uniqueInventoryItemId->
                latestInventoryStocks.add(
                    ItemValue(uniqueInventoryItemId,
                        latestInventories[uniqueInventoryItemId]?.totalCostPrice.toNotNull()
                    )
                )
            }
            val lastInventories = allInventories.groupBy { it.uniqueInventoryId }.mapValues { it.value.maxByOrNull { _inventory-> _inventory.date } }.values.filterNotNull()
            if (period.isAllTime) {
                val totalCost = lastInventories.sumOf { it.totalCostPrice }
                emit(Resource.Success(ItemValue("Total Inventory Cost", totalCost)))
            }else{
                val firstDate = period.firstDate.toTimestamp()
                val lastDate = period.lastDate.toTimestamp()
                val allFilteredInventories = allInventories.filter { it.date in firstDate .. lastDate }
                val latestFilteredInventories = allFilteredInventories.groupBy { it.uniqueInventoryId }.mapValues { it.value.maxByOrNull { _inventory-> _inventory.date } }.values.filterNotNull()
                val totalCost = latestFilteredInventories.sumOf { it.totalCostPrice }
                emit(Resource.Success(ItemValue("Total Inventory Cost", totalCost)))
            }
        }catch (e:Exception){
            emit(Resource.Error("Could not get value"))
        }
    }

    override suspend fun getTotalCostValue(period: PeriodDropDownItem): Flow<Resource<ItemValue>> = flow{
        emit(Resource.Loading())
        try {
            if (period.isAllTime){
                val allInventoryItems = appDatabase.inventoryItemDao.getAllInventoryItems() ?: emptyList()
                val itemValues = allInventoryItems.map {item->
                    val itemCostValue = item.currentCostPrice.toNotNull().times(item.totalNumberOfUnits.toNotNull())
                    ItemValue(item.inventoryItemName, itemCostValue)
                }
                val itemValue = ItemValue("Total Cost", itemValues.sumOf { it.value })
                emit(Resource.Success(itemValue))
            }
            else{
                val firstDate = dateToTimestamp(period.firstDate.toDate())
                val lastDate = dateToTimestamp(period.lastDate.toDate())
                val allInventories = appDatabase.inventoryDao.getAllInventories()?.filter { it.date in firstDate  .. lastDate } ?: emptyList()
                val itemValues = allInventories.map { item->
                    val itemCostValue = item.unitCostPrice.toNotNull().times(item.quantityInfo.getTotalNumberOfUnits())
                    ItemValue("", itemCostValue)
                }
                val itemValue = ItemValue("Total Cost", itemValues.sumOf { it.value })
                emit(Resource.Success(itemValue))
            }
        }catch (e: Exception){
            emit(Resource.Error(
                message = "Unknown error",
                data = null
            ))
        }
    }

    override suspend fun getTotalExpectedSalesValue(period: PeriodDropDownItem): Flow<Resource<ItemValue>> = flow {
        emit(Resource.Loading())
        try {
            val allInventoryItems = appDatabase.inventoryItemDao.getAllInventoryItems() ?: emptyList()
            val getInventoryItem = mutableMapOf<String, InventoryItemEntity>()
            allInventoryItems.forEach { item->
                getInventoryItem[item.uniqueInventoryItemId] = item
            }
            if (period.isAllTime){
                val itemValues = allInventoryItems.map {item->
                    val itemSellingPriceValue = item.currentSellingPrice.toNotNull().times(item.totalNumberOfUnits.toNotNull())
                    ItemValue(item.inventoryItemName, itemSellingPriceValue)
                }
                val itemValue = ItemValue("Total Selling Price", itemValues.sumOf { it.value })
                emit(Resource.Success(itemValue))
            }
            else{
                val firstDate = dateToTimestamp(period.firstDate.toDate())
                val lastDate = dateToTimestamp(period.lastDate.toDate())
                val allInventories = appDatabase.inventoryDao.getAllInventories()?.filter { it.date in firstDate  .. lastDate } ?: emptyList()
                val itemValues = allInventories.map { item->
                    val inventoryItem = getInventoryItem[item.uniqueInventoryItemId]
                    val itemCostValue = inventoryItem?.currentSellingPrice.toNotNull().times(inventoryItem?.totalNumberOfUnits.toNotNull())
                    ItemValue("", itemCostValue)
                }
                val itemValue = ItemValue("Total Selling Price", itemValues.sumOf { it.value })
                emit(Resource.Success(itemValue))
            }
        }catch (e: Exception){
            emit(Resource.Error(
                message = "Unknown error",
                data = null
            ))
        }
    }

    override suspend fun getMostAvailableItem(period: PeriodDropDownItem): Flow<Resource<ItemValue>> = flow{
        emit(Resource.Loading())
        try {
            val allInventoryItems = appDatabase.inventoryItemDao.getAllInventoryItems() ?: emptyList()
            val getInventoryItem = mutableMapOf<String, InventoryItemEntity>()
            allInventoryItems.forEach { item->
                getInventoryItem[item.uniqueInventoryItemId] = item
            }
            val mostAvailableItem = allInventoryItems.maxByOrNull { it.totalNumberOfUnits.toNotNull() }
            val itemValue = ItemValue(mostAvailableItem?.inventoryItemName.toNotNull(), mostAvailableItem?.totalNumberOfUnits.toNotNull().toDouble())
            emit(Resource.Success(itemValue))
        }catch (e: Exception){
            emit(Resource.Error(
                message = "Unknown error",
                data = null
            ))
        }
    }

    override suspend fun getLeastAvailableItem(period: PeriodDropDownItem): Flow<Resource<ItemValue>> = flow{
        emit(Resource.Loading())
        try {
            val allInventoryItems = appDatabase.inventoryItemDao.getAllInventoryItems() ?: emptyList()
            val getInventoryItem = mutableMapOf<String, InventoryItemEntity>()
            allInventoryItems.forEach { item->
                getInventoryItem[item.uniqueInventoryItemId] = item
            }
            val leastAvailableItem = allInventoryItems.minByOrNull { it.totalNumberOfUnits.toNotNull() }
            val itemValue = ItemValue(leastAvailableItem?.inventoryItemName.toNotNull(), leastAvailableItem?.totalNumberOfUnits.toNotNull().toDouble())
            emit(Resource.Success(itemValue))
        }catch (e: Exception){
            emit(Resource.Error(
                message = "Unknown error",
                data = null
            ))
        }
    }

    override suspend fun generateInventoryList(
        context: Context,
        date: String,
        inventories: List<InventoryQuantityDisplayValues>,
    ): Flow<Resource<String?>> = flow{
        val pageHeight = inventories.size.plus(8).times(50)
        val pageWidth = 800
        val pdfDocument = PdfDocument()
        val paint = Paint()
        val title = Paint()
        val body = Paint()
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val centerWidth = canvas.width.div(2f)

        val userPreferences = UserPreferences(context)
        val company = userPreferences.getShopInfo.first().toCompanyEntity()
        val shopName = company?.companyName ?: "Shop route"
        val shopContact = company?.companyName ?: "Contact"
        val shopLocation = company?.companyLocation ?: "Location"


        // Write Shop route
        title.color = Color.BLACK
        title.textSize = 40f
        title.textAlign = Paint.Align.CENTER
        canvas.drawText(shopName.toEllipses(25), centerWidth, 60f, title)

        // Write Location
        body.color = Color.BLACK
        body.textSize = 25f
        body.textAlign = Paint.Align.CENTER
        canvas.drawText("Location: ${shopLocation.toEllipses(50)}", centerWidth, 95f, body)

        // Write Contact
        body.color = Color.BLACK
        body.textSize = 25f
        body.textAlign = Paint.Align.CENTER
        canvas.drawText("Contact: ${shopContact.toEllipses(50)}", centerWidth, 130f, body)

        // Draw line
        paint.color = Color.rgb(180, 180, 180)
        canvas.drawLine(20f, 140f, canvas.width.minus(20f), 141f, paint)


        // Draw Rectangle
        paint.color = Color.rgb(150, 50, 50)
        canvas.drawRect(20f, 140f, canvas.width.minus(20f), 190f, paint)


        // Write Inventory List
        body.color = Color.WHITE
        body.textSize = 25f
        body.textAlign = Paint.Align.CENTER
        canvas.drawText("Inventory List", centerWidth, 177f, body)


        // Draw Start Border
        paint.color = Color.rgb(150, 50, 50)
        canvas.drawRect(20f, 190f, 21f, 240f, paint)

        // Draw End Border
        paint.color = Color.rgb(150, 50, 50)
        canvas.drawRect(canvas.width.minus(21f), 190f, canvas.width.minus(20f), 240f, paint)


        // Write Date
        body.color = Color.BLACK
        body.textSize = 25f
        body.textAlign = Paint.Align.LEFT
        canvas.drawText("Date: ", 25f, 227f, body)
        canvas.drawText(date.toEllipses(23), 150f, 227f, body)


        // Draw Rectangle
        paint.color = Color.rgb(150, 50, 50)
        canvas.drawRect(20f, 240f, canvas.width.minus(20f), 290f, paint)


        // Write Invoice title
        body.color = Color.WHITE
        body.textSize = 25f
        body.textAlign = Paint.Align.LEFT
        canvas.drawText("Qty", 25f, 277f, body)
        canvas.drawText("Item Name", 150f, 277f, body)
        canvas.drawText("Unit Cost", canvas.width.minus(300f), 277f, body)
        canvas.drawText("Amount", canvas.width.minus(150f), 277f, body)

        inventories.forEachIndexed { index, inventory->
            val newLine = index.plus(1).times(50f)

            // Draw Start Border
            paint.color = Color.rgb(150, 50, 50)
            canvas.drawRect(20f, newLine.plus(240f), 21f, newLine.plus(290f), paint)

            // Draw End Border
            paint.color = Color.rgb(150, 50, 50)
            canvas.drawRect(canvas.width.minus(21f), newLine.plus(240f), canvas.width.minus(20f), newLine.plus(290f), paint)


            // Write Invoice title
            body.color = Color.BLACK
            body.textSize = 25f
            body.textAlign = Paint.Align.LEFT
            canvas.drawText(inventory.totalUnits.toString().toEllipses(10), 25f, newLine.plus(277f), body)
            canvas.drawText(inventory.inventoryItemEntity.inventoryItemName.toEllipses(30), 150f, newLine.plus(277f), body)
            canvas.drawText(inventory.unitCost.shortened().toEllipses(10), canvas.width.minus(300f), newLine.plus(277f), body)
            canvas.drawText(inventory.totalCost.shortened().toEllipses(10), canvas.width.minus(150f), newLine.plus(277f), body)

        }

        val nextLine = inventories.size.plus(1).times(50f)
        // Draw Rectangle
        paint.color = Color.rgb(150, 50, 50)
        canvas.drawRect(20f, nextLine.plus(240f), canvas.width.minus(20f), nextLine.plus(290f), paint)


        // Write Invoice total
        val totalInventoryCost = inventories.sumOf { it.totalCost }
        body.color = Color.WHITE
        body.textSize = 25f
        body.textAlign = Paint.Align.LEFT
        canvas.drawText("Total", 25f, nextLine.plus(277f), body)
        body.textAlign = Paint.Align.RIGHT
        canvas.drawText("GHS ${totalInventoryCost.shortened()}", canvas.width.minus(30f), nextLine.plus(277f), body)


        // Write Invoice total
        body.color = Color.BLACK
        body.textSize = 25f
        body.textAlign = Paint.Align.LEFT
        canvas.drawText("Prepared by: ", 25f, nextLine.plus(327f), body)





        pdfDocument.finishPage(page)
        val directory = getDirectory(context)
        val file = File(directory, "inventory.pdf")

        withContext(Dispatchers.IO) {
            pdfDocument.writeTo(FileOutputStream(file))
        }
        pdfDocument.close()
        emit(Resource.Success("Pdf document successfully created"))

    }


    override suspend fun getInventoryCost(periodDropDownItem: PeriodDropDownItem): Flow<Resource<ItemValue?>> = flow{
        emit(Resource.Loading())
        try {
            val allInventories = appDatabase.inventoryDao.getAllInventories() ?: emptyList()
            if (periodDropDownItem.isAllTime) {
                val totalInventoryCost = allInventories.sumOf { it.totalCostPrice }
                emit(Resource.Success(ItemValue("Total Revenue", totalInventoryCost)))
            }else{
                val firstDate = periodDropDownItem.firstDate.toTimestamp()
                val lastDate = periodDropDownItem.lastDate.toTimestamp()
                val allFilteredInventories = allInventories.filter { it.date in firstDate .. lastDate }
                val totalInventoryCost = allFilteredInventories.sumOf { it.totalCostPrice }
                emit(Resource.Success(ItemValue("Total Revenue", totalInventoryCost)))
            }
        }catch (e:Exception){
            emit(Resource.Error("Could not get value"))
        }
    }


    private fun getDirectory(context: Context): File{
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
            File(it, context.resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir
    }

}