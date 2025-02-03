package com.arrivo.utilities

class Settings {

    companion object {

        // GLOBAL EXCEPTION HANDLER
        const val ERROR_INVALID_VALUE_MESSAGE = "Invalid value"
        const val ERROR_ID_NOT_FOUND_MESSAGE = "Id not found"
        const val ERROR_DATA_CONFLICT_MESSAGE = "Data conflict"
        const val ERROR_DATA_CORRUPTED_MESSAGE = "Data corrupted"
        const val ERROR_OPERATION_UNSUPPORTED_MESSAGE = "Operation unsupported"
        const val ERROR_DELIVERY_NOT_EDITABLE_MESSAGE = "Unable to edit delivery"
        const val ERROR_DATA_ACCESS_MESSAGE = "Data access exception"
        const val ERROR_UNEXPECTED_EXCEPTION_MESSAGE = "Unexpected exception"
        const val ERROR_PHONE_NUMBER_EXISTS_MESSAGE = "Phone number already exists"
        const val ERROR_EMAIL_ALREADY_IN_USE_MESSAGE = "Email already in use"
        const val ERROR_NO_EMAIL_ASSOCIATED = "User does not have an email associated"
        const val ERROR_TASK_TITLE_ALREADY_EXISTS_MESSAGE = "Title already exists"
        const val ERROR_INVALID_REQUEST_FORMAT_MESSAGE = "Invalid request format"

        // DELIVERY
        const val DELIVERY_EMP_ALREADY_ASSIGNED_MESSAGE =
            "The employee is already assigned to some delivery at this time"
        const val UNABLE_TO_EDIT_DELIVERY_TASKS_MESSAGE = "Unable to edit tasks during delivery in progress"
        const val UNABLE_TO_EDIT_DELIVERY_EMPLOYEE_MESSAGE = "Unable to edit delivery date during delivery in progress"
        const val DELIVERY_ALREADY_COMPLETED_MESSAGE = "Delivery already completed!"
        const val DELIVERY_NOT_IN_PROGRESS_MESSAGE = "Delivery is not in progress"
        const val DELIVERY_BREAK_ALREADY_USED = "Break has been already used!"

        const val OPTIMIZATION_VEHICLE_LABEL = "Vehicle"
        const val OPTIMIZATION_TASK_LABEL_PREFIX = "Task-"
        const val TIME_EXCEED_BORDER = 480 // 8h
        const val OPTIMIZATION_TIME_DEFAULT_START = 8 // 8 AM
        const val OPTIMIZATION_AVAILABLE_TIME_HOURS = 8
        const val OPTIMIZATION_TIMEOUT_SECONDS: Long = 9L
        const val OPTIMIZATION_CAR_COST_PER_HOUR: Double = 50.0
        const val OPTIMIZATION_CAR_COST_PER_KILOMETER: Double = 0.5

        // TASKS
        const val NO_DELIVERY_ASSIGNED = "No delivery has been assigned to this task"
        const val TASK_ALREADY_COMPLETED_MESSAGE = "Task already completed!"

        // SECURITY
        const val INVALID_TOKEN_MESSAGE = "Invalid token"
        const val USER_NOT_FOUND_MESSAGE = "User not found"
    }

}