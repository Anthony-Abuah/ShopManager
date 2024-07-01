package com.example.myshopmanagerapp.core

import com.example.myshopmanagerapp.feature_app.data.local.entities.personnel.PersonnelEntity
import com.example.myshopmanagerapp.feature_app.data.util.GsonParser
import com.example.myshopmanagerapp.feature_app.data.util.JsonParser
import com.example.myshopmanagerapp.feature_app.domain.model.QuantityCategorization
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object TypeConverters {
    private val jsonParser: JsonParser = GsonParser(Gson())


    fun String?.toRevenueTypes(): RevenueTypes {
        return this?.let {
            jsonParser.fromJson<RevenueTypes>(
                it, object : TypeToken<RevenueTypes>(){}.type)
        }?: emptyList()
    }

    fun RevenueTypes?.toRevenueTypesJson(): String{
        return jsonParser.toJson(
            this,
            object : TypeToken<RevenueTypes>(){}.type
        ) ?: "[]"
    }
    fun String?.toExpenseNames(): ExpenseNames {
        return this?.let {
            jsonParser.fromJson<ExpenseNames>(
                it, object : TypeToken<ExpenseNames>(){}.type)
        }?: emptyList()
    }

    fun ExpenseNames?.toExpenseNamesJson(): String{
        return jsonParser.toJson(
            this,
            object : TypeToken<ExpenseNames>(){}.type
        ) ?: "[]"
    }

    fun BankPersonnelList?.toBankPersonnelJson(): String{
        return jsonParser.toJson(
            this,
            object : TypeToken<BankPersonnelList>(){}.type
        ) ?: "[]"
    }

    fun String?.toBankPersonnelList(): BankPersonnelList {
        return this?.let {
            jsonParser.fromJson<BankPersonnelList>(
                it, object : TypeToken<BankPersonnelList>(){}.type)
        }?: emptyList()
    }

    fun PersonnelEntities?.toPersonnelEntitiesJson(): String{
        return jsonParser.toJson(
            this,
            object : TypeToken<PersonnelEntities>(){}.type
        ) ?: "[]"
    }

    fun String?.toPersonnelEntities(): PersonnelEntities {
        return this?.let {
            jsonParser.fromJson<PersonnelEntities>(
                it, object : TypeToken<PersonnelEntities>(){}.type)
        }?: emptyList()
    }


    fun PersonnelEntity?.toPersonnelEntityJson(): String{
        return jsonParser.toJson(
            this,
            object : TypeToken<PersonnelEntity>(){}.type
        ) ?: "[]"
    }

    fun String?.toPersonnelEntity(): PersonnelEntity? {
        return this?.let {
            jsonParser.fromJson<PersonnelEntity>(
                it, object : TypeToken<PersonnelEntity>(){}.type)
        }
    }

    fun SupplierRoles?.toSupplierRolesJson(): String{
        return jsonParser.toJson(
            this,
            object : TypeToken<SupplierRoles>(){}.type
        ) ?: "[]"
    }

    fun String?.toSupplierRoles(): SupplierRoles {
        return this?.let {
            jsonParser.fromJson<SupplierRoles>(
                it, object : TypeToken<SupplierRoles>(){}.type)
        }?: emptyList()
    }

    fun String?.toPersonnelRoles(): PersonnelRoles {
        return this?.let {
            jsonParser.fromJson<PersonnelRoles>(
                it, object : TypeToken<PersonnelRoles>(){}.type)
        }?: emptyList()
    }

    fun PersonnelRoles?.toPersonnelRolesJson(): String{
        return jsonParser.toJson(
            this,
            object : TypeToken<PersonnelRoles>(){}.type
        ) ?: "[]"
    }

    fun String?.toExpenseTypes(): ExpenseTypes {
        return this?.let {
            jsonParser.fromJson<ExpenseTypes>(
                it, object : TypeToken<ExpenseTypes>(){}.type)
        }?: emptyList()
    }

    fun ExpenseTypes?.toExpenseTypesJson(): String{
        return jsonParser.toJson(
            this,
            object : TypeToken<ExpenseTypes>(){}.type
        ) ?: "[]"
    }

    fun String?.toManufacturers(): Manufacturers {
        return this?.let {
            jsonParser.fromJson<Manufacturers>(
                it, object : TypeToken<Manufacturers>(){}.type)
        }?: emptyList()
    }

    fun Manufacturers?.toManufacturersJson(): String{
        return jsonParser.toJson(
            this,
            object : TypeToken<Manufacturers>(){}.type
        ) ?: "[]"
    }

    fun String?.toItemCategories(): ItemCategories {
        return this?.let {
            jsonParser.fromJson<ItemCategories>(
                it, object : TypeToken<ItemCategories>(){}.type)
        }?: emptyList()
    }

    fun ItemCategories?.toItemCategoriesJson(): String{
        return jsonParser.toJson(
            this,
            object : TypeToken<ItemCategories>(){}.type
        ) ?: "[]"
    }

    fun String?.toQuantityCategorizations(): QuantityCategorizations {
        return this?.let {
            jsonParser.fromJson<QuantityCategorizations>(
                it, object : TypeToken<QuantityCategorizations>(){}.type)
        }?: emptyList()
    }

    fun QuantityCategorizations?.toQuantityCategorizationJson(): String{
        return jsonParser.toJson(
            this,
            object : TypeToken<QuantityCategorizations>(){}.type
        ) ?: "[]"
    }



}