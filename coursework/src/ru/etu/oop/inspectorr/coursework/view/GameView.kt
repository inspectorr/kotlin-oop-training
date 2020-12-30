package ru.etu.oop.inspectorr.coursework.view

import javafx.animation.AnimationTimer
import javafx.geometry.Insets
import javafx.scene.canvas.Canvas
import javafx.scene.control.Alert
import javafx.scene.paint.Color
import ru.etu.oop.inspectorr.coursework.controller.GameController
import tornadofx.*

class GameView : View("LIFE") {
    companion object {
        const val CANVAS_WIDTH = 400.0
        const val CANVAS_HEIGHT = 400.0
        const val SPACING = 5.0
        const val FRAMES_PER_SEC = 10
    }

    val controller: GameController by inject()

    val canvas = Canvas(CANVAS_WIDTH, CANVAS_HEIGHT)
    val gc get() = canvas.graphicsContext2D

    fun draw() {
        gc.fill = Color.BLACK
        gc.fillRect(0.0, 0.0, canvas.width, canvas.height)
        gc.fill = Color.WHITE
        controller.apply {
            rectsToDraw { x, y ->
                gc.fillRect(x, y, cellWidth, cellHeight)
            }
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

    fun start() {
        animation.start()
    }

    fun stop() {
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
        Alert(Alert.AlertType.INFORMATION).apply {
            title =  "GAME OVER"
            contentText = "See how your last generation looks like!"
            show()
        }
    }

    fun randomize() {
        controller.randomize()
        draw()
    }

    init {
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
            }
        }
    }
}