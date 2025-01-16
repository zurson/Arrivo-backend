package com.arrivo.exceptions

class IdNotFoundException(message: String) : RuntimeException(message)

class DataConflictException(message: String) : RuntimeException(message)