package com.kunal.vqms.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kunal.vqms.R
import com.kunal.vqms.constants.Constants
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.activity_maps.*

/* @author Anshul on 21/10/2020 */
/* Maps Activity for nearby Ration Shops */

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, PermissionsListener {
    private var permissionsManager: PermissionsManager = PermissionsManager(this)
    private lateinit var mapboxMap: MapboxMap
    private val rationShops:MutableList<Pair<String,GeoPoint>>? = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Firebase.firestore
        db.collection("shops").get().addOnSuccessListener {shopList -> shopList.documents.iterator().forEach {shop->
                rationShops?.add(Pair(shop.id,shop.get("location") as GeoPoint))
            }
        }.addOnCompleteListener {
            Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
            setContentView(R.layout.activity_maps)
            mapView?.onCreate(savedInstanceState)
            mapView?.getMapAsync(this)
        }

    }
    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        mapboxMap.setOnMarkerClickListener {
            startActivity(Intent(this@MapsActivity,BookAppointment::class.java).putExtra("place",it.snippet))
            false
        }
        mapboxMap.setStyle(
            Style.Builder().fromUri(
                "mapbox://styles/mapbox/cjerxnqt3cgvp2rmyuxbeqme7"
            )
        ) {
            enableLocationComponent(it)
                rationShops?.forEach {point ->
                    mapboxMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(point.second.latitude, point.second.longitude))
                            .setSnippet(point.first)
                    )
                }
        }
    }
        @SuppressLint("MissingPermission")
        private fun enableLocationComponent(loadedMapStyle: Style) {
            if (PermissionsManager.areLocationPermissionsGranted(this)) {

                val customLocationComponentOptions = LocationComponentOptions.builder(this)
                    .trackingGesturesManagement(true)
                    .accuracyColor(ContextCompat.getColor(this, R.color.colorAccent))
                    .build()

                val locationComponentActivationOptions = LocationComponentActivationOptions.builder(this, loadedMapStyle)
                    .locationComponentOptions(customLocationComponentOptions)
                    .build()
                mapboxMap.locationComponent.apply {

                    activateLocationComponent(locationComponentActivationOptions)

                    isLocationComponentEnabled = true

                    cameraMode = CameraMode.TRACKING

                    renderMode = RenderMode.COMPASS
                }
            } else {
                permissionsManager = PermissionsManager(this)
                permissionsManager.requestLocationPermissions(this)
            }
        }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            enableLocationComponent(mapboxMap.style!!)
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show()
            finish()
        }
    }
}