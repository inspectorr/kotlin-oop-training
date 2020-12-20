fun main() {
    val shapes = ShapeAccumulator()

    shapes.addAll(listOf(
        Circle(2.0),
        Rect(20.0, 10.0),
        Square(100.0),
        Triangle(10.0, 10.0, 10.0)
    ))

    val shapeStore = ShapeFileStorage()
    shapeStore.save(shapes, "temp.json")
    val restoredShapes = shapeStore.open("temp.json")
    println(restoredShapes)
}