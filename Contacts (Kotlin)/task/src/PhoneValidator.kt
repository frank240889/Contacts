package contacts

interface PhoneValidator {
    val patternPhone: Regex
    fun isPhoneValid(phone: String): Boolean
}