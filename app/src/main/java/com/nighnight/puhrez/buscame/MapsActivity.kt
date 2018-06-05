package com.nighnight.puhrez.buscame

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import com.google.android.gms.maps.*

import kotlinx.android.synthetic.main.activity_maps.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.tasks.OnSuccessListener
import com.nighnight.puhrez.buscame.R.id.map
import java.io.Serializable

const val MY_LOCATION_REQUEST_CODE = 1
const val ITEM_MARKER = "com.nighnight.puhrez.buscame.ItemMarker"
class MapsActivity :
        AppCompatActivity(),
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var mLoc: Location? = null
    private var nearByItems =  mutableMapOf<String, ItemMarker>()
    private val tag = "MapActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        this.enableLocation()
    }

    private fun enableLocation() {
        // Get MyLocation Permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation
                    .addOnSuccessListener {
                        loc: Location? ->
                        this.moveTo(loc); mLoc = loc; this.populate(loc)
                    }
        } else {
            this.requestLocationPermission()
        }
    }

    private fun populate(location: Location?) {
        // Populates the map around location with items
        if (location == null) return
        mMap.setOnMarkerClickListener(this)
        for (i in 1..10) {
            var item = ItemMarker.generateFromLocation(location)
            var marker = MarkerOptions().title(item.name).position(item.latLng())
            mMap.addMarker(marker)
            nearByItems[item.name] = item
        }
    }

    private fun moveTo(loc: Location? = null) {
        val locLatLng = if (loc == null) LatLng(0.0, 0.0) else LatLng(loc.latitude, loc.longitude)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locLatLng, 15.0f))
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
            // show rationale
            val snack = Snackbar.make(map.view!!, R.string.location_permission_required,
                    Snackbar.LENGTH_INDEFINITE
            )
            snack.setAction(R.string.ok, {_ -> snack.dismiss()})
            snack.show()
            this.requestLocationPermission()
        } else {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    MY_LOCATION_REQUEST_CODE)
        }
    }
    override fun onRequestPermissionsResult(
            requestCode: Int, permissions:
            Array<out String>,
            grantResults: IntArray) {
        if (requestCode == MY_LOCATION_REQUEST_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            this.enableLocation()
        } else {
            this.requestLocationPermission()
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        if (marker == null) return false
        val item = nearByItems[marker.title]
        if (item == null) return false

        Log.i(tag, "clicked on ${item.name}")
        val intent = Intent(this, ItemDescriptionActivity::class.java).apply {
            putExtra(ITEM_MARKER, item as Serializable)
        }
        startActivity(intent)

        return true
    }
}
