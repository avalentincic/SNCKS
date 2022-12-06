package com.example.sncksapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lib.VendingMachine
import com.example.sncksapp.databinding.FragmentMainBinding
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var app: MyApplication

    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
    private lateinit var map : MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        app = activity?.application as MyApplication
        map = binding.mapview
        map.setTileSource(TileSourceFactory.MAPNIK)

        val mapController = map.controller
        mapController.setZoom(18.0)
        val startPoint = GeoPoint(46.559113978089606, 15.639132629280839)
        mapController.setCenter(startPoint)

        handleMarkers(app.vendingMachines, map)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @Suppress("DEPRECATION")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        val permissionsToRequest = ArrayList<String>()
        var i = 0
        while (i < grantResults.size) {
            permissionsToRequest.add(permissions[i])
            i++
        }
        if (permissionsToRequest.size > 0) {
            @Suppress("DEPRECATION")
            requestPermissions(
                permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS_REQUEST_CODE)
        }
    }


    override fun onResume() {
        super.onResume()
        map.onResume()
    }
}

fun handleMarkers(items: ArrayList<VendingMachine>, m:MapView){
    val markers: MutableList<Marker> = ArrayList()

    for (vm in items) {

        val markerLocation = GeoPoint(vm.latitude, vm.longitude)

        val marker = Marker(m)
        marker.position = markerLocation
        marker.title = vm.name
        //marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))

        markers.add(marker)
    }

    m.overlays.addAll(markers)
    m.invalidate()
}

/*
var marker = Marker(map)
marker.position = startPoint
//marker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.vending_machine)
//marker.setAnchor(Marker.ANCHOR_BOTTOM, Marker.ANCHOR_CENTER)
map.overlays.add(marker)
map.invalidate()*/