package ru.etu.oop.inspectorr.coursework.view

import javafx.animation.AnimationTimer
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.canvas.Canvas
import javafx.scene.control.Alert
import javafx.scene.control.TextField
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
        const val INPUT_WIDTH = 50.0
        val CURSOR_FILL_COLOR = Color.color(1.0, 1.0, 1.0, 0.5)
        val CURSOR_STROKE_COLOR = Color.BLUEVIOLET
        val ALIVE_CELL_FILL_COLOR = Color.WHITE
        val ALIVE_CELL_STROKE_COLOR = Color.BLACK
        val DEAD_CELL_COLOR = Color.BLACK
    }

    val controller: GameController by inject()

    val canvas = Canvas(CANVAS_WIDTH, CANVAS_HEIGHT)
    val gc get() = canvas.graphicsContext2D
    val generationLabel = label()

    init {
        init()
    }

    fun init() {
        clearState()
        initCanvasEvents()
        updateGenerationText()
    }

    fun draw() {
        gc.fill = DEAD_CELL_COLOR
        gc.fillRect(0.0, 0.0, canvas.width, canvas.height)
        gc.fill = ALIVE_CELL_FILL_COLOR
        gc.stroke = ALIVE_CELL_STROKE_COLOR
        controller.apply {
            rectsToDraw { x, y ->
                gc.fillRect(x, y, cellWidth, cellHeight)
                if (cellWidth > 2 && cellHeight > 2) {
                    gc.strokeRect(x, y, cellWidth, cellHeight)
                }
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
        if (animating) return
        animating = true
        animation.start()
    }

    fun stop() {
        if (!animating) return
        animating = false
        animation.stop()
    }

    fun next() {
        if (controller.hasNext) {
            controller.next()
            draw()
            updateGenerationText()
        } else {
            onGameOver()
        }
    }

    fun onGameOver() {
        stop()
        Alert(Alert.AlertType.CONFIRMATION).apply {
            title =  "GAME OVER"
            contentText = "Your generation score is ${controller.generation}!"
            show()
            onHidden = EventHandler {
                init()
            }
        }
    }

    fun randomize() {
        stop()
        controller.randomize()
        draw()
        updateGenerationText()
    }

    fun updateGenerationText() {
        generationLabel.text = "GENERATION: ${controller.generation}"
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
        stop()
        controller.clear()
        draw()
        updateGenerationText()
    }

    fun resetField() {
        stop()
        controller.resetFieldSize(widthTextField.text.toInt(), heightTextField.text.toInt())
        init()
    }

    val widthTextField = TextField(controller.fieldWidth.toString()).apply { prefWidth = INPUT_WIDTH }
    val heightTextField = TextField(controller.fieldHeight.toString()).apply { prefWidth = INPUT_WIDTH }

    override val root = borderpane {
        top = canvas
        center {
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
                hbox(SPACING) {
                    add(widthTextField)
                    label("X")
                    add(heightTextField)
                    button("Reset field") {
                        action {
                            resetField()
                        }
                        useMaxWidth = true
                    }
                }
                add(generationLabel)
            }
        }
    }
}