package ru.etu.inspectorr.lab6.view

import ru.etu.inspectorr.lab6.model.CircleModel
import ru.etu.inspectorr.lab6.model.RectModel
import ru.etu.inspectorr.lab6.model.SquareModel
import ru.etu.inspectorr.lab6.model.TriangleModel
import tornadofx.*

class CircleCreateFragment : Fragment("Circle") {
    private val model: CircleModel by inject()
    private fun save() {
        model.commit()
        close()
    }
    override val root = form {
        fieldset {
            field("Radius") { textfield(model.radius) }
        }
        button("Save") { action { save() } }
    }
}

class RectCreateFragment : Fragment("Rectangle") {
    private val model: RectModel by inject()
    private fun save() {
        model.commit()
        close()
    }
    override val root = form {
        fieldset {
            field("Width") { textfield(model.width) }
            field("Height") { textfield(model.height) }
        }
        button("Save") { action { save() } }
    }
}

class SquareCreateFragment : Fragment("Square") {
    private val model: SquareModel by inject()
    private fun save() {
        model.commit()
        close()
    }
    override val root = form {
        fieldset {
            field("Size") { textfield(model.size) }
        }
        button("Save") { action { save() } }
    }
}

class TriangleCreateFragment : Fragment("Triangle") {
    private val model: TriangleModel by inject()
    private fun save() {
        model.commit()
        close()
    }
    override val root = form {
        fieldset {
            field("A") { textfield(model.a) }
            field("B") { textfield(model.b) }
            field("C") { textfield(model.c) }
        }
        button("Save") { action { save() } }
    }
}
