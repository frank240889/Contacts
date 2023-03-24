package contacts

import java.util.Date

abstract class Contact(
    open var name: String,
    open var phone: String,
    open var createdAt: String = Date().toString(),
    open var modifiedAt: String = createdAt,
) {
    abstract fun properties(): String
    abstract fun setProperty(property: String, value: String)
    abstract fun getContactType(): String
}
