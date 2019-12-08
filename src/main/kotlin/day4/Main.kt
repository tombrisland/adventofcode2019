package day4

object Main {

    private val INPUT = 236491..713787

    @JvmStatic
    fun main(args: Array<String>) {

        val validPasswords = INPUT
                .map(this::isValidPassword)
                .filter { it }
                .count()

        println("There was $validPasswords in $INPUT")
    }

    private fun isValidPassword(password: Int): Boolean {
        val candidate = password.toString()

        var containsDouble = false
        var repeated = 0

        candidate.forEachIndexed { i, it ->
            if (i > 0) {
                if (candidate[i].toInt() < candidate[i-1].toInt()) return false
                if (candidate[i] == candidate[i-1]) {
                    repeated++
                } else {
                    if (repeated == 1) {
                        containsDouble = true
                    }
                    repeated = 0
                }
            }
        }

        if (repeated == 1) {
            containsDouble = true
        }

        return containsDouble
    }
}