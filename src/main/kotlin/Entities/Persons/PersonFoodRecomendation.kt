package Entities.Persons

import java.sql.ResultSet

data class PersonFoodRecomendation (
    val caloriesNorm: Int,
    val personId: String
)
{
    companion object {
        fun resultSetToPersonFoodRecomendation(personFoodRecomendation: ResultSet) : PersonFoodRecomendation {
            val caloriesNorm = personFoodRecomendation.getInt("СaloriesNorm")
            val personId = personFoodRecomendation.getString("PersonId")

            return PersonFoodRecomendation(caloriesNorm, personId)
        }
    }
}