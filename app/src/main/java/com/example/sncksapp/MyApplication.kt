package com.example.sncksapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.lib.VendingMachine
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.lang.reflect.Type


const val MY_FILE_NAME = "mydata.json"
const val TAG = "DB" //

class MyApplication : Application() {

    lateinit var database: DatabaseReference
    lateinit var vendingMachines: ArrayList<VendingMachine>
    private lateinit var gson: Gson
    private lateinit var file: File
    private val CHANNEL_ID = "channel_id_example_01"
    private val notificationId = 101

    override fun onCreate() {
        super.onCreate()
        vendingMachines = ArrayList<VendingMachine>()
        gson = Gson()
        file = File(filesDir, MY_FILE_NAME)
        database = Firebase.database("https://sncks-app-pora-default-rtdb.europe-west1.firebasedatabase.app/").reference
        initDataCarListener()
        createNotificationChannel()
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

    fun saveToDB() {
        database.child("vending_machines").setValue(vendingMachines)
    }

    fun readFromDB() {
        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val arrayList = dataSnapshot.child("vending_machines").getValue<ArrayList<VendingMachine>>()
                Log.i(TAG,arrayList.toString())
                vendingMachines = arrayList!!
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })

    }

    fun findVendingById(id: String): VendingMachine {
        return vendingMachines.first { it.id.toString().equals(id, true)}
    }

    fun updateVending(id: String, name:String , lat: Double, long:Double){
        vendingMachines.first { it.id.toString().equals(id, true)}.apply {
            this.name = name
            this.latitude = lat
            this.longitude = long
        }
    }

    private fun initDataCarListener() {
        database.child("vending_machines").addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.i(TAG, "Add: " + snapshot.key + " ");
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                /*val arrayList = snapshot.children
                for (c in snapshot.children){
                    var vm = c.getValue<VendingMachine>()
                    Log.i(TAG, vm.toString())
                }*/
                //Log.i(TAG, arrayList.toString())
                val MSG = vendingMachines[snapshot.key!!.toInt()].name + " has just RESTOCKED!"
                Log.i(TAG, "Changed: "+ snapshot.key + " ");
                createNotify("SNCKS", MSG, R.drawable.snack, vendingMachines[snapshot.key!!.toInt()].id)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.i(TAG, "Removed: " + snapshot.key + " ");
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Log.i(TAG, "Moved: " + snapshot.key + " ");
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "MyTestChannel"
            val descriptionText = "Testing notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotify(title: String, content: String, imageId:Int, vmID: String) {

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("fragment", "detailFragment")
            putExtra("ID", vmID)
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(imageId)
            .setContentTitle(title)
            .setContentText(content)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId,builder.build())
    }


}


