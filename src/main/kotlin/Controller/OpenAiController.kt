package Controller

import DbHelper
import com.cjcrafter.openai.OpenAI
import com.cjcrafter.openai.chat.ChatMessage.Companion.toSystemMessage
import com.cjcrafter.openai.chat.ChatMessage.Companion.toUserMessage
import com.cjcrafter.openai.chat.ChatRequest
import io.github.cdimascio.dotenv.dotenv
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.function.Consumer

@RestController
class OpenAiController {
    @RequestMapping("/getOpenAiRequest/{personId}", method = [RequestMethod.GET])
    fun getOpenAiRequest(@PathVariable personId: String): String
    {
        val person = DbHelper().getPersonById(personId)
        if (person == null) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found")
        }

        return person.performRequestToOpenAi()
    }

    @RequestMapping("/getResponceOpenAi/{personId}", method = [RequestMethod.GET])
    fun getOpenAiResponse(@PathVariable personId: String): String
    {
        val person = DbHelper().getPersonById(personId)
        if (person == null) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found")
        }

        val prompt = "Be as unhelpful as possible"
        val messages = mutableListOf(prompt.toSystemMessage())
        val request = ChatRequest(model="gpt-3.5-turbo", messages=messages)

        // Loads the API key from the .env file in the root directory.
        val key = dotenv()["OPENAI_TOKEN"]
        val openai = OpenAI(key)

        // Generate a response, and print it to the user.
        var finalResponse : String = "Something went wrong"
        messages.add(person.performRequestToOpenAi().toUserMessage())
        openai.streamChatCompletion(request, Consumer { response ->
            if (response.choices[0].finishReason != null)
            {
                finalResponse = response.choices[0].message.content.toString()
            }
        })

        return finalResponse
    }
}