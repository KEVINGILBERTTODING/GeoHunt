package com.geohunt.presentation.splashScreen.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geohunt.BuildConfig
import com.geohunt.R
import com.geohunt.core.resource.Resource
import com.geohunt.domain.usecase.GetAppHealthUseCase
import com.geohunt.presentation.splashScreen.event.SplashEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashVm @Inject constructor(
    private val getAppHealthUseCase: GetAppHealthUseCase,
    @ApplicationContext private val context: Context
): ViewModel(){
    val event = MutableSharedFlow<SplashEvent>()

    init {
        viewModelScope.launch {
            val response = getAppHealthUseCase.getAppHealth()
            val appVersion = BuildConfig.VERSION_CODE
            if (response is Resource.Success) {
                val data = response.data

                if (data == null) {
                    event.emit(SplashEvent.onError(
                        context.getString(R.string.something_went_wrong)))
                    return@launch
                }

                if (data.app_status.is_health.not()) {
                    event.emit(SplashEvent.onError(
                        context.getString(R.string.something_went_wrong)))
                    return@launch
                }

                if (data.app_status.is_maintenance) {
                    event.emit(SplashEvent.onMaintenance(data.app_status.maintenance_message))
                    return@launch
                }

                if (appVersion < data.app_status.min_version){
                    event.emit(SplashEvent.onUpdate(data.app_status.update_message,
                        true, data.app_status.update_url))
                    return@launch
                }

                if (appVersion < data.app_status.latest_version) {
                    event.emit(SplashEvent.onUpdate(data.app_status.update_message,
                        data.app_status.force_update, data.app_status.update_url))
                    return@launch
                }

                event.emit(SplashEvent.onSuccess)

            }else {
                event.emit(SplashEvent.onError(
                    context.getString(R.string.something_went_wrong)))
            }
        }
    }
}