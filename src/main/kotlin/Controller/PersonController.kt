import Entities.Persons.Person
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException


@RestController
class PersonController{
    @RequestMapping("/getPerson/{personId}", method = [RequestMethod.GET])
    fun getPerson(@PathVariable personId: String): Person
    {
        val person = DbHelper().getPersonById(personId)
        if (person == null) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found")
        }

        return person
    }

    @RequestMapping("/createPerson", method = [RequestMethod.POST])
    fun createPerson(@RequestBody person: Person) : String
    {
        if (person == null) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Person body is empty")
        }
        if(person.name.isEmpty())
        {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is empty")
        }
        if(person.lastName.isEmpty())
        {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Last name is empty")
        }
        if(person.growth <= 30 || person.growth > 270)
        {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "growth is unreal")
        }
        if(person.weight <= 10 )
        {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "weight is unreal")
        }
        if(person.age <= 12)
        {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "too young to use app")
        }

        val isPersonInsert = DbHelper().insertPerson(person)
        if (!isPersonInsert) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot insert person into db")
        }
        val isPersonFoodRecomendationInsert = DbHelper().insertPersonFoodRecomendation(person)
        if (!isPersonFoodRecomendationInsert) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot insert person food recomendation into db")
        }

        return person.id
    }
}