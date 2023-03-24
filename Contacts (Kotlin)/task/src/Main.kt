package contacts

private val PHONE_BOOK_NAME = "contacts.db"
fun main() {
    val contactApp = ContactApp()
    contactApp.loadBooks(PHONE_BOOK_NAME) {
        contactApp.gotoMainMenu()
    }
}
