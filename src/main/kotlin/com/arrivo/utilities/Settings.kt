package com.arrivo.utilities

class Settings {

    companion object{

        const val ERROR_INVALID_VALUE_MESSAGE = "Invalid value"
        const val ERROR_ID_NOT_FOUND_MESSAGE = "Id not found"
        const val ERROR_DATA_CONFLICT_MESSAGE = "Data conflict"
        const val ERROR_UNEXPECTED_EXCEPTION_MESSAGE = "Unexpected exception"
        const val ERROR_PHONE_NUMBER_EXISTS_MESSAGE = "Phone number already exists"
        const val ERROR_EMAIL_ALREADY_IN_USE_MESSAGE = "Email already in use"
        const val ERROR_NO_EMAIL_ASSOCIATED = "User does not have an email associated"
        const val ERROR_TASK_TITLE_ALREADY_EXISTS_MESSAGE = "Title already exists"
        const val ERROR_INVALID_REQUEST_FORMAT_MESSAGE = "Invalid request format"

        const val DELIVERY_EMP_ALREADY_ASSIGNED_MESSAGE = "The employee is already assigned to some delivery at this time"

        const val OPTIMIZATION_VEHICLE_LABEL = "Vehicle"
        const val OPTIMIZATION_TASK_LABEL_PREFIX = "Task-"
        const val TIME_EXCEED_BORDER = 480 // 8h
        const val OPTIMIZATION_TIME_DEFAULT_START = 8 // 8 AM
        const val OPTIMIZATION_AVAILABLE_TIME_HOURS = 8
    }

}