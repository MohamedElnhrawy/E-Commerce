package com.gtera.ui.profile.branches

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.gtera.BR
import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.data.interfaces.GpsListener
import com.gtera.data.interfaces.LocationListener
import com.gtera.data.model.Branche
import com.gtera.databinding.BranchesLayoutBinding
import com.gtera.databinding.MapBranchDetailsBinding
import com.gtera.ui.profile.branches.branchinfo.BranchInfoVM
import com.gtera.utils.GpsUtils
import com.gtera.utils.Utilities.bitmapDescriptorFromVector
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomsheet.BottomSheetDialog


class BranchesFragment : BaseFragment<BranchesLayoutBinding, BranchesVM>(), BranchesNavigator,
    OnMapReadyCallback {
    override fun selectBranch(branche: Branche) {
        val point = LatLng(branche.latitude!!.toDouble(),branche.longitude!!.toDouble())
        googleMap?.clear()
        lastMarker = null

//        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 12.0f))
        val markerOptions = MarkerOptions()
        markerOptions.position(point)
        markerOptions.title(branche.name)
        markerOptions.icon(bitmapDescriptorFromVector(this.context!!, R.drawable.ic_selected_location))
        val marker = googleMap!!.addMarker(markerOptions)
        lastMarker = marker
        viewModel?.showBranchInfo(lastMarker?.title)

    }


    private var mapBranchDetailsBinding: MapBranchDetailsBinding? = null
    private lateinit var bottomSheetDialog: BottomSheetDialog



    var lastMarker: Marker? = null
    var branches = ArrayList<Branche>()

    override fun drawUserLocation(location: Location) {
//        googleMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude,location.longitude), 12.0f))
        val markerOptions = MarkerOptions()
        markerOptions.position(LatLng(location.latitude, location.longitude))
        markerOptions.title(getString(R.string.str_user_location))
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
        markerOptions.icon(bitmapDescriptorFromVector(this.context!!, R.drawable.ic_location))
        googleMap!!.addMarker(markerOptions)

        googleMap?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    location.latitude,
                    location.longitude
                ), 10.0f
            )
        )
    }

    override fun drawBranches(branches: ArrayList<Branche>) {
        this.branches = branches
        googleMap?.clear()
        lastMarker = null
        val icon = bitmapDescriptorFromVector(this.context!!, R.drawable.ic_location)

        googleMap?.let {
            for (item in branches) {
                val point = item.latitude?.toDouble()?.let {
                    item.longitude?.toDouble()?.let { long ->
                        LatLng(it, long)
                    }
                }
                val markerOptions = MarkerOptions()
                point?.let { markerOptions.position(it) }
                markerOptions.title(item.name)
                markerOptions.snippet(item.id.toString())
                markerOptions.icon(icon)
                googleMap!!.addMarker(markerOptions)
            }
        }

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap

        viewModel?.getBranches()

        this.googleMap?.setOnMarkerClickListener {

            lastMarker?.setIcon(bitmapDescriptorFromVector(this.context!!, R.drawable.ic_location))
            lastMarker = it
            lastMarker?.setIcon(
                bitmapDescriptorFromVector(
                    this.context!!,
                    R.drawable.ic_selected_location
                )
            )
            viewModel?.showBranchInfo(lastMarker?.title)

            false
        }
    }

    private var fusedLocationClient: FusedLocationProviderClient? = null

    private var googleMap: GoogleMap? = null

    override val layoutId: Int
        get() = R.layout.branches_layout
    override val viewModelClass: Class<BranchesVM>
        get() = BranchesVM::class.java

    override fun setNavigator(viewModel: BranchesVM?) {
        viewModel!!.setNavigator(this)
    }

    override val toolbarTitle: String?
        get() = getString(R.string.str_profile_get_in_toutch)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val transaction = childFragmentManager.beginTransaction()
        val fragment = SupportMapFragment()
        transaction.add(R.id.mapView, fragment).commit()
        fragment.getMapAsync(this)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getLocationContinusly()
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(baseActivity!!)

    }

    override fun getLastLocation(listener: LocationListener) {

        this.context?.let {
            LocationServices.getFusedLocationProviderClient(it).getLastLocation()
                .addOnSuccessListener(object : OnSuccessListener<Location> {
                    override fun onSuccess(location: Location?) {
                        location?.let { it1 -> listener.currentLocation(it1) }
                    }

                })
        }

    }

    fun getLocationContinusly() {
        val mLocationRequest = LocationRequest.create()
        mLocationRequest.interval = 60000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult == null) {
                    return
                }
                for (location in locationResult.locations) {
                    if (location != null) {
                        //TODO: UI updates.
                    }
                }
            }
        }
        this.context?.let {
            LocationServices.getFusedLocationProviderClient(it)
                .requestLocationUpdates(mLocationRequest, mLocationCallback, null)
        }
    }

    override fun openGps(listener: GpsListener) {
        context?.let {
            GpsUtils(it).turnGPSOn(object : GpsUtils.onGpsListener {
                override fun gpsStatus(isGPSEnable: Boolean) {
                    listener.gpsStatus(isGPSEnable)
                }
            })
        }
    }

    override fun showBranchInfo(viewModel: BranchInfoVM) {
        mapBranchDetailsBinding =
            DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.map_branch_details, null, false)


        mapBranchDetailsBinding?.viewModel = viewModel
        mapBranchDetailsBinding?.setVariable(
            BR.viewModel,
            viewModel
        )
        bottomSheetDialog = BottomSheetDialog(context!!,R.style.TransparentDialog)
        bottomSheetDialog.setContentView(mapBranchDetailsBinding?.root!!)
        bottomSheetDialog.show()

    }

    override fun dismissDialog() {
        bottomSheetDialog.dismiss()
    }
}