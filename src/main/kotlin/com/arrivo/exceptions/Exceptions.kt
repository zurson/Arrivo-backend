package com.arrivo.exceptions

class IdNotFoundException(message: String) : RuntimeException(message)

class DataConflictException(message: String) : RuntimeException(message)

class DeliveryNotEditableException(message: String) : RuntimeException(message)

class DataCorruptedException(message: String) : RuntimeException(message)