package com.example.facturas.services

import com.example.facturas.services.models.Filter

class FilterService private constructor() {
    var filter: Filter = Filter()

    companion object {
        private var _INSTANCE: FilterService? = null

        fun getInstance(): FilterService {
            return _INSTANCE ?: FilterService()
        }
    }
}