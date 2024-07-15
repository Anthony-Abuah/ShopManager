package com.example.myshopmanagerapp.feature_app.data.repository

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.*
import com.example.myshopmanagerapp.core.Constants.Zero
import com.example.myshopmanagerapp.core.Functions.dateToTimestamp
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
import com.example.myshopmanagerapp.feature_app.data.local.entities.debt_repayment.DebtRepaymentEntity
import com.example.myshopmanagerapp.feature_app.domain.model.ItemValue
import com.example.myshopmanagerapp.feature_app.domain.model.PeriodDropDownItem
import com.example.myshopmanagerapp.feature_app.domain.model.UniqueId
import com.example.myshopmanagerapp.feature_app.domain.repository.DebtRepaymentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.util.*

class DebtRepaymentRepositoryImpl(
    private val appDatabase: AppDatabase,
): DebtRepaymentRepository{
    override fun getAllDebtRepayment(): Flow<Resource<DebtRepaymentEntities?>> = flow{
        emit(Resource.Loading())
        val allDebtRepayment: List<DebtRepaymentEntity>?
        try {
            allDebtRepayment = appDatabase.debtRepaymentDao.getAllDebtRepayment() ?: emptyList()
            emit(Resource.Success(allDebtRepayment))
        }catch (e: Exception){
            emit(Resource.Error(
                message = "Couldn't load all DebtRepayment from Database",
                data = emptyList()
            ))
        }
    }

    override suspend fun addDebtRepayment(debtRepayment: DebtRepaymentEntity): Flow<Resource<String?>> = flow {
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val uniquePersonnelId = userPreferences.getPersonnelInfo.first()?.toPersonnelEntity()?.uniquePersonnelId.toNotNull()
            val personnel = appDatabase.personnelDao.getPersonnel(uniquePersonnelId)

            val importantFieldsAreEmpty = ((debtRepayment.uniqueDebtRepaymentId.isBlank()) || (debtRepayment.debtRepaymentAmount <= 0.1))
            val uniqueCustomerId = debtRepayment.uniqueCustomerId
            val customer = appDatabase.customerDao.getCustomer(uniqueCustomerId)
            val dateNow = LocalDate.now().toDate().time
            val allCustomerDebts = appDatabase.debtDao.getDebtByCustomer(uniqueCustomerId) ?: emptyList()
            val firstDebtDate = allCustomerDebts.minByOrNull { it.date }?.date ?: Zero.toLong()
            val allCustomerDebtRepayments = appDatabase.debtRepaymentDao.getCustomerDebtRepayment(uniqueCustomerId) ?: emptyList()
            val lastDebtRepaymentDate = allCustomerDebtRepayments.maxByOrNull { it.date }?.date ?: Zero.toLong()
            val outstandingDebt = allCustomerDebts.sumOf { it.debtAmount }.minus(allCustomerDebtRepayments.sumOf { it.debtRepaymentAmount })

            when(true) {
                (importantFieldsAreEmpty) -> {
                    emit(Resource.Error("Unable to add debt repayment because some important fields are empty or blank"))
                }
                (customer == null) -> {
                    emit(Resource.Error("Unable to add debt repayment because customer does not exist"))
                }
                (debtRepayment.date > dateNow) -> {
                    emit(Resource.Error("Unable to add debt repayment because selected date hasn't come yet"))
                }
                (firstDebtDate > debtRepayment.date) -> {
                    emit(Resource.Error("Unable to add debt repayment.\nSelected date is before this customer's first debt"))
                }
                (lastDebtRepaymentDate > debtRepayment.date) -> {
                    emit(Resource.Error("Unable to add debt repayment.\nSelected date is before this customer's last debt repayment"))
                }
                (debtRepayment.debtRepaymentAmount > outstandingDebt) -> {
                    emit(Resource.Error("Unable to add debt repayment.\nThe debt repayment amount is more than the outstanding debt of $outstandingDebt"))
                }
                (personnel == null) -> {
                    emit(Resource.Error("Unable to add debt repayment\nCould not find info about personnel updating the debt"))
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to add debt repayment." +
                            "\nPlease log in into an account to save info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to add debt repayment." +
                            "\nPlease log in into a personnel account to save info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is entering the information"))
                }
                else -> {
                    val customerDebt = outstandingDebt.minus(debtRepayment.debtRepaymentAmount)
                    val updatedCustomer = customer.copy(debtAmount = customerDebt)
                    appDatabase.debtRepaymentDao.insertDebtRepayment(debtRepayment.copy(uniquePersonnelId = uniquePersonnelId), updatedCustomer)

                    val addedDebtRepaymentIdsJson = AdditionEntityMarkers(context).getAddedDebtRepaymentIds.first().toNotNull()
                    val addedDebtRepaymentIds = addedDebtRepaymentIdsJson.toUniqueIds().plus(UniqueId(debtRepayment.uniqueDebtRepaymentId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedDebtRepaymentIds(addedDebtRepaymentIds.toUniqueIdsJson())

                    val addedCustomerIdsJson = AdditionEntityMarkers(context).getAddedCustomerIds.first().toNotNull()
                    val addedCustomerIds = addedCustomerIdsJson.toUniqueIds().plus(UniqueId(customer.uniqueCustomerId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedCustomerIds(addedCustomerIds.toUniqueIdsJson())

                    val updatedCustomerIdsJson = ChangesEntityMarkers(context).getChangedCustomerIds.first().toNotNull()
                    val updatedCustomerIds = updatedCustomerIdsJson.toUniqueIds().plus(UniqueId(customer.uniqueCustomerId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedCustomerIds(updatedCustomerIds.toUniqueIdsJson())

                    emit(Resource.Success("Debt repayment added successfully"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Couldn't add debt repayment\nError message: ${e.message}"))
        }
    }

    override suspend fun addDebtRepayments(debtRepayments: DebtRepaymentEntities) {
        try {
            val allDebtRepayments = appDatabase.debtRepaymentDao.getAllDebtRepayment() ?: emptyList()
            val allUniqueDebtRepaymentIds = allDebtRepayments.map { it.uniqueDebtRepaymentId }
            val newDebtRepayments = debtRepayments.filter { !allUniqueDebtRepaymentIds.contains(it.uniqueDebtRepaymentId) }
            appDatabase.debtRepaymentDao.addDebtRepayments(newDebtRepayments)
        }catch (_: Exception){}
    }

    override suspend fun getDebtRepayment(uniqueDebtRepaymentId: String): DebtRepaymentEntity? {
        return appDatabase.debtRepaymentDao.getDebtRepayment(uniqueDebtRepaymentId)
    }

    override suspend fun getDebtRepaymentByCustomer(uniqueCustomerId: String): DebtRepaymentEntities? {
        return appDatabase.debtRepaymentDao.getCustomerDebtRepayment(uniqueCustomerId)
    }
    override suspend fun getDebtRepaymentToPersonnel(uniquePersonnelId: String): DebtRepaymentEntities? {
        return appDatabase.debtRepaymentDao.getPersonnelDebtRepayment(uniquePersonnelId)
    }

    override suspend fun updateDebtRepayment(debtRepayment: DebtRepaymentEntity): Flow<Resource<String?>> = flow  {
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val uniquePersonnelId = userPreferences.getPersonnelInfo.first()?.toPersonnelEntity()?.uniquePersonnelId.toNotNull()
            val personnel = appDatabase.personnelDao.getPersonnel(uniquePersonnelId)

            val importantFieldsAreEmpty = ((debtRepayment.uniqueDebtRepaymentId.isBlank()) || (debtRepayment.debtRepaymentAmount <= 0.1))
            val dateNow = dateToTimestamp(Date())
            val uniqueCustomerId = debtRepayment.uniqueCustomerId
            val customer = appDatabase.customerDao.getCustomer(uniqueCustomerId)
            val allCustomerDebts = appDatabase.debtDao.getDebtByCustomer(uniqueCustomerId) ?: emptyList()
            val firstDebtDate = allCustomerDebts.minByOrNull { it.date }?.date ?: Zero.toLong()
            val allCustomerDebtRepayments = appDatabase.debtRepaymentDao.getCustomerDebtRepayment(uniqueCustomerId) ?: emptyList()
            val currentCustomerDebtRepayments = allCustomerDebtRepayments.filter { it.uniqueDebtRepaymentId != debtRepayment.uniqueDebtRepaymentId }
            val lastDebtRepaymentDate = currentCustomerDebtRepayments.maxByOrNull { it.date }?.date ?: Zero.toLong()
            val outstandingDebt = allCustomerDebts.sumOf { it.debtAmount }.minus(currentCustomerDebtRepayments.sumOf { it.debtRepaymentAmount })

            when(true){
                (importantFieldsAreEmpty) -> {
                    emit(Resource.Error("Unable to update debt repayment because some important fields are empty or blank"))
                }
                (customer == null) -> {
                    emit(Resource.Error("Unable to update debt repayment because customer does not exist"))
                }
                (debtRepayment.date > dateNow) -> {
                    emit(Resource.Error("Unable to update debt repayment because selected date hasn't come yet"))
                }
                (firstDebtDate > debtRepayment.date) -> {
                    emit(Resource.Error("Unable to update debt repayment.\nSelected date is before this customer's first debt"))
                }
                (lastDebtRepaymentDate > debtRepayment.date) -> {
                    emit(Resource.Error("Unable to update debt repayment.\nSelected date is before this customer's last debt repayment"))
                }
                (debtRepayment.debtRepaymentAmount > outstandingDebt) -> {
                    emit(Resource.Error("Unable to update debt repayment.\nThe debt repayment amount is more than the outstanding debt of $outstandingDebt"))
                }
                (personnel == null) -> {
                    emit(Resource.Error("Unable to update debt repayment\nCould not find info about personnel updating the debt"))
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to update debt repayment." +
                            "\nPlease log in into an account to update info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to update debt repayment." +
                            "\nPlease log in into a personnel account to update info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is entering the information"))
                }
                else->{
                    val customerDebt = outstandingDebt.minus(debtRepayment.debtRepaymentAmount)
                    val updatedCustomer = customer.copy(debtAmount = customerDebt)
                    appDatabase.debtRepaymentDao.updateDebtRepayment(debtRepayment.copy(uniquePersonnelId = uniquePersonnelId), updatedCustomer)

                    val addedDebtRepaymentIdsJson = AdditionEntityMarkers(context).getAddedDebtRepaymentIds.first().toNotNull()
                    val addedDebtRepaymentIds = addedDebtRepaymentIdsJson.toUniqueIds().plus(UniqueId(debtRepayment.uniqueDebtRepaymentId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedDebtRepaymentIds(addedDebtRepaymentIds.toUniqueIdsJson())

                    val updatedDebtRepaymentIdsJson = ChangesEntityMarkers(context).getChangedDebtRepaymentIds.first().toNotNull()
                    val updatedDebtRepaymentIds = updatedDebtRepaymentIdsJson.toUniqueIds().plus(UniqueId(debtRepayment.uniqueDebtRepaymentId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedDebtRepaymentIds(updatedDebtRepaymentIds.toUniqueIdsJson())

                    val addedCustomerIdsJson = AdditionEntityMarkers(context).getAddedCustomerIds.first().toNotNull()
                    val addedCustomerIds = addedCustomerIdsJson.toUniqueIds().plus(UniqueId(customer.uniqueCustomerId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedCustomerIds(addedCustomerIds.toUniqueIdsJson())

                    val updatedCustomerIdsJson = ChangesEntityMarkers(context).getChangedCustomerIds.first().toNotNull()
                    val updatedCustomerIds = updatedCustomerIdsJson.toUniqueIds().plus(UniqueId(customer.uniqueCustomerId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedCustomerIds(updatedCustomerIds.toUniqueIdsJson())

                    emit(Resource.Success("Debt repayment updated successfully"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Couldn't update debt repayment\nError message: ${e.message}"))
        }
    }

    override suspend fun deleteDebtRepayment(uniqueDebtRepaymentId: String): Flow<Resource<String?>> = flow  {
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val personnelIsAdmin = userPreferences.getPersonnelInfo.first()?.toPersonnelEntity()?.hasAdminRights?: false

            val debtRepayment = appDatabase.debtRepaymentDao.getDebtRepayment(uniqueDebtRepaymentId)
            val uniqueCustomerId = debtRepayment?.uniqueCustomerId.toNotNull()
            val customer = appDatabase.customerDao.getCustomer(uniqueCustomerId)
            val allCustomerDebtRepayments = appDatabase.debtRepaymentDao.getCustomerDebtRepayment(uniqueCustomerId) ?: emptyList()
            val lastDebtRepaymentDate = allCustomerDebtRepayments.maxByOrNull { it.date }?.date ?: Zero.toLong()

            when(true){
                (debtRepayment == null) -> {
                    emit(Resource.Error("Unable to delete debt repayment because this debt repayment's details could not be fetched from the database"))
                }
                (customer == null) -> {
                    emit(Resource.Error("Unable to delete debt repayment because this debt repayment's customer details could not be fetched from the database"))
                }
                (lastDebtRepaymentDate != debtRepayment.date) -> {
                    emit(Resource.Error("Unable to delete debt repayment.\nOther debt repayments for this customer have been made after this particular debt repayment"))
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to delete debt repayment." +
                            "\nPlease log in into an account to delete info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to delete debt repayment." +
                            "\nPlease log in into a personnel account to delete info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is deleting the information"))
                }
                !personnelIsAdmin->{
                    emit(Resource.Error("Unable to delete debt repayment." +
                            "\nThis is because this personnel has not been given privileges to delete information" +
                            "\nTo be able to delete any information, you must have admin rights" +
                            "\nNB: Only an admin can bestow admin rights"))
                }
                else->{
                    val allCustomerDebts = appDatabase.debtDao.getDebtByCustomer(uniqueCustomerId) ?: emptyList()
                    val totalCustomerDebt = allCustomerDebts.sumOf { it.debtAmount }
                    val totalCustomerDebtRepayment = allCustomerDebtRepayments.sumOf { it.debtRepaymentAmount }.minus(debtRepayment.debtRepaymentAmount)
                    val outstandingDebt = totalCustomerDebt.minus(totalCustomerDebtRepayment)
                    val updatedCustomer = customer.copy(debtAmount = outstandingDebt)
                    appDatabase.debtRepaymentDao.deleteDebtRepayment(uniqueDebtRepaymentId, updatedCustomer)

                    val addedDebtRepaymentIdsJson = AdditionEntityMarkers(context).getAddedDebtRepaymentIds.first().toNotNull()
                    val addedDebtRepaymentIds = addedDebtRepaymentIdsJson.toUniqueIds().filter { it.uniqueId != uniqueDebtRepaymentId }.toSet().toList()
                    AdditionEntityMarkers(context).saveAddedDebtRepaymentIds(addedDebtRepaymentIds.toUniqueIdsJson())

                    val deletedDebtRepaymentIdsJson = ChangesEntityMarkers(context).getChangedDebtRepaymentIds.first().toNotNull()
                    val deletedDebtRepaymentIds = deletedDebtRepaymentIdsJson.toUniqueIds().plus(UniqueId(debtRepayment.uniqueDebtRepaymentId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedDebtRepaymentIds(deletedDebtRepaymentIds.toUniqueIdsJson())

                    val addedCustomerIdsJson = AdditionEntityMarkers(context).getAddedCustomerIds.first().toNotNull()
                    val addedCustomerIds = addedCustomerIdsJson.toUniqueIds().plus(UniqueId(customer.uniqueCustomerId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedCustomerIds(addedCustomerIds.toUniqueIdsJson())

                    val updatedCustomerIdsJson = ChangesEntityMarkers(context).getChangedCustomerIds.first().toNotNull()
                    val updatedCustomerIds = updatedCustomerIdsJson.toUniqueIds().plus(UniqueId(customer.uniqueCustomerId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedCustomerIds(updatedCustomerIds.toUniqueIdsJson())

                    emit(Resource.Success("Debt repayment deleted successfully"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Couldn't delete debt repayment\nError message: ${e.message}"))
        }
    }

    override suspend fun deleteAllDebtRepayment() {
        appDatabase.debtRepaymentDao.deleteAllDebtRepayments()
    }

    override suspend fun generateDebtRepaymentList(
        context: Context,
        debtRepayments: DebtRepaymentEntities,
        mapOfCustomers: Map<String, String>
    ): Flow<Resource<String?>> = flow{
        val pageHeight = debtRepayments.size.plus(8).times(50)
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
        val shopName = company?.companyName ?: "Shop name"
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

        // Write DebtRepayment title
        body.color = Color.WHITE
        body.textSize = 25f
        body.textAlign = Paint.Align.LEFT
        title.color = Color.WHITE
        title.textSize = 25f
        title.textAlign = Paint.Align.RIGHT
        canvas.drawText("No.", 25f, 177f, body)
        canvas.drawText("Date", 100f, 177f, body)
        canvas.drawText("Customer", canvas.width.minus(460f), 177f, body)
        canvas.drawText("Repayment Amount", canvas.width.minus(30f), 177f, title)

        debtRepayments.forEachIndexed { index, debtRepayment->
            val newLine = index.plus(1).times(50f)

            // Draw Start Border
            paint.color = Color.rgb(150, 50, 50)
            canvas.drawRect(20f, newLine.plus(140f), 21f, newLine.plus(190f), paint)

            // Draw End Border
            paint.color = Color.rgb(150, 50, 50)
            canvas.drawRect(canvas.width.minus(21f), newLine.plus(140f), canvas.width.minus(20f), newLine.plus(190f), paint)

            val date = "${debtRepayment.dayOfWeek?.take(3)}, ${debtRepayment.date.toDateString()}"
            val customer = mapOfCustomers[debtRepayment.uniqueCustomerId] ?: Constants.NotAvailable
            val debtRepaymentAmount = debtRepayment.debtRepaymentAmount.shortened()

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
            canvas.drawText(debtRepaymentAmount.toEllipses(10), canvas.width.minus(30f), newLine.plus(180f), title)
        }

        val nextLine = debtRepayments.size.plus(1).times(50f)
        // Draw Rectangle
        paint.color = Color.rgb(150, 50, 50)
        canvas.drawRect(20f, nextLine.plus(140f), canvas.width.minus(20f), nextLine.plus(190f), paint)


        // Write Invoice total
        body.color = Color.WHITE
        body.textSize = 25f
        body.textAlign = Paint.Align.LEFT
        canvas.drawText("Total Repayment Amount", 25f, nextLine.plus(175f), body)
        body.textAlign = Paint.Align.RIGHT
        canvas.drawText("GHS ${debtRepayments.sumOf { it.debtRepaymentAmount }.shortened()}", canvas.width.minus(30f), nextLine.plus(175f), body)


        pdfDocument.finishPage(page)
        val directory = getDirectory(context)
        val file = File(directory, "debtRepayments.pdf")

        withContext(Dispatchers.IO) {
            pdfDocument.writeTo(FileOutputStream(file))
        }
        pdfDocument.close()
        emit(Resource.Success("Pdf document successfully created"))

    }

    override suspend fun getPeriodicDebtRepaymentAmount(periodDropDownItem: PeriodDropDownItem): Flow<Resource<ItemValue?>>  =flow{
        emit(Resource.Loading())
        try {
            val allDebtRepayments = appDatabase.debtRepaymentDao.getAllDebtRepayment() ?: emptyList()
            if (periodDropDownItem.isAllTime) {
                val totalRevenueDebt = allDebtRepayments.sumOf { it.debtRepaymentAmount }
                emit(Resource.Success(ItemValue("Total Debt Repayment", totalRevenueDebt)))
            }else{
                val firstDate = periodDropDownItem.firstDate.toTimestamp()
                val lastDate = periodDropDownItem.lastDate.toTimestamp()
                val allFilteredDebtRepayments = allDebtRepayments.filter { it.date in firstDate .. lastDate }
                val totalRevenueDebt = allFilteredDebtRepayments.sumOf { it.debtRepaymentAmount }
                emit(Resource.Success(ItemValue("Total Debt Repayment", totalRevenueDebt)))
            }
        }catch (e:Exception){
            emit(Resource.Error("Could not get value"))
        }
    }


    private fun getDirectory(context: Context): File {
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
            File(it, context.resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir
    }


}