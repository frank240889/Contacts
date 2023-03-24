package contacts

class Person(
    override var name: String = "",
    var surname: String = "",
    override var phone: String = "",
    birthdate: String,
    gender: String
): Contact(name, phone) {
    var birthdate = birthdate
        set(value) {
            field = if (value.isEmpty()) {
                //println("Bad birth date!")
                "[no data]"
            } else {
                value
            }
        }

    var gender = birthdate
        set(value) {
            field = if (value.isEmpty()) {
                //println("Bad gender!")
                "[no data]"
            } else {
                value
            }
        }

    init {
        this.birthdate = birthdate
        this.gender = gender
    }
    fun hasNumber() = phone.isNotEmpty()

    override fun properties(): String {
        return "(name, surname, phone, birthdate, gender)"
    }

    override fun setProperty(property: String, value: String) {
        when (property) {
            "name" -> {
                name = value
            }
            "surname" -> {
                surname = value
            }
            "phone" -> {
                phone = value
            }
            "birthdate" -> {
                birthdate = value
            }
            "gender" -> {
                gender = value
            }
        }
    }

    override fun getContactType() = this::javaClass.name

    override fun toString(): String {
        return "$name $surname, $phone, $birthdate, $gender ${super.toString()}"
    }
}