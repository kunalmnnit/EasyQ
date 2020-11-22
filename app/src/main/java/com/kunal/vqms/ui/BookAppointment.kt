package com.kunal.vqms.ui

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kunal.vqms.R
import kotlinx.android.synthetic.main.activity_book_appointment.*
import java.util.*
import kotlin.collections.HashMap

/* @author ArdhyaK on 30/10/2020 */
/* Activity for booking appointment */

class BookAppointment : AppCompatActivity(){
    private lateinit var shopID:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_appointment)
        shopID = intent.getStringExtra("place").toString()
        getShopName()
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
        datePicker.show(supportFragmentManager,"DATE_PICKER")
        var hour=0
        var min=0
        val timePickerDialog = TimePickerDialog(this,{ view, hourOfDay, minute ->
            hour = hourOfDay
            min = minute
            val calender = Calendar.getInstance()
            calender.set(0,0,0,hourOfDay,minute)
            time.setText(DateFormat.format("hh:mm aa",calender))
        },12,0,false)
        timePickerDialog.updateTime(hour,min)
        datePicker.addOnPositiveButtonClickListener{
            date.text = datePicker.headerText
            timePickerDialog.show()
        }

        date_picker.setOnClickListener {
                datePicker.show(supportFragmentManager,"DATE_PICKER")
        }

        time_picker.setOnClickListener {
            timePickerDialog.show()
        }
        book.setOnClickListener {
            bookSlot(datePicker.selection!!,hour,min)
        }
    }
    private fun getShopName(){
        val db = Firebase.firestore
        var shopName:String?=null
        db.collection("shops").document(shopID).get().addOnSuccessListener {shop ->
            shopName = shop.get("name") as String
            place.setText(shopName)
        }
    }

    private fun bookSlot(timestamp:Long,hour:Int,min:Int) {

        val db = Firebase.firestore
        val auth = Firebase.auth
        val uid = auth.currentUser!!.uid
        val booking:MutableMap<String,Any> = HashMap()
        booking["user"] = uid
        booking["date"] = timestamp
        booking["time"] = (hour*60*60+min*60)*1000
        val docId = db.collection("shops")
            .document(shopID)
            .collection("bookings")
            .document().id
        db.collection("shops")
            .document(shopID)
            .collection("bookings")
            .document(docId)
            .set(booking)
            .addOnSuccessListener {
                Toast.makeText(this,"Slot booked successfully",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(this,"Slot booking failed",Toast.LENGTH_SHORT).show()

            }
        db.collection("users").document(uid).update("bookings", FieldValue.arrayUnion(docId))
    }
}