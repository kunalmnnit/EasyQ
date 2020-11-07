package com.kunal.vqms.ui

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.alan.alansdk.AlanConfig
import com.kunal.vqms.R
import com.kunal.vqms.adapter.ItemListAdapter
import com.kunal.vqms.model.Item
import com.kunal.vqms.util.LocaleHelper
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var adapter: ItemListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.app_name)
        item_list_view.layoutManager = LinearLayoutManager(this)
        item_list_view.setHasFixedSize(true)
        item_list_view.layoutManager = LinearLayoutManager(this)
        val itemList = listOf(Item(R.drawable.ration_shops,getString(R.string.item2)),
        )
        adapter = ItemListAdapter(itemList,baseContext)
        item_list_view.adapter = adapter
        val key = "d68ce1a947377a1b1cd3f86d54b7142e2e956eca572e1d8b807a3e2338fdd0dc/stage"
        val config = AlanConfig.builder()
            .setProjectId(key)
            .build()

        alan_button.initWithConfig(config)
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
        when(item.itemId) {
            R.id.change_language -> {
                if(LocaleHelper.getLanguage(this)=="en") {
                    LocaleHelper.setLocale(this,"hi")
                } else {
                    LocaleHelper.setLocale(this,"en")
                }
                return true
            }
        }
        return true
    }
}