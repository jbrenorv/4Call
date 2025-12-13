package com.jbrenorv.acall.core.common.exception

class UserNotFoundException(userId: String) : Exception("User $userId not found")
