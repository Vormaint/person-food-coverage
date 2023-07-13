package Entities.Persons

import java.sql.ResultSet
import java.util.*

data class Person(val id: String = UUID.randomUUID().toString(),
                  val name: String,
                  val lastName: String,
                  val growth: Float,
                  val weight: Float,
                  val sex: Sex,
                  val age: Int,
                  val activityLevel: ActivityLevel)
{
    fun calculateCalories(): Int {
        val activityFactor = when (activityLevel) {
            ActivityLevel.MINIMUM -> 1.2
            ActivityLevel.LOW -> 1.375
            ActivityLevel.MEDIUM -> 1.55
            ActivityLevel.HIGH -> 1.725
            ActivityLevel.VERY_HIGH -> 1.9
        }

        val sexFactor = when (sex) {
            Sex.MALE -> 5
            Sex.FEMALE -> -141
        }
        return ((9.99 * weight + 6.25 * growth - 4.92 * age + sexFactor) * activityFactor).toInt()
    }

    fun performRequestToOpenAi() : String
    {
        val sex = when (sex) {
            Sex.MALE -> "man"
            Sex.FEMALE -> "women"
        }

        val activityLevel = when (activityLevel) {
            ActivityLevel.MINIMUM -> "minimun"
            ActivityLevel.LOW -> "low"
            ActivityLevel.MEDIUM -> "medium"
            ActivityLevel.HIGH -> "high"
            ActivityLevel.VERY_HIGH -> "very high"
        }

        val calculatedCalories = calculateCalories()

        return "Make a daily meal plan with a complete macronutrient profile, taking into account that the $sex has $activityLevel mobility and needs $calculatedCalories calories. " +
                "Provide a detailed example of meals with measurements and recipes for the dishes used."
    }

    companion object {
        fun resultSetToPerson(personResult: ResultSet) : Person {
            val id = personResult.getString("Id")
            val name = personResult.getString("Name")
            val lastName = personResult.getString("LastName")
            val growth = personResult.getFloat("Growth")
            val weight = personResult.getFloat("Weight")
            val sex = Sex.valueOf(personResult.getString("Sex"))
            val age = personResult.getInt("Age")
            val activityLevel = ActivityLevel.valueOf(personResult.getString("ActivityLevel"))

            return Person(id, name, lastName, growth, weight, sex, age, activityLevel)
        }
    }
}

enum class ActivityLevel {
    MINIMUM,
    LOW,
    MEDIUM,
    HIGH,
    VERY_HIGH
}

enum class Sex
{
    MALE,
    FEMALE
}
