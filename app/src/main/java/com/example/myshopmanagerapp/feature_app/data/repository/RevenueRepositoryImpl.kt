package com.example.myshopmanagerapp.feature_app.data.repository

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.*
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
import com.example.myshopmanagerapp.feature_app.data.local.entities.revenue.RevenueEntity
import com.example.myshopmanagerapp.feature_app.domain.model.ItemValue
import com.example.myshopmanagerapp.feature_app.domain.model.PeriodDropDownItem
import com.example.myshopmanagerapp.feature_app.domain.model.UniqueId
import com.example.myshopmanagerapp.feature_app.domain.repository.RevenueRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.util.*

class RevenueRepositoryImpl(
    private val appDatabase: AppDatabase,
): RevenueRepository{
    override fun getAllRevenues(): Flow<Resource<RevenueEntities?>> = flow{
        emit(Resource.Loading())
        val allRevenues: List<RevenueEntity>?
        try {
            allRevenues = appDatabase.revenueDao.getAllRevenues()
            emit(Resource.Success(allRevenues))
        }catch (e: Exception){
            emit(Resource.Error(
                message = "Couldn't load all Revenues from Database\nError Message: ${e.message}",
                data = emptyList()
            ))
        }
    }

    override suspend fun addRevenue(revenue: RevenueEntity): Flow<Resource<String?>> = flow {
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val uniquePersonnelId = userPreferences.getPersonnelInfo.first()?.toPersonnelEntity()?.uniquePersonnelId.toNotNull()
            val personnel = appDatabase.personnelDao.getPersonnel(uniquePersonnelId)

            val importantFieldsAreMissing = revenue.revenueAmount < 0.1
            val dateNow = LocalDate.now().toDate().time
            when(true) {
                importantFieldsAreMissing -> {
                    emit(Resource.Error("Unable to add revenue\nPlease enter the revenue amount"))
                }
                (personnel == null)->{
                    emit(Resource.Error("Unable to add revenue\nCould not get personnel details"))
                }
                (revenue.date > dateNow)->{
                    emit(Resource.Error("Unable to add revenue\nThe selected date hasn't come yet"))
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
                    appDatabase.revenueDao.addRevenue(revenue.copy(uniquePersonnelId = uniquePersonnelId))
                    val addedRevenueIdsJson = AdditionEntityMarkers(context).getAddedRevenueIds.first().toNotNull()
                    val addedRevenueIds = addedRevenueIdsJson.toUniqueIds().plus(UniqueId(revenue.uniqueRevenueId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedRevenueIds(addedRevenueIds.toUniqueIdsJson())
                    emit(Resource.Success("Revenue successfully added"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Unable to add revenue\nError Message: ${e.message}"))
        }
    }

    override suspend fun generateRevenueList(
        context: Context,
        revenues: RevenueEntities
    ): Flow<Resource<String?>> = flow{
        val pageHeight = revenues.size.plus(8).times(50)
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

        // Write Revenue title
        body.color = Color.WHITE
        body.textSize = 25f
        body.textAlign = Paint.Align.LEFT
        title.color = Color.WHITE
        title.textSize = 25f
        title.textAlign = Paint.Align.RIGHT
        canvas.drawText("No.", 25f, 177f, body)
        canvas.drawText("Date", 100f, 177f, body)
        canvas.drawText("Revenue Type", canvas.width.minus(460f), 177f, body)
        canvas.drawText("Revenue Amount", canvas.width.minus(30f), 177f, title)

        revenues.forEachIndexed { index, revenue->
            val newLine = index.plus(1).times(50f)

            // Draw Start Border
            paint.color = Color.rgb(150, 50, 50)
            canvas.drawRect(20f, newLine.plus(140f), 21f, newLine.plus(190f), paint)

            // Draw End Border
            paint.color = Color.rgb(150, 50, 50)
            canvas.drawRect(canvas.width.minus(21f), newLine.plus(140f), canvas.width.minus(20f), newLine.plus(190f), paint)


            val date = "${revenue.dayOfWeek?.take(3)}, ${revenue.date.toDateString()}"
            val revenueType = revenue.revenueType ?: Constants.NotAvailable
            val revenueAmount = revenue.revenueAmount.shortened()

            // Write Invoice title
            body.color = Color.BLACK
            body.textSize = 25f
            body.textAlign = Paint.Align.LEFT
            title.color = Color.BLACK
            title.textSize = 25f
            title.textAlign = Paint.Align.RIGHT
            canvas.drawText(index.plus(1).toString().toEllipses(3), 25f, newLine.plus(180f), body)
            canvas.drawText(date.toEllipses(30), 100f, newLine.plus(180f), body)
            canvas.drawText(revenueType.toEllipses(30), canvas.width.minus(460f), newLine.plus(180f), body)
            canvas.drawText(revenueAmount.toEllipses(10), canvas.width.minus(30f), newLine.plus(180f), title)

        }

        val nextLine = revenues.size.plus(1).times(50f)
        // Draw Rectangle
        paint.color = Color.rgb(150, 50, 50)
        canvas.drawRect(20f, nextLine.plus(140f), canvas.width.minus(20f), nextLine.plus(190f), paint)


        // Write Invoice total
        body.color = Color.WHITE
        body.textSize = 25f
        body.textAlign = Paint.Align.LEFT
        canvas.drawText("Total Revenue Amount", 25f, nextLine.plus(175f), body)
        body.textAlign = Paint.Align.RIGHT
        canvas.drawText("GHS ${revenues.sumOf { it.revenueAmount }.shortened()}", canvas.width.minus(30f), nextLine.plus(175f), body)


        pdfDocument.finishPage(page)
        val directory = getDirectory(context)
        val file = File(directory, "revenues.pdf")

        withContext(Dispatchers.IO) {
            pdfDocument.writeTo(FileOutputStream(file))
        }
        pdfDocument.close()
        emit(Resource.Success("Pdf document successfully created"))

    }

    override suspend fun addRevenues(revenues: RevenueEntities) {
        try {
            val allRevenues = appDatabase.revenueDao.getAllRevenues() ?: emptyList()
            val allUniqueRevenueIds = allRevenues.map { it.uniqueRevenueId }
            val newRevenues = revenues.filter { !allUniqueRevenueIds.contains(it.uniqueRevenueId) }
            appDatabase.revenueDao.addRevenues(newRevenues)
        }catch (_: Exception){}
    }

    override suspend fun getRevenueHours(periodDropDownItem: PeriodDropDownItem): Flow<Resource<ItemValue?>> =flow{
        emit(Resource.Loading())
        try {
            val allRevenues = appDatabase.revenueDao.getAllRevenues() ?: emptyList()
            if (periodDropDownItem.isAllTime) {
                val totalRevenue = allRevenues.sumOf { it.numberOfHours.toNotNull() }
                emit(Resource.Success(ItemValue("Revenue hours", totalRevenue.toDouble())))
            }else{
                val firstDate = periodDropDownItem.firstDate.toTimestamp()
                val lastDate = periodDropDownItem.lastDate.toTimestamp()
                val allFilteredRevenues = allRevenues.filter { it.date in firstDate .. lastDate }
                val totalHours = allFilteredRevenues.sumOf { it.numberOfHours.toNotNull() }
                emit(Resource.Success(ItemValue("Revenue hours", totalHours.toDouble())))
            }
        }catch (e:Exception){
            emit(Resource.Error("Unable to get revenue hours\nError Message: ${e.message}"))
        }
    }

    override suspend fun getRevenueDays(periodDropDownItem: PeriodDropDownItem): Flow<Resource<ItemValue?>> = flow {
        emit(Resource.Loading())
        try {
            val allRevenues = appDatabase.revenueDao.getAllRevenues() ?: emptyList()
            if (periodDropDownItem.isAllTime) {
                val numberOfDays = allRevenues.count()
                emit(Resource.Success(ItemValue("Revenue days", numberOfDays.toDouble())))
            }else{
                val firstDate = periodDropDownItem.firstDate.toTimestamp()
                val lastDate = periodDropDownItem.lastDate.toTimestamp()
                val allFilteredRevenues = allRevenues.filter { it.date in firstDate .. lastDate }
                val numberOfDays = allFilteredRevenues.count()
                emit(Resource.Success(ItemValue("Revenue days", numberOfDays.toDouble())))
            }
        }catch (e:Exception){
            emit(Resource.Error("Could not get value"))
        }
    }

    override suspend fun getMaximumRevenueDay(periodDropDownItem: PeriodDropDownItem): Flow<Resource<ItemValue?>> =flow{
        emit(Resource.Loading())
        try {
            val allRevenues = appDatabase.revenueDao.getAllRevenues() ?: emptyList()
            if (periodDropDownItem.isAllTime) {
                val maxRevenue = allRevenues.maxBy { it.revenueAmount }
                val maxRevenueDay = "${maxRevenue.dayOfWeek?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }}, ${maxRevenue.date.toDate().toDateString()}"
                emit(Resource.Success(ItemValue(maxRevenueDay, maxRevenue.revenueAmount)))
            }else{
                val firstDate = periodDropDownItem.firstDate.toTimestamp()
                val lastDate = periodDropDownItem.lastDate.toTimestamp()
                val allFilteredRevenues = allRevenues.filter { it.date in firstDate .. lastDate }
                val maxRevenue = allFilteredRevenues.maxBy { it.revenueAmount }
                val maxRevenueDay = "${maxRevenue.dayOfWeek?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }}, ${maxRevenue.date.toDateString()}"
                emit(Resource.Success(ItemValue(maxRevenueDay, maxRevenue.revenueAmount)))
            }
        }catch (e:Exception){
            emit(Resource.Error("Could not get value"))
        }
    }

    override suspend fun getMinimumRevenueDay(periodDropDownItem: PeriodDropDownItem): Flow<Resource<ItemValue?>> = flow {
        emit(Resource.Loading())
        try {
            val allRevenues = appDatabase.revenueDao.getAllRevenues() ?: emptyList()
            if (periodDropDownItem.isAllTime) {
                val minRevenue = allRevenues.minBy { it.revenueAmount }
                val minRevenueDay = "${minRevenue.dayOfWeek?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }}, ${minRevenue.date.toDateString()}"
                emit(Resource.Success(ItemValue(minRevenueDay, minRevenue.revenueAmount)))
            }else{
                val firstDate = periodDropDownItem.firstDate.toTimestamp()
                val lastDate = periodDropDownItem.lastDate.toTimestamp()
                val allFilteredRevenues = allRevenues.filter { it.date in firstDate .. lastDate }
                val minRevenue = allFilteredRevenues.minBy { it.revenueAmount }
                val minRevenueDay = "${minRevenue.dayOfWeek?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }}, ${minRevenue.date.toDateString()}"
                emit(Resource.Success(ItemValue(minRevenueDay, minRevenue.revenueAmount)))
            }
        }catch (e:Exception){
            emit(Resource.Error("Could not get value"))
        }
    }

    override suspend fun getRevenue(uniqueRevenueId: String): RevenueEntity? {
        return appDatabase.revenueDao.getRevenue(uniqueRevenueId)
    }

    override suspend fun updateRevenue(revenue: RevenueEntity): Flow<Resource<String?>> = flow {
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val uniquePersonnelId = userPreferences.getPersonnelInfo.first()?.toPersonnelEntity()?.uniquePersonnelId.toNotNull()
            val personnel = appDatabase.personnelDao.getPersonnel(uniquePersonnelId)

            val importantFieldsAreMissing = revenue.revenueAmount < 0.1
            val dateNow = LocalDate.now().toDate().time
            when(true) {
                importantFieldsAreMissing -> {
                    emit(Resource.Error("Unable to update revenue\nPlease enter the revenue amount"))
                }
                (personnel == null) -> {
                    emit(Resource.Error("Unable to update revenue\nCould not get personnel details"))
                }
                (revenue.date > dateNow) -> {
                    emit(Resource.Error("Unable to update revenue\nThe selected date hasn't come yet"))
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
                else -> {
                    appDatabase.revenueDao.updateRevenue(revenue.copy(uniquePersonnelId = uniquePersonnelId))
                    val addedRevenueIdsJson = AdditionEntityMarkers(context).getAddedRevenueIds.first().toNotNull()
                    val addedRevenueIds = addedRevenueIdsJson.toUniqueIds().plus(UniqueId(revenue.uniqueRevenueId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedRevenueIds(addedRevenueIds.toUniqueIdsJson())

                    val updatedRevenueIdsJson = ChangesEntityMarkers(context).getChangedRevenueIds.first().toNotNull()
                    val updatedRevenueIds = updatedRevenueIdsJson.toUniqueIds().plus(UniqueId(revenue.uniqueRevenueId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedRevenueIds(updatedRevenueIds.toUniqueIdsJson())
                    emit(Resource.Success("Revenue successfully updated"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Unable to update revenue\nError Message: ${e.message}"))
        }
    }

    override suspend fun deleteRevenue(revenueId: Int) {
        appDatabase.revenueDao.deleteRevenue(revenueId)
    }

    override suspend fun deleteRevenue(uniqueRevenueId: String) : Flow<Resource<String?>> = flow {
        emit(Resource.Loading())
        try {
            val revenue = appDatabase.revenueDao.getRevenue(uniqueRevenueId)
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val personnelIsAdmin = userPreferences.getPersonnelInfo.first()?.toPersonnelEntity()?.hasAdminRights?: false

            when(true){
                (revenue == null)->{
                    emit(Resource.Error("Unable to delete revenue \nCould not get the revenue details you want to delete"))
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
                else->{
                    appDatabase.revenueDao.deleteRevenue(uniqueRevenueId)
                    val addedRevenueIdsJson = AdditionEntityMarkers(context).getAddedRevenueIds.first().toNotNull()
                    val addedRevenueIds = addedRevenueIdsJson.toUniqueIds().filter{it.uniqueId != uniqueRevenueId}.toSet().toList()
                    AdditionEntityMarkers(context).saveAddedRevenueIds(addedRevenueIds.toUniqueIdsJson())

                    val deletedRevenueIdsJson = ChangesEntityMarkers(context).getChangedRevenueIds.first().toNotNull()
                    val deletedRevenueIds = deletedRevenueIdsJson.toUniqueIds().plus(UniqueId(uniqueRevenueId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedRevenueIds(deletedRevenueIds.toUniqueIdsJson())
                    emit(Resource.Success("Revenue successfully deleted"))
                }
            }
        }catch (e:Exception){
            emit(Resource.Error("Unable to delete revenue\nError Message: ${e.message}"))
        }
    }

    override suspend fun deleteAllRevenues() {
        appDatabase.revenueDao.deleteAllRevenues()
    }

    override suspend fun getRevenueAmount(periodDropDownItem: PeriodDropDownItem): Flow<Resource<ItemValue?>> =flow {
        emit(Resource.Loading())
        try {
            val allRevenues = appDatabase.revenueDao.getAllRevenues() ?: emptyList()
            if (periodDropDownItem.isAllTime) {
                val totalRevenue = allRevenues.sumOf { it.revenueAmount }
                emit(Resource.Success(ItemValue("Total Revenue", totalRevenue)))
            }else{
                val firstDate = periodDropDownItem.firstDate.toTimestamp()
                val lastDate = periodDropDownItem.lastDate.toTimestamp()
                val allFilteredRevenues = allRevenues.filter { it.date in firstDate .. lastDate }
                val totalRevenue = allFilteredRevenues.sumOf { it.revenueAmount }
                emit(Resource.Success(ItemValue("Total Revenue", totalRevenue)))
            }
        }catch (e:Exception){
            emit(Resource.Error("Could not get value"))
        }
    }

    override suspend fun getExpenseAmount(periodDropDownItem: PeriodDropDownItem): Flow<Resource<ItemValue?>> =flow{
        emit(Resource.Loading())
        try {
            val allExpenses = appDatabase.expenseDao.getAllExpenses() ?: emptyList()
            if (periodDropDownItem.isAllTime) {
                val totalExpense = allExpenses.sumOf { it.expenseAmount }
                emit(Resource.Success(ItemValue("Total Expense", totalExpense)))
            }else{
                val firstDate = periodDropDownItem.firstDate.toTimestamp()
                val lastDate = periodDropDownItem.lastDate.toTimestamp()
                val allFilteredExpenses = allExpenses.filter { it.date in firstDate .. lastDate }
                val totalExpense = allFilteredExpenses.sumOf { it.expenseAmount }
                emit(Resource.Success(ItemValue("Total Expense", totalExpense)))
            }
        }catch (e:Exception){
            emit(Resource.Error("Could not get value"))
        }
    }

    override suspend fun getDebtAmount(periodDropDownItem: PeriodDropDownItem): Flow<Resource<ItemValue?>> =flow{
        emit(Resource.Loading())
        try {
            val allDebts = appDatabase.debtDao.getAllDebt() ?: emptyList()
            if (periodDropDownItem.isAllTime) {
                val totalRevenueDebt = allDebts.sumOf { it.debtAmount }
                emit(Resource.Success(ItemValue("Total Debt", totalRevenueDebt)))
            }else{
                val firstDate = periodDropDownItem.firstDate.toTimestamp()
                val lastDate = periodDropDownItem.lastDate.toTimestamp()
                val allFilteredDebts = allDebts.filter { it.date in firstDate .. lastDate }
                val totalRevenueDebt = allFilteredDebts.sumOf { it.debtAmount }
                emit(Resource.Success(ItemValue("Total Debt", totalRevenueDebt)))
            }
        }catch (e:Exception){
            emit(Resource.Error("Could not get value"))
        }
    }

    override suspend fun getDebtRepaymentAmount(periodDropDownItem: PeriodDropDownItem): Flow<Resource<ItemValue?>>  =flow{
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

    override suspend fun getMaximumExpenseDay(periodDropDownItem: PeriodDropDownItem): Flow<Resource<ItemValue?>> = flow {
        emit(Resource.Loading())
        try {
            val allExpenses = appDatabase.expenseDao.getAllExpenses() ?: emptyList()
            if (periodDropDownItem.isAllTime) {
                val maxRevenue = allExpenses.maxBy { it.expenseAmount }
                val maxRevenueDay = "${maxRevenue.dayOfWeek?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }}, ${maxRevenue.date.toDateString()}"
                emit(Resource.Success(ItemValue(maxRevenueDay, maxRevenue.expenseAmount)))
            }else{
                val firstDate = periodDropDownItem.firstDate.toTimestamp()
                val lastDate = periodDropDownItem.lastDate.toTimestamp()
                val allFilteredRevenues = allExpenses.filter { it.date in firstDate .. lastDate }
                val maxRevenue = allFilteredRevenues.maxBy { it.expenseAmount }
                val maxRevenueDay = "${maxRevenue.dayOfWeek?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }}, ${maxRevenue.date.toDateString()}"
                emit(Resource.Success(ItemValue(maxRevenueDay, maxRevenue.expenseAmount)))
            }
        }catch (e:Exception){
            emit(Resource.Error("Could not get value"))
        }
    }

    override suspend fun getMinimumExpenseDay(periodDropDownItem: PeriodDropDownItem): Flow<Resource<ItemValue?>> = flow{
        emit(Resource.Loading())
        try {
            val allExpenses = appDatabase.expenseDao.getAllExpenses() ?: emptyList()
            if (periodDropDownItem.isAllTime) {
                val minRevenue = allExpenses.minBy { it.expenseAmount }
                val minRevenueDay = "${minRevenue.dayOfWeek?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }}, ${minRevenue.date.toDateString()}"
                emit(Resource.Success(ItemValue(minRevenueDay, minRevenue.expenseAmount)))
            }else{
                val firstDate = periodDropDownItem.firstDate.toTimestamp()
                val lastDate = periodDropDownItem.lastDate.toTimestamp()
                val allFilteredRevenues = allExpenses.filter { it.date in firstDate .. lastDate }
                val minRevenue = allFilteredRevenues.minBy { it.expenseAmount }
                val minRevenueDay = "${minRevenue.dayOfWeek?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }}, ${minRevenue.date.toDateString()}"
                emit(Resource.Success(ItemValue(minRevenueDay, minRevenue.expenseAmount)))
            }
        }catch (e:Exception){
            emit(Resource.Error("Could not get value"))
        }
    }

    override suspend fun getShopRevenue(periodDropDownItem: PeriodDropDownItem): Flow<Resource<ItemValue?>> = flow{
        emit(Resource.Loading())
        try {
            val allRevenues = appDatabase.revenueDao.getAllRevenues() ?: emptyList()
            if (periodDropDownItem.isAllTime) {
                val totalRevenue = allRevenues.sumOf { it.revenueAmount }
                emit(Resource.Success(ItemValue("Total Revenue", totalRevenue)))
            }else{
                val firstDate = periodDropDownItem.firstDate.toTimestamp()
                val lastDate = periodDropDownItem.lastDate.toTimestamp()
                val allFilteredRevenues = allRevenues.filter { it.date in firstDate .. lastDate }
                val totalRevenue = allFilteredRevenues.sumOf { it.revenueAmount }
                emit(Resource.Success(ItemValue("Total Revenue", totalRevenue)))
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