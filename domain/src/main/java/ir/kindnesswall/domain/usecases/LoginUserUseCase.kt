package ir.kindnesswall.domain.usecases

import ir.kindnesswall.domain.repositories.AuthenticationRepository

class LoginUserUseCase(private val authenticationRepository: AuthenticationRepository) {

  suspend operator fun invoke(phoneNumber: String, verificationCode: String) {
    authenticationRepository.loginUser(phoneNumber, verificationCode)
  }
}