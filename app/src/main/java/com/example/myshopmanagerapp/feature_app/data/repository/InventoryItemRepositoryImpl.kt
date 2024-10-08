package com.example.myshopmanagerapp.feature_app.data.repository

import com.example.myshopmanagerapp.core.*
import com.example.myshopmanagerapp.core.Constants.NotAvailable
import com.example.myshopmanagerapp.core.Constants.ONE
import com.example.myshopmanagerapp.core.Constants.Unit
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.Functions.toTimestamp
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelEntity
import com.example.myshopmanagerapp.core.TypeConverters.toUniqueIds
import com.example.myshopmanagerapp.core.TypeConverters.toUniqueIdsJson
import com.example.myshopmanagerapp.feature_app.MyShopManagerApp
import com.example.myshopmanagerapp.feature_app.data.local.AppDatabase
import com.example.myshopmanagerapp.feature_app.data.local.entities.inventory_items.InventoryItemEntity
import com.example.myshopmanagerapp.feature_app.domain.model.*
import com.example.myshopmanagerapp.feature_app.domain.repository.InventoryItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.util.*

class InventoryItemRepositoryImpl(
    private val appDatabase: AppDatabase,
): InventoryItemRepository{
    override fun getAllInventoryItems(): Flow<Resource<InventoryItemEntities?>> = flow{
        emit(Resource.Loading())
        val allInventoryItems: List<InventoryItemEntity>?
        try {
            allInventoryItems = appDatabase.inventoryItemDao.getAllInventoryItems()?.sortedBy { it.inventoryItemName }
            emit(Resource.Success(allInventoryItems))
        }catch (e: Exception){
            emit(Resource.Error(
                message = "Couldn't load all inventory items from database",
                data = emptyList()
            ))
        }
    }

    override suspend fun addInventoryItem(inventoryItem: InventoryItemEntity): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false

            val allInventoryItems = appDatabase.inventoryItemDao.getAllInventoryItems() ?: emptyList()
            val allItemNames = allInventoryItems.map { it.inventoryItemName.lowercase(Locale.ROOT) }
            val allUniqueItemIds = allInventoryItems.map { it.uniqueInventoryItemId }
            val itemName = inventoryItem.inventoryItemName.lowercase(Locale.ROOT)

            when(true){
                (allItemNames.contains(itemName))->{
                    emit(Resource.Error("Cannot add ${itemName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }} item because it already exists"))
                }
                (allUniqueItemIds.contains(inventoryItem.uniqueInventoryItemId))->{
                    emit(Resource.Error("Cannot add item because the generated item Id already exists" +
                            "\nPlease check to see if the item details are not already saved and try again"))
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to add inventory item." +
                            "\nPlease log in into an account to save info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to add inventory item." +
                            "\nPlease log in into a personnel account to save info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is entering the information"))
                }
                else->{
                    val categorizations = inventoryItem.quantityCategorizations.ifEmpty {
                        listOf( QuantityCategorization(Unit, ONE))
                    }
                    appDatabase.inventoryItemDao.addInventoryItem(inventoryItem.copy(
                        inventoryItemName = inventoryItem.inventoryItemName.trim(),
                        sellingPrices = listOf(Price(Date().time, Unit, inventoryItem.currentSellingPrice.toNotNull())),
                        quantityCategorizations = categorizations)
                    )
                    val addedInventoryItemIdsJson = AdditionEntityMarkers(context).getAddedInventoryItemIds.first().toNotNull()
                    val addedInventoryItemIds = addedInventoryItemIdsJson.toUniqueIds().plus(UniqueId(inventoryItem.uniqueInventoryItemId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedInventoryItemIds(addedInventoryItemIds.toUniqueIdsJson())
                    emit(Resource.Success("${inventoryItem.inventoryItemName} successfully added"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Couldn't add inventory item\nError message: ${e.message}"))
        }
    }

    override suspend fun updateInventoryItem(inventoryItem: InventoryItemEntity): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false

            val oldInventoryItem = appDatabase.inventoryItemDao.getInventoryItem(inventoryItem.uniqueInventoryItemId)
            val allInventoryItems = appDatabase.inventoryItemDao.getAllInventoryItems() ?: emptyList()
            val allItemNames = allInventoryItems.minus(oldInventoryItem).filterNotNull()
                .map { it.inventoryItemName.lowercase(Locale.ROOT)}
            when(true){
                (oldInventoryItem == null)->{
                    emit(Resource.Error("Cannot update ${inventoryItem.inventoryItemName}. Unable to fetch the details of this inventory item"))
                }
                (allItemNames.contains(inventoryItem.inventoryItemName.lowercase(Locale.ROOT)))->{
                    emit(Resource.Error("Cannot update item: ${inventoryItem.inventoryItemName} because it already exists"))
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to update revenue." +
                            "\nPlease log in into an account to update info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to update revenue." +
                            "\nPlease log in into a personnel account to update info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is entering the information"))
                }
                else->{
                    val categorizations = inventoryItem.quantityCategorizations.ifEmpty {
                        listOf( QuantityCategorization(Unit, ONE))
                    }
                    appDatabase.inventoryItemDao.updateInventoryItem(inventoryItem.copy(
                        inventoryItemName = inventoryItem.inventoryItemName.trim(),
                        quantityCategorizations = categorizations)
                    )

                    val addedInventoryItemIdsJson = AdditionEntityMarkers(context).getAddedInventoryItemIds.first().toNotNull()
                    val addedInventoryItemIds = addedInventoryItemIdsJson.toUniqueIds().plus(UniqueId(inventoryItem.uniqueInventoryItemId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedInventoryItemIds(addedInventoryItemIds.toUniqueIdsJson())

                    val updatedInventoryItemIdsJson = ChangesEntityMarkers(context).getChangedInventoryItemIds.first().toNotNull()
                    val updatedInventoryItemIds = updatedInventoryItemIdsJson.toUniqueIds().plus(UniqueId(inventoryItem.uniqueInventoryItemId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedInventoryItemIds(updatedInventoryItemIds.toUniqueIdsJson())
                    emit(Resource.Success("${inventoryItem.inventoryItemName} successfully updated"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Couldn't update inventory item\nError message: ${e.message}"))
        }
    }

    override suspend fun deleteInventoryItem(uniqueInventoryItemId: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val personnelIsAdmin = userPreferences.getPersonnelInfo.first()?.toPersonnelEntity()?.hasAdminRights?: false

            val allStocks = appDatabase.stockDao.getItemStocks(uniqueInventoryItemId) ?: emptyList()
            val allInventories = appDatabase.inventoryDao.getItemInventories(uniqueInventoryItemId) ?: emptyList()

            when(true){
                (allStocks.isNotEmpty())->{
                    emit(Resource.Error("Cannot delete this item because it is associated with some stocks." +
                            "\nYou need to delete those stocks in order to delete this item")
                    )
                }
                (allInventories.isNotEmpty())->{
                    emit(Resource.Error("Cannot delete this item because it is associated with some inventories." +
                            "\nYou need to delete those inventories in order to delete this item")
                    )
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to delete revenue." +
                            "\nPlease log in into an account to delete info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to delete revenue." +
                            "\nPlease log in into a personnel account to delete info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is deleting the information"))
                }
                !personnelIsAdmin->{
                    emit(Resource.Error("Unable to delete revenue." +
                            "\nThis is because this personnel has not been given privileges to delete information" +
                            "\nTo be able to delete any information, you must have admin rights" +
                            "\nNB: Only an admin can bestow admin rights"))
                }
                else ->{
                    appDatabase.inventoryItemDao.deleteInventoryItem(uniqueInventoryItemId)
                    val addedInventoryItemIdsJson = AdditionEntityMarkers(context).getAddedInventoryItemIds.first().toNotNull()
                    val addedInventoryItemIds = addedInventoryItemIdsJson.toUniqueIds().filter{it.uniqueId != uniqueInventoryItemId}.toSet().toList()
                    AdditionEntityMarkers(context).saveAddedInventoryItemIds(addedInventoryItemIds.toUniqueIdsJson())

                    val deletedInventoryItemIdsJson = ChangesEntityMarkers(context).getChangedInventoryItemIds.first().toNotNull()
                    val deletedInventoryItemIds = deletedInventoryItemIdsJson.toUniqueIds().plus(UniqueId(uniqueInventoryItemId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedInventoryItemIds(deletedInventoryItemIds.toUniqueIdsJson())
                    emit(Resource.Success("Item successfully deleted"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Couldn't delete inventory item\nError message: ${e.message}"))
        }
    }

    override suspend fun getShopItemCostValues(period: PeriodDropDownItem): Flow<Resource<List<ItemValue>>> = flow{
        emit(Resource.Loading())
        try {
            val allInventoryItems = appDatabase.inventoryItemDao.getAllInventoryItems()
            if (allInventoryItems.isNullOrEmpty()) {
                emit(Resource.Error("No inventory items have been added yet"))
            }else{
                val itemValues = allInventoryItems.map { item->
                    val itemName = item.inventoryItemName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                    val costPrice = item.currentCostPrice ?: 0.0
                    val numberOfRemainingValue = item.totalNumberOfUnits ?: 0
                    val totalValueOfItem = costPrice.times(numberOfRemainingValue)
                    ItemValue(itemName, totalValueOfItem)
                }
                emit(Resource.Success(itemValues))
            }
        }catch (e: Exception){
            emit(Resource.Error("Unknown error! Could not get item values"))
        }
    }

    override suspend fun getPeriodicInventoryItems(period: PeriodDropDownItem): Flow<Resource<Map<InventoryItemEntity?, Int>>>  = flow{
        emit(Resource.Loading())
        try {
            val allStocks = appDatabase.stockDao.getAllStocks()?.sortedBy { it.date } ?: emptyList()
            val allInventoryItems = appDatabase.inventoryItemDao.getAllInventoryItems() ?: emptyList()
            if (period.isAllTime) {
                val mutableInventoryItems = mutableMapOf<InventoryItemEntity?, Int>()
                val lastStocks = allStocks.groupBy { it.uniqueInventoryItemId }.mapValues { it.value.maxByOrNull { _inventory-> _inventory.date } }.values.filterNotNull()
                lastStocks.forEach { stock->
                    val inventoryItem = allInventoryItems.firstOrNull { it.uniqueInventoryItemId == stock.uniqueInventoryItemId }
                    mutableInventoryItems[inventoryItem] = stock.totalNumberOfUnits
                }
                emit(Resource.Success(mutableInventoryItems))
            }
            else{
                val firstDate = period.firstDate.toTimestamp()
                val lastDate = period.lastDate.toTimestamp()
                val allFilteredStocks = allStocks.filter { it.date in firstDate .. lastDate }
                val mutableInventoryItems = mutableMapOf<InventoryItemEntity?, Int>()
                val lastStocks = allFilteredStocks.groupBy { it.uniqueInventoryItemId }.mapValues { it.value.maxByOrNull { _inventory-> _inventory.date } }.values.filterNotNull()
                lastStocks.forEach { stock->
                    val inventoryItem = allInventoryItems.firstOrNull { it.uniqueInventoryItemId == stock.uniqueInventoryItemId }
                    mutableInventoryItems[inventoryItem] = stock.totalNumberOfUnits
                }
                emit(Resource.Success(mutableInventoryItems))
            }
        }catch (e:Exception){
            emit(Resource.Error("Could not get value"))
        }
    }

    override fun getShopItemSellingValues(): Flow<Resource<List<ItemValue>>> = flow{
        emit(Resource.Loading())
        try {
            val allInventoryItems = appDatabase.inventoryItemDao.getAllInventoryItems()
            if (allInventoryItems.isNullOrEmpty()) {
                emit(Resource.Error("No inventory items have been added yet"))
            }else{
                val itemValues = allInventoryItems.map { item->
                    val itemName = item.inventoryItemName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                    val sellingPrice = item.currentSellingPrice ?: 0.0
                    val numberOfRemainingValue = item.totalNumberOfUnits ?: 0
                    val totalValueOfItem = sellingPrice.times(numberOfRemainingValue)
                    ItemValue(itemName, totalValueOfItem)
                }
                emit(Resource.Success(itemValues))
            }
        }catch (e: Exception){
            emit(Resource.Error("Unknown error! Could not get item values"))
        }
    }

    override fun getShopItemSellingValues(period: PeriodDropDownItem): Flow<Resource<List<ItemValue>>>  = flow{
        emit(Resource.Loading())
        try {
            val allInventoryItems = appDatabase.inventoryItemDao.getAllInventoryItems()
            if (allInventoryItems.isNullOrEmpty()) {
                emit(Resource.Error("No inventory items have been added yet"))
            }else{
                val itemValues = allInventoryItems.map { item->
                    val itemName = item.inventoryItemName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                    val sellingPrice = item.currentSellingPrice ?: 0.0
                    val numberOfRemainingValue = item.totalNumberOfUnits ?: 0
                    val totalValueOfItem = sellingPrice.times(numberOfRemainingValue)
                    ItemValue(itemName, totalValueOfItem)
                }
                emit(Resource.Success(itemValues))
            }
        }catch (e: Exception){
            emit(Resource.Error("Unknown error! Could not get item values"))
        }
    }

    override fun getShopItemProfitValues(): Flow<Resource<List<ItemValue>>> = flow{
        emit(Resource.Loading())
        try {
            val allInventoryItems = appDatabase.inventoryItemDao.getAllInventoryItems()
            if (allInventoryItems.isNullOrEmpty()) {
                emit(Resource.Error("No inventory items have been added yet"))
            }else{
                val itemValues = allInventoryItems.map { item->
                    val itemName = item.inventoryItemName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                    val costPrice = item.currentCostPrice ?: 0.0
                    val sellingPrice = item.currentSellingPrice ?: 0.0
                    val numberOfRemainingValue = item.totalNumberOfUnits ?: 0
                    val profitValue = (sellingPrice.minus(costPrice)).times(numberOfRemainingValue)
                    ItemValue(itemName, profitValue)
                }
                emit(Resource.Success(itemValues))
            }
        }catch (e: Exception){
            emit(Resource.Error("Unknown error! Could not get item values"))
        }
    }

    override fun getShopItemProfitValues(period: PeriodDropDownItem): Flow<Resource<List<ItemValue>>> = flow{
        emit(Resource.Loading())
        try {
            val allInventoryItems = appDatabase.inventoryItemDao.getAllInventoryItems()
            if (allInventoryItems.isNullOrEmpty()) {
                emit(Resource.Error("No inventory items have been added yet"))
            }else{
                val itemValues = allInventoryItems.map { item->
                    val itemName = item.inventoryItemName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                    val costPrice = item.currentCostPrice ?: 0.0
                    val sellingPrice = item.currentSellingPrice ?: 0.0
                    val numberOfRemainingValue = item.totalNumberOfUnits ?: 0
                    val profitValue = (sellingPrice.minus(costPrice)).times(numberOfRemainingValue)
                    ItemValue(itemName, profitValue)
                }
                emit(Resource.Success(itemValues))
            }
        }catch (e: Exception){
            emit(Resource.Error("Unknown error! Could not get item values"))
        }
    }

    override fun getShopItemProfitPercentageValues(): Flow<Resource<List<ItemValue>>> = flow{
        emit(Resource.Loading())
        try {
            val allInventoryItems = appDatabase.inventoryItemDao.getAllInventoryItems()
            if (allInventoryItems.isNullOrEmpty()) {
                emit(Resource.Error("No inventory items have been added yet"))
            }else{
                val itemValues = allInventoryItems.map { item->
                    val itemName = item.inventoryItemName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                    val costPrice = item.currentCostPrice ?: 0.0
                    val sellingPrice = item.currentSellingPrice ?: 0.0
                    val profitPercentageValue = if(costPrice <= 0.001) costPrice else ((sellingPrice.minus(costPrice)).div(costPrice)).times(100.0)
                    ItemValue(itemName, profitPercentageValue)
                }
                emit(Resource.Success(itemValues))
            }
        }catch (e: Exception){
            emit(Resource.Error("Unknown error! Could not get item values"))
        }
    }

    override fun getShopItemProfitPercentageValues(period: PeriodDropDownItem): Flow<Resource<List<ItemValue>>>  = flow{
        emit(Resource.Loading())
        try {
            val allInventoryItems = appDatabase.inventoryItemDao.getAllInventoryItems()
            if (allInventoryItems.isNullOrEmpty()) {
                emit(Resource.Error("No inventory items have been added yet"))
            }else{
                val itemValues = allInventoryItems.map { item->
                    val itemName = item.inventoryItemName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                    val costPrice = item.currentCostPrice ?: 0.0
                    val sellingPrice = item.currentSellingPrice ?: 0.0
                    val profitPercentageValue = if(costPrice <= 0.001) costPrice else ((sellingPrice.minus(costPrice)).div(costPrice)).times(100.0)
                    ItemValue(itemName, profitPercentageValue)
                }
                emit(Resource.Success(itemValues))
            }
        }catch (e: Exception){
            emit(Resource.Error("Unknown error! Could not get item values"))
        }
    }

    override fun getMaximumInventoryItem(): Flow<Resource<InventoryItemEntity>> = flow{
        emit(Resource.Loading())
        try {
            val allItems = appDatabase.inventoryItemDao.getAllInventoryItems() ?: emptyList()
            val maximumInventoryItem = allItems.maxByOrNull { it.totalNumberOfUnits ?: 0 }
            if (maximumInventoryItem == null){
                emit(Resource.Error("There are no inventory items added yet"))
            }else{
                emit(Resource.Success(maximumInventoryItem))
            }
        }catch (e: Exception){
            emit(Resource.Error("Unknown error! Could not get value"))
        }
    }

    override fun getMaximumInventoryItem(period: PeriodDropDownItem): Flow<Resource<InventoryItemEntity>>  = flow{
        emit(Resource.Loading())
        try {
            val allItems = appDatabase.inventoryItemDao.getAllInventoryItems() ?: emptyList()
            val maximumInventoryItem = allItems.maxByOrNull { it.totalNumberOfUnits ?: 0 }
            if (maximumInventoryItem == null){
                emit(Resource.Error("There are no inventory items added yet"))
            }else{
                emit(Resource.Success(maximumInventoryItem))
            }
        }catch (e: Exception){
            emit(Resource.Error("Unknown error! Could not get value"))
        }
    }

    override fun getMinimumInventoryItem(): Flow<Resource<InventoryItemEntity>> = flow {
        emit(Resource.Loading())
        try {
            val allItems = appDatabase.inventoryItemDao.getAllInventoryItems() ?: emptyList()
            val minimumInventoryItem = allItems.minByOrNull { it.totalNumberOfUnits ?: 0 }
            if (minimumInventoryItem == null){
                emit(Resource.Error("There are no inventory items added yet"))
            }else{
                emit(Resource.Success(minimumInventoryItem))
            }
        }catch (e: Exception){
            emit(Resource.Error("Unknown error! Could not get value"))
        }
    }

    override fun getMinimumInventoryItem(period: PeriodDropDownItem): Flow<Resource<InventoryItemEntity>>  = flow {
        emit(Resource.Loading())
        try {
            val allItems = appDatabase.inventoryItemDao.getAllInventoryItems() ?: emptyList()
            val minimumInventoryItem = allItems.minByOrNull { it.totalNumberOfUnits ?: 0 }
            if (minimumInventoryItem == null){
                emit(Resource.Error("There are no inventory items added yet"))
            }else{
                emit(Resource.Success(minimumInventoryItem))
            }
        }catch (e: Exception){
            emit(Resource.Error("Unknown error! Could not get value"))
        }
    }

    override suspend fun addInventoryItems(inventoryItems: InventoryItemEntities) {
        try {
            val allInventoryItems = appDatabase.inventoryItemDao.getAllInventoryItems() ?: emptyList()
            val allUniqueInventoryItemIds = allInventoryItems.map { it.uniqueInventoryItemId }
            val newInventoryItems = inventoryItems.filter { !allUniqueInventoryItemIds.contains(it.uniqueInventoryItemId) }
            appDatabase.inventoryItemDao.addInventoryItems(newInventoryItems)
        }catch (_: Exception){}
    }

    override suspend fun getInventoryItem(uniqueInventoryItemId: String): InventoryItemEntity? {
        return appDatabase.inventoryItemDao.getInventoryItem(uniqueInventoryItemId)
    }

    override suspend fun deleteAllInventoryItems() {
        appDatabase.inventoryItemDao.deleteAllInventoryItems()
    }


}