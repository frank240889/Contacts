package contacts

data class Organization(
    override var name: String = "",
    var address: String = "",
    override var phone: String = ""
): Contact(name, phone) {
    override fun properties(): String {
        return "(name, address, phone)"
    }

    override fun setProperty(property: String, value: String) {
        when (property) {
            "name" -> {
                name = value
            }
            "address" -> {
                address = value
            }
            "phone" -> {
                phone = value
            }
        }
    }

    override fun getContactType() = this::javaClass.name

    override fun toString(): String {
        return "$name $name, $address, $phone, ${super.toString()}"
    }
}