
import Entities.Persons.Person
import Entities.Persons.PersonFoodRecomendation
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class DbHelper() {
    private var conn: Connection? = null

    init {
        try {
            connect()
        } catch (exception: SQLException) {
            throw exception
        }
    }

    @Throws(SQLException::class)
    fun connect() {
        conn = DriverManager.getConnection(ConfigManager.getDatabaseUrl(),
            ConfigManager.getDatabaseUser(),
            ConfigManager.getDatabasePassword())
    }

    @Throws(SQLException::class)
    fun close() {
        conn?.close()
    }

    fun getPersonById(personId: String): Person? {
        val statement = conn?.createStatement()
        val rs = statement?.executeQuery("SELECT * FROM Persons WHERE Id = '${personId}'")

        while (rs?.next() == true) {
            return Person.resultSetToPerson(rs)
        }
        return null
    }

    fun getPersonFoodRecomendationByPersonId(personId: String): PersonFoodRecomendation? {
        val statement = conn?.createStatement()
        val rs = statement?.executeQuery("SELECT * FROM PersonFoodRecomendations WHERE PersonId = '${personId}'")

        while (rs?.next() == true) {
            return PersonFoodRecomendation.resultSetToPersonFoodRecomendation(rs)
        }
        return null
    }

    fun insertPerson(person: Person): Boolean {
        val sql = "INSERT INTO Persons (Id, Name, LastName, Growth, Weight, Sex, Age, ActivityLevel) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
        val statement = conn?.prepareStatement(sql)
        statement?.setString(1, person.id)
        statement?.setString(2, person.name)
        statement?.setString(3, person.lastName)
        statement?.setFloat(4, person.growth)
        statement?.setFloat(5, person.weight)
        statement?.setString(6, person.sex.name)
        statement?.setInt(7, person.age)
        statement?.setString(8, person.activityLevel.name)
        val rowsAffected = statement?.executeUpdate() ?: 0
        return rowsAffected > 0
    }

    fun insertPersonFoodRecomendation(person: Person) : Boolean {
        val sql = "INSERT INTO PersonFoodRecomendations (СaloriesNorm, PersonId) VALUES (?, ?)"
        val statement = conn?.prepareStatement(sql)
        statement?.setInt(1, person.calculateCalories())
        statement?.setString(2, person.id)
        val rowsAffected = statement?.executeUpdate() ?: 0
        return rowsAffected > 0
    }
}
