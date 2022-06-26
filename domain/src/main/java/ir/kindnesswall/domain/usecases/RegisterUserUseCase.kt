package ir.kindnesswall.domain.usecases

import ir.kindnesswall.domain.repositories.AuthenticationRepository

class RegisterUserUseCase(private val authenticationRepository: AuthenticationRepository) {

  suspend operator fun invoke(phoneNumber: String) {
    authenticationRepository.registerUser(phoneNumber)
  }
}