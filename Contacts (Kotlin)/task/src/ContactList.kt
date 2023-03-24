package contacts

class ContactList(
    mutableList: MutableList<Contact?>,
    phoneValidator: PhoneValidator
): MutableList<Contact?> by mutableList, PhoneValidator by phoneValidator