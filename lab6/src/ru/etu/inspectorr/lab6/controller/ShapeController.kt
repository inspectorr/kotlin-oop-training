package ru.etu.inspectorr.lab6.controller

import Shape
import ShapeAccumulator
import ShapeFileStorage
import ru.etu.inspectorr.lab6.model.*
import tornadofx.Controller
import tornadofx.asObservable
import java.util.*

class ShapeController : Controller() {
    private val fileStorage = ShapeFileStorage()
    val shapes = mutableListOf<Shape>().asObservable()

    init {
        openFromDefaultFile()
    }

    fun openFromDefaultFile() {
        try {
            val shapeAccumulator = fileStorage.open(DEFAULT_FILENAME)
            shapes.addAll(shapeAccumulator.shapes)
        } catch (e: Exception) {
            // if no file - do nothing, create on close
        }
    }

    fun saveToDefaultFile() {
        val shapeAccumulator = ShapeAccumulator()
        shapeAccumulator.addAll(shapes)
        fileStorage.save(shapeAccumulator, DEFAULT_FILENAME)
    }

    fun moveUp(i: Int) {
        if (i > 0) {
            Collections.swap(shapes, i, i - 1)
        }
    }

    fun moveDown(i: Int) {
        if (i < shapes.size - 1) {
            Collections.swap(shapes, i, i + 1)
        }
    }

    fun addCircle(model: CircleModel) {
        shapes.add(CircleCreateStrategy.create(model))
    }

    fun addRect(model: RectModel) {
        shapes.add(RectCreateStrategy.create(model))
    }

    fun addSquare(model: SquareModel) {
        shapes.add(SquareCreateStrategy.create(model))
    }

    fun addTriangle(model: TriangleModel) {
        shapes.add(TriangleCreateStrategy.create(model))
    }

    fun remove(i: Int) {
        shapes.remove(i, i + 1)
    }

    companion object {
        const val DEFAULT_FILENAME = "temp.json"
    }
}