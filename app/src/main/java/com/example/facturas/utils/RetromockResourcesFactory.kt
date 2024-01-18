package com.example.facturas.utils

import android.content.res.AssetManager
import co.infinum.retromock.BodyFactory
import java.io.IOException
import java.io.InputStream

class ResourceBodyFactory(
    private val assetManager: AssetManager
) : BodyFactory {

    @Throws(IOException::class)
    override fun create(input: String): InputStream {
        return assetManager.open(input)
    }

}