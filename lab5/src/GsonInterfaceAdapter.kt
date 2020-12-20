import com.google.gson.*
import java.lang.reflect.Type

class GsonInterfaceAdapter<T> : JsonSerializer<T>, JsonDeserializer<T> {
    companion object {
        const val CLASSNAME = "CLASSNAME"
        const val DATA = "DATA"
    }

    override fun serialize(src: T, type: Type, context: JsonSerializationContext): JsonElement {
        return JsonObject().apply {
            addProperty(CLASSNAME, (src as Any)::class.java.name)
            add(DATA, context.serialize(src))
        }
    }

    override fun deserialize(json: JsonElement, type: Type, context: JsonDeserializationContext): T {
        val jsonObject = json.asJsonObject
        val className = jsonObject.get(CLASSNAME).asString
        val classByName = Class.forName(className)
        val data = jsonObject.get(DATA)
        return context.deserialize(data, classByName)
    }
}