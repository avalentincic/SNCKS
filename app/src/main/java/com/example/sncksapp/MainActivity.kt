package com.example.sncksapp

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.lib.VendingMachine
import com.example.sncksapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var app: MyApplication
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MyApplication

        //load data from json file
        //app.initData()
        app.readFromDB()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val fragmentName = intent.getStringExtra("fragment")
        val vmID = intent.getStringExtra("ID")
        if (fragmentName == "detailFragment") {
            val bundle = bundleOf(
                "ID" to vmID
            )
            navController.navigate(
                R.id.action_mainFragment_to_detailFragment, bundle
            )
        }
        Log.i("FR", intent.extras.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_list -> {
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.action_mainFragment_to_ListFragment)
                true
            }
            R.id.action_add -> {
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.action_ListFragment_to_editVendingFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

}

