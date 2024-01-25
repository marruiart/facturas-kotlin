package com.example.facturas.data.local.converters

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateConverter {
    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return if (dateString == null) null else LocalDate.parse(dateString, formatter)
    }

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.format(formatter)
    }
}