package ir.kindnesswall.view.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.data.model.CategoryModel
import ir.kindnesswall.data.repository.GeneralRepo
import ir.kindnesswall.domain.common.CustomResult

class CategoryViewModel(private val generalRepo: GeneralRepo) : ViewModel() {
    var multiSelection: Boolean = true

    var catgories: ArrayList<CategoryModel> = arrayListOf()
    var prvSelectedCategories: ArrayList<CategoryModel>? = arrayListOf()

    fun getCategories(): LiveData<CustomResult<List<CategoryModel>>> {
        return generalRepo.getAllCatgories(viewModelScope)
    }
}