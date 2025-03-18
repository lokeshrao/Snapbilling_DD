package com.snapbizz.onboarding

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snapbizz.common.config.models.StoreDetailsResponse
import com.snapbizz.core.datastore.SnapDataStore
import com.snapbizz.core.utils.DispatcherProvider
import com.snapbizz.core.utils.ResourceProvider
import com.snapbizz.core.utils.SnapCommonUtils
import com.snapbizz.onboarding.data.OnboardingRepositoryImpl
import com.snapbizz.onboarding.downSync.DownSyncHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val resourceProvider: ResourceProvider,
    val onBoardingRepo: OnboardingRepositoryImpl,
    private val downSyncHelper: DownSyncHelper
) : ViewModel() {

    private val _isOtpSent = MutableStateFlow(false)
    val isOtpSent: StateFlow<Boolean> = _isOtpSent

    private val _isVerified = MutableStateFlow(false)
    val isVerified: StateFlow<Boolean> = _isVerified

    private val _isLoginSuccess = MutableStateFlow(false)
    val isLoginSuccess: StateFlow<Boolean> = _isLoginSuccess

    private val _deviceId = MutableStateFlow("")
    val deviceId: StateFlow<String> = _deviceId

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _message = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _message

    private val _phoneNo = MutableStateFlow("")
    val phoneNo: StateFlow<String> = _phoneNo

    private val _storeDetails = MutableStateFlow<StoreDetailsResponse?>(null)
    val storeDetails: StateFlow<StoreDetailsResponse?> = _storeDetails

    private val _otp = MutableStateFlow("")
    val otp: StateFlow<String> = _otp

    private val _syncMessages =
        MutableStateFlow<String?>(resourceProvider.getString(R.string.loading))
    val syncMessages: StateFlow<String?> = _syncMessages

    fun setPhoneNo(phoneNo: String) {
        _phoneNo.value = phoneNo
    }

    fun setOtp(otp: String) {
        _otp.value = otp
    }

    fun setLoader(show: Boolean) {
        _loading.value = show
    }

    fun setStoreDetails(storeDetailsResponse: StoreDetailsResponse) {
        _storeDetails.value = storeDetailsResponse
    }

    fun getDeviceId(context: Context) {
        viewModelScope.launch {
            _deviceId.value = SnapCommonUtils.getDeviceId(context)
        }
    }

    fun clearError() {
        _message.value = null
    }

    fun sendOtp(phoneNo: String, deviceId: String) {
        viewModelScope.launch(dispatcherProvider.io) {
            _loading.value = true
            _message.value = null
            val result = validatePhone(phoneNo)?.let { phone ->
                try {
                    onBoardingRepo.sendOtp(phone, deviceId)
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }
            handleSendOtpResult(result)
            _loading.value = false
        }
    }

    fun onVerifyOtp(phoneNo: String, otp: String, deviceId: String) {
        viewModelScope.launch(dispatcherProvider.io) {
            _loading.value = true
            _message.value = null
            val result = validatePhoneAndOtp(phoneNo, otp)?.let { (phone, otp) ->
                try {
                    onBoardingRepo.verifyOtp(phone, otp, deviceId)
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }
            handleVerifyOtpResult(result)
            _loading.value = false
        }
    }

    private fun validatePhone(phoneNo: String): Long? {
        return if (phoneNo.length == 10) {
            phoneNo.toLongOrNull()
        } else {
            _message.value = resourceProvider.getString(R.string.invalid_phone_number)
            null
        }
    }

    private fun validatePhoneAndOtp(phoneNo: String, otp: String): Pair<Long, Int>? {
        val otpDigit = otp.toIntOrNull()
        return when {
            !isValidPhoneNumber(phoneNo) -> {
                _message.value = resourceProvider.getString(R.string.invalid_phone_number)
                null
            }

            otpDigit == null -> {
                _message.value = resourceProvider.getString(R.string.otp_cannot_be_blank)
                null
            }

            else -> {
                phoneNo.toLongOrNull()?.let { phone ->
                    Pair(phone, otpDigit)
                } ?: run {
                    _message.value = resourceProvider.getString(R.string.invalid_phone_number_format)
                    null
                }
            }
        }
    }

    private fun isValidPhoneNumber(phoneNo: String): Boolean {
        return phoneNo.length == 10 && phoneNo.all { it.isDigit() }
    }

    private fun handleSendOtpResult(result: Result<Unit>?) {
        result?.let {
            if (it.isSuccess) {
                _isOtpSent.value = true
                _message.value=resourceProvider.getString(R.string.otp_sent)
            } else {
                if (it.exceptionOrNull()?.message?.contains(resourceProvider.getString(R.string.registration_error_prefix)) == true) {
                    _isOtpSent.value = false
                }
                _message.value = it.exceptionOrNull()?.message
            }
        }
    }

    private fun handleVerifyOtpResult(result: Result<StoreDetailsResponse>?) {
        result?.let {
            if (it.isSuccess) {
                _storeDetails.value = result.getOrNull()
                _isVerified.value = true
                _message.value = result.getOrNull()?.status

            } else {
                if (it.exceptionOrNull()?.message?.contains(resourceProvider.getString(R.string.registration_error)) == true) {
                    _isOtpSent.value = false
                }
                _message.value = it.exceptionOrNull()?.message
            }
        }
    }

//    fun doDownloadSync(success: () -> Unit) {
//        SnapDataStore.saveStoreDetails(storeDetails.value, posId.value?:0)
//        hphSharedPreferences.loadPrefs()
//        _message.value = null
//        _loading.value = true
//        viewModelScope.launch(dispatcherProvider.io) {
//            _syncMessages.value = " Fetching categories .."
//            onBoardingRepo.getCategoryData().onFailure {
//                _loading.value = false
//                _message.value = it.message
//                return@launch
//            }
//            downSyncHelper.doDownloadSync(_syncMessages).onSuccess {
//                setStoreRegistered {
//                    _message.value = resourceProvider.getString(R.string.sync_completed)
//                    _loading.value = false
//                    viewModelScope.launch(dispatcherProvider.main) {
//                        success()
//                    }
//                }
//            }.onFailure {
//                _loading.value = false
//                _message.value = it.message
//            }
//        }
//    }

    fun setStoreRegistered(result: () -> Unit) {
        try {
            //hphSharedPreferences.setStoreAsRegistered(true)
            result()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

}