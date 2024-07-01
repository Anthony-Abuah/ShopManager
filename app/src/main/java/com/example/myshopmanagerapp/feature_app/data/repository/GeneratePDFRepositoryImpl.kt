package com.example.myshopmanagerapp.feature_app.data.repository

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.core.*
import com.example.myshopmanagerapp.core.Constants.NotAvailable
import com.example.myshopmanagerapp.core.Functions.generateReceiptId
import com.example.myshopmanagerapp.core.Functions.shortened
import com.example.myshopmanagerapp.core.Functions.toCompanyEntity
import com.example.myshopmanagerapp.core.Functions.toDate
import com.example.myshopmanagerapp.core.Functions.toDateString
import com.example.myshopmanagerapp.core.Functions.toEllipses
import com.example.myshopmanagerapp.core.Functions.toNotNull
import com.example.myshopmanagerapp.feature_app.data.local.entities.receipt.ReceiptDao
import com.example.myshopmanagerapp.feature_app.data.local.entities.receipt.ReceiptEntity
import com.example.myshopmanagerapp.feature_app.domain.model.InventoryQuantityDisplayValues
import com.example.myshopmanagerapp.feature_app.domain.model.ReceiptInfo
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
            val date = receipt.date
            val customerName = receipt.customerName
            val uniqueReceiptId = generateReceiptId(date, customerName)
            val allReceipts = receiptDao.getAllReceipts() ?: emptyList()
            val allReceiptIds = allReceipts.map { it.uniqueReceiptId }
            if (allReceiptIds.contains(uniqueReceiptId)){
                emit(Resource.Error(
                    data = "Auto generated receipt id already exists. \nPlease try again",
                    message = "Couldn't create receipt"
                ))
            }else{
                val receiptInfo = receipt.copy(uniqueReceiptId = uniqueReceiptId)
                receiptDao.addReceipt(receiptInfo)
                emit(Resource.Success("Receipt successfully saved"))
            }
        }catch (e: Exception){
            emit(Resource.Error(
                data = "${e.message}",
                message = "Couldn't create receipt"
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
            canvas.drawText("ReceiptId: ", centerWidth, 175f, body)
            body.textAlign = Paint.Align.RIGHT
            canvas.drawText(receiptInfo.uniqueReceiptId, canvas.width.minus(30f), 175f, body)

            // Draw Start Border
            paint.color = Color.rgb(150, 50, 50)
            canvas.drawRect(20f, 190f, 21f, 240f, paint)

            // Draw End Border
            paint.color = Color.rgb(150, 50, 50)
            canvas.drawRect(canvas.width.minus(21f), 190f, canvas.width.minus(20f), 240f, paint)


            // Write Customer Name
            body.color = Color.BLACK
            body.textSize = 25f
            body.textAlign = Paint.Align.LEFT
            canvas.drawText("Customer: ", 25f, 227f, body)
            canvas.drawText(receiptInfo.customerName.toEllipses(23), 150f, 227f, body)


            // Write Customer Contact
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
            canvas.drawText("Qty", 25f, 277f, body)
            canvas.drawText("Item Name", 150f, 277f, body)
            canvas.drawText("Unit Cost", canvas.width.minus(300f), 277f, body)
            canvas.drawText("Amount", canvas.width.minus(150f), 277f, body)

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
                canvas.drawText(item.quantity.shortened().toEllipses(10), 25f, newLine.plus(277f), body)
                canvas.drawText(item.itemName.toEllipses(30), 150f, newLine.plus(277f), body)
                canvas.drawText(item.unitPrice.shortened().toEllipses(10), canvas.width.minus(300f), newLine.plus(277f), body)
                canvas.drawText(item.amount.shortened().toEllipses(10), canvas.width.minus(150f), newLine.plus(277f), body)

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
        val pageInfo = PageInfo.Builder(pageWidth, pageHeight, 2).create()
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
            title.color = if ((customer.debtAmount ?: 0.0) > 0)Color.RED else Color.BLACK
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
        val pageInfo = PageInfo.Builder(pageWidth, pageHeight, 2).create()
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
        canvas.drawText("No.", 25f, 183f, body)
        canvas.drawText("Date", 100f, 183f, body)
        canvas.drawText("Revenue Type", canvas.width.minus(450f), 183f, body)
        canvas.drawText("Revenue Amount", canvas.width.minus(175f), 183f, body)

        revenues.forEachIndexed { index, revenue->
            val newLine = index.plus(1).times(50f)

            // Draw Start Border
            paint.color = Color.rgb(150, 50, 50)
            canvas.drawRect(20f, newLine.plus(140f), 21f, newLine.plus(190f), paint)

            // Draw End Border
            paint.color = Color.rgb(150, 50, 50)
            canvas.drawRect(canvas.width.minus(21f), newLine.plus(140f), canvas.width.minus(20f), newLine.plus(190f), paint)


            val date = "${revenue.dayOfWeek?.take(3)}, ${revenue.date.toDateString()}"
            val revenueType = revenue.revenueType ?: NotAvailable
            val revenueAmount = revenue.revenueAmount.shortened()

            // Write Invoice title
            body.color = Color.BLACK
            body.textSize = 25f
            body.textAlign = Paint.Align.LEFT
            title.color = Color.BLACK
            title.textSize = 25f
            title.textAlign = Paint.Align.RIGHT
            canvas.drawText(index.plus(1).toString().toEllipses(3), 25f, newLine.plus(183f), body)
            canvas.drawText(date.toEllipses(30), 100f, newLine.plus(183f), body)
            canvas.drawText(revenueType.toEllipses(30), canvas.width.minus(460f), newLine.plus(183f), body)
            canvas.drawText(revenueAmount.toEllipses(10), canvas.width.minus(30f), newLine.plus(183f), title)

        }

        val nextLine = revenues.size.plus(1).times(50f)
        // Draw Rectangle
        paint.color = Color.rgb(150, 50, 50)
        canvas.drawRect(20f, nextLine.plus(140f), canvas.width.minus(20f), nextLine.plus(190f), paint)


        // Write Invoice total
        body.color = Color.WHITE
        body.textSize = 25f
        body.textAlign = Paint.Align.LEFT
        canvas.drawText("Total Debt Amount", 25f, nextLine.plus(175f), body)
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

    override suspend fun generateSupplierList(
        context: Context,
        suppliers: SupplierEntities
    ): Flow<Resource<String?>> {
        TODO("Not yet implemented")
    }

    override suspend fun generateInventoryItemList(
        context: Context,
        inventoryItems: InventoryItemEntities
    ): Flow<Resource<String?>> {
        TODO("Not yet implemented")
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
        val pageInfo = PageInfo.Builder(pageWidth, pageHeight, 1).create()
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


    private fun getDirectory(context: Context): File{
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
            File(it, context.resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir
    }

}