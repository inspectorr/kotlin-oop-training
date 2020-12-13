import java.lang.IllegalStateException
import kotlin.math.pow
import kotlin.math.sqrt

interface Shape {
    val area: Double
    val perimeter: Double
}

class Circle(val radius: Double) : Shape {
    override val area: Double get() = Math.PI * radius.pow(2.0)
    override val perimeter: Double get() = Math.PI * 2 * radius
}

open class Rect(val width: Double, val height: Double) : Shape {
    override val area: Double get() = width * height
    override val perimeter: Double get() = 2 * (width + height)
}

class Square(size: Double) : Rect(size, size)

class Triangle(val a: Double, val b: Double, val c: Double) : Shape {
    init {
        validate()
    }

    private fun validate() {
        if (a + b <= c || b + c <= a || a + c <= b) {
            throw IllegalStateException()
        }
    }

    override val area: Double
        get() {
            val p = perimeter / 2
            return sqrt(p * (p - a) * (p - b) * (p - c))
        }

    override val perimeter: Double get() = a + b + c
}

fun main() {
    val list = listOf(
            Circle(30.0),
            Rect(20.0, 34.0),
            Square(25.0),
            Triangle(10.0, 10.0, 10.0)
    )

    println("Sum area: ${list.sumByDouble {el -> el.area}}")
    println("Max area: ${list.maxBy { el -> el.area }}")
    println("Min area: ${list.minBy { el -> el.area }}")
    println("Max perimeter: ${list.maxBy { el -> el.perimeter }}")
    println("Min perimeter: ${list.minBy { el -> el.perimeter }}")
}
