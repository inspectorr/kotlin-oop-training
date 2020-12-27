class ShapeAccumulator {
    val shapes: MutableList<Shape> = mutableListOf()

    fun add(shape: Shape) {
        shapes.add(shape)
    }

    fun addAll(shapes: Collection<Shape>) {
        shapes.forEach { this.add(it) }
    }

    val maxAreaShape: Shape?
        get() = shapes.maxBy { el -> el.area }

    val minAreaShape: Shape?
        get() = shapes.minBy { el -> el.area }

    val maxPerimeterShape: Shape?
        get() = shapes.maxBy { el -> el.perimeter }

    val minPerimeterShape: Shape?
        get() = shapes.minBy { el -> el.perimeter }

    val totalArea: Double
        get() = shapes.sumByDouble { el -> el.area }

    val totalPerimeter: Double
        get() = shapes.sumByDouble { el -> el.perimeter }

    override fun toString(): String {
        return shapes.toString()
    }
}
