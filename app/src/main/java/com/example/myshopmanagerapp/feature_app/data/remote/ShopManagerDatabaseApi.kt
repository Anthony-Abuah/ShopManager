package com.example.myshopmanagerapp.feature_app.data.remote



import com.example.myshopmanagerapp.core.Routes.addBanks
import com.example.myshopmanagerapp.core.Routes.addCashIns
import com.example.myshopmanagerapp.core.Routes.addCompany
import com.example.myshopmanagerapp.core.Routes.addCustomers
import com.example.myshopmanagerapp.core.Routes.addDebtRepayments
import com.example.myshopmanagerapp.core.Routes.addDebts
import com.example.myshopmanagerapp.core.Routes.addExpenses
import com.example.myshopmanagerapp.core.Routes.addInventories
import com.example.myshopmanagerapp.core.Routes.addInventoryItems
import com.example.myshopmanagerapp.core.Routes.addInventoryStocks
import com.example.myshopmanagerapp.core.Routes.addListOfPersonnel
import com.example.myshopmanagerapp.core.Routes.addListOfSavings
import com.example.myshopmanagerapp.core.Routes.addReceipts
import com.example.myshopmanagerapp.core.Routes.addRevenues
import com.example.myshopmanagerapp.core.Routes.addStocks
import com.example.myshopmanagerapp.core.Routes.addSuppliers
import com.example.myshopmanagerapp.core.Routes.addWithdrawals
import com.example.myshopmanagerapp.core.Routes.changeCompanyName
import com.example.myshopmanagerapp.core.Routes.changeContact
import com.example.myshopmanagerapp.core.Routes.changeEmail
import com.example.myshopmanagerapp.core.Routes.changeLocation
import com.example.myshopmanagerapp.core.Routes.changeOtherInfo
import com.example.myshopmanagerapp.core.Routes.changePassword
import com.example.myshopmanagerapp.core.Routes.changeProductsAndServices
import com.example.myshopmanagerapp.core.Routes.createOnlineCompany
import com.example.myshopmanagerapp.core.Routes.deleteCompany
import com.example.myshopmanagerapp.core.Routes.getAllCompanies
import com.example.myshopmanagerapp.core.Routes.getAllCompanyBanks
import com.example.myshopmanagerapp.core.Routes.getAllCompanyCashIns
import com.example.myshopmanagerapp.core.Routes.getAllCompanyCustomers
import com.example.myshopmanagerapp.core.Routes.getAllCompanyDebtRepayments
import com.example.myshopmanagerapp.core.Routes.getAllCompanyDebts
import com.example.myshopmanagerapp.core.Routes.getAllCompanyExpenses
import com.example.myshopmanagerapp.core.Routes.getAllCompanyInventories
import com.example.myshopmanagerapp.core.Routes.getAllCompanyInventoryItems
import com.example.myshopmanagerapp.core.Routes.getAllCompanyInventoryStocks
import com.example.myshopmanagerapp.core.Routes.getAllCompanyPersonnel
import com.example.myshopmanagerapp.core.Routes.getAllCompanyReceipts
import com.example.myshopmanagerapp.core.Routes.getAllCompanyRevenues
import com.example.myshopmanagerapp.core.Routes.getAllCompanySavings
import com.example.myshopmanagerapp.core.Routes.getAllCompanyStocks
import com.example.myshopmanagerapp.core.Routes.getAllCompanySuppliers
import com.example.myshopmanagerapp.core.Routes.getAllCompanyWithdrawals
import com.example.myshopmanagerapp.core.Routes.getCompany
import com.example.myshopmanagerapp.core.Routes.login
import com.example.myshopmanagerapp.core.Routes.resetPassword
import com.example.myshopmanagerapp.core.Routes.smartAddCompanyBanks
import com.example.myshopmanagerapp.core.Routes.smartAddCompanyCashIns
import com.example.myshopmanagerapp.core.Routes.smartAddCompanyCustomers
import com.example.myshopmanagerapp.core.Routes.smartAddCompanyDebtRepayments
import com.example.myshopmanagerapp.core.Routes.smartAddCompanyDebts
import com.example.myshopmanagerapp.core.Routes.smartAddCompanyExpenses
import com.example.myshopmanagerapp.core.Routes.smartAddCompanyInventories
import com.example.myshopmanagerapp.core.Routes.smartAddCompanyInventoryItems
import com.example.myshopmanagerapp.core.Routes.smartAddCompanyInventoryStocks
import com.example.myshopmanagerapp.core.Routes.smartAddCompanyPersonnel
import com.example.myshopmanagerapp.core.Routes.smartAddCompanyReceipts
import com.example.myshopmanagerapp.core.Routes.smartAddCompanyRevenues
import com.example.myshopmanagerapp.core.Routes.smartAddCompanySavings
import com.example.myshopmanagerapp.core.Routes.smartAddCompanyStocks
import com.example.myshopmanagerapp.core.Routes.smartAddCompanySuppliers
import com.example.myshopmanagerapp.core.Routes.smartAddCompanyWithdrawals
import com.example.myshopmanagerapp.feature_app.data.remote.dto.bank.BankAccountInfoDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.bank.ListOfBankResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.bank.SmartBankAccount
import com.example.myshopmanagerapp.feature_app.data.remote.dto.cash_in.CashInInfoDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.cash_in.ListOfCashInResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.cash_in.SmartCashIns
import com.example.myshopmanagerapp.feature_app.data.remote.dto.company.CompanyInfoDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.company.CompanyResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.company.ListOfCompanyResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.customer.CustomerInfoDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.customer.ListOfCustomerResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.customer.SmartCustomers
import com.example.myshopmanagerapp.feature_app.data.remote.dto.debt.DebtInfoDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.debt.ListOfDebtResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.debt.SmartDebts
import com.example.myshopmanagerapp.feature_app.data.remote.dto.debt_repayment.DebtRepaymentInfoDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.debt_repayment.ListOfDebtRepaymentResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.debt_repayment.SmartDebtRepayments
import com.example.myshopmanagerapp.feature_app.data.remote.dto.expense.ExpenseInfoDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.expense.ListOfExpenseResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.expense.SmartExpenses
import com.example.myshopmanagerapp.feature_app.data.remote.dto.inventory.InventoryInfoDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.inventory.ListOfInventoryResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.inventory.SmartInventories
import com.example.myshopmanagerapp.feature_app.data.remote.dto.inventory_stock.InventoryStockInfoDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.inventory_stock.ListOfInventoryStockResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.inventory_stock.SmartInventoryStocks
import com.example.myshopmanagerapp.feature_app.data.remote.dto.inventoy_item.InventoryItemInfoDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.inventoy_item.ListOfInventoryItemResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.inventoy_item.SmartInventoryItems
import com.example.myshopmanagerapp.feature_app.data.remote.dto.personnel.ListOfPersonnelResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.personnel.PersonnelInfoDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.personnel.SmartPersonnel
import com.example.myshopmanagerapp.feature_app.data.remote.dto.receipt.ListOfReceiptResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.receipt.ReceiptInfoDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.receipt.SmartReceipts
import com.example.myshopmanagerapp.feature_app.data.remote.dto.revenue.ListOfRevenueResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.revenue.RevenueInfoDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.revenue.SmartRevenues
import com.example.myshopmanagerapp.feature_app.data.remote.dto.savings.ListOfSavingsResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.savings.SavingsInfoDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.savings.SmartSavings
import com.example.myshopmanagerapp.feature_app.data.remote.dto.stock.ListOfStockResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.stock.SmartStocks
import com.example.myshopmanagerapp.feature_app.data.remote.dto.stock.StockInfoDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.supplier.ListOfSupplierResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.supplier.SmartSuppliers
import com.example.myshopmanagerapp.feature_app.data.remote.dto.supplier.SupplierInfoDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.withdrawal.ListOfWithdrawalResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.withdrawal.SmartWithdrawals
import com.example.myshopmanagerapp.feature_app.data.remote.dto.withdrawal.WithdrawalInfoDto
import com.example.myshopmanagerapp.feature_app.domain.model.AddCompanyResponse
import com.example.myshopmanagerapp.feature_app.domain.model.AddEntitiesResponse
import retrofit2.Call
import retrofit2.http.*

interface ShopManagerDatabaseApi {

    @GET(login)
    suspend fun login(@Path("email") email: String, @Path("password") password: String): CompanyResponseDto?

    @GET(getAllCompanies)
    suspend fun fetchAllCompanies(): ListOfCompanyResponseDto

    @GET(getCompany)
    suspend fun getCompany(@Path("uniqueCompanyId") uniqueCompanyId: String): CompanyResponseDto?


    @PUT(changePassword)
    fun changePassword(@Path("uniqueCompanyId") uniqueCompanyId: String, @Path("currentPassword") currentPassword: String, @Path("updatedPassword") updatedPassword: String): Call<CompanyResponseDto>?

    @PUT(resetPassword)
    fun resetPassword(@Path("uniqueCompanyId") uniqueCompanyId: String, @Path("email") email: String, @Path("updatedPassword") updatedPassword: String, @Path("personnelPassword") personnelPassword: String): Call<CompanyResponseDto>?

    @PUT(changeEmail)
    fun changeEmail(@Path("uniqueCompanyId") uniqueCompanyId: String, @Path("currentPassword") currentPassword: String, @Path("email") email: String): Call<CompanyResponseDto>?

    @PUT(changeCompanyName)
    fun changeShopName(@Path("uniqueCompanyId") uniqueCompanyId: String, @Path("currentPassword") currentPassword: String, @Path("companyName") companyName: String): Call<CompanyResponseDto>?

    @PUT(changeContact)
    fun changeCompanyContact(@Path("uniqueCompanyId") uniqueCompanyId: String, @Path("currentPassword") currentPassword: String, @Path("companyContact") companyContact: String): Call<CompanyResponseDto>?

    @PUT(changeLocation)
    fun changeCompanyLocation(@Path("uniqueCompanyId") uniqueCompanyId: String, @Path("currentPassword") currentPassword: String, @Path("companyLocation") companyLocation: String): Call<CompanyResponseDto>?

    @PUT(changeProductsAndServices)
    fun changeCompanyProductsAndServices(@Path("uniqueCompanyId") uniqueCompanyId: String, @Path("currentPassword") currentPassword: String, @Path("companyProductsAndServices") companyProductsAndServices: String): Call<CompanyResponseDto>?

    @PUT(changeOtherInfo)
    fun changeCompanyOtherInfo(@Path("uniqueCompanyId") uniqueCompanyId: String, @Path("currentPassword") currentPassword: String, @Path("otherInfo") companyOtherInfo: String): Call<CompanyResponseDto>?

    @DELETE(deleteCompany)
    suspend fun deleteCompany(@Path("uniqueCompanyId") uniqueCompanyId: String): Call<CompanyResponseDto>?

    @POST(addCompany)
    fun addCompany(@Body companyInfo: CompanyInfoDto): Call<AddCompanyResponse>?

    @POST(createOnlineCompany)
    fun createOnlineCompany(@Body companyInfo: CompanyInfoDto): Call<AddCompanyResponse>?


    // Customer route
    @POST(addCustomers)
    fun addCustomers(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body customers: List<CustomerInfoDto>): Call<AddEntitiesResponse>?

    @POST(smartAddCompanyCustomers)
    fun smartBackUpCustomers(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body smartCustomers: SmartCustomers): Call<AddEntitiesResponse>?

    @GET(getAllCompanyCustomers)
    suspend fun fetchAllCompanyCustomers(@Path("uniqueCompanyId") uniqueCompanyId: String): ListOfCustomerResponseDto?

    // Supplier route
    @POST(addSuppliers)
    fun addSuppliers(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body suppliers: List<SupplierInfoDto>): Call<AddEntitiesResponse>?
    @GET(getAllCompanySuppliers)
    suspend fun fetchAllCompanySuppliers(@Path("uniqueCompanyId") uniqueCompanyId: String): ListOfSupplierResponseDto?

    @POST(smartAddCompanySuppliers)
    fun smartBackUpSuppliers(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body smartSuppliers: SmartSuppliers): Call<AddEntitiesResponse>?

    // Bank route
    @POST(addBanks)
    fun addBankAccounts(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body banks: List<BankAccountInfoDto>): Call<AddEntitiesResponse>?
    @GET(getAllCompanyBanks)
    suspend fun fetchAllCompanyBanks(@Path("uniqueCompanyId") uniqueCompanyId: String): ListOfBankResponseDto?

    @POST(smartAddCompanyBanks)
    fun smartBackUpBankAccounts(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body smartBankAccounts: SmartBankAccount): Call<AddEntitiesResponse>?

    // CashIn route
    @POST(addCashIns)
    fun addCashIns(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body cashIns: List<CashInInfoDto>): Call<AddEntitiesResponse>?

    @POST(smartAddCompanyCashIns)
    fun smartBackUpCashIns(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body smartCashIns: SmartCashIns): Call<AddEntitiesResponse>?

    @GET(getAllCompanyCashIns)
    suspend fun fetchAllCompanyCashIns(@Path("uniqueCompanyId") uniqueCompanyId: String): ListOfCashInResponseDto?

    // Receipt route
    @POST(addReceipts)
    fun addReceipts(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body debts: List<ReceiptInfoDto>): Call<AddEntitiesResponse>?
    @GET(getAllCompanyReceipts)
    suspend fun fetchAllCompanyReceipts(@Path("uniqueCompanyId") uniqueCompanyId: String): ListOfReceiptResponseDto?

    @POST(smartAddCompanyReceipts)
    fun smartBackUpReceipts(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body smartReceipts: SmartReceipts): Call<AddEntitiesResponse>?


    // Debt route
    @POST(addDebts)
    fun addDebts(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body debts: List<DebtInfoDto>): Call<AddEntitiesResponse>?
    @GET(getAllCompanyDebts)
    suspend fun fetchAllCompanyDebts(@Path("uniqueCompanyId") uniqueCompanyId: String): ListOfDebtResponseDto?


    @POST(smartAddCompanyDebts)
    fun smartBackUpDebts(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body smartDebts: SmartDebts): Call<AddEntitiesResponse>?


    // Debt Repayment route
    @POST(addDebtRepayments)
    fun addDebtRepayments(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body debtRepayments: List<DebtRepaymentInfoDto>): Call<AddEntitiesResponse>?
    @GET(getAllCompanyDebtRepayments)
    suspend fun fetchAllCompanyDebtRepayments(@Path("uniqueCompanyId") uniqueCompanyId: String): ListOfDebtRepaymentResponseDto?

    @POST(smartAddCompanyDebtRepayments)
    fun smartBackUpDebtRepayments(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body smartDebtRepayments: SmartDebtRepayments): Call<AddEntitiesResponse>?


    // Expense route
    @POST(addExpenses)
    fun addExpenses(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body expenses: List<ExpenseInfoDto>): Call<AddEntitiesResponse>?
    @GET(getAllCompanyExpenses)
    suspend fun fetchAllCompanyExpenses(@Path("uniqueCompanyId") uniqueCompanyId: String): ListOfExpenseResponseDto?

    @POST(smartAddCompanyExpenses)
    fun smartBackUpExpenses(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body smartExpenses: SmartExpenses): Call<AddEntitiesResponse>?

    // Revenue route
    @POST(addRevenues)
    fun addRevenues(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body revenues: List<RevenueInfoDto>): Call<AddEntitiesResponse>?
    @GET(getAllCompanyRevenues)
    suspend fun fetchAllCompanyRevenues(@Path("uniqueCompanyId") uniqueCompanyId: String): ListOfRevenueResponseDto?

    @POST(smartAddCompanyRevenues)
    fun smartBackUpRevenues(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body smartRevenues: SmartRevenues): Call<AddEntitiesResponse>?

    // Inventory route
    @POST(addInventories)
    fun addInventories(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body inventories: List<InventoryInfoDto>): Call<AddEntitiesResponse>?
    @GET(getAllCompanyInventories)
    suspend fun fetchAllCompanyInventories(@Path("uniqueCompanyId") uniqueCompanyId: String): ListOfInventoryResponseDto?

    @POST(smartAddCompanyInventories)
    fun smartBackUpInventories(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body smartInventories: SmartInventories): Call<AddEntitiesResponse>?


    // InventoryItem route
    @POST(addInventoryItems)
    fun addInventoryItems(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body inventoryItems: List<InventoryItemInfoDto>): Call<AddEntitiesResponse>?
    @GET(getAllCompanyInventoryItems)
    suspend fun fetchAllCompanyInventoryItems(@Path("uniqueCompanyId") uniqueCompanyId: String): ListOfInventoryItemResponseDto?

    @POST(smartAddCompanyInventoryItems)
    fun smartBackUpInventoryItems(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body smartInventoryItems: SmartInventoryItems): Call<AddEntitiesResponse>?

    // Inventory Stock route
    @POST(addInventoryStocks)
    fun addInventoryStocks(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body inventoryStocks: List<InventoryStockInfoDto>): Call<AddEntitiesResponse>?
    @GET(getAllCompanyInventoryStocks)
    suspend fun fetchAllCompanyInventoryStocks(@Path("uniqueCompanyId") uniqueCompanyId: String): ListOfInventoryStockResponseDto?

    @POST(smartAddCompanyInventoryStocks)
    fun smartBackUpInventoryStocks(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body smartInventoryStocks: SmartInventoryStocks): Call<AddEntitiesResponse>?

    // Stock route
    @POST(addStocks)
    fun addStocks(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body stocks: List<StockInfoDto>): Call<AddEntitiesResponse>?
    @GET(getAllCompanyStocks)
    suspend fun fetchAllCompanyStocks(@Path("uniqueCompanyId") uniqueCompanyId: String): ListOfStockResponseDto?

    @POST(smartAddCompanyStocks)
    fun smartBackUpStocks(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body smartStocks: SmartStocks): Call<AddEntitiesResponse>?

    // Savings route
    @POST(addListOfSavings)
    fun addListOfSavings(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body savings: List<SavingsInfoDto>): Call<AddEntitiesResponse>?
    @GET(getAllCompanySavings)
    suspend fun fetchAllCompanySavings(@Path("uniqueCompanyId") uniqueCompanyId: String): ListOfSavingsResponseDto?

    @POST(smartAddCompanySavings)
    fun smartBackUpSavings(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body smartSavings: SmartSavings): Call<AddEntitiesResponse>?

    // Withdrawal route
    @POST(addWithdrawals)
    fun addWithdrawals(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body withdrawals: List<WithdrawalInfoDto>): Call<AddEntitiesResponse>?
    @GET(getAllCompanyWithdrawals)
    suspend fun fetchAllCompanyWithdrawals(@Path("uniqueCompanyId") uniqueCompanyId: String): ListOfWithdrawalResponseDto?

    @POST(smartAddCompanyWithdrawals)
    fun smartBackUpWithdrawals(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body smartWithdrawals: SmartWithdrawals): Call<AddEntitiesResponse>?

    // Personnel route
    @POST(addListOfPersonnel)
    fun addListOfPersonnel(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body personnel: List<PersonnelInfoDto>): Call<AddEntitiesResponse>?
    @GET(getAllCompanyPersonnel)
    suspend fun fetchAllCompanyPersonnel(@Path("uniqueCompanyId") uniqueCompanyId: String): ListOfPersonnelResponseDto?

    @POST(smartAddCompanyPersonnel)
    fun smartBackUpPersonnel(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body smartPersonnel: SmartPersonnel): Call<AddEntitiesResponse>?





/*
    @GET("/v1/teams/events")
    suspend fun getTeamEvents(
        @Header(Header_Key) key: String,
        @Header(Header_Host) host: String,
        @Query("team_id") team_id: Int,
        @Query("page") page: Int,
        @Query("course_events") course_events: String
    ): TeamEventsDto



    @GET("/v1/events/statistics")
    suspend fun getEventStats(
        @Header(Header_Key) key: String,
        @Header(Header_Host) host: String,
        @Query("event_id") event_id: Int,
    ): EventStatsDto


    @GET("/v1/events/h2h-events")
    suspend fun getHeadToHeadEvents(
        @Header(Header_Key) key: String,
        @Header(Header_Host) host: String,
        @Query("custom_event_id") custom_event_id: String,
    ): HeadToHeadEventsDto



    @GET("/v1/events/odds/all")
    suspend fun getEventOdds(
        @Header(Header_Key) key: String,
        @Header(Header_Host) host: String,
        @Query("event_id") eventId: Int,
        @Query("odds_format") oddsFormat: String,
        @Query("provider_id") providerId: Int,
    ): EventOddsDto
*/

}