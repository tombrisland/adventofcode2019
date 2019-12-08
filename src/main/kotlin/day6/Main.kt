package day6

import common.Utils


object Main {

    private const val INPUT = "/day6/input.txt"

    private const val SPLIT_CHAR = ")"

    @JvmStatic
    fun main(args: Array<String>) {
        val lines = Utils.getFileFromResource(INPUT).readLines()

        val orbits = sortInput(lines
                .map(this::convertToOrbit)
                .toMutableList())

        val tree = orbits
                .fold(Tree()) { tree, orbit ->
                    when (val node = tree.findNode(orbit.first)) {
                        null -> {
                            Tree(orbit.first, Tree(orbit.second))
                        }
                        else -> {
                            node.nodes.add(Tree(orbit.second))
                            tree
                        }
                    }
                }

        println(tree)

        println("There were ${tree.connections()} direct and indirect orbits present")
        // find the common parent between two nodes
        // calculate connections for those paths only
        val commonParent = findCommonParent(tree, "YOU", "SAN")

        println(commonParent)


    }

    private fun findCommonParent(tree : Tree, a : String, b : String) : Int {
        val parentsOfA = tree.findParents(a)
        val parentsOfB = tree.findParents(b)

        val intersect = (parentsOfA intersect parentsOfB).first()

        return parentsOfA.subList(0, parentsOfA.indexOf(intersect)).size + parentsOfB.subList(0, parentsOfB.indexOf(intersect)).size
    }


    private fun sortInput(input: MutableList<Pair<String, String>>): MutableList<Pair<String, String>> {
        val orbits = mutableListOf<Pair<String, String>>()

        var found = false

        do {
            for (line in input) {
                found = input.all { line.first != it.second }

                if (found) {
                    orbits.add(line)
                    break
                }
            }

            input.removeAll(orbits)

        } while (found && input.isNotEmpty())

        return orbits
    }

    private fun convertToOrbit(line: String): Pair<String, String> {
        val split = line.split(SPLIT_CHAR)
        return Pair(split.first(), split.last())
    }
}