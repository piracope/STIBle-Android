package g58089.mobg5.stible.model

enum class ErrorType(val guiltyField: ErrorGuilty) {
    BAD_EMAIL_FORMAT(ErrorGuilty.EMAIL),
    NO_PASSWORD(ErrorGuilty.PASSWORD),
    BAD_CREDENTIALS(ErrorGuilty.BOTH),
    NO_INTERNET(ErrorGuilty.NONE)

}