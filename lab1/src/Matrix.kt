import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

class Matrix(inputElements: Array<Array<Float>>) {
    private val elements = inputElements.copyOf().map { arr -> arr.copyOf() }

    init {
        validate()
    }

    constructor(width: Int, height: Int): this(Array(height) { Array(width) { 0f } })

    private fun validate() {
        if (elements.isEmpty()) return

        elements.forEach {
            if (it.size != width) {
                throw IllegalStateException()
            }
        }
    }

    operator fun get(i: Int, j: Int): Float {
        return elements[i][j]
    }

    operator fun set(i: Int, j: Int, element: Float) {
        elements[i][j] = element
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

    private inline fun iterate(action: (i: Int, j: Int, element: Float) -> Unit) {
        elements.forEachIndexed { i, floats ->
            floats.forEachIndexed { j, element ->
                action(i, j, element);
            }
        }
    }

    private inline fun iterateAssign(action: (i: Int, j: Int, element: Float) -> Float) {
        iterate { i, j, element -> this[i, j] = action(i, j, element) }
    }

    private inline fun generateAssign(action: (i: Int, j: Int) -> Float): Matrix {
        val result = Matrix(width, height)
        result.iterateAssign { i, j, _ -> action(i, j) }
        return result
    }

    private fun requireSameDimensions(matrix: Matrix) {
        if (matrix.height != height || matrix.width != width) {
            throw IllegalArgumentException()
        }
    }

    private fun requireSameOppositeDimensions(matrix: Matrix) {
        if (width != matrix.height || height != matrix.width) {
            throw IllegalArgumentException()
        }
    }

    private fun requireSquare() {
        if (width != height) {
            throw IllegalStateException()
        }
    }

    operator fun plus(matrix: Matrix): Matrix {
        requireSameDimensions(matrix)
        return generateAssign { i, j -> this[i, j] + matrix[i, j] }
    }

    operator fun minus(matrix: Matrix): Matrix {
        requireSameDimensions(matrix)
        return generateAssign { i, j -> this[i, j] - matrix[i, j] }
    }

    operator fun times(number: Float): Matrix {
        return generateAssign { i, j -> this[i, j] * number }
    }

    operator fun times(matrix: Matrix): Matrix {
        requireSameOppositeDimensions(matrix)
        return generateAssign { i, j -> this[i, j] + matrix[j, i] }
    }

    val determinator: Float
        get() {
            requireSquare()

            val l = Matrix(width, height)
            val u = Matrix(width, height)

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

    override operator fun equals(other: Any?): Boolean {
        if (other !is Matrix) return false
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
