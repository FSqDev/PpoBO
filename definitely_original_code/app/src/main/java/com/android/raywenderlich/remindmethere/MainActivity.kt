/*
 * Copyright (c) 2018 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.android.raywenderlich.remindmethere

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.LocationManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

  companion object {
    private const val MY_LOCATION_REQUEST_CODE = 329
    private const val NEW_REMINDER_REQUEST_CODE = 330
    private const val EXTRA_LAT_LNG = "EXTRA_LAT_LNG"

    fun newIntent(context: Context, latLng: LatLng): Intent {
      val intent = Intent(context, MainActivity::class.java)
      intent.putExtra(EXTRA_LAT_LNG, latLng)
      return intent
    }
  }

  private var map: GoogleMap? = null

  private lateinit var locationManager: LocationManager

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val mapFragment = supportFragmentManager
        .findFragmentById(R.id.map) as SupportMapFragment
    mapFragment.getMapAsync(this)

    newReminder.visibility = View.GONE
    currentLocation.visibility = View.GONE
    newReminder.setOnClickListener {
      map?.run {
        val intent = NewReminderActivity.newIntent(
            this@MainActivity,
            cameraPosition.target,
            cameraPosition.zoom)
        startActivityForResult(intent, NEW_REMINDER_REQUEST_CODE)
      }
    }

    locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

    if (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(
          this,
          arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
          MY_LOCATION_REQUEST_CODE)
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (requestCode == NEW_REMINDER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
      showReminders()

      val reminder = getRepository().getLast()
      map?.moveCamera(CameraUpdateFactory.newLatLngZoom(reminder?.latLng, 15f))

      Snackbar.make(main, R.string.reminder_added_success, Snackbar.LENGTH_LONG).show()
    }
  }

  override fun onRequestPermissionsResult(requestCode: Int,
                                          permissions: Array<out String>,
                                          grantResults: IntArray) {
    if (requestCode == MY_LOCATION_REQUEST_CODE) {
      onMapAndPermissionReady()
    }
  }

  private fun onMapAndPermissionReady() {
    if (map != null
        && ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
      map?.isMyLocationEnabled = true
      newReminder.visibility = View.VISIBLE
      currentLocation.visibility = View.VISIBLE

      currentLocation.setOnClickListener {
        val bestProvider = locationManager.getBestProvider(Criteria(), false)
        val location = locationManager.getLastKnownLocation(bestProvider)
        if (location != null) {
          val latLng = LatLng(location.latitude, location.longitude)
          map?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        }
      }

      showReminders()

      centerCamera()
    }
  }

  private fun centerCamera() {
    if (intent.extras != null && intent.extras.containsKey(EXTRA_LAT_LNG)) {
      val latLng = intent.extras.get(EXTRA_LAT_LNG) as LatLng
      map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }
  }

  private fun showReminders() {
    map?.run {
      clear()
      for (reminder in getRepository().getAll()) {
        showReminderInMap(this@MainActivity, this, reminder)
      }
    }
  }

  override fun onMapReady(googleMap: GoogleMap) {
    map = googleMap
    map?.run {
      uiSettings.isMyLocationButtonEnabled = false
      uiSettings.isMapToolbarEnabled = false
      setOnMarkerClickListener(this@MainActivity)
    }

    onMapAndPermissionReady()
  }

  override fun onMarkerClick(marker: Marker): Boolean {
    val reminder = getRepository().get(marker.tag as String)

    if (reminder != null) {
      showReminderRemoveAlert(reminder)
    }

    return true
  }

  private fun showReminderRemoveAlert(reminder: Reminder) {
    val alertDialog = AlertDialog.Builder(this).create()
    alertDialog.run {
      setMessage(getString(R.string.reminder_removal_alert))
      setButton(AlertDialog.BUTTON_POSITIVE,
          getString(R.string.reminder_removal_alert_positive)) { dialog, _ ->
        removeReminder(reminder)
        dialog.dismiss()
      }
      setButton(AlertDialog.BUTTON_NEGATIVE,
          getString(R.string.reminder_removal_alert_negative)) { dialog, _ ->
        dialog.dismiss()
      }
      show()
    }
  }

  private fun removeReminder(reminder: Reminder) {
    getRepository().remove(
        reminder,
        success = {
          showReminders()
          Snackbar.make(main, R.string.reminder_removed_success, Snackbar.LENGTH_LONG).show()
        },
        failure = {
          Snackbar.make(main, it, Snackbar.LENGTH_LONG).show()
        })
  }
}
