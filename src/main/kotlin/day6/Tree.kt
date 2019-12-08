package day6

import java.util.*

class Tree(var value: String) {

    constructor() : this(UUID.randomUUID().toString())

    constructor(value: String, node: Tree) : this(value) {
        this.nodes.add(node)
    }

    val nodes = mutableListOf<Tree>()

    fun findNode(value: String): Tree? {
        return findNode(mutableListOf(), Tree(value)).second
    }

    fun findParents(value: String): List<Tree> {
        return findNode(mutableListOf(), Tree(value)).first.toList()
    }

    private fun findNode(parents: MutableList<Tree>, node: Tree): Pair<MutableList<Tree>, Tree?> {
        val find = nodes.indexOf(node)

        return when {
            find > -1 -> {
                parents.add(this)
                Pair(parents, nodes[find])
            }
            nodes.size > 0 -> {
                for (child in nodes) {
                    val n = child.findNode(parents, node)
                    if (n.second != null) {
                        parents.add(this)
                        return Pair(parents, n.second)
                    }
                }
                return Pair(parents, null)
            }
            else -> {
                return Pair(parents, null)
            }
        }
    }

    private fun connections(depth : Int) : Int {
        val direct = nodes.size

        return direct*depth + nodes.map { it.connections(depth+1) }.sum()
    }

    fun connections(): Int {
        return connections(1)
    }

    private fun print(buffer: StringBuilder, prefix: String, childrenPrefix: String) {
        buffer.append(prefix).append(value).append('\n')

        val it: MutableIterator<Tree> = nodes.iterator()

        while (it.hasNext()) {
            val next = it.next()
            if (it.hasNext()) {
                next.print(buffer, "$childrenPrefix├── ", "$childrenPrefix│   ")
            } else {
                next.print(buffer, "$childrenPrefix└── ", "$childrenPrefix    ")
            }
        }
    }

    override fun toString(): String {
        val buffer = StringBuilder()
        print(buffer, "", "")
        return buffer.toString()
    }

    override fun equals(other: Any?): Boolean {
        other as Tree
        return this.value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}