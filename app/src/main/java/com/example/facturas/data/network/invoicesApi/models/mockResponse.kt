package com.example.facturas.data.network.invoicesApi.models

import com.google.gson.Gson

val json = """
  {
  "numFacturas": 8,
  "facturas": [
    {
      "descEstado": "Pendiente de pago",
      "importeOrdenacion": 1.56,
      "fecha": "07/02/2019"
    },
    {
      "descEstado": "Pagada",
      "importeOrdenacion": 25.14,
      "fecha": "05/02/2019"
    },
    {
      "descEstado": "Pagada",
      "importeOrdenacion": 22.69,
      "fecha": "08/01/2019"
    },
    {
      "descEstado": "Pendiente de pago",
      "importeOrdenacion": 12.84,
      "fecha": "07/12/2018"
    },
    {
      "descEstado": "Pagada",
      "importeOrdenacion": 35.16,
      "fecha": "16/11/2018"
    },
    {
      "descEstado": "Pagada",
      "importeOrdenacion": 18.27,
      "fecha": "05/10/2018"
    },
    {
      "descEstado": "Pendiente de pago",
      "importeOrdenacion": 61.17,
      "fecha": "05/09/2018"
    },
    {
      "descEstado": "Pagada",
      "importeOrdenacion": 37.18,
      "fecha": "07/08/2018"
    },
    {
      "descEstado": "Pendiente de pago",
      "importeOrdenacion": 1.56,
      "fecha": "07/02/2019"
    },
    {
      "descEstado": "Pagada",
      "importeOrdenacion": 25.14,
      "fecha": "05/02/2019"
    },
    {
      "descEstado": "Pagada",
      "importeOrdenacion": 22.69,
      "fecha": "08/01/2019"
    },
    {
      "descEstado": "Pendiente de pago",
      "importeOrdenacion": 12.84,
      "fecha": "07/12/2018"
    },
    {
      "descEstado": "Pagada",
      "importeOrdenacion": 35.16,
      "fecha": "16/11/2018"
    },
    {
      "descEstado": "Pagada",
      "importeOrdenacion": 18.27,
      "fecha": "05/10/2018"
    },
    {
      "descEstado": "Cuota Fija",
      "importeOrdenacion": 70.56,
      "fecha": "05/09/2024"
    },
    {
      "descEstado": "Pagada",
      "importeOrdenacion": 100.08,
      "fecha": "07/08/2024"
    }
  ]
}
""".trimIndent()

val mockResponse: InvoicesListResponse = Gson().fromJson(json, InvoicesListResponse::class.java)