package com.example.myshopmanagerapp.core

import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import co.yml.charts.common.extensions.isNotNull
import com.example.myshopmanagerapp.core.Constants.CAMERAX_PERMISSIONS
import com.example.myshopmanagerapp.core.Constants.emptyString
import com.example.myshopmanagerapp.core.Constants.longDateFormat
import com.example.myshopmanagerapp.core.Constants.shortDateFormat
import com.example.myshopmanagerapp.feature_app.data.local.entities.company.CompanyEntity
import com.example.myshopmanagerapp.feature_app.data.remote.dto.company.CompanyInfoDto
import com.example.myshopmanagerapp.feature_app.data.util.GsonParser
import com.example.myshopmanagerapp.feature_app.data.util.JsonParser
import com.example.myshopmanagerapp.feature_app.domain.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.pow
import kotlin.math.roundToInt

object Functions {

    private val listOfNumbers = listOf(1,2,3,4,5,6,7,8,9,0)
    fun LocalDate?.toDate(): Date = Date.from(this?.atStartOfDay(ZoneId.systemDefault())?.toInstant()) ?: Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())
    val shortDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(shortDateFormat)!!
    val longDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(longDateFormat)!!
    fun String?.toNotNull(): String = this ?: emptyString
    fun Long?.toNotNull(): Long = this ?: 0.toLong()
    fun Double?.toNotNull(): Double = this ?: 0.0
    fun Double?.toRoundedInt(): Int = if (this?.isNaN() == true) 0 else this?.roundToInt() ?: 0
    fun Int?.toNotNull(): Int = this ?: 0

    fun Long.toDate(): Date = Date(this)

    fun Long.toLocalDate(): LocalDate = this.toDate().toLocalDate()

    fun Long.toDateString(): String = this.toDate().toDateString()

    fun Date.toLocalDate(): LocalDate = this.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

    fun String.toLocalDate(): LocalDate = LocalDate.parse(this, shortDateFormatter)

    fun LocalDate.toDateString(): String = shortDateFormatter.format(this)

    fun Date.toDateString(): String = this.toLocalDate().toDateString()

    fun Date?.toTimestamp(): Long = this?.time ?: 0

    fun LocalDate?.toTimestamp(): Long = if (this.isNotNull()) this.toDate().toTimestamp() else 0

    fun Double.toTwoDecimalPlaces(): Double = if(this.isNaN()) 0.0 else (this * 100.0).roundToInt() / 100.0

    fun String?.toLocation(): String {
        val location = this ?: "Location: My Shop Location"
        return if (location.length > 30){
            "${location.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }}..."
        }else location
    }

    fun String.removeBoxBracket(): String{
        return this.removePrefix("[").removeSuffix("]")
    }

    fun String?.toContact(): String {
        val contact = this ?: "Contact: 0123456789"
        return if (contact.length > 20){
            "$contact..."
        }else contact
    }

    private fun QuantityCategorization.toItemQuantity(itemQuantity: ItemQuantity): ItemQuantity?{
        return if ((this.sizeName == itemQuantity.sizeName) && (this.unitsPerThisSize == itemQuantity.unitsPerSize)){
            itemQuantity
        }else{ null }
    }
    private fun QuantityCategorization.toItemQuantity(quantity: Int): ItemQuantity{
        return ItemQuantity(this.sizeName, quantity, this.unitsPerThisSize)
    }

    private fun ItemQuantities.toQuantityCategorizations(): QuantityCategorizations{
        return this.map { it.toQuantityCategorization() }
    }

    fun QuantityCategorizations.getItemQuantities(itemQuantities: ItemQuantities): ItemQuantities {
        val nullableCategorizations = mutableListOf<ItemQuantity?>()
        val sortedCategorizations = this.sortedBy { it.unitsPerThisSize }
        val remainingCategorizations = sortedCategorizations.minus(itemQuantities.toQuantityCategorizations())
        val remainingItemQuantities = mutableListOf<ItemQuantity>()
        val items = itemQuantities.sortedBy { it.unitsPerSize }
        sortedCategorizations.forEach { quantityCategorization ->
            items.forEach { itemQuantity ->
                nullableCategorizations.add(quantityCategorization.toItemQuantity(itemQuantity))
            }
        }
        remainingCategorizations.forEach { quantityCategorization ->
            remainingItemQuantities.add(quantityCategorization.toItemQuantity(0))
        }
        return remainingItemQuantities.plus(nullableCategorizations.filterNotNull()).sortedBy { it.unitsPerSize }
    }

    fun String.toEllipses(length: Int): String {
        return if (this.length > length){
            "${this.take(length.minus(3))}..."
        }else this
    }

    fun Double.toNearestCeiling(): Double {
        val exponent = kotlin.math.ceil(kotlin.math.log10(this)).toInt()
        return kotlin.math.ceil(this/10.0.pow(exponent.toDouble()))*10.0.pow(exponent.toDouble())
    }

    fun Double.shortened(): String{
        return when(true){
             (this < 100000.0) ->{this.toTwoDecimalPlaces().toString()}
             (this < 1000000) ->{"${this.div(100000).toTwoDecimalPlaces()}K"}
             (this < 100000000) ->{"${this.div(1000000).toTwoDecimalPlaces()}M"}
             (this < 100000000000) ->{"${this.div(1000000000).toTwoDecimalPlaces()}B"}
            else-> this.toTwoDecimalPlaces().toString()
        }
    }

    fun toIntegerHours(value: String): Int{
        return value.take(2).trim().toInt()
    }

    fun convertToDouble(number: String): Double{
        return try { number.toDouble() }catch (e: NumberFormatException){
            0.0
        }
    }

    //fun String.convertToInt(): Int = convertToDouble(this).toInt()

    fun dateToTimestamp(date: Date): Long {
        return date.time
    }

    fun nullableDateToTimestamp(date: Date?): Long {
        return date?.time ?: 0
    }

    fun textIsInvalid(name: String): Boolean{
        var value = true
        try { name.toDouble() }catch (e: NumberFormatException){
            value = false
        }
        return value
    }

    fun amountIsNotValid(amount: String): Boolean{
        return try { (amount.toDouble().isNaN())
        }catch (e: NumberFormatException){
            true
        }
    }

    fun convertToInt(number: String): Int{
        return try { number.toDouble().toInt()}catch (e: NumberFormatException){
            0
        }
    }

    fun roundDouble(value: Double) : Double{
        val rounded = value.times(100.0).toInt()
        return rounded.toDouble().div(100.0)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun hasRequiredPermissions(applicationContext: Context): Boolean{
        return CAMERAX_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                applicationContext, it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }


    private fun captureImage(
        controller: LifecycleCameraController,
        context: Context,
        onPhotoTaken: (Bitmap) -> Unit
    ) {
        controller.takePicture(
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)

                    val matrix = Matrix().apply {
                        postRotate(image.imageInfo.rotationDegrees.toFloat())
                    }
                    val rotatedBitmap = Bitmap.createBitmap(
                        image.toBitmap(),
                        0,
                        0,
                        image.width,
                        image.height,
                        matrix,
                        true
                    )

                    onPhotoTaken(rotatedBitmap)
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e("Camera", "Couldn't take photo: ", exception)
                }
            }
        )
    }

    private fun getImageUri(inContext: Context?, inImage: Bitmap): Uri {

        val tempFile = File.createTempFile("temprentpk", ".png")
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val bitmapData = bytes.toByteArray()

        val fileOutPut = FileOutputStream(tempFile)
        fileOutPut.write(bitmapData)
        fileOutPut.flush()
        fileOutPut.close()
        return Uri.fromFile(tempFile)
    }

    private fun Context.saveImageAsUri(bitmap: Bitmap): Uri? {
        var uri: Uri? = null
        try {
            val fileName = System.nanoTime().toString() + ".png"
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/")
                    put(MediaStore.MediaColumns.IS_PENDING, 1)
                } else {
                    val directory =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                    val file = File(directory, fileName)
                    put(MediaStore.MediaColumns.DATA, file.absolutePath)
                }
            }

            uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            uri?.let {
                contentResolver.openOutputStream(it).use { output ->
                    if (output != null) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    values.apply {
                        clear()
                        put(MediaStore.Audio.Media.IS_PENDING, 0)
                    }
                    contentResolver.update(uri, values, null, null)
                }
            }
            return uri
        }catch (e: java.lang.Exception) {
            if (uri != null) {
                // Don't leave an orphan entry in the MediaStore
                contentResolver.delete(uri, null, null)
            }
            throw e
        }
    }

    fun generateReceiptId(date: Long, customerName: String): String{
        val thisDate = date.toDate().toDateString().replace("-", "")
        val thisCustomerName = customerName.take(1).uppercase(Locale.ROOT)
        var randomNumbers: String = emptyString
        for (numbers in 1..5){
            randomNumbers += listOfNumbers.random().toString()
        }
        return "${thisDate}-$randomNumbers-$thisCustomerName"
    }

    fun generateUniqueCustomerId(customerName: String): String{
        val thisCustomerName = customerName.replace(" ", "").lowercase(Locale.ROOT)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        var randomNumbers: String = emptyString
        for (numbers in 1..5){
            randomNumbers += listOfNumbers.random().toString()
        }
        return "Customer_${thisCustomerName}_$randomNumbers"
    }

    fun generateUniquePersonnelId(personnelName: String): String{
        val thisPersonnelName = personnelName.replace(" ", "").lowercase(Locale.ROOT)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        var randomNumbers: String = emptyString
        for (numbers in 1..5){
            randomNumbers += listOfNumbers.random().toString()
        }
        return "Personnel_${thisPersonnelName}_$randomNumbers"
    }

    fun generateUniqueBankId(bankName: String): String{
        val thisBankName = bankName.replace(" ", "").lowercase(Locale.ROOT)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        var randomNumbers: String = emptyString
        for (numbers in 1..5){
            randomNumbers += listOfNumbers.random().toString()
        }
        return "B.A_${thisBankName}_$randomNumbers"
    }

    fun generateUniqueCompanyId(companyName: String): String{
        val thisCompanyName = companyName.replace(" ", "").lowercase(Locale.ROOT)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        var randomNumbers: String = emptyString
        for (numbers in 1..5){
            randomNumbers += listOfNumbers.random().toString()
        }
        return "Company_${thisCompanyName}_$randomNumbers"
    }

    fun generateUniqueSupplierId(supplierName: String): String{
        val thisSupplierName = supplierName.replace(" ", "").lowercase(Locale.ROOT)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        var randomNumbers: String = emptyString
        for (numbers in 1..5){
            randomNumbers += listOfNumbers.random().toString()
        }
        return "Supplier_${thisSupplierName}_$randomNumbers"
    }

    fun generateUniqueDebtId(value: String): String{
        val thisValue = value.replace(" ", "").lowercase(Locale.ROOT)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        var randomNumbers: String = emptyString
        for (numbers in 1..5){
            randomNumbers += listOfNumbers.random().toString()
        }
        return "Debt_${thisValue}_$randomNumbers"
    }

    fun generateUniqueDebtRepaymentId(value: String): String{
        val thisValue = value.replace(" ", "").lowercase(Locale.ROOT)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        var randomNumbers: String = emptyString
        for (numbers in 1..5){
            randomNumbers += listOfNumbers.random().toString()
        }
        return "DebtRepayment_${thisValue}_$randomNumbers"
    }
    fun generateUniqueRevenueId(value: String): String{
        val thisValue = value.replace(" ", "").lowercase(Locale.ROOT)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        var randomNumbers: String = emptyString
        for (numbers in 1..5){
            randomNumbers += listOfNumbers.random().toString()
        }
        return "Revenue_${thisValue}_$randomNumbers"
    }


    fun generateUniqueExpenseId(value: String): String{
        val thisValue = value.replace(" ", "").lowercase(Locale.ROOT)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        var randomNumbers: String = emptyString
        for (numbers in 1..5){
            randomNumbers += listOfNumbers.random().toString()
        }
        return "Expense_${thisValue}_$randomNumbers"
    }
    fun generateUniqueInventoryItemId(value: String): String{
        val thisValue = value.replace(" ", "").lowercase(Locale.ROOT)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        var randomNumbers: String = emptyString
        for (numbers in 1..5){
            randomNumbers += listOfNumbers.random().toString()
        }
        return "Item_${thisValue}_$randomNumbers"
    }

    fun generateUniqueInventoryId(value: String): String{
        val thisValue = value.replace(" ", "").lowercase(Locale.ROOT)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        var randomNumbers: String = emptyString
        for (numbers in 1..5){
            randomNumbers += listOfNumbers.random().toString()
        }
        return "Inventory_${thisValue}_$randomNumbers"
    }
    fun generateUniqueStockId(value: String): String{
        val thisValue = value.replace(" ", "").lowercase(Locale.ROOT)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        var randomNumbers: String = emptyString
        for (numbers in 1..5){
            randomNumbers += listOfNumbers.random().toString()
        }
        return "Stock_${thisValue}_$randomNumbers"
    }

    fun generateUniqueSavingsId(value: String): String{
        val thisValue = value.replace(" ", "").lowercase(Locale.ROOT)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        var randomNumbers: String = emptyString
        for (numbers in 1..5){
            randomNumbers += listOfNumbers.random().toString()
        }
        return "Savings_${thisValue}_$randomNumbers"
    }

    fun generateUniqueWithdrawalId(value: String): String{
        val thisValue = value.replace(" ", "").lowercase(Locale.ROOT)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        var randomNumbers: String = emptyString
        for (numbers in 1..5){
            randomNumbers += listOfNumbers.random().toString()
        }
        return "Withdrawal_${thisValue}_$randomNumbers"
    }

    fun ItemQuantities.getTotalNumberOfUnits(): Int{
        var totalUnits = 0
        this.forEach { itemQuantity ->
            val units = itemQuantity.unitsPerSize.times(itemQuantity.quantity)
            totalUnits = totalUnits.plus(units)
        }
        return totalUnits
    }

    fun ItemQuantities.addItemQuantities(itemQuantities: ItemQuantities): ItemQuantities{
        if (itemQuantities.isEmpty()){
            return this
        }else {
            val thisItemQuantities = mutableListOf<ItemQuantity>()
            this.forEach { itemQuantity ->
                itemQuantities.forEach { _itemQuantity ->
                    if ((itemQuantity.sizeName == _itemQuantity.sizeName) && (itemQuantity.unitsPerSize == _itemQuantity.unitsPerSize)) {
                        thisItemQuantities.add(itemQuantity.addQuantity(_itemQuantity))
                    }
                }
            }
            this.forEach { itemQuantity ->
                val nameIsContained =
                    thisItemQuantities.map { it.sizeName }.contains(itemQuantity.sizeName)
                val unitQuantityIsContained =
                    thisItemQuantities.map { it.unitsPerSize }.contains(itemQuantity.unitsPerSize)
                if (!(nameIsContained && unitQuantityIsContained)) {
                    thisItemQuantities.add(itemQuantity)
                }
            }
            itemQuantities.forEach { itemQuantity ->
                val nameIsContained =
                    thisItemQuantities.map { it.sizeName }.contains(itemQuantity.sizeName)
                val unitQuantityIsContained =
                    thisItemQuantities.map { it.unitsPerSize }.contains(itemQuantity.unitsPerSize)
                if (!(nameIsContained && unitQuantityIsContained)) {
                    thisItemQuantities.add(itemQuantity)
                }
            }
            return thisItemQuantities
        }
    }

    fun ItemQuantities.subtractItemQuantities(itemQuantities: ItemQuantities): ItemQuantities{
        val thisItemQuantities = mutableListOf<ItemQuantity>()
        val subtractedQuantities = this.minus(itemQuantities)
        this.forEach { itemQuantity ->
            itemQuantities.forEach{ _itemQuantity ->
                if ((itemQuantity.sizeName == _itemQuantity.sizeName) && (itemQuantity.unitsPerSize == _itemQuantity.unitsPerSize)){
                    thisItemQuantities.add(itemQuantity.subtractQuantity(_itemQuantity))
                }
            }
        }
        return subtractedQuantities.plus(thisItemQuantities)
    }



    private val jsonParser: JsonParser = GsonParser(Gson())



    fun toRolesJson(personnelRoles: List<PersonnelRole>?): String{
        return jsonParser.toJson(
            personnelRoles,
            object : TypeToken<List<PersonnelRole>>(){}.type
        ) ?: "[]"
    }

    fun fromManufacturersJson(manufacturers: String?): List<Manufacturer> {
        return manufacturers?.let {
            jsonParser.fromJson<List<Manufacturer>>(
                it, object : TypeToken<List<Manufacturer>>(){}.type)
        } ?: emptyList()
    }


    fun fromCategoriesJson(categories: String?): List<ItemCategory> {
        return categories?.let {
            jsonParser.fromJson<List<ItemCategory>>(
                it, object : TypeToken<List<ItemCategory>>(){}.type)
        } ?: emptyList()
    }





    fun CompanyEntity.toCompanyEntityJson(): String?{
        return jsonParser.toJson(
            this,
            object : TypeToken<CompanyEntity>(){}.type
        )
    }



    fun String?.toCompanyInfoDto(): CompanyInfoDto? {
        return this?.let {
            jsonParser.fromJson<CompanyInfoDto>(
                it, object : TypeToken<CompanyInfoDto>(){}.type)
        }
    }
    fun String?.toCompanyEntity(): CompanyEntity? {
        return this?.let {
            if (this.isNotBlank()) {
                jsonParser.fromJson<CompanyEntity>(
                    it, object : TypeToken<CompanyEntity>() {}.type
                )
            }else null
        }
    }

    fun String?.toPrices(): Prices {
        return this?.let {
            jsonParser.fromJson<Prices>(
                it, object : TypeToken<Prices>(){}.type)
        } ?: emptyList()
    }

    fun String?.toStockEntities(): StockEntities {
        return this?.let {
            jsonParser.fromJson<StockEntities>(
                it, object : TypeToken<StockEntities>(){}.type)
        } ?: emptyList()
    }



    fun Prices?.toPricesJson(): String{
        return jsonParser.toJson(
            this,
            object : TypeToken<Prices>(){}.type
        ) ?: "[]"
    }

    fun StockEntities?.toStockEntitiesJson(): String{
        return jsonParser.toJson(
            this,
            object : TypeToken<StockEntities>(){}.type
        ) ?: "[]"
    }



    fun String?.toItemQuantities(): ItemQuantities {
        return this?.let {
            jsonParser.fromJson<ItemQuantities>(
                it, object : TypeToken<ItemQuantities>(){}.type)
        } ?: emptyList()
    }

    fun ItemQuantities?.toItemQuantitiesJson(): String{
        return jsonParser.toJson(this, object : TypeToken<ItemQuantities>(){}.type) ?: "[]"
    }


}