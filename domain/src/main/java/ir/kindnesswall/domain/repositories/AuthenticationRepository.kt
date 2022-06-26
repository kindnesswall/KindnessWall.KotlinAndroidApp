package ir.kindnesswall.domain.repositories

interface AuthenticationRepository {

  suspend fun registerUser(phoneNumber: String)

  suspend fun loginUser(phoneNumber: String, verificationCode: String)
}