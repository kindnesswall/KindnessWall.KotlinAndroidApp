package ir.kindnesswall.domain.entities

import java.io.Serializable

class CategoryModel : Serializable {
  var title: String? = null
  var id: Int = 0

  var isSelected: Boolean = false
}