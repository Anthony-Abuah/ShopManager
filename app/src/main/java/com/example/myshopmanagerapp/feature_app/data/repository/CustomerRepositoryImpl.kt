package com.example.myshopmanagerapp.feature_app.data.repository

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.*
import com.example.myshopmanagerapp.core.Functions.shortened
import com.example.myshopmanagerapp.core.Functions.toCompanyEntity
import com.example.myshopmanagerapp.core.Functions.toEllipses
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelEntity
import com.example.myshopmanagerapp.core.TypeConverters.toUniqueIds
import com.example.myshopmanagerapp.core.TypeConverters.toUniqueIdsJson
import com.example.myshopmanagerapp.feature_app.MyShopManagerApp
import com.example.myshopmanagerapp.feature_app.data.local.AppDatabase
import com.example.myshopmanagerapp.feature_app.data.local.entities.customers.CustomerEntity
import com.example.myshopmanagerapp.feature_app.domain.model.UniqueId
import com.example.myshopmanagerapp.feature_app.domain.repository.CustomerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.*

class CustomerRepositoryImpl(
    private val appDatabase: AppDatabase,
): CustomerRepository{
    override fun getAllCustomers(): Flow<Resource<CustomerEntities?>> = flow{
        emit(Resource.Loading())
        val allCustomers: List<CustomerEntity>?
        try {
            allCustomers = appDatabase.customerDao.getAllCustomers()
            emit(Resource.Success(allCustomers))
        }catch (e: Exception){
            emit(Resource.Error(
                message = "Couldn't load all Customers from Database",
                data = emptyList()
            ))
        }
    }

    override suspend fun addCustomer(customer: CustomerEntity): Flow<Resource<String?>> = flow {
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val allCustomers = appDatabase.customerDao.getAllCustomers() ?: emptyList()
            val allCustomerNames = allCustomers.map { it.customerName.trim().lowercase(Locale.ROOT)}
            val name = customer.customerName.trim().lowercase(Locale.ROOT)
            
            when(true){
                (customer.customerName.isBlank() || customer.customerContact.isBlank())->{
                    emit(Resource.Error("Unable to add customer \nPlease ensure that the customer's route and contact are provided"))
                }
                (allCustomerNames.contains(name))->{
                    emit(Resource.Error("Unable to add customer \nCustomer with route, $name, already exists"))
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to add customer." +
                            "\nPlease log in into an account to save info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to add customer." +
                            "\nPlease log in into a personnel account to save info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is entering the information"))
                }
                else->{
                    appDatabase.customerDao.addCustomer(customer)
                    val addedCustomerIdsJson = AdditionEntityMarkers(context).getAddedCustomerIds.first().toNotNull()
                    val addedCustomerIds = addedCustomerIdsJson.toUniqueIds().plus(UniqueId(customer.uniqueCustomerId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedCustomerIds(addedCustomerIds.toUniqueIdsJson())
                    emit(Resource.Success("Customer successfully added"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Unable to add customer\nError Message: ${e.message}"))
        }
    }

    override suspend fun addCustomers(customers: CustomerEntities) {
        try {
            val allCustomers = appDatabase.customerDao.getAllCustomers() ?: emptyList()
            val allUniqueCustomerIds = allCustomers.map { it.uniqueCustomerId }
            val newCustomers = customers.filter { !allUniqueCustomerIds.contains(it.uniqueCustomerId) }
            appDatabase.customerDao.addCustomers(newCustomers)
        }catch (_: Exception){}
    }

    override suspend fun getCustomer(uniqueCustomerId: String): CustomerEntity? {
        return appDatabase.customerDao.getCustomer(uniqueCustomerId)
    }

    override suspend fun getCustomer(uniqueCustomerId: Int): CustomerEntity? {
        return appDatabase.customerDao.getCustomer(uniqueCustomerId)
    }

    override suspend fun getCustomerByName(customerName: String): CustomerEntities? {
        return appDatabase.customerDao.getCustomerByName(customerName)
    }

    override suspend fun updateCustomer(customer: CustomerEntity): Flow<Resource<String?>> = flow  {
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val unUpdatedCustomer = appDatabase.customerDao.getCustomer(customer.uniqueCustomerId)
            val allCustomer = appDatabase.customerDao.getAllCustomers() ?: emptyList()
            val allCustomerNames = allCustomer.map { it.customerName.trim().lowercase(Locale.ROOT)}.minus(unUpdatedCustomer?.customerName?.trim()?.lowercase(Locale.ROOT)).filterNotNull()
            val name = customer.customerName.trim().lowercase(Locale.ROOT)

            when(true){
                (customer.customerName.isBlank() || customer.customerContact.isBlank())->{
                    emit(Resource.Error("Unable to add customer \nPlease ensure that the customer's route and contact are provided"))
                }
                (unUpdatedCustomer == null)->{
                    emit(Resource.Error("Unable to update customer \nCould not get the customer you want to update"))
                }
                (allCustomerNames.contains(name))->{
                    emit(Resource.Error("Unable to update customer \nCustomer with route: $name, already exists"))
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to update customer." +
                            "\nPlease log in into an account to save info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to update customer." +
                            "\nPlease log in into a personnel account to save info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is entering the information"))
                }
                else->{
                    appDatabase.customerDao.updateCustomer(customer)
                    val addedCustomerIdsJson = AdditionEntityMarkers(context).getAddedCustomerIds.first().toNotNull()
                    val addedCustomerIds = addedCustomerIdsJson.toUniqueIds().plus(UniqueId(customer.uniqueCustomerId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedCustomerIds(addedCustomerIds.toUniqueIdsJson())

                    val updatedCustomerIdsJson = ChangesEntityMarkers(context).getChangedCustomerIds.first().toNotNull()
                    val updatedCustomerIds = updatedCustomerIdsJson.toUniqueIds().plus(UniqueId(customer.uniqueCustomerId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedCustomerIds(updatedCustomerIds.toUniqueIdsJson())
                    emit(Resource.Success("Customer successfully updated"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error("Unable to update customer\nError Message: ${e.message}"))
        }
    }

    override suspend fun updateCustomerDebt(uniqueCustomerId: String, debtAmount: Double) {
        appDatabase.customerDao.updateCustomerDebt(uniqueCustomerId, debtAmount)
    }

    override suspend fun deleteCustomer(customerId: Int): Flow<Resource<String?>> = flow  {
        appDatabase.customerDao.deleteCustomer(customerId)
    }

    override suspend fun deleteCustomer(uniqueCustomerId: String): Flow<Resource<String?>> = flow  {
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val isLoggedIn = userPreferences.getLoggedInState.first() ?: false
            val personnelIsLoggedIn = userPreferences.getPersonnelLoggedInState.first() ?: false
            val personnelIsAdmin = userPreferences.getPersonnelInfo.first()?.toPersonnelEntity()?.hasAdminRights?: false

            val customer = appDatabase.customerDao.getCustomer(uniqueCustomerId)
            val allDebts = appDatabase.debtDao.getAllDebt()?.map { it.uniqueCustomerId } ?: emptyList()
            val allDebtRepayments = appDatabase.debtRepaymentDao.getAllDebtRepayment()?.map { it.uniqueCustomerId } ?: emptyList()

            when(true){
                (customer == null)->{
                    emit(Resource.Error("Unable to delete customer \nCould not get the customer you want to delete"))
                }
                (allDebts.contains(uniqueCustomerId) || allDebtRepayments.contains(uniqueCustomerId))->{
                    emit(Resource.Error("Could not delete customer \nThis customer is already associated with debts and debt repayments" +
                            "\nTo delete customer, you have to delete all the records this customer is associated with"))
                }
                !isLoggedIn->{
                    emit(Resource.Error("Unable to delete customer." +
                            "\nPlease log in into an account to delete info" +
                            "\nCreate a new account if you don't have one" +
                            "\nYou can create an account even if offline"))
                }
                !personnelIsLoggedIn->{
                    emit(Resource.Error("Unable to delete customer." +
                            "\nPlease log in into a personnel account to delete info" +
                            "\nCreate a new personnel account if you don't have one" +
                            "\nThis helps the app to determine which personnel is deleting the information"))
                }
                !personnelIsAdmin->{
                    emit(Resource.Error("Unable to delete customer." +
                            "\nThis is because this personnel has not been given privileges to delete information" +
                            "\nTo be able to delete any information, you must have admin rights" +
                            "\nNB: Only an admin can bestow admin rights"))
                }
                else->{
                    appDatabase.customerDao.deleteCustomer(uniqueCustomerId)
                    val addedCustomerIdsJson = AdditionEntityMarkers(context).getAddedCustomerIds.first().toNotNull()
                    val addedCustomerIds = addedCustomerIdsJson.toUniqueIds().filter{it.uniqueId != uniqueCustomerId}.toSet().toList()
                    AdditionEntityMarkers(context).saveAddedCustomerIds(addedCustomerIds.toUniqueIdsJson())

                    val deletedCustomerIdsJson = ChangesEntityMarkers(context).getChangedCustomerIds.first().toNotNull()
                    val deletedCustomerIds = deletedCustomerIdsJson.toUniqueIds().plus(UniqueId(uniqueCustomerId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedCustomerIds(deletedCustomerIds.toUniqueIdsJson())
                    emit(Resource.Success("Customer successfully deleted"))
                }
            }
            
        }catch (e: Exception){
            emit(Resource.Error("Unable to delete customer\nError Message: ${e.message}"))
        }
    }

    override suspend fun deleteAllCustomers() {
        appDatabase.customerDao.deleteAllCustomers()
    }


    override suspend fun generateCustomerList(
        context: Context,
        customers: CustomerEntities
    ): Flow<Resource<String?>> = flow{
        val pageHeight = customers.size.plus(8).times(50)
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



        // Write Invoice title
        body.color = Color.WHITE
        body.textSize = 25f
        body.textAlign = Paint.Align.LEFT
        canvas.drawText("No.", 25f, 183f, body)
        canvas.drawText("Customer Name", 100f, 183f, body)
        canvas.drawText("Contact", canvas.width.minus(350f), 183f, body)
        canvas.drawText("Debt Amount", canvas.width.minus(175f), 183f, body)

        customers.forEachIndexed { index, customer->
            val newLine = index.plus(1).times(50f)

            // Draw Start Border
            paint.color = Color.rgb(150, 50, 50)
            canvas.drawRect(20f, newLine.plus(140f), 21f, newLine.plus(190f), paint)

            // Draw End Border
            paint.color = Color.rgb(150, 50, 50)
            canvas.drawRect(canvas.width.minus(21f), newLine.plus(140f), canvas.width.minus(20f), newLine.plus(190f), paint)


            val debtAmount = customer.debtAmount?.shortened() ?: "0.00"
            // Write Invoice title
            body.color = Color.BLACK
            body.textSize = 25f
            body.textAlign = Paint.Align.LEFT
            title.color = if ((customer.debtAmount ?: 0.0) > 0) Color.RED else Color.BLACK
            title.textSize = 25f
            title.textAlign = Paint.Align.RIGHT
            canvas.drawText(index.plus(1).toString().toEllipses(3), 25f, newLine.plus(183f), body)
            canvas.drawText(customer.customerName.toEllipses(30), 100f, newLine.plus(183f), body)
            canvas.drawText(customer.customerContact.toEllipses(15), canvas.width.minus(360f), newLine.plus(183f), body)
            canvas.drawText(debtAmount.toEllipses(10), canvas.width.minus(30f), newLine.plus(183f), title)

        }

        val nextLine = customers.size.plus(1).times(50f)
        // Draw Rectangle
        paint.color = Color.rgb(150, 50, 50)
        canvas.drawRect(20f, nextLine.plus(140f), canvas.width.minus(20f), nextLine.plus(190f), paint)


        // Write Invoice total
        body.color = Color.WHITE
        body.textSize = 25f
        body.textAlign = Paint.Align.LEFT
        canvas.drawText("Total Debt Amount", 25f, nextLine.plus(175f), body)
        body.textAlign = Paint.Align.RIGHT
        canvas.drawText("GHS ${customers.sumOf { it.debtAmount?: 0.0 }.shortened()}", canvas.width.minus(30f), nextLine.plus(175f), body)


        pdfDocument.finishPage(page)
        val directory = getDirectory(context)
        val file = File(directory, "customers.pdf")

        withContext(Dispatchers.IO) {
            pdfDocument.writeTo(FileOutputStream(file))
        }
        pdfDocument.close()
        emit(Resource.Success("Pdf document successfully created"))

    }

    private fun getDirectory(context: Context): File{
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
            File(it, context.resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir
    }

}