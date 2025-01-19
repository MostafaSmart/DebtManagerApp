package com.smarttec.moveisapptow.helper

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import java.util.Calendar

class DateTimeCore {

     fun showDateTimePicker(context: Context,onDateTimeSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()


        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->

                TimePickerDialog(
                    context,
                    { _, hourOfDay, minute ->

                        val selectedDateTime = "$year-${month + 1}-$dayOfMonth $hourOfDay:$minute"
                        onDateTimeSelected(selectedDateTime)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true // صيغة 24 ساعة
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

}