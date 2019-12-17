package day15

import common.Position
import common.Utils
import common.intcode.Program
import day15.Main.Movement.*
import day15.Main.StatusCode.*
import javax.print.attribute.standard.Destination

object Main {

    private const val INPUT = "/day15/input.txt"

    @JvmStatic
    fun main(args: Array<String>) {
        val lines = Utils.getFileFromResource(INPUT)
                .readLines()

        val instructions = Utils.getInputList(lines)
                .map(String::toLong)

        val program = Program(instructions)

        var move = NORTH
        var current = Position(0, 0)

        val area = mutableMapOf(Pair(current, SUCCESS))
        area[current] = START

        while (true) {

            move = if (surroundingsMapped(area, current)) {
                followRightWall(area, move, current)
            } else {
                mapSurroundings(area, current)
            }

            val status = StatusCode
                    .fromInt(program.execute(move.ordinal.toLong() + 1).toInt())

            val position = move.applyToPosition(current)

            when (status) {
                COLLISION -> {
                    area[position] = COLLISION
                }
                SUCCESS -> {
                    area[position] = SUCCESS
                    current = position
                }
                END -> {
                    area[position] = END
                    current = position
                    println("Found end at $position")
                }
                else -> {
                }
            }

            if (status == END) break
        }

        printArea(area)

        val results = calculatePotentialMoves(area, mutableListOf(), Position(0, 0), current, mutableListOf())

        println(results.size - 1)

//        // find furthest tile from oxygen
//
//        val furthestPoint = area
//                .filter { it.value == SUCCESS }
//                .map { calculatePotentialMoves(area, mutableListOf(), current, it.key, mutableListOf()).size - 1 }
//
//        println(furthestPoint.max())
        oxygenate(area, current)

    }

    private fun oxygenate(area: Map<Position, StatusCode>, start: Position) {
        val oxygen = mutableListOf(start)

        var t = 0

        do {
            val options = oxygen.map {
                getMovementOptions(it)
                        .map { it.first }
                        .filter {
                            area[it] == SUCCESS
                                    || area[it] == END
                                    || area[it] == START
                        }
                        .filter { !oxygen.contains(it) }
            }.flatten()

            oxygen.addAll(options)

            t++
        } while (options.isNotEmpty())

        println(t)
    }

    private fun calculatePotentialMoves(area: Map<Position, StatusCode>, traversed: MutableList<Position>, pos: Position, destination: Position, steps: MutableList<Position>): List<Position> {
        steps.add(pos)

        if (destination == pos) {
            return steps.toList()
        }

        // add position to traversed
        traversed.add(pos)
        // list movement options
        // filter against already traversed
        val options = getMovementOptions(pos)
                .filter {
                    area[it.first] == SUCCESS
                            || area[it.first] == END
                            || area[it.first] == START
                }
                .filter { !traversed.contains(it.first) }

        // if dead end
        if (options.isEmpty()) return listOf()

        return options
                .map { calculatePotentialMoves(area, traversed, it.second.applyToPosition(pos), destination, steps) }
                .flatten()
    }

    private fun printArea(area: Map<Position, StatusCode>) {
        val minX = area.minBy { it.key.x }!!.key.x
        val minY = area.minBy { it.key.y }!!.key.y
        val maxX = area.maxBy { it.key.x }!!.key.x
        val maxY = area.maxBy { it.key.y }!!.key.y

        for (y in minY..maxY) {
            for (x in minX..maxX) {
                val location = area[Position(x, y)]

                if (location != null) {
                    when (location) {
                        END -> {
                            print("X")
                        }
                        COLLISION -> {
                            print("#")
                        }
                        SUCCESS -> {
                            print(" ")
                        }
                        START -> {
                            print("S")
                        }
                        else -> {
                            print(" ")
                        }
                    }
                } else {
                    print("#")
                }
            }
            println()
        }
    }

    private fun getMovementOptions(position: Position): List<Pair<Position, Movement>> {
        return listOf(
                Pair(position.above(), NORTH),
                Pair(position.below(), SOUTH),
                Pair(position.left(), WEST),
                Pair(position.right(), EAST)
        )
    }

    private fun surroundingsMapped(area: Map<Position, StatusCode>, current: Position): Boolean {
        return getMovementOptions(current).all { area.containsKey(it.first) }
    }

    private fun mapSurroundings(area: Map<Position, StatusCode>, current: Position): Movement {
        return getMovementOptions(current).last { !area.containsKey(it.first) }.second
    }

    private fun followRightWall(area: Map<Position, StatusCode>, move: Movement, current: Position): Movement {

        // if there is an open spot to the RIGHT then move in that direction
        val positionToRight = getPositionToRight(move, current)
        if (positionNotBlocked(area, positionToRight.first)) return positionToRight.second

        // if not blocked Forward on same path
        val positionAhead = move.applyToPosition(current)
        if (positionNotBlocked(area, positionAhead)) return move

        // try and turn left
        val positionToLeft = getPositionToLeft(move, current)
        if (positionNotBlocked(area, positionToLeft.first)) return positionToLeft.second

        // otherwise retrace steps
        return move.right().right();
    }

    private fun getPositionToRight(move: Movement, pos: Position): Pair<Position, Movement> {
        return when (move) {
            NORTH -> Pair(Position(pos.x + 1, pos.y), EAST)
            SOUTH -> Pair(Position(pos.x - 1, pos.y), WEST)
            WEST -> Pair(Position(pos.x, pos.y + 1), NORTH)
            EAST -> Pair(Position(pos.x, pos.y - 1), SOUTH)
        }
    }

    private fun getPositionToLeft(move: Movement, pos: Position): Pair<Position, Movement> {
        return when (move) {
            NORTH -> Pair(Position(pos.x - 1, pos.y), WEST)
            SOUTH -> Pair(Position(pos.x + 1, pos.y), EAST)
            WEST -> Pair(Position(pos.x, pos.y - 1), SOUTH)
            EAST -> Pair(Position(pos.x, pos.y + 1), NORTH)
        }
    }

    private fun positionNotBlocked(area: Map<Position, StatusCode>, pos: Position): Boolean {
        val status = area[pos]

        return status != null && status != COLLISION
    }

    enum class StatusCode {
        COLLISION, SUCCESS, END, START;

        companion object {
            fun fromInt(int: Int): StatusCode {
                return values()[int]
            }
        }
    }

    enum class Movement {
        NORTH, SOUTH, WEST, EAST;

        fun right(): Movement {
            return when (this) {
                NORTH -> EAST
                SOUTH -> WEST
                WEST -> NORTH
                EAST -> SOUTH
            }
        }

        companion object {
            fun fromInt(int: Int): Movement {
                return values()[int]
            }
        }

        fun applyToPosition(pos: Position): Position {
            return when (this) {
                NORTH -> Position(pos.x, pos.y + 1)
                EAST -> Position(pos.x + 1, pos.y)
                SOUTH -> Position(pos.x, pos.y - 1)
                WEST -> Position(pos.x - 1, pos.y)
            }
        }
    }
}