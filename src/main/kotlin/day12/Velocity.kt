package day12

class Velocity(x : Int, y : Int, z : Int) : Dimensions3(x, y, z) {
    override fun toString(): String {
        return "Velocity(x=$x, y=$y, z=$z)"
    }
}