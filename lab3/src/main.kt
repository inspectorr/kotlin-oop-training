enum class PhoneType {
    MOBILE,
    HOME,
    WORK
}

class Phone(
        var number: String,
        var type: PhoneType
) {
    override fun toString(): String {
        return number
    }
}

class Contact(
        var firstName: String,
        var lastName: String,
        private val phones: MutableList<Phone> = mutableListOf()
) {
    override fun toString(): String {
        return "$firstName $lastName $phones"
    }

    fun containsSubstring(string: String): Boolean {
        return (
                firstName.contains(string) ||
                lastName.contains(string) ||
                phones.any { phone -> phone.number.contains(string) }
        )
    }
}

class PhoneBook(
        private val contacts: HashSet<Contact> = hashSetOf()
) {
    fun add(contact: Contact) {
        contacts.add(contact)
    }

    fun remove(contact: Contact) {
        contacts.remove(contact)
    }

    fun search(string: String): List<Contact> {
        val list = mutableListOf<Contact>()
        for (it in contacts) {
            if (it.containsSubstring(string)) {
                list.add(it)
            }
        }
        return list.toList()
    }

    override fun toString(): String {
        return contacts.joinToString("\n")
    }
}

fun main() {
    val book = PhoneBook()

    val contact1 = Contact(
            "Ivan",
            "Ivanov",
            mutableListOf(
                    Phone("+79991234567", PhoneType.MOBILE),
                    Phone("88129923232", PhoneType.WORK)
            )
    )

    val contact2 = Contact(
            "Mark",
            "Rumbo",
            mutableListOf(
                    Phone("+79991234567", PhoneType.MOBILE),
                    Phone("88129923232", PhoneType.WORK)
            )
    )

    book.add(contact1)
    book.add(contact2)

    println(book.search("799"))
    println(book.search("Ivan"))

    contact1.firstName = "Not Ivan"

    println(book)

    book.remove(contact1)

    println(book)

}