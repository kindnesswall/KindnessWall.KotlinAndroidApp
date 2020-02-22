package com.farshidabz.kindnesswall.data.model

import java.io.Serializable

class FilterModel : Serializable {
    var categoryModel: ArrayList<CategoryModel>? = null
    var regionModel: RegionModel? = null
    var city: CityModel? = null
    var provinceId: Int = 0
}