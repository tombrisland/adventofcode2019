package common

class Instruction(private val value: Int) {

    private val op = value.toString()

    val operator = parseOperator()
    private val parameters = mutableListOf<Int>()

    fun firstParameter(): Int {
        return parameters[0]
    }

    fun secondParameter(): Int {
        return parameters[1]
    }

    val isExit = operator.code == 99

    private val codeParameters = mapOf(
            Pair(1, 2),
            Pair(2, 2),
            Pair(3, 0),
            Pair(4, 1),
            Pair(5, 2),
            Pair(6, 2),
            Pair(7, 2),
            Pair(8, 2)
    )

    private val numParameters = codeParameters[operator.code]

    private fun parseOperator(): Operator {
        return if (op.length <= 2) {
            Operator(op.toInt(), listOf())
        } else {
            val code = op.substring(op.length - 1, op.length).toInt()
            val modes = op
                    .reversed()
                    .substring(2)
                    .map { Mode.values()[it.toString().toInt()] }

            Operator(code, modes)
        }
    }

    fun addParameter(program: List<Int>, parameter: Int): Boolean {
        if (parameters.size == numParameters!!) {
            return false
        }

        val mode =
                if (parameters.size >= operator.modes.size) {
                    Mode.POSITION
                } else {
                    operator.modes[parameters.size]
                }

        parameters.add(
                when (mode) {
                    Mode.POSITION -> program[program[parameter]]
                    Mode.IMMEDIATE -> program[parameter]
                })

        return true
    }

    override fun toString(): String {
        return "Instruction(operator=$operator, parameters=$parameters)"
    }

    enum class Mode {
        POSITION, IMMEDIATE;
    }

    class Operator(val code: Int, val modes: List<Mode>) {
        override fun toString(): String {
            return "Operator(code=$code)"
        }
    }
}