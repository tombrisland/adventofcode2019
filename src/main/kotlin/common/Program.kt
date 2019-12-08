package common

import java.lang.RuntimeException
import java.util.*

class Program(instructions: List<Int>) {

    private val memory = instructions.toMutableList()

    private val inputs = mutableListOf<Int>()

    var isExited = false
    var instructionPointer = 0

    constructor(program: List<Int>, phase: Int) : this(program) {
        inputs.add(phase)
    }

    private fun Boolean.toInt() = if (this) 1 else 0

    fun execute(input: Int): Int {

        inputs.add(input)

        var returnValue: Int? = null

        do {
            val instruction = Instruction(memory[instructionPointer++])

            if (instruction.isExit) {
                isExited = true
                return input
            }

            do {
                val acceptsParameters = instruction.addParameter(memory, instructionPointer++)
            } while (acceptsParameters)

            val output = memory[instructionPointer - 1]

            when (instruction.operator.code) {
                1 -> {
                    memory[output] = instruction.firstParameter() + instruction.secondParameter()
                }
                2 -> {
                    memory[output] = instruction.firstParameter() * instruction.secondParameter()
                }
                3 -> {
                    memory[output] = inputs.removeAt(0)
                }
                4 -> {
                    returnValue = instruction.firstParameter(); instructionPointer--
                }
                5 -> {
                    if (instruction.firstParameter() != 0) instructionPointer = instruction.secondParameter() else instructionPointer--
                }
                6 -> {
                    if (instruction.firstParameter() == 0) instructionPointer = instruction.secondParameter() else instructionPointer--
                }
                7 -> {
                    memory[output] = (instruction.firstParameter() < instruction.secondParameter()).toInt()
                }
                8 -> {
                    memory[output] = (instruction.firstParameter() == instruction.secondParameter()).toInt()
                }
            }

            if (returnValue != null) {
                return returnValue
            }

        } while (instructionPointer < memory.size)

        throw RuntimeException("Program didn't produce an output")
    }

    override fun toString(): String {
        return "Program(inputs=$inputs, isExited=$isExited)"
    }
}