package day12

class Point(x : Int, y : Int, z : Int) : Dimensions3(x, y, z) {

    override fun toString(): String {
        return "Point(x=$x, y=$y, z=$z)"
    }

    override fun equals(other: Any?): Boolean {
        other as Point

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + z
        return result
    }


}

