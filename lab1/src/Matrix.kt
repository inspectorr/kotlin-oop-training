import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

open class Matrix<T>(open var elements: Array<Array<T>>) {
    init {
        validate()
    }

    private fun validate() {
        if (elements.isEmpty()) return

        elements.forEach {
            if (it.size != width) {
                throw IllegalStateException()
            }
        }
    }

    val width: Int
        get() {
            if (elements.isEmpty()) return 0
            return elements[0].size
        }

    val height: Int
        get() {
            return elements.size
        }

    open operator fun get(i: Int, j: Int): T {
        return elements[i][j]
    }

    open operator fun set(i: Int, j: Int, element: T) {
        elements[i][j] = element
    }

    inline fun iterate(action: (i: Int, j: Int, element: T) -> Unit) {
        elements.forEachIndexed { i, elements ->
            elements.forEachIndexed { j, element ->
                action(i, j, element)
            }
        }
    }

    inline fun iterateAssign(action: (i: Int, j: Int, element: T) -> T) {
        iterate { i, j, element -> this[i, j] = action(i, j, element) }
    }

    fun requireSameDimensions(matrix: Matrix<T>) {
        if (matrix.height != height || matrix.width != width) {
            throw IllegalArgumentException()
        }
    }

    fun requireSameOppositeDimensions(matrix: Matrix<T>) {
        if (width != matrix.height || height != matrix.width) {
            throw IllegalArgumentException()
        }
    }

    fun requireSquare() {
        if (width != height) {
            throw IllegalStateException()
        }
    }
}

class FloatMatrix(elements: Array<Array<Float>>) : Matrix<Float>(elements.map { it.clone() }.toTypedArray()) {
    constructor(width: Int, height: Int): this(Array(height) { Array(width) { 0f } })

    private inline fun generateAssign(action: (i: Int, j: Int) -> Float): FloatMatrix {
        val result = FloatMatrix(width, height)
        result.iterateAssign { i, j, _ -> action(i, j) }
        return result
    }
    
    operator fun plus(floatMatrix: FloatMatrix): FloatMatrix {
        requireSameDimensions(floatMatrix)
        return generateAssign { i, j -> this[i, j] + floatMatrix[i, j] }
    }

    operator fun minus(floatMatrix: FloatMatrix): FloatMatrix {
        requireSameDimensions(floatMatrix)
        return generateAssign { i, j -> this[i, j] - floatMatrix[i, j] }
    }

    operator fun times(number: Float): FloatMatrix {
        return generateAssign { i, j -> this[i, j] * number }
    }

    operator fun times(floatMatrix: FloatMatrix): FloatMatrix {
        requireSameOppositeDimensions(floatMatrix)
        return generateAssign { i, j -> this[i, j] + floatMatrix[j, i] }
    }

    val determinator: Float
        get() {
            requireSquare()

            val l = FloatMatrix(width, height)
            val u = FloatMatrix(width, height)

            iterate { i, _, _ -> l[i, i] = 1f }

            iterate { i, j, element ->
                if (i <= j) {
                    u[i, j] = element - Array(i) { k -> l[i, k] * u[k, j] }.sum()
                } else {
                    l[i, j] = (element - Array(j) { k -> l[i, k] * u[k, j] }.sum()) / u[j, j]
                }
            }

            var detL = 1f
            var detU = 1f

            for (i in 0 until width) {
                detL *= l[i, i]
                detU *= u[i, i]
            }

            return detL * detU
        }

    override fun equals(other: Any?): Boolean {
        if (other !is FloatMatrix) return false
        return hashCode() == other.hashCode()
    }

    override fun hashCode(): Int {
        return elements.hashCode()
    }

    override fun toString(): String {
        var out = ""
        iterate { _, j, element ->
            out += element.toString() + if (j != width - 1) " " else "\n"
        }
        return out
    }
}
