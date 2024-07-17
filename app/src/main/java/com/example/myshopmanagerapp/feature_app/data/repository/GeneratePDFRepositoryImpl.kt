package com.example.myshopmanagerapp.feature_app.data.repository

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.*
import com.example.myshopmanagerapp.core.Functions.generateReceiptId
import com.example.myshopmanagerapp.core.Functions.shortened
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.Functions.toEllipses
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.core.TypeConverters.toPersonnelEntity
import com.example.myshopmanagerapp.core.TypeConverters.toUniqueIds
import com.example.myshopmanagerapp.core.TypeConverters.toUniqueIdsJson
import com.example.myshopmanagerapp.feature_app.MyShopManagerApp
import com.example.myshopmanagerapp.feature_app.data.local.entities.receipt.ReceiptDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.receipt.ReceiptEntity
import com.example.myshopmanagerapp.feature_app.domain.model.ReceiptInfo
import com.example.myshopmanagerapp.feature_app.domain.model.UniqueId
import com.example.myshopmanagerapp.feature_app.domain.repository.GeneratePDFRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class GeneratePDFRepositoryImpl(
    private val receiptDao: ReceiptDao
) : GeneratePDFRepository{
    override fun getAllReceipts(): Flow<Resource<ReceiptEntities?>> = flow{
        emit(Resource.Loading())
        val allReceipts: List<ReceiptEntity>?
        try {
            allReceipts = receiptDao.getAllReceipts()?.sortedByDescending { it.date }
            emit(Resource.Success(allReceipts))
        }catch (e: Exception){
            emit(Resource.Error(
                message = "Couldn't load all Receipts from Database",
                data = emptyList()
            ))
        }
    }

    override suspend fun addReceipt(receipt: ReceiptEntity): Flow<Resource<String?>> = flow{
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val personnel = userPreferences.getPersonnelInfo.first().toPersonnelEntity()
            val date = receipt.date
            val customerName = receipt.customerName
            val uniqueReceiptId = generateReceiptId(date, customerName)
            val allReceipts = receiptDao.getAllReceipts() ?: emptyList()
            val allReceiptIds = allReceipts.map { it.uniqueReceiptId }

            when(true){
                receipt.items.isEmpty() ->{
                    emit(Resource.Error(
                        data = "Auto generated receipt id already exists. \nPlease try again",
                        message = "Couldn't create receipt"
                    ))
                }
                (allReceiptIds.contains(uniqueReceiptId)) ->{
                    emit(Resource.Error(
                        data = "Auto generated receipt id already exists. \nPlease try again",
                        message = "Couldn't create receipt"
                    ))
                }
                (personnel == null) ->{
                    emit(Resource.Error(
                        data = "Could not get personnel.\nPlease log in as personnel to generate this receipt",
                        message = "Couldn't create receipt"
                    ))
                }
                else-> {
                    val personnelName = "${personnel.lastName} ${personnel.firstName} ${personnel.otherNames}"
                    val personnelRole = personnel.role.toNotNull()
                    val receiptInfo = receipt.copy(uniqueReceiptId = uniqueReceiptId, personnelName = personnelName, personnelRole = personnelRole)
                    receiptDao.addReceipt(receiptInfo)
                    val addedReceiptIdsJson = AdditionEntityMarkers(context).getAddedReceiptIds.first().toNotNull()
                    val addedReceiptIds = addedReceiptIdsJson.toUniqueIds().plus(UniqueId(receipt.uniqueReceiptId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedReceiptIds(addedReceiptIds.toUniqueIdsJson())
                    emit(Resource.Success("Receipt successfully saved"))
                }
            }

        }catch (e: Exception){
            emit(Resource.Error(
                data = "${e.message}",
                message = "Couldn't create receipt"
            ))
        }

    }

    override suspend fun updateReceipt(receipt: ReceiptEntity): Flow<Resource<String?>> = flow{
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            val userPreferences = UserPreferences(context)
            val personnel = userPreferences.getPersonnelInfo.first().toPersonnelEntity()

            val oldReceipt = receiptDao.getReceipt(receipt.uniqueReceiptId)

            when(true) {
                receipt.items.isEmpty() -> {
                    emit(
                        Resource.Error(
                            data = "Auto generated receipt id already exists. \nPlease try again",
                            message = "Couldn't update receipt"
                        )
                    )
                }
                (oldReceipt == null) -> {
                    emit(
                        Resource.Error(
                            data = "Could not load the details of the receipt you want to update. \nPlease try again",
                            message = "Couldn't update receipt"
                        )
                    )
                }
                (personnel == null) -> {
                    emit(
                        Resource.Error(
                            data = "Could not get personnel.\nPlease log in as personnel to generate this receipt",
                            message = "Couldn't update receipt"
                        )
                    )
                }
                else -> {
                    val personnelName = "${personnel.lastName} ${personnel.firstName} ${personnel.otherNames}"
                    val personnelRole = personnel.role.toNotNull()
                    val receiptInfo = receipt.copy( personnelName = personnelName, personnelRole = personnelRole)
                    receiptDao.updateReceipt(receiptInfo)

                    val addedReceiptIdsJson = AdditionEntityMarkers(context).getAddedReceiptIds.first().toNotNull()
                    val addedReceiptIds = addedReceiptIdsJson.toUniqueIds().plus(UniqueId(receipt.uniqueReceiptId)).toSet().toList()
                    AdditionEntityMarkers(context).saveAddedReceiptIds(addedReceiptIds.toUniqueIdsJson())

                    val updatedReceiptIdsJson = ChangesEntityMarkers(context).getChangedReceiptIds.first().toNotNull()
                    val updatedReceiptIds = updatedReceiptIdsJson.toUniqueIds().plus(UniqueId(receipt.uniqueReceiptId)).toSet().toList()
                    ChangesEntityMarkers(context).saveChangedReceiptIds(updatedReceiptIds.toUniqueIdsJson())
                    emit(Resource.Success("Receipt successfully updated"))
                }
            }
        }catch (e: Exception){
            emit(Resource.Error(
                data = "${e.message}",
                message = "Couldn't update receipt"
            ))
        }
    }

    override suspend fun deleteReceipt(uniqueReceiptId: String): Flow<Resource<String?>> = flow{
        emit(Resource.Loading())
        try {
            val context = MyShopManagerApp.applicationContext()
            receiptDao.deleteReceipt(uniqueReceiptId)
            val addedReceiptIdsJson = AdditionEntityMarkers(context).getAddedReceiptIds.first().toNotNull()
            val addedReceiptIds = addedReceiptIdsJson.toUniqueIds().filter{it.uniqueId != uniqueReceiptId}.toSet().toList()
            AdditionEntityMarkers(context).saveAddedReceiptIds(addedReceiptIds.toUniqueIdsJson())

            val deletedReceiptIdsJson = ChangesEntityMarkers(context).getChangedReceiptIds.first().toNotNull()
            val deletedReceiptIds = deletedReceiptIdsJson.toUniqueIds().plus(UniqueId(uniqueReceiptId)).toSet().toList()
            ChangesEntityMarkers(context).saveChangedReceiptIds(deletedReceiptIds.toUniqueIdsJson())
            emit(Resource.Success("Receipt successfully deleted"))

        }catch (e: Exception){
            emit(Resource.Error(
                data = "${e.message}",
                message = "Couldn't delete receipt"
            ))
        }
    }

    override suspend fun getReceipt(uniqueReceiptId: String): ReceiptEntity? = receiptDao.getReceipt(uniqueReceiptId)


    override suspend fun generateReceipt(context: Context, receiptInfo: ReceiptInfo): Flow<Resource<String?>> = flow{
        emit(Resource.Loading())
        try {
            val pageHeight = receiptInfo.items.size.plus(8).times(50)
            val pageWidth = 800
            val pdfDocument = PdfDocument()
            val paint = Paint()
            val title = Paint()
            val body = Paint()
            val pageInfo = PageInfo.Builder(pageWidth, pageHeight, 1).create()
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas
            val centerWidth = canvas.width.div(2f)

            // Write Shop route
            title.color = Color.BLACK
            title.textSize = 40f
            title.textAlign = Paint.Align.CENTER
            canvas.drawText(receiptInfo.shopName.toEllipses(25), centerWidth, 60f, title)

            // Write Location
            body.color = Color.BLACK
            body.textSize = 25f
            body.textAlign = Paint.Align.CENTER
            canvas.drawText("Location: ${receiptInfo.shopLocation.toEllipses(50)}", centerWidth, 95f, body)

            // Write Contact
            body.color = Color.BLACK
            body.textSize = 25f
            body.textAlign = Paint.Align.CENTER
            canvas.drawText("Contact: ${receiptInfo.shopContact.toEllipses(50)}", centerWidth, 130f, body)

            // Draw line
            paint.color = Color.rgb(180, 180, 180)
            canvas.drawLine(20f, 140f, canvas.width.minus(20f), 141f, paint)


            // Draw Rectangle
            paint.color = Color.rgb(150, 50, 50)
            canvas.drawRect(20f, 140f, canvas.width.minus(20f), 190f, paint)


            // Write Date
            body.color = Color.WHITE
            body.textSize = 25f
            body.textAlign = Paint.Align.LEFT
            canvas.drawText("Date: ", 25f, 177f, body)
            canvas.drawText(receiptInfo.date.toDate().toDateString(), 150f, 177f, body)



            // Write ReceiptId
            body.color = Color.WHITE
            body.textSize = 25f
            body.textAlign = Paint.Align.LEFT
            canvas.drawText("Receipt Id: ", centerWidth, 175f, body)
            body.textAlign = Paint.Align.RIGHT
            canvas.drawText(receiptInfo.uniqueReceiptId, canvas.width.minus(30f), 175f, body)

            // Draw Start Border
            paint.color = Color.rgb(150, 50, 50)
            canvas.drawRect(20f, 190f, 21f, 240f, paint)

            // Draw End Border
            paint.color = Color.rgb(150, 50, 50)
            canvas.drawRect(canvas.width.minus(21f), 190f, canvas.width.minus(20f), 240f, paint)


            // Write Receipt Name
            body.color = Color.BLACK
            body.textSize = 25f
            body.textAlign = Paint.Align.LEFT
            canvas.drawText("Customer: ", 25f, 227f, body)
            canvas.drawText(receiptInfo.customerName.toEllipses(23), 150f, 227f, body)


            // Write Receipt Contact
            body.color = Color.BLACK
            body.textSize = 25f
            body.textAlign = Paint.Align.LEFT
            canvas.drawText("Tel: ", centerWidth.plus(70f), 227f, body)
            body.textAlign = Paint.Align.RIGHT
            canvas.drawText(receiptInfo.shopContact.toEllipses(30), canvas.width.minus(30f), 227f, body)



            // Draw Rectangle
            paint.color = Color.rgb(150, 50, 50)
            canvas.drawRect(20f, 240f, canvas.width.minus(20f), 290f, paint)


            // Write Invoice title
            body.color = Color.WHITE
            body.textSize = 25f
            body.textAlign = Paint.Align.LEFT
            title.color = Color.WHITE
            title.textSize = 25f
            title.textAlign = Paint.Align.RIGHT
            canvas.drawText("Qty", 25f, 277f, body)
            canvas.drawText("Item Name", 150f, 277f, body)
            canvas.drawText("Unit Cost", canvas.width.minus(300f), 277f, body)
            canvas.drawText("Amount", canvas.width.minus(30f), 277f, title)

            receiptInfo.items.forEachIndexed { index, item->
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
                title.color = Color.BLACK
                title.textSize = 25f
                title.textAlign = Paint.Align.RIGHT
                canvas.drawText(item.quantity.shortened().toEllipses(10), 25f, newLine.plus(277f), body)
                canvas.drawText(item.itemName.toEllipses(25), 150f, newLine.plus(277f), body)
                canvas.drawText(item.unitPrice.shortened().toEllipses(10), canvas.width.minus(300f), newLine.plus(277f), body)
                canvas.drawText(item.amount.shortened().toEllipses(10), canvas.width.minus(30f), newLine.plus(277f), title)

            }

            val nextLine = receiptInfo.items.size.plus(1).times(50f)
            // Draw Rectangle
            paint.color = Color.rgb(150, 50, 50)
            canvas.drawRect(20f, nextLine.plus(240f), canvas.width.minus(20f), nextLine.plus(290f), paint)


            // Write Invoice total
            body.color = Color.WHITE
            body.textSize = 25f
            body.textAlign = Paint.Align.LEFT
            canvas.drawText("Total", 25f, nextLine.plus(277f), body)
            body.textAlign = Paint.Align.RIGHT
            canvas.drawText("GHS ${receiptInfo.totalAmount.shortened()}", canvas.width.minus(30f), nextLine.plus(277f), body)


            val issuer = if (receiptInfo.personnelRole == null) receiptInfo.personnelName.toNotNull()
            else receiptInfo.personnelName.toNotNull() + "(${receiptInfo.personnelRole})"

            // Write Invoice total
            body.color = Color.BLACK
            body.textSize = 25f
            body.textAlign = Paint.Align.LEFT
            canvas.drawText("Prepared by: ", 25f, nextLine.plus(327f), body)
            canvas.drawText(issuer, 175f, nextLine.plus(327f), body)





            pdfDocument.finishPage(page)
            val directory = getDirectory(context)
            val file = File(directory, "invoice.pdf")

            withContext(Dispatchers.IO) {
                pdfDocument.writeTo(FileOutputStream(file))
            }
            pdfDocument.close()
            emit(Resource.Success("Pdf document successfully created"))


        }
        catch (e: Exception){
            emit(Resource.Error(
                data = "${e.message}",
                message = "${e.message}",
            ))
        }
    }



    private fun getDirectory(context: Context): File{
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
            File(it, context.resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir
    }

}