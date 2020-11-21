package com.kunal.vqms.ui

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kunal.vqms.R
import kotlinx.android.synthetic.main.activity_user_profile.*


class UserProfileActivity : AppCompatActivity() {
    private val CAMERA_REQUEST:Int = 1888
    private val MY_CAMERA_PERMISSION_CODE = 100
    private var filePath:Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        pick_file.setOnClickListener {
            requestPermission()
        }
        update.setOnClickListener {
            saveProfile()
        }
    }
    private fun requestPermission() {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                if(checkSelfPermission(Manifest.permission.CAMERA)==PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED) {
                    requestPermissions(arrayOf(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE),CAMERA_REQUEST)
                } else {
                    openCamera()
                }
            } else {
                openCamera()
            }
    }
    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE,"picture")
        values.put(MediaStore.Images.Media.DESCRIPTION,"From Camera")
        filePath = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,filePath)
        startActivityForResult(cameraIntent,MY_CAMERA_PERMISSION_CODE)
    }
    private fun saveProfile() {
        progress_bar.show()
        val storage = Firebase.storage
        val auth = Firebase.auth
        val db = Firebase.firestore
        val uid = auth.currentUser!!.uid
        val Name = name.text.toString()
        val user:MutableMap<String,Any> = HashMap()
        if(Name=="") {
            Toast.makeText(this,"Please enter your Name",Toast.LENGTH_SHORT).show()
            return
        }
        else if(filePath==null) {
            Toast.makeText(this,"Please Upload your BPL Card",Toast.LENGTH_SHORT).show()
            return
        }
        user["name"]=Name
        storage.reference.child("users").child(uid).putFile(filePath!!).addOnSuccessListener {snapshot ->
            snapshot.storage.downloadUrl.addOnSuccessListener {uri->
                user["url"]=uri.toString()
                db.collection("users").document(uid).set(user).addOnSuccessListener {
                    progress_bar.hide()
                    startActivity(Intent(this@UserProfileActivity,MainActivity::class.java))
                    finish()
                }.addOnFailureListener{
                    progress_bar.hide()
                    Toast.makeText(this,"Failed to create your profile!",Toast.LENGTH_SHORT).show()
                }
            }
        }.addOnFailureListener{
            progress_bar.hide()
            Toast.makeText(this,"Failed uploading your BPL Card!",Toast.LENGTH_SHORT).show()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            CAMERA_REQUEST -> {
                if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    openCamera()
                else
                    requestPermission()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK) {
            image_view.setImageURI(filePath)
        }
    }
}