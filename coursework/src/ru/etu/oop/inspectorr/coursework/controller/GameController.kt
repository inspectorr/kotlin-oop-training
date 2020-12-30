package ru.etu.oop.inspectorr.coursework.controller

import ru.etu.oop.inspectorr.coursework.model.Configuration
import ru.etu.oop.inspectorr.coursework.model.Game
import ru.etu.oop.inspectorr.coursework.view.GameView
import tornadofx.Controller

class GameController : Controller() {
    val cellsWidthCount = 10
    val cellsHeightCount = 10

    val configuration = Configuration.randomize(cellsWidthCount, cellsHeightCount)

    fun randomize() {
        configuration.randomize()
    }

    val game = Game(configuration)

    val cellWidth: Double get() = GameView.CANVAS_WIDTH / cellsWidthCount
    val cellHeight: Double get() = GameView.CANVAS_HEIGHT / cellsHeightCount

    inline fun rectsToDraw(action: (x: Double, y: Double) -> Unit) {
        game.configuration.iterateAlive { y, x, _ ->
            action(x * cellWidth, y * cellHeight)
        }
    }

    val hasNext: Boolean get() = game.hasNext

    fun next() {
        game.next()
    }
}