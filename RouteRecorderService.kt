package com.example.seesea.navigation

import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.google.android.gms.location.*

class RouteRecorderService : Service() {

    private val binder = LocalBinder()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationCallback: LocationCallback? = null
    private var currentRoute: NauticalRoute? = null
    private var currentLocation: Location? = null

    inner class LocalBinder : Binder() {
        fun getService(): RouteRecorderService = this@RouteRecorderService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        startLocationUpdates()
    }

    fun startNewRoute() {
        currentRoute = NauticalRoute()
    }

    fun addWaypoint(waypoint: Waypoint) {
        currentRoute?.waypoints?.add(waypoint)
    }

    fun stopRecording() {
        // Parar gravação mas manter dados em memória
    }

    fun getCurrentRoute(): NauticalRoute? = currentRoute

    fun getCurrentWaypoints(): List<Waypoint> = currentRoute?.waypoints ?: emptyList()

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 5000
            fastestInterval = 2000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    currentLocation = location
                    currentRoute?.addPosition(location.latitude, location.longitude)
                }
            }
        }

        locationCallback?.let {
            fusedLocationClient.requestLocationUpdates(locationRequest, it, null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
        }
    }
}
