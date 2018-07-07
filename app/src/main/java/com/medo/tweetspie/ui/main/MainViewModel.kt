package com.medo.tweetspie.ui.main

import androidx.lifecycle.LiveData
import com.medo.tweetspie.base.BaseViewModel
import com.medo.tweetspie.system.UserRepository

class MainViewModel(
        userRepository: UserRepository
) : BaseViewModel() {

    val user: LiveData<String> = userRepository.getUser()

//    init {
//        permission.postValue(permissionManager.hasLocationPermission())
//    }
//
//    fun fetchWeather() {
//        locationManager.getLocation()?.addOnSuccessListener {
//            weatherRepository.fetch(it.latitude, it.longitude)
//        }
//    }
}