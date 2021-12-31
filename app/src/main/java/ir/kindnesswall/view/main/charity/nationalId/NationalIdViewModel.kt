package ir.kindnesswall.view.main.charity.nationalId

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NationalIdViewModel  :ViewModel(){
    var animationStateTextNationalId = mutableStateOf(false)
    var animationStateEditTextNationalId = mutableStateOf(false)
    var valueOfNationalId= mutableStateOf("")
    init {
        handleEvent(AnimationNationalId)
    }



    fun handleEvent(event: NationalIdEvent){
        when(event){
            AnimationNationalId -> {
                enableAnimationView()
            }
        }

    }










    private fun enableAnimationView(){
        viewModelScope.launch {
            delay(400)
            animationStateTextNationalId.value=true
            animationStateEditTextNationalId.value=true
        }
    }


}