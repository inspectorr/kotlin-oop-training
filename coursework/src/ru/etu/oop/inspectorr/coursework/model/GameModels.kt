package ru.etu.oop.inspectorr.coursework.model

import Matrix
import java.util.*
import kotlin.collections.HashSet
import kotlin.math.abs

enum class CellState {
    ALIVE,
    DEAD
}

class Cell(var state: CellState = CellState.DEAD) {
    val isAlive: Boolean get() = state == CellState.ALIVE
    fun live() { if (!isAlive) state = CellState.ALIVE }
    fun die() { if (isAlive) state = CellState.DEAD }
    companion object {
        fun willLive(cell: Cell, aliveNeighbours: Int): Boolean {
            return !cell.isAlive && aliveNeighbours == 3 || cell.isAlive && aliveNeighbours in 2..3
        }
    }
}

class Configuration(cells: Array<Array<Cell>>) : Matrix<Cell>(cells.map { it.clone() }.toTypedArray()){
    var cells: Array<Array<Cell>>
        get() = super.elements
        set(cells) { super.elements = cells }

    constructor(width: Int, height: Int): this(Array(width) { Array(height) { Cell() } })

    inline fun iterateAlive(action: (y: Int, x: Int, cell: Cell) -> Unit) {
        iterate { y, x, cell ->
            if (cell.isAlive) action(y, x, cell)
        }
    }

    private fun normalize(n: Int, max: Int): Int {
        return if (n > max - 1 || n < 0) max - abs(n) else n
    }

    override operator fun get(y: Int, x: Int): Cell {
        return super.get(normalize(y, height), normalize(x, width))
    }

    override operator fun set(y: Int, x: Int, cell: Cell) {
        return super.set(normalize(y, height), normalize(x, width), cell)
    }

    private fun getCellNeighbours(y: Int, x: Int): List<Cell> {
        return listOf(
            this[y - 1, x - 1],
            this[y - 1, x],
            this[y - 1, x + 1],
            this[y, x + 1],
            this[y + 1, x + 1],
            this[y + 1, x],
            this[y + 1, x - 1],
            this[y, x - 1]
        )
    }

    private fun countAliveNeighbours(y: Int, x: Int): Int {
        return getCellNeighbours(y, x).count { it.isAlive }
    }

    var generation = 0

    fun next() {
        val next = Configuration(width, height)

        iterate { y, x, current ->
            next[y, x].apply {
                if (Cell.willLive(current, countAliveNeighbours(y, x))) {
                    live()
                } else {
                    die()
                }
            }
        }

        cells = next.cells
        generation++
    }

    fun hash(): Int {
        // unique value for two numbers
        fun unique(n1: Int, n2: Int): Int {
            val tmp = n1 + (n2 + 1) / 2
            return n1 + tmp * tmp
        }

        var hash = 0
        iterate { y, x, cell ->
            if (cell.isAlive) {
                hash = unique(hash, unique(x, y))
            }
        }
        return hash
    }

    fun randomize() {
        generation = 0
        iterate { _, _, cell ->
            cell.die()
            if (Random().nextBoolean()) cell.live()
        }
    }

    fun clear() {
        generation = 0
        iterate { _, _, cell ->
            cell.die()
        }
    }

    companion object {
        fun randomize(width: Int, height: Int): Configuration {
            return Configuration(width, height).apply { randomize() }
        }
    }
}

class Game(var configuration: Configuration) {
    private val hashHistory = HashSet<Int>()

    val hasNext: Boolean get() = !hashHistory.contains(configuration.hash())

    fun next() {
        hashHistory.add(configuration.hash())
        configuration.next()
    }

    fun clear() {
        hashHistory.clear()
        configuration.clear()
    }

    fun randomize() {
        hashHistory.clear()
        configuration.randomize()
    }

    fun reset(width: Int, height: Int) {
        hashHistory.clear()
        configuration = Configuration(width, height)
    }
}
