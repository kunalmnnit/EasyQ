package com.kunal.vqms.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kunal.vqms.R
import io.paperdb.Paper

/* @author Kunal on 16/10/2020 */
/* Authentication Activity */

class LoginActivity : AppCompatActivity() {
    private lateinit var providers:List<AuthUI.IdpConfig>
    private val REQUEST_CODE:Int = 1234
    private val TAG:String = "AUTH"
    private var auth:FirebaseAuth?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Paper.init(this)
        val language:String? = Paper.book().read("language")
        if(language==null)
            Paper.book().write("language","en")
        providers = listOf(
            AuthUI.IdpConfig.PhoneBuilder().build()
        )
        auth = Firebase.auth
        if(auth!!.currentUser!=null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        } else {
            showSignInOptions()
        }
    }
    private fun showSignInOptions() {
         startActivityForResult(AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setTheme(R.style.MyTheme)
                            .setLogo(R.drawable.logo)
                            .setIsSmartLockEnabled(false)
                            .build(),REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==REQUEST_CODE)
        {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                startActivity(Intent(this@LoginActivity, UserProfileActivity::class.java))
                finish()
            } else {
                if(response==null)
                    finish()
                else
                    Log.d(TAG,response.error.toString())
            }
        }
    }
}
