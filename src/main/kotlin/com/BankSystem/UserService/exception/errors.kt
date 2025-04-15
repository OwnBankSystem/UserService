package com.BankSystem.UserService.exception

class UserAlreadyExistException(msg: String?): RuntimeException(msg)

class UserNotFoundException(msg: String?): RuntimeException(msg)