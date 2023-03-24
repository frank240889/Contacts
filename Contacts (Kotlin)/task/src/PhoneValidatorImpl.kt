package contacts

class PhoneValidatorImpl: PhoneValidator {
    override val patternPhone: Regex
        get() = """\+?((\([a-zA-Z0-9]+\))|([a-zA-Z0-9]+))?(\s|-)?((\([a-zA-Z0-9]{2}\))|([a-zA-Z0-9]{2,}))?((\s|-)?(\(?[a-zA-Z0-9]{2,}\)?))+""".toRegex()

    override fun isPhoneValid(phone: String): Boolean {
        if (phone.length == 1 && phone.first().isLetterOrDigit())
            return true

        val parenthesis = """.\((?:\S*\s+\S*)\)""".toRegex()
        val validOpenedParenthesis = phone.count { '(' == it } <= 1
        val validClosedParenthesis = phone.count { ')'== it } <= 1
        return patternPhone.matches(phone) && !parenthesis.matches(phone) && validClosedParenthesis && validOpenedParenthesis
    }
}