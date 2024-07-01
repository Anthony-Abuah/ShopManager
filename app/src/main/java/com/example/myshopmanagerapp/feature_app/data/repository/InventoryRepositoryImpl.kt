package com.example.myshopmanagerapp.feature_app.data.repository

import com.example.myshopmanagerapp.core.Constants.Unit
import com.example.myshopmanagerapp.core.Constants.ZERO
import com.example.myshopmanagerapp.core.Functions.addItemQuantities
import com.example.myshopmanagerapp.core.Functions.dateToTimestamp
import com.example.myshopmanagerapp.core.Functions.generateUniqueStockId
import com.example.myshopmanagerapp.core.Functions.getTotalNumberOfUnits
import com.example.myshopmanagerapp.core.Functions.subtractItemQuantities
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.InventoryEntities
import com.example.myshopmanagerapp.core.Resource
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelEntity
import com.example.myshopmanagerapp.core.UserPreferences
import com.example.myshopmanagerapp.feature_app.MyShopManagerApp
import com.example.myshopmanagerapp.feature_app.data.local.AppDatabase
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory.InventoryEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_items.InventoryItemEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_stock.InventoryStockEntity
import com.example.myshopmanagerapp.feature_app.data.local.entities.stock.StockEntity
import com.example.myshopmanagerapp.feature_app.domain.model.ItemValue
import com.example.myshopmanagerapp.feature_app.domain.model.PeriodDropDownItem
import com.example.myshopmanagerapp.feature_app.domain.model.Price
import com.example.myshopmanagerapp.feature_app.domain.repository.InventoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
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
                    val quantityInfo = inventory.quantityInfo
                    val dateString = inventory.date.toDateString()
                    val uniqueStockId = generateUniqueStockId("${inventoryItem.inventoryItemName}-${dateString}_")

                    val stock = StockEntity(
                        0,
                        uniqueStockId = uniqueStockId,
                        date = inventory.date,
                        dayOfWeek = inventory.dayOfWeek,
                        uniquePersonnelId = uniquePersonnelId,
                        uniqueInventoryItemId = inventory.uniqueInventoryItemId,
                        stockQuantityInfo = quantityInfo,
                        totalNumberOfUnits = quantityInfo.getTotalNumberOfUnits(),
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
                        itemQuantityInfo = thisInventoryItemQuantityInfo.addItemQuantities(quantityInfo),
                        totalNumberOfUnits = thisInventoryItemQuantityInfo.addItemQuantities(quantityInfo).getTotalNumberOfUnits(),
                        costPrices = costPrices,
                        currentCostPrice = inventory.unitCostPrice
                    )
                    appDatabase.inventoryDao.addInventoryWithStock(
                        stock,
                        inventory.copy(uniquePersonnelId = uniquePersonnelId),
                        inventoryStock,
                        thisInventoryItem
                    )
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
        appDatabase.inventoryDao.addInventories(inventories)
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
                    val updatedStock = oldStock.copy(
                        date = inventory.date,
                        uniquePersonnelId = uniquePersonnelId,
                        dayOfWeek = inventory.dayOfWeek,
                        stockQuantityInfo = inventory.quantityInfo,
                        totalNumberOfUnits = inventory.quantityInfo.getTotalNumberOfUnits(),
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
}