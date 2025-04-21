package org.example.exptions

// Custom exception for data source errors
class DataSourceException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)