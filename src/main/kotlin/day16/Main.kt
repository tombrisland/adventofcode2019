package day16

import common.Utils

object Main {

    private const val INPUT = "/day16/input.txt"
    private val BASE_PATTERN = listOf(0, 1, 0, -1)

    private const val MESSAGE_OFFSET = 7
    private const val MESSAGE_LENGTH = 8

    @JvmStatic
    fun main(args: Array<String>) {
        val lines = Utils.getFileFromResource(INPUT)
                .readLines()

        val signal = Utils.getInputList(lines)
                .reduce { acc, s -> acc + s }
                .map { it.toString().toInt() }

        val phases = 100

        var output = signal.toList() + signal.toList()

        for (phase in (0 until phases)) {
            output = output.mapIndexed { i, _ ->
                val pattern = Pattern(BASE_PATTERN, i)
                pattern.nextItem()

                takeUnits(output.fold(0) { acc, it -> acc + (it * pattern.nextItem()) })
            }
        }

        println("The output after 100 phases was $output")

        val offset = signal
                .take(MESSAGE_OFFSET)
                .fold("") { acc, n -> acc + n.toString() }.toInt()


        var input = (0 until 10000).map { signal }.flatten()

        for (i in (0 until phases)) {
            input = calculatePhase(input)
        }

        val message = input
                .subList(offset, offset + MESSAGE_LENGTH)
                .fold("") { acc, n -> acc + n.toString() }

        println("The message at position $offset was $message")
    }

    private fun calculatePhase(signal : List<Int>): MutableList<Int> {
        val phase = signal.toMutableList()

        // Last digit is always the same
        phase[signal.size - 1] = signal.last()

        val range = IntProgression.fromClosedRange(signal.size - 2, 0, -1)

        for (i in range) {
            phase[i] = (signal[i] + phase[i+1]) % 10
        }

        return phase
    }

    private fun takeUnits(int: Int): Int {
        return int.toString().last().toString().toInt()
    }

    private fun getElementForIndex(signal : List<Int>, index : Int): Int {
        return index % signal.size
    }
}