package com.amir.bagshop.util

import android.annotation.SuppressLint
import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import java.text.SimpleDateFormat
import java.util.Calendar

val coroutineExceptionHandler = CoroutineExceptionHandler { _, Throwable ->


    Log.v("exception", "error is $Throwable")


}

fun formatPrice(price: String): String {
    return try {
        val number = price.toLong()
        "%,d".format(number).replace(",", "٬")
    } catch (e: NumberFormatException) {
        price // اگه ورودی عدد نبود، همون رو برمیگردونه
    }
}


@SuppressLint("SimpleDateFormat")
fun styleTime(timeInMillies: Long): String {

    val formatter = SimpleDateFormat("yyyy/MM/dd hh:mm")

    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timeInMillies

    return formatter.format(calendar.time)
}