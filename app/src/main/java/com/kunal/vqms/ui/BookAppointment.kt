package com.kunal.vqms.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.kunal.vqms.R
import kotlinx.android.synthetic.main.activity_book_appointment.*
import java.util.*

class BookAppointment : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_appointment)
        place.setText(intent.getStringExtra("place"))
        setSupportActionBar(toolbar)
        toolbar.title=getString(R.string.book_slot)
        val calender = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calender.clear()
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select a Date")
            .setSelection(today)
            .setCalendarConstraints(CalendarConstraints.Builder().setStart(today).build())
            .build()
        datePicker.addOnPositiveButtonClickListener{
            date.text = datePicker.headerText
        }
        val timePicker = MaterialTimePicker.Builder()
                                        .setTitleText("Select a Time")
                                        .setTimeFormat(TimeFormat.CLOCK_12H)
                                        .build()
         timePicker.addOnPositiveButtonClickListener {
             time.text = "${timePicker.hour}:${timePicker.minute}"
         }
        date_picker.setOnClickListener {
                datePicker.show(supportFragmentManager,"DATE_PICKER")
        }
        time_picker.setOnClickListener {
            timePicker.show(supportFragmentManager,"TIME_PICKER")
        }

    }
}