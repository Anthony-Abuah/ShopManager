package com.example.myshopmanagerapp.core


object FormRelatedString{

    const val ShortNotes = "Short Notes/Description"
    const val UpdateShortNotes = "Update short notes/description"
    const val UpdateChanges = "Update Changes"
    const val ResetPassword = "Reset Password"
    const val ResetPasswordMessage = "Are you sure you want to reset this personnel's password\nPassword would be reset to 1234"
    const val PasswordResetUnsuccessful = "Password reset unsuccessful"
    const val PasswordResetSuccessful = "Password has been reset to 1234"
    const val SelectDate = "Select the date"
    const val DayOfTheWeek = "Day of the week"
    const val Date = "Date"
    const val Customer_Name = "Customer's name"
    const val Personnel_Name = "Personnel name"
    const val BankAccount = "Bank Account"
    const val UniqueWithdrawalId = "Unique Withdrawal Id"
    const val UniqueReceiptId = "Unique Receipt Id"
    const val UniqueCashInId = "Unique CashIn Id"
    const val Save = "Save"
    const val EnterValue = "Enter value"
    const val SearchPlaceholder = "Search..."
    const val AmountPlaceholder = "Eg: 500.00"
    const val AmountLabel = "Enter Amount"
    const val SelectBankAccount = "Select bank account"
    const val SizeNamePlaceholder = "Eg: Crate"
    const val EnterSizeName = "Enter size name"
    const val QuantityPerUnitPlaceholder = "Eg: 10"
    const val EnterQuantityOfUnits = "Enter number of units it contains"
    const val DeleteSizeCategory = "Remove Size Category"
    const val DeleteSizeCategoryText = "Are you sure you want to remove this size category"
    const val SizeNameInfo = "Display size name info"
    const val SizeQuantityInfo = "Display size quantity info"
    const val AddQuantityCategorization = "Add size categorization"
    const val ShortNotesPlaceholder = "Any other information you'd like to add"
    const val StartDate = "Start date"
    const val EndDate = "End date"
    const val AbsoluteRemoteBackUp = "Absolute remote backup"
    const val SmartRemoteBackUp = "Smart remote backup"
    const val AbsoluteSync = "Absolute data sync"
    const val SmartSync = "Smart data sync"
    const val ConfirmBackup = "Confirm backup"
    const val ConfirmRestore = "Confirm restore"
    const val ConfirmSync = "Confirm sync"
    const val LocalBackUp = "Local backup"
    const val RestoreData = "Restore data from local backup"
    const val ClickToSaveDatabaseToFile = "Click to save database to file"
    const val ClickToRestoreDatabaseToFile = "Click to restore database from file"
    const val ClickToBackupDataRemotely = "Click to backup data remotely"
    const val ClickToSyncData = "Click to sync remote data with local data"

    const val ShopName = "Shop Name"
    const val ItemsSold = "Products/Items Sold"
    const val NumberOfInventoryItems = "Number of inventory items"
    const val NumberOfPersonnel = "Number of personnel"
    const val NumberOfOwingCustomers = "Number of owing customers"
    const val NumberOfBankAccounts = "Number of bank accounts"
    const val TotalSavingsAmount = "Total savings amount"
    const val TotalWithdrawals = "Total withdrawals"
    const val TotalDebtAmount = "Total debt amount"
    const val TotalDebtRepaymentAmount = "Total debt repayment amount"
    const val TotalOutstandingDebtAmount = "Total outstanding debt amount"
    const val ShopValue = "Shop value"
    const val ShopValueInfo = "Shop value is based on the total value(cost price) of the last stock or inventory taken for an inventory item. " +
            "If no inventory or stock is taken for an item, the value will be zero"
    const val GHS = "GHS"


    const val LocalBackUpDialogMessage = "This will save a copy of your database to your local storage file so that you can restore it later." +
            "\n\nAre you sure want to back up database locally?"

    const val AbsoluteBackUpDialogMessage = "This will add all your current data to your remote data if it doesn't exist already. " +
            "This is recommended if it is the first time you're backing up your data remotely" +
            "\n\nAre you sure want to back up database remotely?"

    const val SmartBackUpDialogMessage = "This will preserve already existing remote data but only add changes to it if there have been any." +
            "New records, updated records and deleted records will be added appropriately. This is recommended if you already have backed up data remotely" +
            "\n\nAre you sure want to back up database remotely?"

    const val AbsoluteSyncDialogMessage = "This will fetch all your remote data and overwrite the current data with it. " +
            "This is recommended if it is the first time you're loading from your remote data source" +
            "\n\nAre you sure want to sync data?"

    const val SmartSyncDialogMessage = "This will fetch all your remote data and add the ones that do not exist in the current data to the current database. " +
            "This is recommended if it another personnel has already backed up data and you want to sync that data with the current data on this device" +
            "\n\nAre you sure want to sync data?"

    const val RestoreBackedUpDataDialogMessage = "If you have already backed up database to file, this action will load that data and replace it with the current data." +
            "\nNB: This process is irreversible" +
            "\n\nAre you sure want to back up database locally?"



    // Add Debt Questions
    const val SelectDebtDate = "Select the date"
    const val DebtDayOfTheWeek = "Day of the week"
    const val DebtCustomerPlaceholder = "Eg: Aunty Esi"
    const val DebtAmountPlaceholder = "Eg: 500.00"
    const val EnterDebtAmount = "Enter debt amount"
    const val SelectDebtCustomer = "Select customer"
    const val SaveDebt = "Save Debt"
    const val UniqueDebtId = "Unique Debt Id"
    const val DebtInformation = "Debt Information"
    const val DebtAmount = "Debt Amount"
    const val DebtCustomer = "Customer's name"
    const val DebtShortNotesPlaceholder = "Any other information you'd like to add concerning this debt"

    // Add Receipt Questions
    const val SelectReceiptDate = "Select the date"
    const val ReceiptDayOfWeek = "Day of the week"
    const val SelectReceiptCustomer = "Select Customer"
    const val ReceiptCustomerPlaceholder = "Eg: Aunty Esi"
    const val EnterUnitCostPrice = "Enter unit cost price"
    const val EnterTotalCostPrice = "Enter total amount"
    const val CostPricePlaceholder = "Eg: 500.00"
    const val AddItem = "Add Item"


    // Add Inventory Questions
    const val SelectInventoryDate = "Select the date"
    const val InventoryDayOfTheWeek = "Day of the week"
    const val InventoryItemPlaceholder = "Eg: Jay Kay exercise book"
    const val InventoryTotalCostPricePlaceholder = "Eg: GHS 5000.00"
    const val InventoryItemQuantityPlaceholder = "Eg: 50 packs, 34 units"
    const val InventoryReceiptPlaceholder = "Eg: 0028890"
    const val EnterReceiptId = "Enter receipt Id"
    const val EnterInventoryUnitCostPrice = "Enter unit cost price"
    const val EnterInventoryTotalCostPrice = "Enter total cost price"
    const val SelectInventoryItem = "Select inventory item"
    const val AddInventoryQuantity = "Click icon to add quantity"
    const val InventoryInformation = "Inventory Information"
    const val UniqueInventoryId = "Unique Inventory Id"
    const val TotalInventoryCost = "Total Inventory Cost"
    const val UnitCostPrice = "Unit cost price"
    const val InventoryItem = "Inventory Item"
    const val InventoryQuantity = "Inventory Quantity"
    const val InventoryReceiptId = "Receipt Id"
    const val InventoryOtherInfoPlaceholder = "Any other information you'd like to add concerning this inventory"

    // Add Expense Questions
    const val ExpenseTypePlaceholder = "Eg: Capital Expenses"
    const val ExpenseNamePlaceholder = "Eg: Transportation"
    const val ExpenseAmountPlaceholder = "Eg: 500.00"
    const val EnterExpenseName = "Enter expense name"
    const val EnterExpenseAmount = "Enter expense amount"
    const val SelectExpenseType = "Select expense type"
    const val SaveExpense = "Save Expense"
    const val ExpenseInformation = "Expense Information"
    const val UniqueExpenseId = "Unique Expense Id"
    const val ExpenseAmount = "Expense Amount"
    const val ExpenseType = "Expense type"
    const val ExpenseName = "Expense type"
    const val ExpenseShortNotesPlaceholder = "Any other information you'd like to add concerning this expense"
    const val ExpenseTypeNotAdded = "Expense type not added"
    const val ExpenseNameNotAdded = "Expense name not added"
    const val AddExpenseType = "Add Expense Type"
    const val AddExpenseName = "Add Expense Type"

    // Add DebtRepayment Questions
    const val SelectDebtRepaymentDate = "Select the date"
    const val DebtRepaymentDayOfTheWeek = "Day of the week"
    const val DebtRepaymentCustomerPlaceholder = "Eg: Aunty Esi"
    const val DebtRepaymentAmountPlaceholder = "Eg: 500.00"
    const val EnterDebtRepaymentAmount = "Enter debt repayment amount"
    const val SelectDebtRepaymentCustomer = "Select customer"
    const val SaveDebtRepayment = "Save Debt Repayment"
    const val DebtRepaymentInformation = "Debt Repayment Information"
    const val UniqueDebtRepaymentId = "Unique Debt Repayment Id"
    const val DebtRepaymentAmount = "Debt Repayment Amount"

    // Add Savings Questions
    const val SelectBankPersonnel = "Select bank personnel"
    const val BankPersonnelPlaceholder = "Eg: Anthony Abuah"
    const val AddBankPersonnel = "Add Bank Personnel"
    const val SavingsBankPlaceholder = "Eg: Lower Pra Rural Bank"
    const val SavingsAmountPlaceholder = "Eg: 500.00"
    const val EnterSavingsAmount = "Enter savings amount"
    const val SelectSavingsBank = "Select Bank Account"
    const val BankPersonnelNotAdded = "Bank personnel not added"
    const val SavingsInformation = "Savings Information"
    const val UniqueSavingsId = "Unique Savings Id"
    const val SavingsAmount = "Savings Amount"
    const val BankPersonnel = "Bank personnel"

    // Add Withdrawal Questions
    const val WithdrawalPersonnelPlaceholder = "Eg: Anthony Abuah"
    const val WithdrawalTransactionIdPlaceholder = "Eg: 004398532349"
    const val EnterTransactionId = "Enter transaction Id"
    const val SelectWithdrawalPersonnel = "Select personnel"
    const val AddWithdrawal = "Add Withdrawal"
    const val WithdrawalInformation = "Withdrawal Information"
    const val WithdrawalAmount = "Withdrawal Amount"
    const val TransactionId = "Transaction Id"
    const val WithdrawalShortNotesPlaceholder = "Any other information you'd like to add concerning this debt repayment"


    // Add Revenue Questions
    const val SelectRevenueDate = "Select the date"
    const val RevenueDayOfTheWeek = "Day of the week"
    const val NumberOfHours = "Number of hours (optional)"
    const val RevenueAmountPlaceholder = "Eg: 500.00"
    const val RevenueHoursPlaceholder = "Eg: 5 hours"
    const val EnterRevenueAmount = "Enter revenue amount"
    const val EnterShortDescription = "Short notes/description"
    const val RevenueTypeNotAdded = "Revenue Type not added"
    const val SelectRevenueType = "Select revenue type"
    const val RevenueType = "Revenue type"
    const val RevenueTypePlaceholder = "Eg: Sales"
    const val SaveRevenue = "Save Revenue"
    const val RevenueInformation = "Revenue Information"
    const val UniqueRevenueId = "Unique Revenue Id"
    const val RevenueAmount = "Revenue Amount"
    const val RevenueShortNotes = "Enter short notes"

    val ListOfHours = listOf(
        "1 hour",
        "2 hours",
        "3 hours",
        "4 hours",
        "5 hours",
        "6 hours",
        "7 hours",
        "8 hours",
        "9 hours",
        "10 hours",
        "11 hours",
        "12 hours",
        "13+ hours",
    )

    // Account information
    const val AccountInformation = "Account Information"

    // Add Customer Questions
    const val CustomerNamePlaceholder = "Eg: Aunty Esi Nyame"
    const val CustomerContactPlaceholder = "Eg: 0500912348"
    const val CustomerLocationPlaceholder = "Eg: Takoradi"
    const val CustomerShortNotesPlaceholder = "Any other thing that you want to write about the customer"
    const val EnterCustomerName = "Enter customer name"
    const val EnterCustomerContact = "Enter customer contact"
    const val EnterCustomerLocation = "Enter customer location"
    const val UpdateCustomerName = "Update customer name"
    const val UpdateCustomerContact = "Update customer contact"
    const val UpdateCustomerLocation = "Update customer location"
    const val CustomerShortNotes = "Short notes/description"
    const val SaveCustomer = "Save Customer"
    const val CustomerInformation = "Customer Information"
    const val UniqueCustomerId = "Unique Customer Id"
    const val CustomerName = "Customer Name"
    const val CustomerContact = "Customer Contact"
    const val CustomerLocation = "Customer Location"
    const val CustomerDebt = "Customer Debt"


    // Add Inventory Item Questions
    const val InventoryItemNamePlaceholder = "Eg: Jay-Kay Note 3 Exercise Book"
    const val InventoryItemManufacturerPlaceholder = "Eg: Jay-Kay"
    const val InventoryItemCategoryPlaceholder = "Eg: Book"
    const val InventoryItemSellingPricePlaceholder = "GHS 5.00"
    const val InventoryItemShortNotesPlaceholder = "Any other thing that you want to write about the item"
    const val EnterInventoryItemName = "Enter item name"
    const val EnterInventoryItemManufacturer = "Enter manufacturer's name"
    const val EnterInventoryItemSellingPrice = "Enter current selling price"
    const val SelectInventoryItemCategory = "Select item category"
    const val InventoryItemShortNotes = "Short notes/description"
    const val UniqueInventoryItemId = "Unique Inventory Item Id"
    const val InventoryItemInformation = "Inventory Item Information"
    const val InventoryItemName = "Item Name"
    const val InventoryItemManufacturer = "Item Manufacturer"
    const val InventoryItemCategory = "Item Category"
    const val InventoryItemCostPrice = "Cost Price"
    const val QuantityCategories = "Quantity Categories"
    const val InventoryItemSellingPrice = "Selling Price"
    const val AddManufacturer = "Add Manufacturer"
    const val ManufacturerPlaceholder = "Eg: Dedee Ltd"
    const val EnterManufacturerName = "Enter manufacturer name"
    const val ManufacturerNotAdded = "Manufacturer not added"
    const val AddCategory = "Add Category"
    const val CategoryPlaceholder = "Eg: Smart phones"
    const val EnterItemCategory = "Enter item category"
    const val CategoryNotAdded = "Category not added"
    const val InventoryStockInfo = "Number of inventories & stocks taken"
    const val InventoryItemQuantityInStock = "Item quantity in stock"


    // Add Personnel Questions
    const val PersonnelNamePlaceholder = "Eg: Anthony"
    const val PersonnelContactPlaceholder = "Eg: 0500912348"
    const val PersonnelRolePlaceholder = "Eg: Store keeper"
    const val PersonnelShortNotesPlaceholder = "Any other thing that you want to write about the personnel"
    const val EnterPersonnelFirstName = "Enter personnel's first name"
    const val EnterPersonnelUserName = "Enter personnel's username"
    const val EnterPersonnelLastName = "Enter personnel's last name"
    const val EnterPersonnelOtherName = "Enter personnel's other names"
    const val EnterPersonnelContact = "Enter personnel's contact"
    const val DoesPersonnelHaveAdminRights = "Has admin privileges?"
    const val EnterPersonnelRole = "Enter personnel role"
    const val UpdatePersonnelFirstName = "Update first name"
    const val UpdatePersonnelLastName = "Update last name"
    const val UpdatePersonnelOtherName = "Update other names"
    const val UpdatePersonnelContact = "Update personnel contact"
    const val UpdatePersonnelRole = "Update personnel role"
    const val PersonnelShortNotes = "Short notes/description"
    const val SavePersonnel = "Save Personnel"
    const val PersonnelInformation = "Personnel Information"
    const val UniquePersonnelId = "Unique Personnel Id"
    const val PersonnelUserName = "Username"
    const val PersonnelFirstName = "First Name"
    const val PersonnelLastName = "Last Name"
    const val PersonnelOtherNames = "Other Names"
    const val PersonnelContact = "Contact"
    const val PersonnelRole = "PersonnelRole"
    const val PersonnelHasAdminRights = "Has Admin Privileges"
    const val PersonnelIsActive = "Is Active"
    const val PersonnelDescription = "Description/Short Notes"


    // Add Supplier Questions
    const val SupplierNamePlaceholder = "Eg: Driver Kwesi Nyame"
    const val SupplierContactPlaceholder = "Eg: 0500912348"
    const val SupplierLocationPlaceholder = "Eg: Tarkwa"
    const val SupplierRolePlaceholder = "Eg: Teacher, Driver, etc"
    const val SupplierShortNotesPlaceholder = "Any other thing that you want to write about the supplier"
    const val EnterSupplierName = "Enter supplier name"
    const val EnterSupplierContact = "Enter supplier contact"
    const val EnterSupplierLocation = "Enter supplier location"
    const val UpdateSupplierName = "Update supplier name"
    const val UpdateSupplierContact = "Update supplier contact"
    const val UpdateSupplierLocation = "Update supplier location"
    const val AddSupplierRole = "Add Supplier Role"
    const val SupplierRoleNotAdded = "Supplier role not added"
    const val SupplierInformation = "Supplier Information"
    const val UniqueSupplierId = "Unique Supplier Id"
    const val SupplierName = "Supplier Name"
    const val SupplierContact = "Supplier Contact"
    const val SupplierLocation = "Supplier Location"
    const val SelectSupplierRole = "Select the role"


    // Add Bank Questions
    const val BankAccountNamePlaceholder = "Eg: Anthony Abuah"
    const val BankNamePlaceholder = "Eg: Lower Pra Rural Bank"
    const val BankContactPlaceholder = "Eg: 0500912348"
    const val BankLocationPlaceholder = "Eg: Takoradi"
    const val BankShortNotesPlaceholder = "Any other info you want to write about the bank/account"
    const val EnterBankAccountName = "Enter bank account name"
    const val EnterBankName = "Enter bank name"
    const val EnterBankContact = "Enter bank contact"
    const val EnterBankLocation = "Enter bank location"
    const val UpdateBankName = "Update bank name"
    const val UpdateBankAccountName = "Update bank account name"
    const val UpdateBankContact = "Update bank contact"
    const val UpdateBankLocation = "Update bank location"
    const val BankShortNotes = "Short notes/description"
    const val SaveBank = "Save Bank Account"
    const val BankAccountInformation = "Bank Account Information"
    const val UniqueBankAccountId = "Unique Bank Account Id"
    const val BankAccountName = "Bank Account Name"
    const val BankName = "Bank Name"
    const val BankContact = "Bank Contact"
    const val BankLocation = "Bank Location"
    const val BankAccountBalance = "Account Balance"


    // Add Stock Questions
    const val StockItemPlaceholder = "Eg: Jay Kay exercise book"
    const val SelectStockDate = "Select stock date"
    const val SelectStockItem = "Select inventory item"
    const val IsInventoryStock = "Is an inventory"
    const val StockInformation = "Stock Information"
    const val UniqueStockId = "Unique Stock Id"
    const val StockQuantity = "Stock Quantity"
    const val StockItemName = "Inventory Item"


    // Item Quantity Categorization
    const val QuantityInUnitPlaceholder = "Eg: 7"
    const val QuantityInSizeNamePlaceholder = "Eg: 7"
    const val UnitsPerPackPlaceholder = "Eg: 7"
    const val EnterNumberOfUnits = "Number of units"
    const val EnterNumberOf = "Number of remaining "
    const val EnterNumberOfCartons = "Number of cartons"
    const val EnterNumberOfBoxes = "Number of boxes"
    const val EnterNumberOfBigBoxes = "Number of big boxes"
    const val EnterNumberOfBiggerBoxes = "Number of bigger boxes"
    const val EnterNumberOfBiggestBoxes = "Number of biggest boxes"
    const val NumberOfUnitsPerPack = "Number of units per pack"
    const val NumberOfPacksPerCarton = "Number of packs per carton"
    const val NumberOfCartonsPerBox = "Number of cartons per box"
    const val NumberOfBoxesPerBigBox = "Number of boxes per big box"
    const val NumberOfBigBoxesPerBiggerBox = "Number of big box per bigger box"
    const val NumberOfBiggerBoxesPerBiggestBox = "Number of bigger boxes per biggest box"
    const val SaveQuantity = "Save"
    const val Cancel = "Cancel"



    // Add Company Questions
    const val CompanyNamePlaceholder = "Eg: Dedee groceries ltd"
    const val CompanyContactPlaceholder = "Eg: +233 123456789"
    const val CompanyOwnerPlaceholder = "Eg: Mr & Mrs Damien"
    const val CompanyEmailPlaceholder = "Eg: shop@gmail.com"
    const val CompanyPasswordPlaceholder = ""
    const val CompanyProductPlaceholder = "Eg: Groceries"
    const val CompanyLocationPlaceholder = "Eg: Takoradi, Ghana"
    const val CompanyShortNotesPlaceholder = "Any other thing that you want to write about the company"
    const val EnterCompanyName = "* Shop name"
    const val EnterCompanyContact = "Shop contact number"
    const val EnterCompanyLocation = "Shop's location"
    const val EnterCompanyOwner = "Enter shop's owner(s)"
    const val EnterPassword = "Enter password"
    const val EnterCurrentPassword = "Enter current password"
    const val EnterNewPassword = "Enter new password"
    const val ConfirmPassword = "Confirm password"
    const val EnterCompanyEmail = "Enter email"
    const val EnterCompanyProducts = "What products are sold?"
    const val CompanyShortNotes = "Short notes/description"
    const val SaveCompany = "Register Shop"

    // Currency
    const val SelectCurrency = "Select Currency"
    const val CurrencyPlaceholder = "Eg: GHS"

    val listOfCurrencies = listOf(
        "GHS Ghana Cedi Ghana",
        "AED UAE Dirham United Arab Emirates",
        "AFN Afghani Afghanistan",
        "ALL Lek Albania",
        "AMD Armeniam Dram Armenia",
        "ANG Netherlands Antillian Guilder Curaçao",
        "ANG Netherlands Antillian Guilder Sint Maarten",
        "AOA Kwanza Angola,",
        "ARS Argentine Peso Argentina",
        /*
        AUD Australian Dollar Australia
        AUD Australian Dollar Christmas Island
        AUD Australian Dollar Cocos (Keeling) Islands
        AUD Australian Dollar Kiribati
        AUD Australian Dollar Nauru
        AUD Australian Dollar Tuvalu
        AWG Aruban Florin Aruba
        AZN Azerbaijanian Manat Azerbaijan
        BAM Convertible Mark Bosnia and Herzegovina
        BBD Barbados Dollar Barbados
        BDT Taka Bangladesh
        BGN Bulgarian Lev Bulgaria
        BHD Bahraini Dinar Bahrain
        BIF Burundi Franc Burundi
        BMD Bermudian Dollar Bermuda
        BND Brunei Dollar Brunei Darussalam
        BOB Boliviano Bolivia (Plurinat.State)
        BRL Brazilian Real Brazil
        BSD Bahamian Dollar Bahamas
        BTN Ngultrum Bhutan
        BWP Pula Botswana
        BYN Belarusian Ruble Belarus
        BZD Belize Dollar Belize
        CAD Canadian Dollar Canada
        CDF Congolese Franc Congo, Dem. Rep. of the
        CHF Swiss Franc Liechtenstein
        CHF Swiss Franc Switzerland
        CLP Chilean Peso Chile
        CNY Yuan Renminbi China
        COP Colombian Peso Colombia
        CRC Costa Rican Colón Costa Rica
        CUP Cuban Peso Cuba
        CVE Cabo Verde Escudo Cabo Verde
        CZK Czech Koruna Czechia
        DJF Djibouti Franc Djibouti
        DKK Danish Krone Denmark
        DKK Danish Krone Faroe Islands
        DKK Danish Krone Greenland
        DOP Dominican Peso Dominican Republic
        DZD Algerian Dinar Algeria
        EGP Egyptian Pound Egypt
        ERN Nakfa Eritrea
        ETB Ethiopian Birr Ethiopia
        LIST OF CURRENCIES SORTED BY ISO CURRENCY CODE AND COUNTRY OR AREA NAME
        ANNEX F.II
        Currency ISO code Currency name Country name
        EUR Euro Andorra
        EUR Euro Austria
        EUR Euro Belgium
        EUR Euro Cyprus
        EUR Euro Estonia
        EUR Euro Finland
        EUR Euro France
        EUR Euro French Guiana
        EUR Euro French Southern Terr
        EUR Euro Germany
        EUR Euro Greece
        EUR Euro Guadeloupe
        EUR Euro Ireland
        EUR Euro Italy
        EUR Euro Latvia
        EUR Euro Lithuania
        EUR Euro Luxembourg
        EUR Euro Malta
        EUR Euro Martinique
        EUR Euro Mayotte
        EUR Euro Montenegro
        EUR Euro Netherlands
        EUR Euro Portugal
        EUR Euro Réunion
        EUR Euro Saint Barthélemy
        EUR Euro Saint-Martin
        EUR Euro St. Pierre and Miquelon
        EUR Euro Slovakia
        EUR Euro Slovenia
        EUR Euro Spain
        FJD Fiji Dollar Fiji, Republic of
        FKP Falkland Islands Pound Falkland Is.(Malvinas)
        GBP Pound Sterling Channel Islands
        GBP Pound Sterling Guernsey
        GBP Pound Sterling Isle of Man
        GBP Pound Sterling Jersey
        GBP Pound Sterling United Kingdom
        GEL Lari Georgia
        GHS Ghana Cedi Ghana
        GIP Gibraltar Pound Gibraltar
        GMD Dalasi Gambia
        GNF Guinea Franc Guinea
        GTQ Quetzal Guatemala
        GYD Guyana Dollar Guyana
        HKD Hong Kong Dollar China, Hong Kong SAR
        HNL Lempira Honduras
        HRK Croatian Kuna Croatia
        HTG Gourde Haiti
        HUF Forint Hungary
        IDR Rupiah Indonesia
        ILS New Israeli Sheqel Israel
        INR Indian Rupee India
        IQD Iraqi Dinar Iraq
        IRR Iranian Rial Iran (Islamic Rep. of)
        ISK Iceland Króna Iceland
        JMD Jamaican Dollar Jamaica
        JOD Jordanian Dinar Jordan
        Currency ISO code Currency name Country name
        JPY Yen Japan
        KES Kenyan Shilling Kenya
        KGS Som Kyrgyzstan
        KHR Riel Cambodia
        KMF Comoro Franc Comoros
        KPW North Korean Won Korea, Dem. People's Rep
        KRW Won Korea, Republic of
        KWD Kuwaiti Dinar Kuwait
        KYD Cayman Islands Dollar Cayman Islands
        KZT Tenge Kazakhstan
        LAK Kip Lao People's Dem. Rep.
        LBP Lebanese Pound Lebanon
        LKR Sri Lanka Rupee Sri Lanka
        LRD Liberian Dollar Liberia
        LSL Loti Lesotho
        LYD Libyan Dinar Libya
        MAD Moroccan Dirham Morocco
        MAD Moroccan Dirham Western Sahara
        MDL Moldovan Leu Moldova, Republic of
        MGA Malagasy Ariary Madagascar
        MKD Denar Macedonia, Fmr Yug Rp of
        MMK Kyat Myanmar
        MNT Tugrik Mongolia
        MOP Pataca China, Macao SAR
        MRO Ouguiya Mauritania
        MUR Mauritius Rupee Mauritius
        MVR Rufiyaa Maldives
        MWK Malawi Kwacha Malawi
        MXN Mexican Peso Mexico
        MYR Malaysian Ringgit Malaysia
        MZN Metical Mozambique
        NAD Namibian Dollar Namibia
        NGN Naira Nigeria
        NIO Córdoba Oro Nicaragua
        NOK Norwegian Krone Norway
        NPR Nepalese Rupee Nepal
        NZD New Zealand Dollar Cook Islands
        NZD New Zealand Dollar New Zealand
        NZD New Zealand Dollar Niue
        NZD New Zealand Dollar Pitcairn Islands
        NZD New Zealand Dollar Tokelau
        OMR Rial Omani Oman
        PAB Balboa Panama
        PEN Sol Peru
        PGK Kina Papua New Guinea
        PHP Philippine Peso Philippines
        PKR Pakistan Rupee Pakistan
        PLN Zloty Poland
        PYG Guaraní Paraguay
        QAR Qatari Rial Qatar
        RON Romanian Leu Romania
        RSD Serbian Dinar Serbia
        RUB Russian Ruble Russian Federation
        RWF Rwanda Franc Rwanda
        SAR Saudi Riyal Saudi Arabia
        SBD Solomon Islands Dollar Solomon Islands
        SCR Seychelles Rupee Seychelles
        Currency ISO code Currency name Country name
        SDG Sudanese Pound Sudan
        SEK Swedish Krona Sweden
        SGD Singapore Dollar Singapore
        SHP Saint Helena Pound Saint Helena
        SLL Leone Sierra Leone
        SOS Somali Shilling Somalia
        SRD Suriname Dollar Suriname
        SSP South Sudanese Pound South Sudan
        STD Dobra Sao Tome and Principe
        SYP Syrian Pound Syrian Arab Republic
        SZL Lilangeni Swaziland
        THB Baht Thailand
        TJS Somoni Tajikistan
        TMT Turkmenistan New Manat Turkmenistan
        TND Tunisian Dinar Tunisia
        TOP Pa'anga Tonga
        TRY Turkish Lira Turkey
        TTD Trinidad and Tobago Dollar Trinidad and Tobago
        TWD New Taiwan Dollar Taiwan Province of China
        TZS Tanzanian Shilling Tanzania, United Rep. of
        UAH Hryvnia Ukraine
        UGX Uganda Shilling Uganda
        USD US Dollar American Samoa
        USD US Dollar Bonaire/S.Eustatius/Saba
        USD US Dollar British Indian Ocean Ter
        USD US Dollar British Virgin Islands
        USD US Dollar Ecuador
        USD US Dollar El Salvador
        USD US Dollar Guam
        USD US Dollar Marshall Islands
        USD US Dollar Micronesia,Fed.States of
        USD US Dollar Northern Mariana Is.
        USD US Dollar Palau
        USD US Dollar Puerto Rico
        USD US Dollar Timor-Leste
        USD US Dollar Turks and Caicos Is.
        USD US Dollar United States of America
        USD US Dollar US Virgin Islands
        UYU Peso Uruguayo Uruguay
        UZS Uzbekistan Sum Uzbekistan
        VEF Bolívar Venezuela, Boliv Rep of
        VND Dong Viet Nam
        VUV Vatu Vanuatu
        WST Tala Samoa
        XAF CFA Franc (BEAC) Cameroon
        XAF CFA Franc (BEAC) Central African Republic
        XAF CFA Franc (BEAC) Chad
        XAF CFA Franc (BEAC) Congo
        XAF CFA Franc (BEAC) Equatorial Guinea
        XAF CFA Franc (BEAC) Gabon
        XCD East Caribbean Dollar Anguilla
        XCD East Caribbean Dollar Antigua and Barbuda
        XCD East Caribbean Dollar Dominica
        XCD East Caribbean Dollar Grenada
        XCD East Caribbean Dollar Montserrat
        XCD East Caribbean Dollar Saint Kitts and Nevis
        XCD East Caribbean Dollar Saint Lucia
        Currency ISO code Currency name Country name
        XCD East Caribbean Dollar Saint Vincent/Grenadines
        XOF CFA Franc (BCEAO) Benin
        XOF CFA Franc (BCEAO) Burkina Faso
        XOF CFA Franc (BCEAO) Côte d'Ivoire
        XOF CFA Franc (BCEAO) Guinea-Bissau
        XOF CFA Franc (BCEAO) Mali
        XOF CFA Franc (BCEAO) Niger
        XOF CFA Franc (BCEAO) Senegal
        XOF CFA Franc (BCEAO) Togo
        XPF CFP Franc French Polynesia
        XPF CFP Franc New Caledonia
        XPF CFP Franc Wallis and Futuna Is.
        YER Yemeni Rial Yemen
        ZAR Rand South Africa
        ZMW Zambian Kwacha Zambia
        ZWL Zimbabwe Dollar Zimbabwe*/
    )


}
