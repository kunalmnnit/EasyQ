package com.kunal.vqms.ui

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.alan.alansdk.AlanCallback
import com.alan.alansdk.AlanConfig
import com.alan.alansdk.AlanState
import com.alan.alansdk.events.EventCommand
import com.google.android.material.navigation.NavigationView
import com.kunal.vqms.R
import com.kunal.vqms.adapter.ItemListAdapter
import com.kunal.vqms.model.Item
import com.kunal.vqms.util.LocaleHelper
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_main.*

/* @author kunal on 16/10/2020 */
/* Ration Shop Recycler View */

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var adapter: ItemListAdapter
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase!!,"en"))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Paper.init(this)
        val key = "d68ce1a947377a1b1cd3f86d54b7142e2e956eca572e1d8b807a3e2338fdd0dc/stage"
        val config = AlanConfig.builder()
            .setProjectId(key)
            .build()
        alan_button.initWithConfig(config)
        alan_button.registerCallback(object : AlanCallback() {
            override fun onCommandReceived(eventCommand: EventCommand?) {
                super.onCommandReceived(eventCommand)
                eventCommand?.data?.let {

                        startActivity(Intent(this@MainActivity,BookAppointment::class.java).putExtra("place",
                            "CsHBeN40fSYvZFCKlvivOZo8ocp2"))
                }
            }
        })
        updateView(Paper.book().read<String>("language"))

    }

    private fun updateView(language: String?) {
        val context = LocaleHelper.setLocale(this,language)
        setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.app_name)
        val actionBarDrawerToggle = ActionBarDrawerToggle(this,drawer_layout,toolbar,R.string.openNavDrawer,R.string.closeNavDrawer)
        drawer_layout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        item_list_view.layoutManager = LinearLayoutManager(this)
        item_list_view.setHasFixedSize(true)
        item_list_view.layoutManager = LinearLayoutManager(this)
        val itemList = listOf(Item(R.drawable.ration_shops,getString(R.string.item2)),
        )
        adapter = ItemListAdapter(itemList,baseContext)
        item_list_view.adapter = adapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val homeIntent = Intent(Intent.ACTION_MAIN)
        homeIntent.addCategory(Intent.CATEGORY_HOME)
        homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(homeIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.change_language) {
                val lang = Paper.book().read<String>("language")
                if(lang == "en") {
                    Paper.book().write("language","hi")
                    print(Paper.book().read<String>("language"))
                    updateView(Paper.book().read("language"))
                } else {
                    Paper.book().write("language","en")
                    updateView(Paper.book().read("language"))
                }
            return true
        }
         return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return true
    }

    override fun onPointerCaptureChanged(hasCapture: Boolean) {
        super.onPointerCaptureChanged(hasCapture)
    }
}