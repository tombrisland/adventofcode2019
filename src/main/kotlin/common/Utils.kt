package common

import java.io.File

object Utils {
    fun getFileFromResource(resource: String) : File {
        return File(Utils::class.java.getResource(resource).file)
    }

    fun range(from : Int, to : Int): IntProgression {
        if (from < to) {
            return IntProgression.fromClosedRange(from, to, 1)
        }
        return IntProgression.fromClosedRange(from, to, -1)
    }

    fun getInputList(lines: List<String>): List<String> {
        return lines
                .reduce { acc, s -> acc + s }
                .split(",")
    }
}