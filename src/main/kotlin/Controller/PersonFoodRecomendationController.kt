package Controller

import DbHelper
import Entities.Persons.PersonFoodRecomendation
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class PersonFoodRecomendationController {
    @RequestMapping("/getPersonFoodRecomendation/{personId}", method = [RequestMethod.GET])
    fun getPersonFoodRecomendation(@PathVariable personId: String): PersonFoodRecomendation
    {
        val personFoodRecomendation = DbHelper().getPersonFoodRecomendationByPersonId(personId)
        if (personFoodRecomendation == null) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found")
        }

        return personFoodRecomendation
    }
}