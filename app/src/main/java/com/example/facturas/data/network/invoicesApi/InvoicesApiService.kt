package com.example.facturas.data.network.invoicesApi

import android.content.res.AssetManager
import co.infinum.retromock.NonEmptyBodyFactory
import co.infinum.retromock.Retromock
import co.infinum.retromock.meta.Mock
import co.infinum.retromock.meta.MockBehavior
import co.infinum.retromock.meta.MockRandom
import co.infinum.retromock.meta.MockResponse
import com.example.facturas.data.network.invoicesApi.models.InvoicesListResponse
import com.example.facturas.utils.AppEnvironment
import com.example.facturas.utils.BASE_URL
import com.example.facturas.utils.ResourceBodyFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface IInvoicesApi {
    suspend fun getAllInvoices(): Response<InvoicesListResponse>
}

interface IInvoicesProdApi : IInvoicesApi {
    @GET("facturas")
    override suspend fun getAllInvoices(): Response<InvoicesListResponse>
}

interface IInvoicesMockApi : IInvoicesApi {
    @Mock
    @MockRandom
    @MockResponse(body = "mock_empty_list.json")
    @MockResponse(body = "mock_invoices_list.json")
    @MockResponse(body = "mock_invoices_paid_list.json")
    @MockBehavior(durationDeviation = 1000, durationMillis = 1000)
    @GET("/")
    override suspend fun getAllInvoices(): Response<InvoicesListResponse>
}

/**
 * This class is responsible for interacting with REST HTTP URL
 */
class InvoicesApiService(
    private val assetManager: AssetManager,
    private val environment: String = AppEnvironment.PROD_ENVIRONMENT
) {
    val api: IInvoicesApi

    init {
        // Instantiate retrofit
        val retrofit = getRetrofitInstance()
        api = if (environment == AppEnvironment.MOCK_ENVIRONMENT) {
            // Instantiate retromock
            val retromock = getRetromockInstance(assetManager)
            retromock.create(IInvoicesMockApi::class.java)
        } else {
            retrofit.create(IInvoicesProdApi::class.java)
        }
    }
}

private fun getRetrofitInstance(): Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

private fun getRetromockInstance(assetManager: AssetManager): Retromock = Retromock.Builder()
    .retrofit(getRetrofitInstance())
    .defaultBodyFactory(NonEmptyBodyFactory(ResourceBodyFactory(assetManager)))
    .build()