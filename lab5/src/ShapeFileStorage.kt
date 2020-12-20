import com.google.gson.*
import java.io.File

class ShapeFileStorage() {
    private val builder = GsonBuilder()
    private val gson: Gson
    init {
        builder.registerTypeAdapter(Shape::class.java, GsonInterfaceAdapter<Shape>())
        gson = builder.create()
    }

    fun save(shapeAccumulator: ShapeAccumulator, filePath: String) {
        val json = gson.toJson(shapeAccumulator)
        val file = File(filePath)
        file.writeText(json);
    }

    fun open(filePath: String): ShapeAccumulator {
        val file = File(filePath)
        val json = file.readText()
        return gson.fromJson(json, ShapeAccumulator::class.java)
    }
}