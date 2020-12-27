package ru.etu.inspectorr.lab6.view

import javafx.geometry.Insets
import ru.etu.inspectorr.lab6.controller.ShapeController
import ru.etu.inspectorr.lab6.model.CircleModel
import ru.etu.inspectorr.lab6.model.RectModel
import ru.etu.inspectorr.lab6.model.SquareModel
import ru.etu.inspectorr.lab6.model.TriangleModel
import tornadofx.*

class ShapesView : View("Shapes") {
    private val controller: ShapeController by inject()

    private val shapeListView = listview(controller.shapes)

    private val selectedShapeIndex: Int
        get() {
            val selection = shapeListView.selectionModel.selectedIndices
            return selection[0]
        }
    private val isSelected: Boolean get() = selectedShapeIndex != -1
    private fun setSelection(i: Int) {
        shapeListView.selectionModel.select(i)
    }

    override val root = borderpane {
        left = shapeListView
        right = vbox(SPACING) {
            padding = Insets(SPACING)
            button("Move up") {
                action {
                    if (isSelected) {
                        controller.moveUp(selectedShapeIndex)
                        setSelection(selectedShapeIndex - 1)
                    }
                }
                useMaxWidth = true
            }
            button("Move down") {
                action {
                    if (isSelected) {
                        controller.moveDown(selectedShapeIndex)
                        setSelection(selectedShapeIndex + 1)
                    }
                }
                useMaxWidth = true
            }
            button("Remove") {
                action {
                    if (isSelected) controller.remove(selectedShapeIndex)
                }
                useMaxWidth = true
            }
            button("Add circle") {
                action {
                    val circleModel = CircleModel()
                    find<CircleCreateFragment>(Scope(circleModel)).openModal(block = true)
                    controller.addCircle(circleModel)
                }
                useMaxWidth = true
            }
            button("Add rectangle") {
                action {
                    val rectModel = RectModel()
                    find<RectCreateFragment>(Scope(rectModel)).openModal(block = true)
                    controller.addRect(rectModel)
                }
                useMaxWidth = true
            }
            button("Add square") {
                action {
                    val squareModel = SquareModel()
                    find<SquareCreateFragment>(Scope(squareModel)).openModal(block = true)
                    controller.addSquare(squareModel)
                }
                useMaxWidth = true
            }
            button("Add triangle") {
                action {
                    val triangleModel = TriangleModel()
                    find<TriangleCreateFragment>(Scope(triangleModel)).openModal(block = true)
                    controller.addTriangle(triangleModel)
                }
                useMaxWidth = true
            }
        }
    }

    override fun onDock() {
        currentWindow?.setOnCloseRequest {
            controller.saveToDefaultFile()
        }
    }

    companion object {
        const val SPACING = 5.0
    }
}