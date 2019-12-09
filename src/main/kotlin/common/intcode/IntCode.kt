package common.intcode

class IntCode(private val input : String) {

    val value : Code
    val modes : List<Mode>

    init {
        if (input.length <= 2) {
            value = Code.fromIntCode(input.toInt())
            modes = listOf()
        } else {
            value = Code.fromIntCode(input.substring(input.length - 1, input.length).toInt())
            modes = parseModes(input)
        }
    }

    private fun parseModes(input : String) : List<Mode> {
        return input
                .reversed()
                .substring(2)
                .padEnd(value.parameters, '0')
                .map { Mode.values()[it.toString().toInt()] }
    }

    override fun toString(): String {
        return "IntCode(input='$input', code=$value, modes=$modes)"
    }

    enum class Code(val value : Int, val parameters : Int, val hasOutput : Boolean) {
        ADD(1, 2, true),
        MUL(2, 2, true),
        INP(3, 0, true),
        OUT(4, 1, false),
        JNZ(5, 2, false),
        JIZ(6, 2, false),
        LES(7, 2, true),
        EQU(8, 2, true),
        RBS(9, 1, false),
        HLT(99, 0, false);

        companion object {
            fun fromIntCode(value : Int) : Code {
                return values().find { it.value == value }!!
            }
        }

        fun size() : Int{
            return 1 + parameters + hasOutput.toInt()
        }
    }

    enum class Mode {
        POSITION, IMMEDIATE, RELATIVE;
    }
}