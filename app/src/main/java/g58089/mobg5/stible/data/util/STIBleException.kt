package g58089.mobg5.stible.data.util

/**
 * Wrapper for exceptions that could occur at the data layer.
 */
class STIBleException(val errorType: ErrorType) : RuntimeException()