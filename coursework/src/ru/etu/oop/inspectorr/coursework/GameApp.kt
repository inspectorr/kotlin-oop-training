package ru.etu.oop.inspectorr.coursework

import javafx.application.Application
import ru.etu.oop.inspectorr.coursework.view.GameView
import tornadofx.App

class GameApp : App(GameView::class)

fun main() {
    Application.launch(GameApp::class.java)
}