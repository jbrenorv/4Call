package com.jbrenorv.acall.core.data.exception

class UserNotFoundException(userId: String) : Exception("User $userId not found")
