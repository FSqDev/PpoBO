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

import android.content.Context
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.GeofenceStatusCodes

object GeofenceErrorMessages {
  fun getErrorString(context: Context, e: Exception): String {
    return if (e is ApiException) {
      getErrorString(context, e.statusCode)
    } else {
      context.resources.getString(R.string.geofence_unknown_error)
    }
  }

  fun getErrorString(context: Context, errorCode: Int): String {
    val resources = context.resources
    return when (errorCode) {
      GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE ->
        resources.getString(R.string.geofence_not_available)

      GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES ->
        resources.getString(R.string.geofence_too_many_geofences)

      GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS ->
        resources.getString(R.string.geofence_too_many_pending_intents)

      else -> resources.getString(R.string.geofence_unknown_error)
    }
  }
}