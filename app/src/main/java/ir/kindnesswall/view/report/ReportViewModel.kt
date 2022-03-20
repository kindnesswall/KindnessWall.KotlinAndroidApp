package ir.kindnesswall.view.report

import androidx.lifecycle.viewModelScope
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.model.EmptyModel
import ir.kindnesswall.data.model.ReportCharityMessageModel
import ir.kindnesswall.data.model.ReportGiftMessageModel
import ir.kindnesswall.data.repository.CharityRepo
import ir.kindnesswall.data.repository.GiftRepo
import ir.kindnesswall.utils.LifecycleViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * Created by Farhad Beigirad on 3/20/22.
 */
class ReportViewModel(
    private val giftRepo: GiftRepo,
    private val charityRepo: CharityRepo,
) : LifecycleViewModel() {

    private val _errorFlow =
        MutableSharedFlow<String>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val errorFlow: Flow<String> get() = _errorFlow

    private val _isSuccessfullyFlow =
        MutableSharedFlow<EmptyModel>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val isSuccessfullyFlow: Flow<EmptyModel> get() = _isSuccessfullyFlow

    fun sendGiftReport(model: ReportDialog.ReportType.Gift, text: String) {
        val requestModel = ReportGiftMessageModel(giftId = model.id, message = text)

        giftRepo.sendMessageGiftReport(viewModelScope, requestModel)
            .observe(this) {
                if (it.status == CustomResult.Status.SUCCESS) {
                    _isSuccessfullyFlow.tryEmit(EmptyModel)
                } else if (it.status == CustomResult.Status.ERROR) {
                    _errorFlow.tryEmit(it.errorMessage?.message ?: "")
                }
            }
    }

    fun sendCharityReport(model: ReportDialog.ReportType.Charity, text: String) {
        val requestModel = ReportCharityMessageModel(charityId = model.id, message = text)

        charityRepo.sendMessageCharityReport(viewModelScope, requestModel)
            .observe(this) {
                if (it.status == CustomResult.Status.SUCCESS) {
                    _isSuccessfullyFlow.tryEmit(EmptyModel)
                } else if (it.status == CustomResult.Status.ERROR) {
                    _errorFlow.tryEmit(it.errorMessage?.message ?: "")
                }
            }
    }
}