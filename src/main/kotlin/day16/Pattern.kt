package day16

class Pattern<T>(private val list : List<T>, private val repeatEach : Int) {
    var pointer = 0
    private var repetitions = 0

    fun nextItem() : T {
        val next = list[pointer]
        repetitions++

        if (repetitions > repeatEach) {
            if (pointer == list.size - 1) {
                pointer = 0
            } else {
                pointer++
            }
            repetitions = 0
        }

        return next
    }
}