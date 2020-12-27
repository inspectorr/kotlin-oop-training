package ru.etu.inspectorr.lab6.model

import Shape
import Circle
import Rect
import Square
import Triangle
import javafx.beans.property.SimpleDoubleProperty
import tornadofx.ItemViewModel

interface ShapeCreateStrategy<ShapeData> {
    fun create(model: ItemViewModel<ShapeData>): Shape
}

class CircleData {
    val radius = SimpleDoubleProperty()
}

class CircleModel : ItemViewModel<CircleData>(CircleData()) {
    val radius = bind(CircleData::radius)
}

object CircleCreateStrategy : ShapeCreateStrategy<CircleData> {
    override fun create(model: ItemViewModel<CircleData>): Circle {
        return Circle(model.item.radius.value)
    }
}

class RectData {
    val width = SimpleDoubleProperty()
    val height = SimpleDoubleProperty()
}

class RectModel : ItemViewModel<RectData>(RectData()) {
    val width = bind(RectData::width)
    val height = bind(RectData::height)
}

object RectCreateStrategy : ShapeCreateStrategy<RectData> {
    override fun create(model: ItemViewModel<RectData>): Shape {
        model.item.apply {
            return Rect(width.value, height.value)
        }
    }
}

class SquareData {
    val size = SimpleDoubleProperty()
}

class SquareModel : ItemViewModel<SquareData>(SquareData()) {
    val size = bind(SquareData::size)
}

object SquareCreateStrategy : ShapeCreateStrategy<SquareData> {
    override fun create(model: ItemViewModel<SquareData>): Shape {
        return Square(model.item.size.value)
    }
}

class TriangleData {
    val a = SimpleDoubleProperty()
    val b = SimpleDoubleProperty()
    val c = SimpleDoubleProperty()
}

class TriangleModel : ItemViewModel<TriangleData>(TriangleData()) {
    val a = bind(TriangleData::a)
    val b = bind(TriangleData::b)
    val c = bind(TriangleData::c)
}

object TriangleCreateStrategy : ShapeCreateStrategy<TriangleData> {
    override fun create(model: ItemViewModel<TriangleData>): Shape {
        model.item.apply {
            return Triangle(a.value, b.value, c.value)
        }
    }
}
