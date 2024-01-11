package com.example.facturas.data.network.invoicesApi

import com.example.facturas.data.network.invoicesApi.models.InvoicesListResponse
import com.example.facturas.data.network.invoicesApi.models.mockResponse

class InvoicesApiService {

    fun getAllMockInvoices(): InvoicesListResponse {
        return mockResponse
    }
}