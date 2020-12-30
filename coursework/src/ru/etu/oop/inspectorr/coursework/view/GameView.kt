package ru.etu.oop.inspectorr.coursework.view

import javafx.animation.AnimationTimer
import javafx.geometry.Insets
import javafx.scene.canvas.Canvas
import javafx.scene.control.Alert
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import ru.etu.oop.inspectorr.coursework.controller.GameController
import tornadofx.*

class GameView : View("LIFE") {
    companion object {
        const val CANVAS_WIDTH = 400.0
        const val CANVAS_HEIGHT = 400.0
        const val SPACING = 5.0
        const val FRAMES_PER_SEC = 10
        val CURSOR_FILL_COLOR = Color.SIENNA
        val CURSOR_STROKE_COLOR = Color.BLUE
        val ALIVE_CELL_COLOR = Color.WHITE
        val DEAD_CELL_COLOR = Color.BLACK
    }

    val controller: GameController by inject()

    val canvas = Canvas(CANVAS_WIDTH, CANVAS_HEIGHT)
    val gc get() = canvas.graphicsContext2D

    init {
        draw()
        initCanvasEvents()
    }

    fun draw() {
        gc.fill = DEAD_CELL_COLOR
        gc.fillRect(0.0, 0.0, canvas.width, canvas.height)
        gc.fill = ALIVE_CELL_COLOR
        controller.apply {
            rectsToDraw { x, y ->
                gc.fillRect(x, y, cellWidth, cellHeight)
            }
        }
    }

    fun drawCursor(x: Double, y: Double) {
        controller.apply {
            gc.fill = CURSOR_FILL_COLOR
            gc.fillRect(flipX(x), flipY(y), cellHeight, cellHeight)
            gc.stroke = CURSOR_STROKE_COLOR
            gc.strokeRect(flipX(x), flipY(y), cellHeight, cellHeight)
        }
    }

    val animation = object : AnimationTimer() {
        var time = 0L
        override fun handle(now: Long) {
            if (now - time > 1_000_000_000 / FRAMES_PER_SEC) {
                next()
                time = now
            }
        }
    }

    var animating = false

    fun start() {
        animating = true
        animation.start()
    }

    fun stop() {
        animating = false
        animation.stop()
    }

    fun next() {
        if (controller.hasNext) {
            controller.next()
            draw()
        } else {
            onGameOver()
        }
    }

    fun onGameOver() {
        animation.stop()
        Alert(Alert.AlertType.CONFIRMATION).apply {
            title =  "GAME OVER"
            contentText = "See how your last generation looks like!"
            show()
        }
        controller.clear()
    }

    fun randomize() {
        controller.randomize()
        draw()
    }

    fun initCanvasEvents() {
        canvas.apply {
            addEventHandler(MouseEvent.MOUSE_DRAGGED) {e ->
                controller.aliveCell(e.x, e.y)
                draw()
                drawCursor(e.x, e.y)
            }
            addEventHandler(MouseEvent.MOUSE_CLICKED) {e ->
                controller.aliveCell(e.x, e.y)
                draw()
                drawCursor(e.x, e.y)
            }
            addEventHandler(MouseEvent.MOUSE_MOVED) {e ->
                if (!animating) {
                    draw()
                    drawCursor(e.x, e.y)
                }
            }
            addEventHandler(MouseEvent.MOUSE_EXITED) {
                draw()
            }
        }
    }

    fun clearState() {
        controller.clear()
        draw()
    }

    override val root = borderpane {
        top = canvas
        bottom {
            vbox(SPACING) {
                padding = Insets(SPACING)
                button("Start") {
                    action {
                        start()
                    }
                    useMaxWidth = true
                }
                button("Stop") {
                    action {
                        stop()
                    }
                    useMaxWidth = true
                }
                button("Next") {
                    action {
                        next()
                    }
                    useMaxWidth = true
                }
                button("Randomize") {
                    action {
                        randomize()
                    }
                    useMaxWidth = true
                }
                button("Clear") {
                    action {
                        clearState()
                    }
                    useMaxWidth = true
                }
            }
        }
    }
}