package common

class Position(val x : Int, val y : Int) {
    override fun toString(): String {
        return "Position(x=$x, y=$y)"
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Position) return false

        return (x == other.x && y == other.y)
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }
}