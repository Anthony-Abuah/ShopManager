package com.example.myshopmanagerapp.core

object Routes {


    const val BASE_URL = "http://127.0.0.1:8081"

    //Company Routes
    const val getAllCompanies = "/getAllCompanies"
    const val getCompany = "/getCompany/{uniqueCompanyId}"
    const val addCompany = "/addCompany"
    const val createOnlineCompany = "/createOnlineCompany"
    const val getCompanyByEmail = "/getCompanyByEmail/{email}"
    const val getCompanyByName = "/getCompanyByName/{companyName}"
    const val updateCompany = "/updateCompany"
    const val changePassword = "/changePassword/{uniqueCompanyId}/{currentPassword}/{updatedPassword}"
    const val resetPassword = "/resetPassword/{uniqueCompanyId}/{email}/{updatedPassword}/{personnelPassword}"
    const val changeCompanyName = "/changeCompanyName/{uniqueCompanyId}/{currentPassword}/{companyName}"
    const val changeEmail = "/changeCompanyEmail/{uniqueCompanyId}/{currentPassword}/{email}"
    const val changeContact = "/changeCompanyContact/{uniqueCompanyId}/{currentPassword}/{companyContact}"
    const val changeLocation = "/changeCompanyLocation/{uniqueCompanyId}/{currentPassword}/{companyLocation}"
    const val changeProductsAndServices = "/changeCompanyProductAndServices/{uniqueCompanyId}/{currentPassword}/{companyProductsAndServices}"
    const val changeOtherInfo = "/changeCompanyOtherInfo/{uniqueCompanyId}/{currentPassword}/{{otherInfo}}"
    const val deleteCompany = "/deleteCompany/{uniqueCompanyId}"
    const val deleteAllCompanies = "/deleteAllCompanies"
    const val login = "/login/{email}/{password}"


    //Customer Routes
    const val getAllCustomers = "/getAllCustomers"
    const val getAllCompanyCustomers = "/getAllCompanyCustomers/{uniqueCompanyId}"
    const val addCustomer = "/addCustomer"
    const val addCustomers = "/addCustomers/{uniqueCompanyId}"
    const val smartAddCompanyCustomers = "/smartAddCompanyCustomers/{uniqueCompanyId}"
    const val updateCustomer = "/updateCustomer"
    const val deleteCompanyCustomer = "/deleteCompanyCustomer/{uniqueCustomerId}/{uniqueCompanyId}"
    const val deleteCustomer = "/deleteCustomer/{uniqueCustomerId}/{uniqueCompanyId}"
    const val deleteAllCompanyCustomers = "/deleteAllCompanyCustomer/{uniqueCompanyId}"


    //Supplier Routes
    const val getAllSuppliers = "/getAllSuppliers"
    const val getAllCompanySuppliers = "/getAllCompanySuppliers/{uniqueCompanyId}"
    const val addSupplier = "/addSupplier"
    const val addSuppliers = "/addSuppliers/{uniqueCompanyId}"
    const val smartAddCompanySuppliers = "/smartAddCompanySuppliers/{uniqueCompanyId}"
    const val updateSupplier = "/updateSupplier"
    const val deleteCompanySupplier = "/deleteCompanySupplier/{uniqueSupplierId}/{uniqueCompanyId}"
    const val deleteSupplier = "/deleteSupplier/{uniqueSupplierId}/{uniqueCompanyId}"
    const val deleteAllCompanySuppliers = "/deleteAllCompanySupplier/{uniqueCompanyId}"



    //Bank Routes
    const val getAllBanks = "/getAllBanks"
    const val getAllCompanyBanks = "/getAllCompanyBanks/{uniqueCompanyId}"
    const val addBank = "/addBank"
    const val addBanks = "/addBanks/{uniqueCompanyId}"
    const val smartAddCompanyBanks = "/smartAddCompanyBanks/{uniqueCompanyId}"
    const val updateBank = "/updateBank"
    const val deleteCompanyBank = "/deleteCompanyBank/{uniqueBankAccountId}/{uniqueCompanyId}"
    const val deleteBank = "/deleteBank/{uniqueBankAccountId}/{uniqueCompanyId}"
    const val deleteAllCompanyBanks = "/deleteAllCompanyBank/{uniqueCompanyId}"

    //Backup Routes
    const val fullBackUp = "/fullBackup"



    //Cash in Routes
    const val getAllCashIns = "/getAllCashIns"
    const val getAllCompanyCashIns = "/getAllCompanyCashIns/{uniqueCompanyId}"
    const val addCashIn = "/addCashIn"
    const val addCashIns = "/addCashIns/{uniqueCompanyId}"
    const val smartAddCompanyCashIns = "/smartAddCompanyCashIns/{uniqueCompanyId}"
    const val updateCashIn = "/updateCashIn"
    const val deleteCompanyCashIn = "/deleteCompanyCashIn/{uniqueCashInId}/{uniqueCompanyId}"
    const val deleteCashIn = "/deleteCashIn/{uniqueCashInId}/{uniqueCompanyId}"
    const val deleteAllCompanyCashIns = "/deleteAllCompanyCashIn/{uniqueCompanyId}"



    //Receipt Routes
    const val getAllReceipts = "/getAllReceipts"
    const val getAllCompanyReceipts = "/getAllCompanyReceipts/{uniqueCompanyId}"
    const val addReceipt = "/addReceipt"
    const val addReceipts = "/addReceipts/{uniqueCompanyId}"
    const val smartAddCompanyReceipts = "/smartAddCompanyReceipts/{uniqueCompanyId}"
    const val updateReceipt = "/updateReceipt"
    const val deleteCompanyReceipt = "/deleteCompanyReceipt/{uniqueReceiptId}/{uniqueCompanyId}"
    const val deleteReceipt = "/deleteReceipt/{uniqueReceiptId}/{uniqueCompanyId}"
    const val deleteAllCompanyReceipts = "/deleteAllCompanyReceipt/{uniqueCompanyId}"


    //Debt Routes
    const val getAllDebts = "/getAllDebts"
    const val getAllCompanyDebts = "/getAllCompanyDebts/{uniqueCompanyId}"
    const val addDebt = "/addDebt"
    const val addDebts = "/addDebts/{uniqueCompanyId}"
    const val smartAddCompanyDebts = "/smartAddCompanyDebts/{uniqueCompanyId}"
    const val updateDebt = "/updateDebt"
    const val deleteCompanyDebt = "/deleteCompanyDebt/{uniqueDebtId}/{uniqueCompanyId}"
    const val deleteDebt = "/deleteDebt/{uniqueDebtId}/{uniqueCompanyId}"
    const val deleteAllCompanyDebts = "/deleteAllCompanyDebt/{uniqueCompanyId}"


    //DebtRepayment Routes
    const val getAllDebtRepayments = "/getAllDebtRepayments"
    const val getAllCompanyDebtRepayments = "/getAllCompanyDebtRepayments/{uniqueCompanyId}"
    const val addDebtRepayment = "/addDebtRepayment"
    const val addDebtRepayments = "/addDebtRepayments/{uniqueCompanyId}"
    const val smartAddCompanyDebtRepayments = "/smartAddCompanyDebtRepayments/{uniqueCompanyId}"
    const val updateDebtRepayment = "/updateDebtRepayment"
    const val deleteCompanyDebtRepayment = "/deleteCompanyDebtRepayment/{uniqueDebtRepaymentId}/{uniqueCompanyId}"
    const val deleteDebtRepayment = "/deleteDebtRepayment/{uniqueDebtRepaymentId}/{uniqueCompanyId}"
    const val deleteAllCompanyDebtRepayments = "/deleteAllCompanyDebtRepayment/{uniqueCompanyId}"


    //Inventory Routes
    const val getAllInventories = "/getAllInventories"
    const val getAllCompanyInventories = "/getAllCompanyInventories/{uniqueCompanyId}"
    const val addInventory = "/addInventory"
    const val addInventories = "/addInventories/{uniqueCompanyId}"
    const val smartAddCompanyInventories = "/smartAddCompanyInventories/{uniqueCompanyId}"
    const val updateInventory = "/updateInventory"
    const val deleteCompanyInventory = "/deleteCompanyInventory/{uniqueInventoryId}/{uniqueCompanyId}"
    const val deleteInventory = "/deleteInventory/{uniqueInventoryId}/{uniqueCompanyId}"
    const val deleteAllCompanyInventories = "/deleteAllCompanyInventory/{uniqueCompanyId}"


    //Expense Routes
    const val getAllExpenses = "/getAllExpenses"
    const val getAllCompanyExpenses = "/getAllCompanyExpenses/{uniqueCompanyId}"
    const val addExpense = "/addExpense"
    const val addExpenses = "/addExpenses/{uniqueCompanyId}"
    const val smartAddCompanyExpenses = "/smartAddCompanyExpenses/{uniqueCompanyId}"
    const val updateExpense = "/updateExpense"
    const val deleteCompanyExpense = "/deleteCompanyExpense/{uniqueExpenseId}/{uniqueCompanyId}"
    const val deleteExpense = "/deleteExpense/{uniqueExpenseId}/{uniqueCompanyId}"
    const val deleteAllCompanyExpenses = "/deleteAllCompanyExpense/{uniqueCompanyId}"



    //Withdrawal Routes
    const val getAllWithdrawals = "/getAllWithdrawals"
    const val getAllCompanyWithdrawals = "/getAllCompanyWithdrawals/{uniqueCompanyId}"
    const val addWithdrawal = "/addWithdrawal"
    const val addWithdrawals = "/addWithdrawals/{uniqueCompanyId}"
    const val smartAddCompanyWithdrawals = "/smartAddCompanyWithdrawals/{uniqueCompanyId}"
    const val updateWithdrawal = "/updateWithdrawal"
    const val deleteCompanyWithdrawal = "/deleteCompanyWithdrawal/{uniqueWithdrawalId}/{uniqueCompanyId}"
    const val deleteWithdrawal = "/deleteWithdrawal/{uniqueWithdrawalId}/{uniqueCompanyId}"
    const val deleteAllCompanyWithdrawals = "/deleteAllCompanyWithdrawal/{uniqueCompanyId}"



    //Savings Routes
    const val getAllSavings = "/getAllSavings"
    const val getAllCompanySavings = "/getAllCompanySavings/{uniqueCompanyId}"
    const val addSavings = "/addSavings"
    const val addListOfSavings = "/addListOfSavings/{uniqueCompanyId}"
    const val smartAddCompanySavings = "/smartAddCompanySavings/{uniqueCompanyId}"
    const val updateSavings = "/updateSavings/{uniqueSavingsId}"
    const val deleteCompanySavings = "/deleteCompanySavings/{uniqueSavingsId}/{uniqueCompanyId}"
    const val deleteSavings = "/deleteSavings/{uniqueSavingsId}/{uniqueCompanyId}"
    const val deleteAllCompanySavings = "/deleteAllCompanySavings/{uniqueCompanyId}"


    //InventoryStock Routes
    const val getAllInventoryStocks = "/getAllInventoryStocks"
    const val getAllCompanyInventoryStocks = "/getAllCompanyInventoryStocks/{uniqueCompanyId}"
    const val addInventoryStock = "/addInventoryStock"
    const val addInventoryStocks = "/addInventoryStocks/{uniqueCompanyId}"
    const val smartAddCompanyInventoryStocks = "/smartAddCompanyInventoryStocks/{uniqueCompanyId}"
    const val updateInventoryStock = "/updateInventoryStock"
    const val deleteCompanyInventoryStock = "/deleteCompanyInventoryStock/{uniqueInventoryStockId}/{uniqueCompanyId}"
    const val deleteInventoryStock = "/deleteInventoryStock/{uniqueInventoryStockId}/{uniqueCompanyId}"
    const val deleteAllCompanyInventoryStocks = "/deleteAllCompanyInventoryStock/{uniqueCompanyId}"



    //InventoryItem Routes
    const val getAllInventoryItems = "/getAllInventoryItems"
    const val getAllCompanyInventoryItems = "/getAllCompanyInventoryItems/{uniqueCompanyId}"
    const val addInventoryItem = "/addInventoryItem"
    const val addInventoryItems = "/addInventoryItems/{uniqueCompanyId}"
    const val smartAddCompanyInventoryItems = "/smartAddCompanyInventoryItems/{uniqueCompanyId}"
    const val updateInventoryItem = "/updateInventoryItem"
    const val deleteCompanyInventoryItem = "/deleteCompanyInventoryItem/{uniqueInventoryId}/{uniqueCompanyId}"
    const val deleteInventoryItem = "/deleteInventoryItem/{uniqueInventoryId}/{uniqueCompanyId}"
    const val deleteAllCompanyInventoryItems = "/deleteAllCompanyInventoryItem/{uniqueCompanyId}"

    //Revenue Routes
    const val getAllRevenues = "/getAllRevenues"
    const val getAllCompanyRevenues = "/getAllCompanyRevenues/{uniqueCompanyId}"
    const val addRevenue = "/addRevenue"
    const val addRevenues = "/addRevenues/{uniqueCompanyId}"
    const val smartAddCompanyRevenues = "/smartAddCompanyRevenues/{uniqueCompanyId}"
    const val updateRevenue = "/updateRevenue"
    const val deleteCompanyRevenue = "/deleteCompanyRevenue/{uniqueRevenueId}/{uniqueCompanyId}"
    const val deleteRevenue = "/deleteRevenue/{uniqueRevenueId}/{uniqueCompanyId}"
    const val deleteAllCompanyRevenues = "/deleteAllCompanyRevenue/{uniqueCompanyId}"


    //Stock Routes
    const val getAllStocks = "/getAllStocks"
    const val getAllCompanyStocks = "/getAllCompanyStocks/{uniqueCompanyId}"
    const val addStock = "/addStock"
    const val addStocks = "/addStocks/{uniqueCompanyId}"
    const val smartAddCompanyStocks = "/smartAddCompanyStocks/{uniqueCompanyId}"
    const val updateStock = "/updateStock"
    const val deleteCompanyStock = "/deleteCompanyStock/{uniqueStockId}/{uniqueCompanyId}"
    const val deleteStock = "/deleteStock/{uniqueStockId}/{uniqueCompanyId}"
    const val deleteAllCompanyStocks = "/deleteAllCompanyStock/{uniqueCompanyId}"


    //Personnel Routes
    const val getAllPersonnel = "/getAllPersonnel"
    const val getAllCompanyPersonnel = "/getAllCompanyPersonnel/{uniqueCompanyId}"
    const val addPersonnel = "/addPersonnel"
    const val addListOfPersonnel = "/addListOfPersonnel/{uniqueCompanyId}"
    const val smartAddCompanyPersonnel = "/smartAddCompanyPersonnel/{uniqueCompanyId}"
    const val updatePersonnel = "/updatePersonnel"
    const val deleteCompanyPersonnel = "/deleteCompanyPersonnel/{uniquePersonnelId}/{uniqueCompanyId}"
    const val deletePersonnel = "/deletePersonnel/{uniquePersonnelId}/{uniqueCompanyId}"
    const val deleteAllCompanyPersonnel = "/deleteAllCompanyPersonnel/{uniqueCompanyId}"




}