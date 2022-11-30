package com.example.sncksapp

import android.app.Application
import android.util.Log
import com.example.lib.VendingMachine
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.lang.reflect.Type


const val MY_FILE_NAME = "mydata.json"

class MyApplication : Application() {

    lateinit var vendingMachines: ArrayList<VendingMachine>
    private lateinit var gson: Gson
    private lateinit var file: File

    override fun onCreate() {
        super.onCreate()
        vendingMachines = ArrayList<VendingMachine>()
        gson = Gson()
        file = File(filesDir, MY_FILE_NAME)
    }

    fun saveToFile() {
        try {
            FileUtils.writeStringToFile(file, gson.toJson(vendingMachines))
        } catch (e: IOException) {
            Log.d("IOException:","Can't save " + file.path)
        }
    }

    fun initData() {
        val userListType: Type = object : TypeToken<ArrayList<VendingMachine?>?>(){}.type
        vendingMachines = try {
            gson.fromJson(FileUtils.readFileToString(file), userListType)
        } catch (e: IOException) {
            ArrayList()
        }
    }

    fun findVendingById(id: String): VendingMachine {
        return vendingMachines.first { it.id.toString().equals(id, true)}
    }
}