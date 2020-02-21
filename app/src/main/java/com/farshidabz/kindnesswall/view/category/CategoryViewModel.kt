package com.farshidabz.kindnesswall.view.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farshidabz.kindnesswall.data.model.CategoryModel
import com.farshidabz.kindnesswall.data.model.CustomResult
import com.farshidabz.kindnesswall.data.repository.GeneralRepo

class CategoryViewModel(private val generalRepo: GeneralRepo) : ViewModel() {
    var catgories: ArrayList<CategoryModel> = arrayListOf()
    var prvSelectedCategories: ArrayList<CategoryModel>? = arrayListOf()

    fun getCategories(): LiveData<CustomResult<List<CategoryModel>>> {
        return generalRepo.getAllCatgories(viewModelScope)
    }
}