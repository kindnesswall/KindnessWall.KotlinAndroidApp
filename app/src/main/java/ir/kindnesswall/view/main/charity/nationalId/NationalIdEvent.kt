package ir.kindnesswall.view.main.charity.nationalId

sealed class NationalIdEvent
object AnimationNationalId:NationalIdEvent()
data class EnterNationalId(var number:String):NationalIdEvent()
object ResultOfSearch:NationalIdEvent()