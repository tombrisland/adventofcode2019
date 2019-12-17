package day15

enum class Tile(private val char: Char) {

    SPACE('.'),
    SCAFFOLD('#'),
    ROBOT_UP('^'),
    ROBOT_DOWN('v'),
    ROBOT_LEFT('<'),
    ROBOT_RIGHT('>'),
    RETURN('\n');

    companion object {
        fun fromChar(char: Char): Tile {
            return values().find { it.char == char }!!
        }
    }

    override fun toString(): String {
        return "$char"
    }
}