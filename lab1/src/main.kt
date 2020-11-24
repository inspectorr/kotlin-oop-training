fun main() {
    val matrix1 = Matrix(arrayOf(
            arrayOf(1f, 2f, 3f),
            arrayOf(4f, 5f, 6f),
            arrayOf(7f, 8f, 9f)
    ))

    val matrix2 = Matrix(arrayOf(
            arrayOf(9f, 8f, 7f),
            arrayOf(6f, 5f, 4f),
            arrayOf(3f, 2f, 1f)
    ))

    println(matrix1)
    println(matrix2)
    println(matrix1.width)
    println(matrix1.height)
    println(matrix1 + matrix2)
    println(matrix1 - matrix2)
    println(matrix1 * 100f)
    println(matrix1 * matrix2)
    println(matrix1 == matrix2)
    println(matrix1.determinator)
}