package day14

import day14.Reaction.Chemical
import kotlin.math.ceil

class Nanofactory(private val reactions: List<Reaction>) {

    private val wastedChemicals = mutableMapOf<Chemical, Double>()

    fun createChemical(chemical: Chemical, quantity: Double): Double {
        val reactions = findReactions(chemical, quantity)

        return reactions
                .groupBy { it.first }
                .map { reaction -> reaction.key.getOreQuantity() * ceil(reaction.value.map { it.second }.sum()) }
                .sum()
    }

    private fun findReactions(chemical: Chemical, quantity: Double): List<Pair<Reaction, Double>> {
        val reaction = findReaction(chemical)
        val needed = handleWaste(reaction, quantity)

        return if (reaction.isOreReaction) {
            listOf(Pair(reaction, needed))
        } else {
            reaction.inputs
                    .map { findReactions(it.chemical, it.quantity*needed) }
                    .flatten()
        }
    }

    private fun handleWaste(reaction: Reaction, required: Double): Double {
        val chemical = reaction.output.chemical
        val outputQuantity = reaction.output.quantity

        // deduct any waste if it is there
        val quantityRequired = subtractWasteFromMap(chemical, required)
        // now calculate how many reactions required for the new total
        val reactionsRequired = ceil(quantityRequired / outputQuantity)
        // add any waste if there is any
        addWaste(chemical, (reactionsRequired * outputQuantity) - quantityRequired)
        // return number of reactions required
        return reactionsRequired
    }

    private fun addWaste(key : Chemical, value : Double) {
        if (!wastedChemicals.containsKey(key)) {
            wastedChemicals[key] = value
        } else {
            wastedChemicals[key] = wastedChemicals[key]?.plus(value)!!
        }
    }

    private fun subtractWasteFromMap(key: Chemical, value: Double) : Double {
        return if (wastedChemicals.containsKey(key)) {
            val result = wastedChemicals[key]

            if (result!! > value) {
                wastedChemicals[key] = result.minus(value)
                0.0
            } else {
                wastedChemicals.remove(key)
                value - result
            }
        } else {
            value
        }
    }


    private fun findReaction(chemical: Chemical): Reaction {
        val reaction = reactions.find { it.output.chemical.name == chemical.name }

        return reaction ?: throw RuntimeException("No reactions found for chemical ${chemical.name}")
    }
}