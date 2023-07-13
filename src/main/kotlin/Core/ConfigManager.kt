import org.json.JSONObject
import java.io.File

object ConfigManager {
    private val config: JSONObject

    init {
        val configFile = File("src/main/kotlin/config.json")
        val configContent = configFile.readText()
        config = JSONObject(configContent)
    }

    fun get(key: String): String {
        return config.getString(key)
    }

    fun getDatabaseUrl() : String{
        return get("databaseUrl")
    }

    fun getDatabaseUser() : String{
        return get("databaseUser")
    }

    fun getDatabasePassword() : String{
        return get("databasePassword")
    }

    fun getOpenAiToken() : String{
        return get("openAiToken")
    }
}
