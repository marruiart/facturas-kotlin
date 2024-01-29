package com.example.facturas.utils

import android.content.res.AssetManager
import co.infinum.retromock.BodyFactory
import java.io.IOException
import java.io.InputStream

class ResourceBodyFactory: BodyFactory {

    @Throws(IOException::class)
    override fun create(input: String): InputStream {
        return App.context.assets.open(input)
    }

}