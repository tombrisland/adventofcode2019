package day14

private const val ARROW = " => "

class Reaction(val inputs : List<Reactant>, val output : Reactant) {

    val isOreReaction = inputs.size == 1 && inputs[0].chemical.name == "ORE"

    fun getOreQuantity(): Int {
        return inputs[0].quantity
    }

    companion object {
        fun parseString(string : String): Reaction {
            val inputsAndOutput = string.split(ARROW)

            return Reaction(parseInputs(
                    inputsAndOutput[0]),
                    Reactant.parseString(inputsAndOutput[1])
            )
        }

        private fun parseInputs(inputs : String): List<Reactant> {
            return inputs
                    .split(", ")
                    .map { Reactant.parseString(it) }
        }
    }

    class Reactant(val chemical : Chemical, val quantity : Int) {

        companion object {
            fun parseString(string : String) : Reactant {
                val split = string.split(" ")
                return Reactant(Chemical(split[1]), split[0].toInt())
            }
        }

        override fun toString(): String {
            return "Chemical(name='$chemical', quantity=$quantity)"
        }
    }

    class Chemical(val name : String) {
        override fun toString(): String {
            return "Chemical(name='$name')"
        }
    }

    override fun toString(): String {
        return "Reaction(chemicals=$inputs, output=$output)"
    }
}