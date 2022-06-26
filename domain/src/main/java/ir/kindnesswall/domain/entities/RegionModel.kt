package ir.kindnesswall.domain.entities

class RegionModel : BaseModel() {
  var province_id: Int = 0
  var city_id: Int = 0
  var latitude: Double = 0.0
  var longitude: Double = 0.0
  var id: Int = 0
  var name: String? = null
}