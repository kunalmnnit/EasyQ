package com.kunal.vqms.ui

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Header
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.JsonObject
import com.kunal.vqms.R
import kotlinx.android.synthetic.main.activity_book_appointment.*
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap

/* @author ArdhyaK on 30/10/2020 */
/* Activity for booking appointment */

class BookAppointment : AppCompatActivity(){
    private lateinit var shopID:String
    private lateinit var mRequestQueue:RequestQueue
    private lateinit var auth:FirebaseAuth
    private val url = "https://fcm.googleapis.com/fcm/send"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_appointment)
        mRequestQueue = Volley.newRequestQueue(this)
        auth=Firebase.auth
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
            sendNotification()
        }
    }
    private fun sendNotification() {

        try {
            val mainObj = JSONObject()
            val uid = auth.currentUser!!.uid
            mainObj.put("to","/topics/$uid")
            val notification = JSONObject()
            notification.put("title","Booking confirmed")
            notification.put("body","Your slot has been booked")
            mainObj.put("notification",notification)
            val request = object: JsonObjectRequest(Method.POST,url,mainObj,Response.Listener{response:JSONObject ->
                Log.d("notification",response.toString())
            },Response.ErrorListener {error ->
                    Log.d("notification",error.toString())
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers:MutableMap<String, String> = HashMap()
                    headers.put("content-type","application/json")
                    headers.put("authorization","key=AAAA_TqgOEs:APA91bGYkjSuPrKo3XXkMHV39LSo9JqIRuc61vkTua2vbjRFZcU-F8zEhVbg7lrdk9HFQXKULHbDl4mda0oUee_rcemiDPvMIQtSq6ze2BCRMR5bXL89q9McVgAOEWAYFLVRIHI2jEtf")
                    return headers
                }
            }
            mRequestQueue.add(request)
        } catch (e:JSONException) {
            e.printStackTrace()
        }
    }

    private fun getShopName(){
        val db = Firebase.firestore
        var shopName:String?=null
        db.collection("shops").document(shopID).get().addOnSuccessListener {shop ->
            shopName = shop.get("name") as String
            place.text = shopName
        }
    }

    private fun bookSlot(timestamp:Long,hour:Int,min:Int) {

        val db = Firebase.firestore
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