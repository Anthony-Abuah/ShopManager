package com.example.myshopmanagerapp.feature_app.data.repository

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.*
import com.example.myshopmanagerapp.core.Constants.Zero
import com.example.myshopmanagerapp.core.Functions.shortened
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
import com.example.myshopmanagerapp.feature_app.data.local.entities.debt.DebtEntity
import com.example.myshopmanagerapp.feature_app.domain.model.ItemValue
import com.example.myshopmanagerapp.feature_app.domain.model.PeriodDropDownItem
import com.example.myshopmanagerapp.feature_app.domain.model.UniqueId
import com.example.myshopmanagerapp.feature_app.domain.repository.DebtRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate

class DebtRepositoryImpl(
    private val appDatabase: AppDatabase,
): DebtRepository{
    override fun getAllDebt(): Flow<Resource<DebtEntities?>> = flow{
        emit(Resource.Loading())
        val allDebt: List<DebtEntity>?
        try {
            allDebt = appDatabase.debtDao.getAllDebt()
            emit(Resource.Success(allDebt))
        }catch (e: Exception){
            emit(Resource.Error(
                message = "Couldn't load all Debt from Database",
                data = emptyList()
            ))
        }
    }

    override suspend fun addDebt(debt: DebtEntity): Flow<Resource<String?>> = flow {
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val uniquePersonnelId = userPreferences.getPersonnelInfo.first()?.toPersonnelEntity()?.uniquePersonnelId.toNotNull()
            val personnel = appDatabase.personnelDao.getPersonnel(uniquePersonnelId)

            val importantFieldsAreEmpty = ((debt.uniqueDebtId.isBlank()) || (debt.debtAmount <= 0.1))
            val uniqueCustomerId = debt.uniqueCustomerId
            val customer = appDatabase.customerDao.getCustomer(uniqueCustomerId)
            val dateNow = LocalDate.now().toDate().time
            when(true){
                (importantFieldsAreEmpty) -> {
                    emit(Resource.Error("Unable to add debt because some important fields are empty or blank"))
                }
                (customer == null) ->{
                    emit(Resource.Error("Unable to add debt because customer does not exist"))
                }
                (debt.date > dateNow) ->{
                    emit(Resource.Error("Unable to add debt because selected date hasn't come yet"))
                }
                (personnel == null) -> {
                    emit(Resource.Error("Unable to add debt\nCould not find info about personnel updating the debt"))
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to add debt." +
                            "\nPlease log in into an account to save info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to add debt." +
                            "\nPlease log in into a personnel account to save info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is entering the information"))
                }
                else ->{
                    val customerDebts = appDatabase.debtDao.getDebtByCustomer(uniqueCustomerId) ?: emptyList()
                    val customerDebtAmount = customerDebts.sumOf { it.debtAmount }
                    val customerDebtRepayments = appDatabase.debtRepaymentDao.getCustomerDebtRepayment(uniqueCustomerId) ?: emptyList()
                    val customerDebtRepaymentAmount = customerDebtRepayments.sumOf { it.debtRepaymentAmount }
                    val currentOutstandingDebt = customerDebtAmount.minus(customerDebtRepaymentAmount)
                    val customerDebt = debt.debtAmount.plus(currentOutstandingDebt)
                    val updatedCustomer = customer.copy(debtAmount = customerDebt)
                    appDatabase.debtDao.insertDebt(debt.copy(uniquePersonnelId = uniquePersonnelId), updatedCustomer)

                    val addedDebtIdsJson = AdditionEntityMarkers(context).getAddedDebtIds.first().toNotNull()
                    val addedDebtIds = addedDebtIdsJson.toUniqueIds().plus(UniqueId(debt.uniqueDebtId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedDebtIds(addedDebtIds.toUniqueIdsJson())

                    val addedCustomerIdsJson = AdditionEntityMarkers(context).getAddedCustomerIds.first().toNotNull()
                    val addedCustomerIds = addedCustomerIdsJson.toUniqueIds().plus(UniqueId(customer.uniqueCustomerId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedCustomerIds(addedCustomerIds.toUniqueIdsJson())

                    val updatedCustomerIdsJson = ChangesEntityMarkers(context).getChangedCustomerIds.first().toNotNull()
                    val updatedCustomerIds = updatedCustomerIdsJson.toUniqueIds().plus(UniqueId(customer.uniqueCustomerId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedCustomerIds(updatedCustomerIds.toUniqueIdsJson())

                    emit(Resource.Success("Debt added successfully"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Couldn't add debt\nError message: ${e.message}"))
        }
    }

    override suspend fun addDebts(debt: DebtEntities) {
        try {
            val allDebts = appDatabase.debtDao.getAllDebt() ?: emptyList()
            val allUniqueDebtIds = allDebts.map { it.uniqueDebtId }
            val newDebts = debt.filter { !allUniqueDebtIds.contains(it.uniqueDebtId) }
            appDatabase.debtDao.addDebts(newDebts)
        }catch (_: Exception){}
    }

    override suspend fun getDebt(uniqueDebtId: String): DebtEntity? {
        return appDatabase.debtDao.getDebt(uniqueDebtId)
    }

    override suspend fun getDebtByCustomer(uniqueCustomerId: String): DebtEntities? {
        return appDatabase.debtDao.getDebtByCustomer(uniqueCustomerId)
    }

    override suspend fun getCustomerDebtAmount(uniqueCustomerId: String): Double {
        val allCustomerDebts = appDatabase.debtDao.getDebtByCustomer(uniqueCustomerId) ?: emptyList()
        val allCustomerDebtRepayment = appDatabase.debtRepaymentDao.getCustomerDebtRepayment(uniqueCustomerId) ?: emptyList()
        val totalCustomerDebt = allCustomerDebts.sumOf { it.debtAmount }
        val totalCustomerDebtRepayment = allCustomerDebtRepayment.sumOf { it.debtRepaymentAmount }
        return totalCustomerDebt.minus(totalCustomerDebtRepayment)
    }

    override suspend fun getDebtToPersonnel(uniquePersonnelId: String): DebtEntities? {
        return appDatabase.debtDao.getDebtToPersonnel(uniquePersonnelId)
    }

    override suspend fun updateDebt(debt: DebtEntity): Flow<Resource<String?>> = flow {
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val uniquePersonnelId = userPreferences.getPersonnelInfo.first()?.toPersonnelEntity()?.uniquePersonnelId.toNotNull()
            val personnel = appDatabase.personnelDao.getPersonnel(uniquePersonnelId)

            val importantFieldsAreEmpty = ((debt.uniqueDebtId.isBlank()) || (debt.debtAmount <= 0.1))
            val uniqueCustomerId = debt.uniqueCustomerId
            val customer = appDatabase.customerDao.getCustomer(uniqueCustomerId)
            val dateNow = LocalDate.now().toDate().time
            val oldDebt = appDatabase.debtDao.getDebt(debt.uniqueDebtId)

            when(true){
                (importantFieldsAreEmpty) -> {
                    emit(Resource.Error("Unable to add debt because some important fields are empty or blank"))
                }
                (customer == null) ->{
                    emit(Resource.Error("Unable to update debt because customer does not exist"))
                }
                (debt.date > dateNow) ->{
                    emit(Resource.Error("Unable to update debt because selected date hasn't come yet"))
                }
                (oldDebt == null) ->{
                    emit(Resource.Error("Unable to update debt because this particular debt's details cannot be loaded"))
                }
                (personnel == null) -> {
                    emit(Resource.Error("Unable to update debt\nCould not find info about personnel updating the debt"))
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to update debt." +
                            "\nPlease log in into an account to update info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to update debt." +
                            "\nPlease log in into a personnel account to update info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is entering the information"))
                }
                else->{
                    val customerDebts = appDatabase.debtDao.getDebtByCustomer(uniqueCustomerId) ?: emptyList()
                    val customerDebtAmount = customerDebts.sumOf { it.debtAmount }
                    val customerDebtRepayments = appDatabase.debtRepaymentDao.getCustomerDebtRepayment(uniqueCustomerId) ?: emptyList()
                    val customerDebtRepaymentAmount = customerDebtRepayments.sumOf { it.debtRepaymentAmount }
                    val currentOutstandingDebt = customerDebtAmount.minus(customerDebtRepaymentAmount)
                    val oldDebtAmount = oldDebt.debtAmount
                    val debtDifference = debt.debtAmount.minus(oldDebtAmount)
                    val customerDebt = debtDifference.plus(currentOutstandingDebt)

                    val updatedCustomer = customer.copy(debtAmount = customerDebt)
                    appDatabase.debtDao.updateDebt(debt.copy(uniquePersonnelId = uniquePersonnelId), updatedCustomer)

                    val addedDebtIdsJson = AdditionEntityMarkers(context).getAddedDebtIds.first().toNotNull()
                    val addedDebtIds = addedDebtIdsJson.toUniqueIds().plus(UniqueId(debt.uniqueDebtId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedDebtIds(addedDebtIds.toUniqueIdsJson())

                    val updatedDebtIdsJson = ChangesEntityMarkers(context).getChangedDebtIds.first().toNotNull()
                    val updatedDebtIds = updatedDebtIdsJson.toUniqueIds().plus(UniqueId(debt.uniqueDebtId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedDebtIds(updatedDebtIds.toUniqueIdsJson())

                    val addedCustomerIdsJson = AdditionEntityMarkers(context).getAddedCustomerIds.first().toNotNull()
                    val addedCustomerIds = addedCustomerIdsJson.toUniqueIds().plus(UniqueId(customer.uniqueCustomerId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedCustomerIds(addedCustomerIds.toUniqueIdsJson())

                    val updatedCustomerIdsJson = ChangesEntityMarkers(context).getChangedCustomerIds.first().toNotNull()
                    val updatedCustomerIds = updatedCustomerIdsJson.toUniqueIds().plus(UniqueId(customer.uniqueCustomerId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedCustomerIds(updatedCustomerIds.toUniqueIdsJson())

                    emit(Resource.Success("Debt updated successfully"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Couldn't update debt\nError message: ${e.message}"))
        }
    }

    override suspend fun deleteDebt(uniqueDebtId: String): Flow<Resource<String?>> = flow {
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val personnelIsAdmin = userPreferences.getPersonnelInfo.first()?.toPersonnelEntity()?.hasAdminRights?: false

            val debt = appDatabase.debtDao.getDebt(uniqueDebtId)
            val customer = appDatabase.customerDao.getCustomer(debt?.uniqueCustomerId.toNotNull())
            val debtRepayments = appDatabase.debtRepaymentDao.getCustomerDebtRepayment(customer?.uniqueCustomerId.toNotNull()) ?: emptyList()
            val lastDebtRepayment = debtRepayments.maxByOrNull { it.date }?.date ?: Zero.toLong()

            when(true){
                (debt == null)->{
                    emit(Resource.Error("Unable to delete debt.\nCannot get debt details"))
                }
                (customer == null)->{
                    emit(Resource.Error("Unable to delete debt.\nCannot get customer info"))
                }
                (lastDebtRepayment > debt.date)->{
                    emit(Resource.Error("Unable to delete debt. \nDebt Repayment(s) have been made for all or some part of this debt"))
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to delete debt." +
                            "\nPlease log in into an account to delete info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to delete debt." +
                            "\nPlease log in into a personnel account to delete info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is deleting the information"))
                }
                !personnelIsAdmin->{
                    emit(Resource.Error("Unable to delete debt." +
                            "\nThis is because this personnel has not been given privileges to delete information" +
                            "\nTo be able to delete any information, you must have admin rights" +
                            "\nNB: Only an admin can bestow admin rights"))
                }
                else->{
                    val uniqueCustomerId = customer.uniqueCustomerId
                    val customerDebts = appDatabase.debtDao.getDebtByCustomer(uniqueCustomerId) ?: emptyList()
                    val customerDebtAmount = customerDebts.sumOf { it.debtAmount }
                    val customerDebtRepayments = appDatabase.debtRepaymentDao.getCustomerDebtRepayment(uniqueCustomerId) ?: emptyList()
                    val customerDebtRepaymentAmount = customerDebtRepayments.sumOf { it.debtRepaymentAmount }
                    val currentOutstandingDebt = customerDebtAmount.minus(customerDebtRepaymentAmount)
                    val customerDebt = currentOutstandingDebt.minus(debt.debtAmount)

                    val updatedCustomer = customer.copy(debtAmount = customerDebt)
                    appDatabase.debtDao.deleteDebt(debt.uniqueDebtId, updatedCustomer)

                    val addedDebtIdsJson = AdditionEntityMarkers(context).getAddedDebtIds.first().toNotNull()
                    val addedDebtIds = addedDebtIdsJson.toUniqueIds().filter { it.uniqueId != uniqueDebtId }.toSet().toList()
                    AdditionEntityMarkers(context).saveAddedDebtIds(addedDebtIds.toUniqueIdsJson())

                    val deletedDebtIdsJson = ChangesEntityMarkers(context).getChangedDebtIds.first().toNotNull()
                    val deletedDebtIds = deletedDebtIdsJson.toUniqueIds().plus(UniqueId(debt.uniqueDebtId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedDebtIds(deletedDebtIds.toUniqueIdsJson())

                    val addedCustomerIdsJson = AdditionEntityMarkers(context).getAddedCustomerIds.first().toNotNull()
                    val addedCustomerIds = addedCustomerIdsJson.toUniqueIds().plus(UniqueId(customer.uniqueCustomerId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedCustomerIds(addedCustomerIds.toUniqueIdsJson())

                    val updatedCustomerIdsJson = ChangesEntityMarkers(context).getChangedCustomerIds.first().toNotNull()
                    val updatedCustomerIds = updatedCustomerIdsJson.toUniqueIds().plus(UniqueId(customer.uniqueCustomerId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedCustomerIds(updatedCustomerIds.toUniqueIdsJson())

                    emit(Resource.Success("Debt deleted successfully"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Couldn't delete debt\nError message: ${e.message}"))
        }
    }

    override suspend fun deleteAllDebt() {
        appDatabase.debtDao.deleteAllDebts()
    }

    override suspend fun generateDebtList(
        context: Context,
        debts: DebtEntities,
        mapOfCustomers: Map<String, String>
    ): Flow<Resource<String?>> = flow{
        val pageHeight = debts.size.plus(8).times(50)
        val pageWidth = 800
        val pdfDocument = PdfDocument()
        val paint = Paint()
        val title = Paint()
        val body = Paint()
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 2).create()
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

        // Draw Rectangle
        paint.color = Color.rgb(150, 50, 50)
        canvas.drawRect(20f, 140f, canvas.width.minus(20f), 190f, paint)

        // Write Debt title
        body.color = Color.WHITE
        body.textSize = 25f
        body.textAlign = Paint.Align.LEFT
        title.color = Color.WHITE
        title.textSize = 25f
        title.textAlign = Paint.Align.RIGHT
        canvas.drawText("No.", 25f, 177f, body)
        canvas.drawText("Date", 100f, 177f, body)
        canvas.drawText("Customer", canvas.width.minus(460f), 177f, body)
        canvas.drawText("Debt Amount", canvas.width.minus(30f), 177f, title)

        debts.forEachIndexed { index, debt->
            val newLine = index.plus(1).times(50f)

            // Draw Start Border
            paint.color = Color.rgb(150, 50, 50)
            canvas.drawRect(20f, newLine.plus(140f), 21f, newLine.plus(190f), paint)

            // Draw End Border
            paint.color = Color.rgb(150, 50, 50)
            canvas.drawRect(canvas.width.minus(21f), newLine.plus(140f), canvas.width.minus(20f), newLine.plus(190f), paint)


            val date = "${debt.dayOfWeek?.take(3)}, ${debt.date.toDateString()}"
            val customer = mapOfCustomers[debt.uniqueCustomerId] ?: Constants.NotAvailable
            val debtAmount = debt.debtAmount.shortened()

            // Write Invoice title
            body.color = Color.BLACK
            body.textSize = 25f
            body.textAlign = Paint.Align.LEFT
            title.color = Color.BLACK
            title.textSize = 25f
            title.textAlign = Paint.Align.RIGHT
            canvas.drawText(index.plus(1).toString().toEllipses(3), 25f, newLine.plus(180f), body)
            canvas.drawText(date.toEllipses(30), 100f, newLine.plus(180f), body)
            canvas.drawText(customer.toEllipses(30), canvas.width.minus(460f), newLine.plus(180f), body)
            canvas.drawText(debtAmount.toEllipses(10), canvas.width.minus(30f), newLine.plus(180f), title)
        }

        val nextLine = debts.size.plus(1).times(50f)
        // Draw Rectangle
        paint.color = Color.rgb(150, 50, 50)
        canvas.drawRect(20f, nextLine.plus(140f), canvas.width.minus(20f), nextLine.plus(190f), paint)


        // Write Invoice total
        body.color = Color.WHITE
        body.textSize = 25f
        body.textAlign = Paint.Align.LEFT
        canvas.drawText("Total Debt Amount", 25f, nextLine.plus(175f), body)
        body.textAlign = Paint.Align.RIGHT
        canvas.drawText("GHS ${debts.sumOf { it.debtAmount }.shortened()}", canvas.width.minus(30f), nextLine.plus(175f), body)


        pdfDocument.finishPage(page)
        val directory = getDirectory(context)
        val file = File(directory, "debts.pdf")

        withContext(Dispatchers.IO) {
            pdfDocument.writeTo(FileOutputStream(file))
        }
        pdfDocument.close()
        emit(Resource.Success("Pdf document successfully created"))

    }

    override suspend fun getPeriodicDebtAmount(periodDropDownItem: PeriodDropDownItem): Flow<Resource<ItemValue?>> =flow{
        emit(Resource.Loading())
        try {
            val allDebts = appDatabase.debtDao.getAllDebt() ?: emptyList()
            if (periodDropDownItem.isAllTime) {
                val totalDebtAmount = allDebts.sumOf { it.debtAmount }
                emit(Resource.Success(ItemValue("Total Debt", totalDebtAmount)))
            }else{
                val firstDate = periodDropDownItem.firstDate.toTimestamp()
                val lastDate = periodDropDownItem.lastDate.toTimestamp()
                val allFilteredDebts = allDebts.filter { it.date in firstDate .. lastDate }
                val totalDebtAmount = allFilteredDebts.sumOf { it.debtAmount }
                emit(Resource.Success(ItemValue("Total Debt", totalDebtAmount)))
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