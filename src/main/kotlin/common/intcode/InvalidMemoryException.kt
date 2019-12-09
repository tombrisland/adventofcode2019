package common.intcode

import java.lang.IndexOutOfBoundsException
import kotlin.RuntimeException

class InvalidMemoryException(val index : Long) : RuntimeException() {
}

