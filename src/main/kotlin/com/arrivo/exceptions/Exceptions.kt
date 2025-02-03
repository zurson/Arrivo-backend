package com.arrivo.exceptions

class IdNotFoundException(message: String) : RuntimeException(message)

class DataConflictException(message: String) : RuntimeException(message)

class DeliveryException(message: String) : RuntimeException(message)

class DataCorruptedException(message: String) : RuntimeException(message)

class CompanyException(message: String): RuntimeException(message)