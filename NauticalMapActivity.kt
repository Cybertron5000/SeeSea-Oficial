package com.example.seesea.navigation

import android.annotation.SuppressLint
import android.content.*
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.example.seesea.R
import com.example.seesea.databinding.ActivityNauticalMapBinding
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class NauticalMapActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNauticalMapBinding
    private lateinit var map: MapView
    private var routeService: RouteRecorderService? = null
    private var isBound = false
    private lateinit var locationOverlay: MyLocationNewOverlay
    private lateinit var compassHelper: CompassHelper

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as RouteRecorderService.LocalBinder
            routeService = binder.getService()
            isBound = true
            updateRouteOnMap()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNauticalMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuração do OpenSeaMap
        Configuration.getInstance().userAgentValue = packageName
        map = binding.mapView
        map.setTileSource(XYTileSource(
            "OpenSeaMap",
            0, 18, 256, ".png",
            arrayOf("https://tiles.openseamap.org/seamark/")
        ))

        // Configuração GPS
        locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), map)
        locationOverlay.enableMyLocation()
        map.overlays.add(locationOverlay)

        // Configuração Bússola
        compassHelper = CompassHelper(this) { azimuth ->
            binding.compassView.rotation = -azimuth
            locationOverlay.setBearing(azimuth.toFloat())
            map.invalidate()
        }

        // Controles de Rota
        binding.btnStartRecording.setOnClickListener {
            routeService?.startNewRoute()
        }

        binding.btnAddWaypoint.setOnClickListener {
            routeService?.currentLocation?.let { loc ->
                routeService?.addWaypoint(
                    Waypoint(
                        GeoPoint(loc.latitude, loc.longitude),
                        "Waypoint ${routeService?.getCurrentWaypoints()?.size?.plus(1)}"
                    )
                )
            }
        }

        binding.btnStopRecording.setOnClickListener {
            routeService?.stopRecording()
            showSaveRouteDialog()
        }
    }

    override fun onStart() {
        super.onStart()
        // Iniciar serviço de gravação de rota
        Intent(this, RouteRecorderService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
            startService(intent)
        }
        compassHelper.start()
    }

    override fun onStop() {
        super.onStop()
        if (isBound) {
            unbindService(connection)
            isBound = false
        }
        compassHelper.stop()
    }

    @SuppressLint("MissingPermission")
    private fun updateRouteOnMap() {
        routeService?.getCurrentRoute()?.let { route ->
            // Implementar overlay da rota no mapa
            // Mostrar waypoints e linha de rota
        }
    }

    private fun showSaveRouteDialog() {
        // Implementar diálogo para salvar rota com nome
    }
}
