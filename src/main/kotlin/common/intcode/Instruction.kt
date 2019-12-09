package common.intcode

import common.intcode.IntCode.Mode
import common.intcode.IntCode.Mode.*

class Instruction(memory: List<Long>, relativeBase: Long, private val input: List<Long>) {

    val intCode : IntCode
    private val parameters : List<Long>
    var outputIndex : Int? = null

    fun p1() : Long { return parameters[0] }
    fun p2() : Long { return parameters[1] }

    companion object {
        private const val INT_CODE = 0
    }

    init {
        intCode = IntCode(input[INT_CODE].toString())
        val expected = intCode.value.parameters

        val modes = intCode.modes

        parameters = (1..expected)
                .map {
                    val parameter = input[it]
                    val mode = if (modes.size < it) POSITION else modes[it-1]

                    calculateParameterValue(memory, relativeBase, parameter, mode)
                }

        val outputPosition = expected + 1

        if (intCode.value.hasOutput) {
            // TODO make this cleaner - it's how the input instruction runs in relative mode
            outputIndex = if (modes.size == outputPosition) {
                input[outputPosition].toInt() + relativeBase.toInt()
            } else {
                input[outputPosition].toInt()
            }
        }
    }

    private fun calculateParameterValue(memory : List<Long>, relativeBase: Long, parameter: Long, mode : Mode) : Long {
        try {
            return when (mode) {
                POSITION -> memory[parameter.toInt()]
                IMMEDIATE -> parameter
                RELATIVE -> memory[(parameter + relativeBase).toInt()]
            }
        } catch (e : IndexOutOfBoundsException) {
            throw InvalidMemoryException(parameter + relativeBase)
        }
    }

    override fun toString(): String {
        return "Instruction(input=$input, intCode=$intCode, parameters=$parameters, outputIndex=$outputIndex)"
    }
}