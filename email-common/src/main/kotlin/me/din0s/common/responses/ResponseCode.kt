package me.din0s.common.responses

enum class ResponseCode(val code: Int) {
    // SUCCESS
    SUCCESS(200),

    // REQUEST ERRORS
    BAD_REQUEST(400),

    // AUTHENTICATION ERRORS
    MAIL_EXISTS(600),
    AUTH_FAIL(601),
    NO_AUTH(602),

    // USER ERRORS,
    INVALID_RECIPIENT(700),
    INVALID_EMAIL_ID(701),
}
