package com.kunal.vqms.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import com.google.android.material.datepicker.MaterialDatePicker
import com.kunal.vqms.R
import kotlinx.android.synthetic.main.activity_book_appointment.*
import java.util.*

class BookAppointment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_appointment)
        place.setText(intent.getStringExtra("place"))
        setSupportActionBar(toolbar)
        toolbar.title=getString(R.string.book_slot)
        val calender = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calender.clear()
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        date_picker.setOnClickListener {
            MaterialDatePicker.Builder.datePicker()
                                .setTitleText("Select a Date")
                                .setSelection(today)
                                .build()
                                .show(supportFragmentManager,"DATE_PICKER")
        }
    }
}