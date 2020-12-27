package ru.etu.inspectorr.lab6

import javafx.application.Application
import ru.etu.inspectorr.lab6.view.ShapesView
import tornadofx.App

class ShapesApp : App(ShapesView::class)

fun main() {
    Application.launch(ShapesApp::class.java)
}