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
import com.example.myshopmanagerapp.core.Routes.addRevenues
import com.example.myshopmanagerapp.core.Routes.addStocks
import com.example.myshopmanagerapp.core.Routes.addSuppliers
import com.example.myshopmanagerapp.core.Routes.addWithdrawals
import com.example.myshopmanagerapp.core.Routes.changePassword
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
import com.example.myshopmanagerapp.core.Routes.getAllCompanyRevenues
import com.example.myshopmanagerapp.core.Routes.getAllCompanySavings
import com.example.myshopmanagerapp.core.Routes.getAllCompanyStocks
import com.example.myshopmanagerapp.core.Routes.getAllCompanySuppliers
import com.example.myshopmanagerapp.core.Routes.getAllCompanyWithdrawals
import com.example.myshopmanagerapp.core.Routes.getCompany
import com.example.myshopmanagerapp.core.Routes.login
import com.example.myshopmanagerapp.feature_app.data.remote.dto.bank.BankInfoDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.bank.ListOfBankResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.cash_in.CashInInfoDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.cash_in.ListOfCashInResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.company.CompanyInfoDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.company.CompanyResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.company.ListOfCompanyResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.customer.CustomerInfoDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.customer.ListOfCustomerResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.debt.DebtInfoDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.debt.ListOfDebtResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.debt_repayment.DebtRepaymentInfoDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.debt_repayment.ListOfDebtRepaymentResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.expense.ExpenseInfoDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.expense.ListOfExpenseResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.inventory.InventoryInfoDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.inventory.ListOfInventoryResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.inventory_stock.InventoryStockInfoDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.inventory_stock.ListOfInventoryStockResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.inventoy_item.InventoryItemInfoDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.inventoy_item.ListOfInventoryItemResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.personnel.ListOfPersonnelResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.personnel.PersonnelInfoDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.revenue.ListOfRevenueResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.revenue.RevenueInfoDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.savings.ListOfSavingsResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.savings.SavingsInfoDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.stock.ListOfStockResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.stock.StockInfoDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.supplier.ListOfSupplierResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.supplier.SupplierInfoDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.withdrawal.ListOfWithdrawalResponseDto
import com.example.myshopmanagerapp.feature_app.data.remote.dto.withdrawal.WithdrawalInfoDto
import com.example.myshopmanagerapp.feature_app.domain.model.AddCompanyResponse
import com.example.myshopmanagerapp.feature_app.domain.model.AddEntitiesResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ShopManagerDatabaseApi {

    @GET(login)
    suspend fun login(@Path("email") email: String, @Path("password") password: String): CompanyResponseDto?

    @GET(getAllCompanies)
    suspend fun fetchAllCompanies(): ListOfCompanyResponseDto

    @GET(getCompany)
    suspend fun getCompany(@Path("uniqueCompanyId") uniqueCompanyId: String): CompanyResponseDto?

    @PUT(changePassword)
    fun changePassword(@Path("uniqueCompanyId") uniqueCompanyId: String, @Path("currentPassword") currentPassword: String, @Path("updatedPassword") updatedPassword: String): Call<CompanyResponseDto>?

    @POST(changePassword)
    suspend fun changePassword1(@Path("uniqueCompanyId") uniqueCompanyId: String, @Path("currentPassword") currentPassword: String, @Path("updatedPassword") updatedPassword: String): Call<CompanyResponseDto>?

    @DELETE(deleteCompany)
    suspend fun deleteCompany(@Path("uniqueCompanyId") uniqueCompanyId: String): Call<CompanyResponseDto>?

    @POST(addCompany)
    fun addCompany(@Body companyInfo: CompanyInfoDto): Call<AddCompanyResponse>?

    @POST(addCompany)
    fun addAccount(@Body companyInfo: CompanyInfoDto): AddCompanyResponse?

    // Customer route
    @POST(addCustomers)
    fun addCustomers(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body customers: List<CustomerInfoDto>): Call<AddEntitiesResponse>?
    @GET(getAllCompanyCustomers)
    suspend fun fetchAllCompanyCustomers(@Path("uniqueCompanyId") uniqueCompanyId: String): ListOfCustomerResponseDto?

    // Supplier route
    @POST(addSuppliers)
    fun addSuppliers(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body suppliers: List<SupplierInfoDto>): Call<AddEntitiesResponse>?
    @GET(getAllCompanySuppliers)
    suspend fun fetchAllCompanySuppliers(@Path("uniqueCompanyId") uniqueCompanyId: String): ListOfSupplierResponseDto?

    // Bank route
    @POST(addBanks)
    fun addBanks(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body banks: List<BankInfoDto>): Call<AddEntitiesResponse>?
    @GET(getAllCompanyBanks)
    suspend fun fetchAllCompanyBanks(@Path("uniqueCompanyId") uniqueCompanyId: String): ListOfBankResponseDto?

    // CashIn route
    @POST(addCashIns)
    fun addCashIns(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body cashIns: List<CashInInfoDto>): Call<AddEntitiesResponse>?
    @GET(getAllCompanyCashIns)
    suspend fun fetchAllCompanyCashIns(@Path("uniqueCompanyId") uniqueCompanyId: String): ListOfCashInResponseDto?

    // Debt route
    @POST(addDebts)
    fun addDebts(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body debts: List<DebtInfoDto>): Call<AddEntitiesResponse>?
    @GET(getAllCompanyDebts)
    suspend fun fetchAllCompanyDebts(@Path("uniqueCompanyId") uniqueCompanyId: String): ListOfDebtResponseDto?

    // Debt Repayment route
    @POST(addDebtRepayments)
    fun addDebtRepayments(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body debtRepayments: List<DebtRepaymentInfoDto>): Call<AddEntitiesResponse>?
    @GET(getAllCompanyDebtRepayments)
    suspend fun fetchAllCompanyDebtRepayments(@Path("uniqueCompanyId") uniqueCompanyId: String): ListOfDebtRepaymentResponseDto?

    // Expense route
    @POST(addExpenses)
    fun addExpenses(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body expenses: List<ExpenseInfoDto>): Call<AddEntitiesResponse>?
    @GET(getAllCompanyExpenses)
    suspend fun fetchAllCompanyExpenses(@Path("uniqueCompanyId") uniqueCompanyId: String): ListOfExpenseResponseDto?

    // Revenue route
    @POST(addRevenues)
    fun addRevenues(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body revenues: List<RevenueInfoDto>): Call<AddEntitiesResponse>?
    @GET(getAllCompanyRevenues)
    suspend fun fetchAllCompanyRevenues(@Path("uniqueCompanyId") uniqueCompanyId: String): ListOfRevenueResponseDto?

    // Inventory route
    @POST(addInventories)
    fun addInventories(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body inventories: List<InventoryInfoDto>): Call<AddEntitiesResponse>?
    @GET(getAllCompanyInventories)
    suspend fun fetchAllCompanyInventories(@Path("uniqueCompanyId") uniqueCompanyId: String): ListOfInventoryResponseDto?

    // InventoryItem route
    @POST(addInventoryItems)
    fun addInventoryItems(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body inventoryItems: List<InventoryItemInfoDto>): Call<AddEntitiesResponse>?
    @GET(getAllCompanyInventoryItems)
    suspend fun fetchAllCompanyInventoryItems(@Path("uniqueCompanyId") uniqueCompanyId: String): ListOfInventoryItemResponseDto?

    // InventoryStock route
    @POST(addInventoryStocks)
    fun addInventoryStocks(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body inventoryStocks: List<InventoryStockInfoDto>): Call<AddEntitiesResponse>?
    @GET(getAllCompanyInventoryStocks)
    suspend fun fetchAllCompanyInventoryStocks(@Path("uniqueCompanyId") uniqueCompanyId: String): ListOfInventoryStockResponseDto?

    // Stock route
    @POST(addStocks)
    fun addStocks(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body stocks: List<StockInfoDto>): Call<AddEntitiesResponse>?
    @GET(getAllCompanyStocks)
    suspend fun fetchAllCompanyStocks(@Path("uniqueCompanyId") uniqueCompanyId: String): ListOfStockResponseDto?

    // Savings route
    @POST(addListOfSavings)
    fun addListOfSavings(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body savings: List<SavingsInfoDto>): Call<AddEntitiesResponse>?
    @GET(getAllCompanySavings)
    suspend fun fetchAllCompanySavings(@Path("uniqueCompanyId") uniqueCompanyId: String): ListOfSavingsResponseDto?

    // Withdrawal route
    @POST(addWithdrawals)
    fun addWithdrawals(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body withdrawals: List<WithdrawalInfoDto>): Call<AddEntitiesResponse>?
    @GET(getAllCompanyWithdrawals)
    suspend fun fetchAllCompanyWithdrawals(@Path("uniqueCompanyId") uniqueCompanyId: String): ListOfWithdrawalResponseDto?

    // Personnel route
    @POST(addListOfPersonnel)
    fun addListOfPersonnel(@Path("uniqueCompanyId") uniqueCompanyId: String, @Body personnel: List<PersonnelInfoDto>): Call<AddEntitiesResponse>?
    @GET(getAllCompanyPersonnel)
    suspend fun fetchAllCompanyPersonnel(@Path("uniqueCompanyId") uniqueCompanyId: String): ListOfPersonnelResponseDto?





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