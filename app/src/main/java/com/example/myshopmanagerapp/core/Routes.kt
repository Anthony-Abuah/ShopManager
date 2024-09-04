package com.example.myshopmanagerapp.core

object Routes {


    const val BASE_URL = "http://127.0.0.1:8081"

    //Company Routes
    const val getAllCompanies = "/getAllCompanies"
    const val getCompany = "/getCompany/{uniqueCompanyId}"
    const val addCompany = "/addCompany"
    const val createOnlineCompany = "/createOnlineCompany"
    const val changePassword = "/changePassword/{uniqueCompanyId}/{currentPassword}/{updatedPassword}"
    const val resetPassword = "/resetPassword/{uniqueCompanyId}/{email}/{updatedPassword}/{personnelPassword}"
    const val changeCompanyName = "/changeCompanyName/{uniqueCompanyId}/{currentPassword}/{companyName}"
    const val changeEmail = "/changeCompanyEmail/{uniqueCompanyId}/{currentPassword}/{email}"
    const val changeContact = "/changeCompanyContact/{uniqueCompanyId}/{currentPassword}/{companyContact}"
    const val changeLocation = "/changeCompanyLocation/{uniqueCompanyId}/{currentPassword}/{companyLocation}"
    const val changeProductsAndServices = "/changeCompanyProductAndServices/{uniqueCompanyId}/{currentPassword}/{companyProductsAndServices}"
    const val changeOtherInfo = "/changeCompanyOtherInfo/{uniqueCompanyId}/{currentPassword}/{otherInfo}"
    const val deleteCompany = "/deleteCompany/{uniqueCompanyId}"
    const val login = "/login/{email}/{password}"


    //Customer Routes
    const val getAllCustomers = "/getAllCustomers"
    const val getAllCompanyCustomers = "/getAllCompanyCustomers/{uniqueCompanyId}"
    const val addCustomer = "/addCustomer"
    const val addCustomers = "/addCustomers/{uniqueCompanyId}"
    const val smartAddCompanyCustomers = "/smartAddCompanyCustomers/{uniqueCompanyId}"
    const val updateCustomer = "/updateCustomer"


    //Supplier Routes
    const val getAllCompanySuppliers = "/getAllCompanySuppliers/{uniqueCompanyId}"
    const val addSuppliers = "/addSuppliers/{uniqueCompanyId}"
    const val smartAddCompanySuppliers = "/smartAddCompanySuppliers/{uniqueCompanyId}"



    //Bank Routes
    const val getAllCompanyBanks = "/getAllCompanyBanks/{uniqueCompanyId}"
    const val addBanks = "/addBanks/{uniqueCompanyId}"
    const val smartAddCompanyBanks = "/smartAddCompanyBanks/{uniqueCompanyId}"


    //Cash in Routes
    const val getAllCompanyCashIns = "/getAllCompanyCashIns/{uniqueCompanyId}"
    const val addCashIns = "/addCashIns/{uniqueCompanyId}"
    const val smartAddCompanyCashIns = "/smartAddCompanyCashIns/{uniqueCompanyId}"



    //Receipt Routes
    const val getAllCompanyReceipts = "/getAllCompanyReceipts/{uniqueCompanyId}"
    const val addReceipts = "/addReceipts/{uniqueCompanyId}"
    const val smartAddCompanyReceipts = "/smartAddCompanyReceipts/{uniqueCompanyId}"


    //Debt Routes
    const val getAllCompanyDebts = "/getAllCompanyDebts/{uniqueCompanyId}"
    const val addDebts = "/addDebts/{uniqueCompanyId}"
    const val smartAddCompanyDebts = "/smartAddCompanyDebts/{uniqueCompanyId}"


    //DebtRepayment Routes
    const val getAllCompanyDebtRepayments = "/getAllCompanyDebtRepayments/{uniqueCompanyId}"
    const val addDebtRepayments = "/addDebtRepayments/{uniqueCompanyId}"
    const val smartAddCompanyDebtRepayments = "/smartAddCompanyDebtRepayments/{uniqueCompanyId}"


    //Inventory Routes
    const val getAllCompanyInventories = "/getAllCompanyInventories/{uniqueCompanyId}"
    const val addInventories = "/addInventories/{uniqueCompanyId}"
    const val smartAddCompanyInventories = "/smartAddCompanyInventories/{uniqueCompanyId}"



    //Expense Routes
    const val getAllCompanyExpenses = "/getAllCompanyExpenses/{uniqueCompanyId}"
    const val addExpenses = "/addExpenses/{uniqueCompanyId}"
    const val smartAddCompanyExpenses = "/smartAddCompanyExpenses/{uniqueCompanyId}"


    //Withdrawal Routes
    const val getAllCompanyWithdrawals = "/getAllCompanyWithdrawals/{uniqueCompanyId}"
    const val addWithdrawals = "/addWithdrawals/{uniqueCompanyId}"
    const val smartAddCompanyWithdrawals = "/smartAddCompanyWithdrawals/{uniqueCompanyId}"



    //Savings Routes
    const val getAllCompanySavings = "/getAllCompanySavings/{uniqueCompanyId}"
    const val addSavings = "/addSavings"
    const val addListOfSavings = "/addListOfSavings/{uniqueCompanyId}"
    const val smartAddCompanySavings = "/smartAddCompanySavings/{uniqueCompanyId}"

    //InventoryStock Routes
    const val getAllCompanyInventoryStocks = "/getAllCompanyInventoryStocks/{uniqueCompanyId}"
    const val addInventoryStocks = "/addInventoryStocks/{uniqueCompanyId}"
    const val smartAddCompanyInventoryStocks = "/smartAddCompanyInventoryStocks/{uniqueCompanyId}"



    //InventoryItem Routes
    const val getAllInventoryItems = "/getAllInventoryItems"
    const val getAllCompanyInventoryItems = "/getAllCompanyInventoryItems/{uniqueCompanyId}"
    const val addInventoryItems = "/addInventoryItems/{uniqueCompanyId}"
    const val smartAddCompanyInventoryItems = "/smartAddCompanyInventoryItems/{uniqueCompanyId}"
    const val updateInventoryItem = "/updateInventoryItem"


    //Revenue Routes
    const val getAllRevenues = "/getAllRevenues"
    const val getAllCompanyRevenues = "/getAllCompanyRevenues/{uniqueCompanyId}"
    const val addRevenues = "/addRevenues/{uniqueCompanyId}"
    const val smartAddCompanyRevenues = "/smartAddCompanyRevenues/{uniqueCompanyId}"



    //Stock Routes
    const val getAllCompanyStocks = "/getAllCompanyStocks/{uniqueCompanyId}"
    const val addStock = "/addStock"
    const val addStocks = "/addStocks/{uniqueCompanyId}"
    const val smartAddCompanyStocks = "/smartAddCompanyStocks/{uniqueCompanyId}"
    const val updateStock = "/updateStock"


    //Personnel Routes
    const val getAllPersonnel = "/getAllPersonnel"
    const val getAllCompanyPersonnel = "/getAllCompanyPersonnel/{uniqueCompanyId}"
    const val addPersonnel = "/addPersonnel"
    const val addListOfPersonnel = "/addListOfPersonnel/{uniqueCompanyId}"
    const val smartAddCompanyPersonnel = "/smartAddCompanyPersonnel/{uniqueCompanyId}"



}