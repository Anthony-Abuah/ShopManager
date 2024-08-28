package com.example.myshopmanagerapp.core

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myshopmanagerapp.R
import com.example.myshopmanagerapp.feature_app.domain.model.ListNumberDropDownItem
import com.example.myshopmanagerapp.feature_app.domain.model.PeriodDropDownItem
import com.example.myshopmanagerapp.feature_app.domain.model.ProfileDropDownItem
import com.example.myshopmanagerapp.feature_app.domain.model.QuantityCategorization
import java.time.LocalDate


object Constants{

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    val CAMERAX_PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_MEDIA_IMAGES
    )

    const val THETABLE_TABLENAME = "theTable"
    const val THETABLE_ID_COLUMN = THETABLE_TABLENAME + "_id"
    const val TheTABLE_OTHER_COLUMN = THETABLE_TABLENAME + "_other"
    const val THEDATABASE_DATABASE_NAME = "thedatabase.whatever"
    const val THEDATABASE_DATABASE_BACKUP_SUFFIX = "-bkp"
    const val SQLITE_WALFILE_SUFFIX = "-wal"
    const val SQLITE_SHMFILE_SUFFIX = "-shm"

    const val One = "One"
    const val Two = "One"
    const val Three = "One"

    const val ShopAppDatabase = "shopAppDatabase"


    const val longDateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    const val shortDateFormat = "dd-MM-yyyy"

    // Entities
    const val Customer_Table = "CustomerTable"
    const val CashIn_Table = "CashInTable"
    const val Debt_Table = "DebtTable"
    const val Receipt_Table = "ReceiptTable"
    const val Revenue_Table = "RevenueTable"
    const val Withdrawal_Table = "WithdrawalTable"
    const val InventoryItem_Table = "InventoryItemTable"
    const val Inventory_Table = "InventoryTable"
    const val DebtRepayment_Table = "DebtRepaymentTable"
    const val BankAccount_Table = "BankAccountTable"
    const val Company_Table = "CompanyTable"
    const val Stock_Table = "StockTable"
    const val Expense_Table = "ExpenseTable"
    const val Supplier_Table = "SupplierTable"
    const val Personnel_Table = "PersonnelTable"
    const val Savings_Table = "SavingsTable"
    const val InventoryStock_Table = "InventoryStockTable"

    const val Loan = "Loan"
    const val Email = "Email"
    const val Password = "1234"
    const val NotAvailable = "N/A"
    const val UnknownItem = "Unknown Item"


    const val Yes = "Yes"
    const val No = "No"
    const val Unit = "Unit"
    const val ZERO = 0
    const val ONE = 1
    const val Cash = "Cash"
    const val emptyString = ""
    const val UnknownError = "Unknown Error!"
    const val Zero = "0"
    const val zero = "0.0"

    const val Edit = "Edit"
    const val Delete = "Delete"
    val editDelete = listOf(Edit, Delete)

    const val Expense_Types = "Expense Types"
    const val ChangesEntityMarkers = "Changes Markers"
    const val AdditionEntityMarkers = "Addition Entity Markers"
    const val UserPreferences = "User Preferences"
    const val StringValue = "String Value"
    const val SupplierRole = "Supplier Role"
    const val DoubleValue = "Double Value"
    const val IntValue = "Int Value"


    const val RegisterMessage = "registerMessage"
    const val IOException_HttpException = "IOExceptionOrHttpException"
    const val ExceptionOrErrorMessage = "ExceptionOrErrorMessage"
    const val ShopInfo = "shopInfo"
    const val IsRegistered = "isRegistered"
    const val ListOfShopLoginInfo = "listOfShopLoginInfo"
    const val IsLoggedIn = "isLoggedIn"
    const val Personnel_IsLoggedIn = "PersonnelIsLoggedIn"
    const val PersonnelEntityInfo = "PersonnelEntityInfo"
    const val Currency = "Currency"
    const val ManufacturerName = "manufacturerName"
    const val BankPersonnel = "bankPersonnel"
    const val PersonnelRoles = "personnelRoles"
    const val ExpenseNames = "expenseNames"
    const val ExpenseTypes = "expenseTypes"
    const val RevenueTypes = "revenueTypes"
    const val ItemCategory = "itemCategory"
    const val Quantity_Categorization = "Quantity Categorization"

    const val AllTime = "All Time"
    private const val ThisWeek = "This Week"
    private const val ThisMonth = "This Month"
    private const val LastMonth = "Last Month"
    private const val ThisYear = "This Year"
    private const val LastYear = "Last Year"
    const val SelectRange = "Select Range"


    private const val Search = "Search"
    private const val All = "All"
    private const val Top10 = "Top 10"
    private const val Top50 = "Top 50"
    private const val Top100 = "Top 100"
    private const val Top500 = "Top 500"
    private const val ListSize = "List Size"


    val defaultQuantityCategorizations = listOf(QuantityCategorization(Unit, ONE))

    val listOfChangePassword = listOf(
        ProfileDropDownItem(
            titleText = "Change password"
        )
    )

    private val dateNow = LocalDate.now()

    val listOfPeriods = listOf(
        PeriodDropDownItem(
            titleText = AllTime,
            isAllTime = true,
            firstDate = LocalDate.now().minusYears(10),
            lastDate = LocalDate.now().plusDays(1)
        ),
        PeriodDropDownItem(
            titleText = ThisWeek,
            isAllTime = false,
            firstDate = dateNow.minusDays(dateNow.dayOfWeek.value.toLong()),
            lastDate = LocalDate.now().plusDays(1)
        ),
        PeriodDropDownItem(
            titleText = ThisMonth,
            isAllTime = false,
            firstDate = LocalDate.of(dateNow.year, dateNow.monthValue , 1),
            lastDate = LocalDate.now().plusDays(1)
        ),
        PeriodDropDownItem(
            titleText = LastMonth,
            isAllTime = false,
            firstDate = LocalDate.of(dateNow.year, dateNow.monthValue-1 , 1),
            lastDate = LocalDate.of(dateNow.year, dateNow.monthValue , 1)
        ),
        PeriodDropDownItem(
            titleText = ThisYear,
            isAllTime = false,
            firstDate = LocalDate.of(dateNow.year, 1, 1),
            lastDate = LocalDate.now().plusDays(1)
        ),
        PeriodDropDownItem(
            titleText = LastYear,
            isAllTime = false,
            firstDate = LocalDate.of(dateNow.year-1, 1,1),
            lastDate = LocalDate.of(dateNow.year, 1,1)
        ),
        PeriodDropDownItem(
            titleText = SelectRange,
            isAllTime = false,
            firstDate = LocalDate.of(dateNow.year-1, 1,1),
            lastDate = LocalDate.of(dateNow.year, 1,1).minusDays(1)
        ),
    )
    val listOfListNumbers = listOf(
        ListNumberDropDownItem(
            titleText = Search,
            number = 1,
            icon = R.drawable.ic_search
        ),
        ListNumberDropDownItem(
            titleText = All,
            number = 0
        ),
        ListNumberDropDownItem(
            titleText = Top10,
            number = 10
        ),
        ListNumberDropDownItem(
            titleText = Top50,
            number = 50
        ),
        ListNumberDropDownItem(
            titleText = Top100,
            number = 100
        ),
        ListNumberDropDownItem(
            titleText = Top500,
            number = 500
        ),
        ListNumberDropDownItem(
            titleText = ListSize,
            number = 2,
            icon = R.drawable.ic_quantity
        )

    )

    val listOfDebtSortItems = listOf(
        ListNumberDropDownItem(
            titleText = "Sort ascending by date",
            number = 1
        ),
        ListNumberDropDownItem(
            titleText = "Sort descending by date",
            number = 2
        ),
        ListNumberDropDownItem(
            titleText = "Sort ascending by amount",
            number = 3
        ),
        ListNumberDropDownItem(
            titleText = "Sort descending by amount",
            number = 4
        ),
        ListNumberDropDownItem(
            titleText = "Get amounts greater than...",
            number = 5
        ),
        ListNumberDropDownItem(
            titleText = "Get amounts less than...",
            number = 6
        )
    )

    val listOfDebtRepaymentSortItems = listOf(
        ListNumberDropDownItem(
            titleText = "Sort ascending by date",
            number = 1
        ),
        ListNumberDropDownItem(
            titleText = "Sort descending by date",
            number = 2
        ),
        ListNumberDropDownItem(
            titleText = "Sort ascending by amount",
            number = 3
        ),
        ListNumberDropDownItem(
            titleText = "Sort descending by amount",
            number = 4
        ),
        ListNumberDropDownItem(
            titleText = "Get amounts greater than...",
            number = 5
        ),
        ListNumberDropDownItem(
            titleText = "Get amounts less than...",
            number = 6
        )
    )

    val listOfStockSortItems = listOf(
        ListNumberDropDownItem(
            titleText = "Sort ascending by date",
            number = 1
        ),
        ListNumberDropDownItem(
            titleText = "Sort descending by date",
            number = 2
        ),
        ListNumberDropDownItem(
            titleText = "Sort ascending by unit quantity",
            number = 3
        ),
        ListNumberDropDownItem(
            titleText = "Sort descending by unit quantity",
            number = 4
        ),
        ListNumberDropDownItem(
            titleText = "Get unit quantity greater than...",
            number = 5
        ),
        ListNumberDropDownItem(
            titleText = "Get unit quantity less than...",
            number = 6
        )
    )

    val listOfRevenueSortItems = listOf(
        ListNumberDropDownItem(
            titleText = "Sort ascending by date",
            number = 1
        ),
        ListNumberDropDownItem(
            titleText = "Sort descending by date",
            number = 2
        ),
        ListNumberDropDownItem(
            titleText = "Sort ascending by amount",
            number = 3
        ),
        ListNumberDropDownItem(
            titleText = "Sort descending by amount",
            number = 4
        ),
        ListNumberDropDownItem(
            titleText = "Get revenues greater than...",
            number = 5
        ),
        ListNumberDropDownItem(
            titleText = "Get revenues less than...",
            number = 6
        )
    )

    val listOfSavingsSortItems = listOf(
        ListNumberDropDownItem(
            titleText = "Sort ascending by date",
            number = 1
        ),
        ListNumberDropDownItem(
            titleText = "Sort descending by date",
            number = 2
        ),
        ListNumberDropDownItem(
            titleText = "Sort ascending by amount",
            number = 3
        ),
        ListNumberDropDownItem(
            titleText = "Sort descending by amount",
            number = 4
        ),
        ListNumberDropDownItem(
            titleText = "Get savings greater than...",
            number = 5
        ),
        ListNumberDropDownItem(
            titleText = "Get savings less than...",
            number = 6
        )
    )

    val listOfWithdrawalSortItems = listOf(
        ListNumberDropDownItem(
            titleText = "Sort ascending by date",
            number = 1
        ),
        ListNumberDropDownItem(
            titleText = "Sort descending by date",
            number = 2
        ),
        ListNumberDropDownItem(
            titleText = "Sort ascending by amount",
            number = 3
        ),
        ListNumberDropDownItem(
            titleText = "Sort descending by amount",
            number = 4
        ),
        ListNumberDropDownItem(
            titleText = "Get withdrawals greater than...",
            number = 5
        ),
        ListNumberDropDownItem(
            titleText = "Get withdrawals less than...",
            number = 6
        )
    )

    val listOfExpenseSortItems = listOf(
        ListNumberDropDownItem(
            titleText = "Sort ascending by date",
            number = 1
        ),
        ListNumberDropDownItem(
            titleText = "Sort descending by date",
            number = 2
        ),
        ListNumberDropDownItem(
            titleText = "Sort ascending by amount",
            number = 3
        ),
        ListNumberDropDownItem(
            titleText = "Sort descending by amount",
            number = 4
        ),
        ListNumberDropDownItem(
            titleText = "Sort ascending by expense name",
            number = 5
        ),
        ListNumberDropDownItem(
            titleText = "Sort descending by expense name",
            number = 6
        ),
        ListNumberDropDownItem(
            titleText = "Get expense greater than...",
            number = 7
        ),
        ListNumberDropDownItem(
            titleText = "Get expense less than...",
            number = 8
        )
    )

    val listOfCustomerSortItems = listOf(
        ListNumberDropDownItem(
            titleText = "Sort ascending by name",
            number = 1
        ),
        ListNumberDropDownItem(
            titleText = "Sort descending by name",
            number = 2
        ),
        ListNumberDropDownItem(
            titleText = "Sort ascending by debt",
            number = 3
        ),
        ListNumberDropDownItem(
            titleText = "Sort descending by debt",
            number = 4
        ),
        ListNumberDropDownItem(
            titleText = "Minimum debt of...",
            number = 5
        ),
        ListNumberDropDownItem(
            titleText = "Maximum debt of...",
            number = 6
        )
    )

    val listOfBankAccountSortItems = listOf(
        ListNumberDropDownItem(
            titleText = "Ascending by account name",
            number = 1
        ),
        ListNumberDropDownItem(
            titleText = "Descending by account name",
            number = 2
        ),
        ListNumberDropDownItem(
            titleText = "Ascending by account balance",
            number = 3
        ),
        ListNumberDropDownItem(
            titleText = "Descending by account balance",
            number = 4
        ),
        ListNumberDropDownItem(
            titleText = "Minimum balance of...",
            number = 5
        ),
        ListNumberDropDownItem(
            titleText = "Maximum balance of...",
            number = 6
        )
    )

    val listOfPersonnelSortItems = listOf(
        ListNumberDropDownItem(
            titleText = "Sort ascending by last name",
            number = 1
        ),
        ListNumberDropDownItem(
            titleText = "Sort descending by last name",
            number = 2
        ),
        ListNumberDropDownItem(
            titleText = "Sort ascending by first name",
            number = 3
        ),
        ListNumberDropDownItem(
            titleText = "Sort descending by first name",
            number = 4
        ),
    )

    val listOfSupplierSortItems = listOf(
        ListNumberDropDownItem(
            titleText = "Sort ascending by supplier name",
            number = 1
        ),
        ListNumberDropDownItem(
            titleText = "Sort descending by supplier name",
            number = 2
        ),
        ListNumberDropDownItem(
            titleText = "Sort ascending by supplier role",
            number = 1
        ),
        ListNumberDropDownItem(
            titleText = "Sort descending by supplier role",
            number = 2
        ),
    )

    val listOfInventoryItemsSortItems = listOf(
        ListNumberDropDownItem(
            titleText = "Sort ascending by name",
            number = 1
        ),
        ListNumberDropDownItem(
            titleText = "Sort descending by name",
            number = 2
        ),
        ListNumberDropDownItem(
            titleText = "Sort ascending by selling price",
            number = 3
        ),
        ListNumberDropDownItem(
            titleText = "Sort descending by selling price",
            number = 4
        ),
        ListNumberDropDownItem(
            titleText = "Sort ascending by cost price",
            number = 5
        ),
        ListNumberDropDownItem(
            titleText = "Sort descending by cost price",
            number = 6
        ),
        ListNumberDropDownItem(
            titleText = "Minimum cost price of...",
            number = 7
        ),
        ListNumberDropDownItem(
            titleText = "Maximum cost price of...",
            number = 8
        ),
        ListNumberDropDownItem(
            titleText = "Minimum selling price of...",
            number = 9
        ),
        ListNumberDropDownItem(
            titleText = "Maximum selling price of...",
            number = 10
        )
    )


}
