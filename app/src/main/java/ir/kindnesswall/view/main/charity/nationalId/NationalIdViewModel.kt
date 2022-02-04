package ir.kindnesswall.view.main.charity.nationalId

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NationalIdViewModel : ViewModel() {
    var animationStateTextNationalId = mutableStateOf(false)
    var animationStateEditTextNationalId = mutableStateOf(false)
    var visibilityLoading = mutableStateOf(false)


    var visibilityListNationalId = mutableStateOf(false)
    var fakeGenerator = mutableStateOf(0)


    var valueOfNationalId = mutableStateOf("")
    var valueOfMessageRequest = mutableStateOf("")

    val colorRoundEditFiled = mutableStateOf(Color.Gray)
    val textButton = mutableStateOf(R.string.next)
    val colorButton = mutableStateOf(Color(0xff11BC89))
    val showErrorMessage = mutableStateOf(false)


    init {
        handleEvent(AnimationNationalId)

    }


    fun handleEvent(event: NationalIdEvent) {
        when (event) {
            AnimationNationalId -> {
                enableAnimationView()
            }
            is EnterNationalId -> {
                nationalCodeVerification(event.number)
            }
            is ResultOfSearch -> {
                resultSearchNationalId()
            }
        }
    }
    private fun resultSearchNationalId() {
        nationalCodeVerification(valueOfNationalId.value)
            .apply {
                val errorStork = first
                valueOfMessageRequest.value = second

                if (errorStork) {
                    visibilityLoading.value = true
                    colorRoundEditFiled.value = Color.Yellow
                    when (valueOfNationalId.value) {
                        "0019191944" -> {
                            visibilityListNationalId.value = false
                            fakeGenerator.value = 1
                            findNationalId()
                        }
                        "0019191945" -> {
                            visibilityListNationalId.value = false
                            fakeGenerator.value = 2
                            findNationalId()
                        }
                        else -> {
                            visibilityListNationalId.value = false
                            cantFindNationalId()
                        }
                    }

                } else {
                    visibilityLoading.value = false
                    colorRoundEditFiled.value = Color.Red
                }


            }
    }

    private fun cantFindNationalId() {
        viewModelScope.launch {
            delay(2000)
            colorRoundEditFiled.value = Color.Red
            visibilityLoading.value = false
            visibilityListNationalId.value = false
            colorButton.value = Color(0xFFE91E63)
            textButton.value = R.string.tryAgain
            showErrorMessage.value = true
        }
    }


    private fun findNationalId() {
        viewModelScope.launch {

            showErrorMessage.value = false
            delay(2000)
            visibilityLoading.value = false
            visibilityListNationalId.value = true
            colorButton.value = Color(0xff11BC89)
            textButton.value = R.string.next


        }
    }


    private fun enableAnimationView() {
        viewModelScope.launch {
            delay(400)
            animationStateTextNationalId.value = true
            animationStateEditTextNationalId.value = true
        }
    }

    private fun nationalCodeVerification(nationalCard: String): Pair<Boolean, String> {
        if (nationalCard.trim().isEmpty()) return Pair(false, "لطفا کد ملی مورد نظر را وارد کنید")
        if (!(nationalCard!!.matches(Regex("(^\\S*\$)")))) return Pair(
            false,
            "بین اعداد فاصله قرار گرفته هست"
        )
        if (nationalCard.length < 10) return Pair(false, "لطفا کد ملی را درست وارد کنید")
        return Pair(true, "")
    }


}