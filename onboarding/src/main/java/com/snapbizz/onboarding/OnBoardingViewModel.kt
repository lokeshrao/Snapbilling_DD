package com.snapbizz.onboarding

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snapbizz.common.config.SnapResult
import com.snapbizz.common.models.StoreDetailsResponse
import com.snapbizz.core.datastore.SnapDataStore
import com.snapbizz.core.utils.DispatcherProvider
import com.snapbizz.core.utils.ResourceProvider
import com.snapbizz.core.utils.SnapCommonUtils
import com.snapbizz.core.utils.SnapConstants.POS_ID_FROM
import com.snapbizz.core.utils.SnapConstants.POS_ID_TO
import com.snapbizz.onboarding.data.OnboardingRepositoryImpl
import com.snapbizz.onboarding.data.OtpState
import com.snapbizz.onboarding.downSync.DownSyncHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random


@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val resourceProvider: ResourceProvider,
    private val onBoardingRepo: OnboardingRepositoryImpl,
    private val downSyncHelper: DownSyncHelper,
    private val snapDataStore: SnapDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(OtpState())
    val uiState: StateFlow<OtpState> = _uiState.asStateFlow()

    private val _storeDetails = MutableStateFlow<StoreDetailsResponse?>(null)
    val storeDetails: StateFlow<StoreDetailsResponse?> = _storeDetails

    private val _posId = MutableStateFlow(Random.nextInt(POS_ID_FROM, POS_ID_TO))
    val posId: StateFlow<Int> = _posId.asStateFlow()

    private val _syncMessages = MutableStateFlow(resourceProvider.getString(R.string.loading))
    val syncMessages: StateFlow<String> = _syncMessages.asStateFlow()

    private fun updateOtpState(update: OtpState.() -> OtpState) {
        _uiState.update { it.update() }
    }

    fun setPhoneNo(phoneNo: String) = updateOtpState { copy(phoneNo = phoneNo) }

    fun setOtp(otp: String) = updateOtpState { copy(otp = otp) }

    fun clearError() = updateOtpState { copy(message = null) }

    fun getDeviceId(context: Context) {
        val deviceId = SnapCommonUtils.getDeviceId(context)
        updateOtpState { copy(deviceId = deviceId) }
        viewModelScope.launch(dispatcherProvider.io) {
            snapDataStore.setDeviceId(deviceId)
        }
    }

    fun updatePosId(posId: String) {
        _storeDetails.update { it?.copy(posId = posId.toIntOrNull()) }
    }

    private fun sendOtp() {
        updateOtpState { copy(isLoading = true) }
        viewModelScope.launch(dispatcherProvider.io) {
            val result = uiState.value.validatePhone(resourceProvider)?.let {
                onBoardingRepo.sendOtp(it, uiState.value.deviceId)
            } ?: SnapResult.Error(resourceProvider.getString(R.string.invalid_phone_number))

            when (result) {
                is SnapResult.Success -> updateOtpState {
                    copy(
                        isLoading = false,
                        isOtpSent = true,
                        message = resourceProvider.getString(R.string.otp_sent)
                    )
                }

                is SnapResult.Error -> updateOtpState {
                    copy(
                        isLoading = false, message = result.message
                    )
                }

                is SnapResult.Loading -> updateOtpState { copy(isLoading = true) }
            }
        }
    }

    private fun onVerifyOtp() {
        updateOtpState { copy(isLoading = true) }
        viewModelScope.launch(dispatcherProvider.io) {
            val result = uiState.value.validatePhoneAndOtp(resourceProvider)?.let { (phone, otp) ->
                onBoardingRepo.verifyOtp(phone, otp, uiState.value.deviceId)
            } ?: SnapResult.Error(resourceProvider.getString(R.string.otp_verification_failure))

            when (result) {
                is SnapResult.Loading -> updateOtpState { copy(isLoading = true) }
                is SnapResult.Success -> updateOtpState {
                    copy(
                        isLoading = false, isVerified = true, message = result.data.status
                    )
                }.also { setStoreDetails(result.data) }

                is SnapResult.Error -> updateOtpState {
                    copy(
                        isLoading = false, message = result.message
                    )
                }
            }
        }
    }

    fun setStoreDetails(storeDetailsResponse: StoreDetailsResponse) {
        viewModelScope.launch(dispatcherProvider.io) {
            snapDataStore.saveStoreDetails(storeDetailsResponse, posId.value)
        }
    }

    fun doDownloadSync(success: () -> Unit) {
        updateOtpState { copy(message = null, isLoading = true) }
        viewModelScope.launch(dispatcherProvider.io) {
            downSyncHelper.doDownloadSync(_syncMessages).onSuccess {
                setStoreRegistered {
                    updateOtpState {
                        copy(
                            message = resourceProvider.getString(R.string.sync_completed),
                            isLoading = false
                        )
                    }
                    viewModelScope.launch(dispatcherProvider.io) { downSyncHelper.updateSearchData() }
                    viewModelScope.launch(dispatcherProvider.main) { success() }
                }
            }.onFailure {
                updateOtpState { copy(isLoading = false, message = it.message) }
            }
        }
    }

    private suspend fun setStoreRegistered(result: () -> Unit) {
        try {
            snapDataStore.setStoreAsRegistered(true)
            result()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun onButtonClick() {
        if (uiState.value.isOtpSent) {
            onVerifyOtp()
        } else {
            sendOtp()
        }
    }

    fun loadStoreDetails() {
        updateOtpState { copy(isLoading = true) }
        viewModelScope.launch(dispatcherProvider.io) {
            _storeDetails.value = snapDataStore.getStoreDetails().first()
            snapDataStore.loadPrefs()
            updateOtpState { copy(isLoading = false) }
        }
    }
}
