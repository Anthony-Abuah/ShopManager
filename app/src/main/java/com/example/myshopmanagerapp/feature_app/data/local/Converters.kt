package com.example.myshopmanagerapp.feature_app.data.local


import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.myshopmanagerapp.core.ItemQuantities
import com.example.myshopmanagerapp.core.Prices
import com.example.myshopmanagerapp.core.QuantityCategorizations
import com.example.myshopmanagerapp.core.StockEntities
import com.example.myshopmanagerapp.feature_app.data.util.JsonParser
import com.example.myshopmanagerapp.feature_app.domain.model.ItemQuantityInfo
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class Converters(
    private val jsonParser: JsonParser
)
{
    //Prices
    @TypeConverter
    fun fromPricesJson(prices: String?): Prices {
        return prices?.let {
            jsonParser.fromJson<Prices>(
                it,
                object : TypeToken<Prices>(){}.type)
        } ?: emptyList()
    }
    @TypeConverter
    fun toPricesJson(prices: Prices?): String{
        return jsonParser.toJson(
            prices,
            object : TypeToken<Prices>(){}.type
        ) ?: "[]"
    }


    //Item Quantities
    @TypeConverter
    fun fromItemQuantitiesJson(itemQuantities: String?): ItemQuantities {
        return itemQuantities?.let {
            jsonParser.fromJson<ItemQuantities>(
                it, object : TypeToken<ItemQuantities>(){}.type)
        } ?: emptyList()
    }
    @TypeConverter
    fun toItemQuantitiesJson(itemQuantities: ItemQuantities?): String{
        return jsonParser.toJson(
            itemQuantities,
            object : TypeToken<ItemQuantities>(){}.type
        ) ?: "[]"
    }


    //Item Quantities
    @TypeConverter
    fun fromQuantityCategorizationsJson(quantityCategorizations: String?): QuantityCategorizations {
        return quantityCategorizations?.let {
            jsonParser.fromJson<QuantityCategorizations>(
                it, object : TypeToken<QuantityCategorizations>(){}.type)
        } ?: emptyList()
    }

    @TypeConverter
    fun toQuantityCategorizationsJson(quantityCategorizations: QuantityCategorizations?): String{
        return jsonParser.toJson(
            quantityCategorizations,
            object : TypeToken<QuantityCategorizations>(){}.type
        ) ?: "[]"
    }



    //Price
    @TypeConverter
    fun fromItemQuantityInfoListJson(itemQuantityInfoList: String?): List<ItemQuantityInfo> {
        return itemQuantityInfoList?.let {
            jsonParser.fromJson<List<ItemQuantityInfo>>(
                it, object : TypeToken<List<ItemQuantityInfo>>(){}.type)
        } ?: emptyList()
    }

    @TypeConverter
    fun toItemQuantityInfoListJson(itemQuantityInfoList: List<ItemQuantityInfo>?): String{
        return jsonParser.toJson(
            itemQuantityInfoList,
            object : TypeToken<List<ItemQuantityInfo>>(){}.type
        ) ?: "[]"
    }


    //StockEntities
    @TypeConverter
    fun fromStockEntitiesJson(stockEntities: String?): StockEntities {
        return stockEntities?.let {
            jsonParser.fromJson<StockEntities>(
                it,
                object : TypeToken<StockEntities>(){}.type)
        } ?: emptyList()
    }
    @TypeConverter
    fun toStockEntitiesJson(stockEntities: StockEntities?): String{
        return jsonParser.toJson(
            stockEntities,
            object : TypeToken<StockEntities>(){}.type
        ) ?: "[]"
    }


}