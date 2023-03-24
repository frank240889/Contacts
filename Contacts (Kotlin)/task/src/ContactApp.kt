package contacts

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.File
import kotlin.system.exitProcess

class ContactApp {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val personAdapter = moshi.adapter(Person::class.java)
    private val organizationAdapter = moshi.adapter(Organization::class.java)
    //private val contactAdapter = moshi.adapter(Contact::class.java)
    private val type = Types.newParameterizedType(List::class.java, Contact::class.java)
    //private val contactsListAdapter = moshi.adapter<List<Contact?>>(type)

    private lateinit var contactList: ContactList

    fun loadBooks(filename: String, onLoadFile: () -> Unit) {
        contactList = getData(filename)
        println("open $filename")
        onLoadFile.invoke()
    }

    private fun getData(filename: String): ContactList {
        return ContactList(
            mutableListOf(),
            PhoneValidatorImpl()
        )
    }

    fun gotoMainMenu() {
        println("Enter action (add, list, search, count, exit):")
        val option = readln()
        handleAction(option)
    }

    private fun handleAction(action: String) {
        when(action) {
            "add" -> {
                gotoAddContactMenu()
                println()
            }
            "list" -> {
                gotoListContactsMenu(contactList)
                println()
            }
            "search" -> {
                gotoSearchMenu()
                println()
            }
            "count" -> {
                gotoCountContactsMenu()
                println()
            }
            "exit" -> {
                exitApp()
            }
        }
    }

    private fun gotoAddContactMenu() {
        println()
        println("Enter the type (person, organization):")
        val addOption = readln()
        val contact = if (addOption == "person") {
            addPerson()
        } else {
            addOrganization()
        }
        println()
        showInfoContact(contact)
        println()
        println("The record added.")
        gotoMainMenu()
    }

    private fun gotoListContactsMenu(contactList: List<Contact?>) {
        listContacts(contactList)
        println("[list] Enter action ([number], back):")
        val action = readln()
        if (action == "back") {
            gotoMainMenu()
        } else {
            getContactAndGoOr(
                action,
                { gotoRecordMenu(it) },
                { gotoListContactsMenu(contactList) }
            )
        }
    }

    private fun gotoSearchMenu() {
        println("Enter search query:")
        val query = readln()
        val results = searchElements(query)
        println("Found ${results.size} results:")
        listContacts(results)
        println("[search] Enter action ([number], back, again):")
        when(val option = readln()) {
            "again" -> {
                gotoSearchMenu()
            }
            "back" -> {
                gotoMainMenu()
            }
            else -> {
                getContactAndGoOr(
                    option,
                    { gotoRecordMenu(it) },
                    { gotoSearchMenu() }
                )
            }
        }
    }

    private fun getContactAndGoOr(
        input: String,
        goto: (Contact) -> Unit,
        orGoto: () -> Unit
    ) {
        getContact(input)?.let {
            goto.invoke(it)
        } ?: orGoto.invoke()
    }

    private fun gotoCountContactsMenu() {
        println("The Phone Book has ${contactList.size} records.")
    }

    private fun gotoRecordMenu(contact: Contact) {
        showInfoContact(contact)
        println("[record] Enter action (edit, delete, menu):")
        when(readln()) {
            "edit" -> {
                gotoToEditMenu(contact)
                gotoRecordMenu(contact)
            }
            "delete" -> {
                gotoDeleteMenu(contact)
                gotoMainMenu()
            }
            else -> {
                gotoMainMenu()
            }
        }
    }

    private fun showInfoContact(input: String) {
        val contact = getContact(input)
        contact?.let {
            showInfoContact(it)
        }
    }

    private fun gotoToEditMenu(contact: Contact) {
        println("Select a field ${contact.properties()}:")
        editContact(contact)
    }

    private fun gotoDeleteMenu(contact: Contact) {
        contactList.remove(contact)
        println("Removed")
    }

    private fun searchElements(query: String): List<Contact?> {
        return contactList.filter {
            query in it.toString().lowercase()
        }
    }

    private fun addOrganization(): Contact {
        println("Enter the organization name:")
        val name = readln()
        println()
        println("Enter the address:")
        val address = readln()
        println()
        println("Enter the number:")
        val phone = readln()
        println()
        val contact = Organization(
            name,
            address,
            if (contactList.isPhoneValid(phone)) {
                phone
            } else {
                "[no number]"
            }
        )
        add(contact)
        return contact
    }

    private fun addPerson(): Contact {
        println("Enter the name:")
        val name = readln()
        println()
        println("Enter the surname:")
        val surname = readln()
        println()
        println("Enter the birth date:")
        val birthdate = readln()
        println()
        println("Enter the gender (M, F):")
        val gender = readln()
        println()
        println("Enter the number:")
        val phone = readln()
        val contact = Person(
            name,
            surname,
            if (contactList.isPhoneValid(phone)) {
                phone
            } else {
                "[no number]"
                   },
            birthdate,
            gender
        )
        add(contact)
        return contact
    }

    private fun add(contact: Contact) {
        contactList.add(contact)
    }

    private fun editContact(contact: Contact) {
        if (contact is Person) {
            editPerson(contact)
        } else {
            editOrganization(contact as Organization)
        }
        println("Saved")
    }

    private fun editPerson(person: Person){
        when(readln()) {
            "name" -> {
                println("Enter name:")
                person.name = readln()
            }
            "surname" -> {
                println("Enter surname:")
                person.surname = readln()
            }
            "birth" -> {
                println("Enter birth date:")
                person.birthdate = readln()
            }
            "gender" -> {
                println("Enter gender:")
                person.gender = readln()
            }
            "number" -> {
                println("Enter number:")
                val inputPhone = readln()
                person.phone = if (contactList.isPhoneValid(inputPhone)) {
                    inputPhone
                } else {
                    println("Wrong number format!")
                    "[no number]"
                }
            }
        }
    }

    private fun editOrganization(organization: Organization) {
        when(readln()) {
            "address" -> {
                println("Enter address:")
                organization.address = readln()
            }
            "number" -> {
                println("Enter the number:")
                val inputPhone = readln()
                organization.phone = if (contactList.isPhoneValid(inputPhone)) {
                    inputPhone
                } else {
                    println("Wrong number format!")
                    "[no number]"
                }
            }
        }
    }

    private fun listContacts(contactList: List<Contact?>) {
        contactList.forEachIndexed { index, contact ->
            println("${index + 1}. ${contact?.toString()}")
        }
    }

    private fun getContact(index: String): Contact? {
        val pos: Int? = try {
            index.toInt()
        } catch (e: NumberFormatException) {
            println("No records")
            null
        }
        return if (pos == null) {
            println("No records")
            null
        } else {
            contactList.elementAt(pos - 1)
        }
    }

    private fun showInfoContact(contact: Contact) {
        if (contact is Person) {
            infoPerson(contact)
        } else {
            infoOrganization(contact as Organization)
        }
    }

    private fun infoPerson(person: Person) {
        println("Name: ${person.name}")
        println("Surname: ${person.surname}")
        println("Birth date: ${person.birthdate}")
        println("Gender: ${person.gender}")
        println("Number: ${person.phone}")
        infoDate(person)
    }

    private fun infoOrganization(organization: Organization) {
        println("Organization name: ${organization.name}")
        println("Address: ${organization.address}")
        println("Number: ${organization.phone}")
        infoDate(organization)
    }

    private fun infoDate(contact: Contact) {
        println("Time created: ${contact.createdAt}")
        println("Time last edit:${contact.modifiedAt}")
    }

    private fun exitApp() {
        exitProcess(0)
    }
}