package com.snapbizz.onboarding

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snapbizz.common.config.SnapResult
import com.snapbizz.common.config.models.StoreDetailsResponse
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

    fun setLoader(show: Boolean) = updateOtpState { copy(isLoading = show) }

    fun getDeviceId(context: Context) {
        val deviceId = SnapCommonUtils.getDeviceId(context)
        updateOtpState { copy(deviceId = deviceId) }
        viewModelScope.launch(dispatcherProvider.io) {
            snapDataStore.setDeviceId(deviceId)
        }
    }

    fun clearError() = updateOtpState { copy(message = null) }

    private fun sendOtp() {
        updateOtpState { copy(isLoading = true) }
        viewModelScope.launch(dispatcherProvider.io) {
            val result = validatePhone(uiState.value.phoneNo)?.let {
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
            val result =
                validatePhoneAndOtp(uiState.value.phoneNo, uiState.value.otp)?.let { (phone, otp) ->
                    onBoardingRepo.verifyOtp(phone, otp, uiState.value.deviceId)
                } ?: SnapResult.Error(resourceProvider.getString(R.string.otp_verification_failure))

            when (result) {
                is SnapResult.Loading -> updateOtpState { copy(isLoading = true) }
                is SnapResult.Success -> updateOtpState {
                    copy(
                        isLoading = false, isVerified = true, message = result.data.status
                    )
                }.also { _storeDetails.value = result.data }

                is SnapResult.Error -> updateOtpState {
                    copy(
                        isLoading = false, message = result.message
                    )
                }
            }
        }
    }

    private fun validatePhone(phoneNo: String): Long? {
        return phoneNo.takeIf { it.length == 10 }?.toLongOrNull()?.also {
            updateOtpState { copy(message = null) }
        } ?: run {
            updateOtpState { copy(message = resourceProvider.getString(R.string.invalid_phone_number)) }
            null
        }
    }

    private fun validatePhoneAndOtp(phoneNo: String, otp: String): Pair<Long, Int>? {
        val otpDigit = otp.toIntOrNull()
        return when {
            !isValidPhoneNumber(phoneNo) -> {
                updateOtpState { copy(message = resourceProvider.getString(R.string.invalid_phone_number)) }
                null
            }

            otpDigit == null -> {
                updateOtpState { copy(message = resourceProvider.getString(R.string.otp_cannot_be_blank)) }
                null
            }

            else -> {
                phoneNo.toLongOrNull()?.let { phone ->
                    Pair(phone, otpDigit)
                } ?: run {
                    updateOtpState { copy(message = resourceProvider.getString(R.string.invalid_phone_number_format)) }
                    null
                }
            }
        }
    }

    private fun isValidPhoneNumber(phoneNo: String) =
        phoneNo.length == 10 && phoneNo.all { it.isDigit() }

    private fun handleVerifyOtpResult(result: Result<StoreDetailsResponse>) {
        updateOtpState {
            copy(
                isVerified = result.isSuccess,
                message = result.exceptionOrNull()?.message ?: result.getOrNull()?.status
            )
        }
        result.getOrNull()?.let { _storeDetails.value = it }
    }

    fun setStoreDetails(storeDetailsResponse: StoreDetailsResponse) {
        _storeDetails.value = storeDetailsResponse
        viewModelScope.launch(dispatcherProvider.io) {
            snapDataStore.saveStoreDetails(storeDetails.value, posId.value)
            snapDataStore.loadPrefs()
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
}
