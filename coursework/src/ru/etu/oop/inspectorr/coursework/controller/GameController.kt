package ru.etu.oop.inspectorr.coursework.controller

import ru.etu.oop.inspectorr.coursework.model.Configuration
import ru.etu.oop.inspectorr.coursework.model.Game
import ru.etu.oop.inspectorr.coursework.view.GameView
import tornadofx.Controller

class GameController : Controller() {
    val cellsWidthCount = 10
    val cellsHeightCount = 10

    val game = Game(Configuration.randomize(cellsWidthCount, cellsHeightCount))

    val cellWidth: Double get() = GameView.CANVAS_WIDTH / cellsWidthCount
    val cellHeight: Double get() = GameView.CANVAS_HEIGHT / cellsHeightCount

    fun cell_X_ByCanvas_X(canvasX: Double): Int = (canvasX / cellWidth).toInt()
    fun cell_Y_ByCanvas_Y(canvasY: Double): Int = (canvasY / cellHeight).toInt()
    fun canvas_X_ByCell_X(cellX: Int): Double = cellX * cellWidth
    fun canvas_Y_ByCell_Y(cellY: Int): Double = cellY * cellHeight

    fun flipX(x: Double): Double {
        return canvas_X_ByCell_X(cell_X_ByCanvas_X(x))
    }

    fun flipY(y: Double): Double {
        return canvas_Y_ByCell_Y(cell_Y_ByCanvas_Y(y))
    }

    inline fun rectsToDraw(action: (x: Double, y: Double) -> Unit) {
        game.configuration.iterateAlive { y, x, _ ->
            action(canvas_X_ByCell_X(x), canvas_Y_ByCell_Y(y))
        }
    }

    fun aliveCell(canvasX: Double, canvasY: Double) {
        game.configuration[cell_Y_ByCanvas_Y(canvasY), cell_X_ByCanvas_X(canvasX)].live()
    }

    fun clear() {
        game.clear()
    }

    fun randomize() {
        game.randomize()
    }

    val hasNext: Boolean get() = game.hasNext

    fun next() {
        game.next()
    }
}