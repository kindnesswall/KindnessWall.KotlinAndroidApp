package ir.kindnesswall.view.main.more.hami

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.model.hami.HamiModel
import ir.kindnesswall.data.repository.HamiRepo

class HamiViewModel(private val hamiRepo: HamiRepo) : ViewModel() {

    fun getHamiLiveData(): LiveData<CustomResult<List<HamiModel>>> {
        return hamiRepo.getHamiList(viewModelScope)
    }
}