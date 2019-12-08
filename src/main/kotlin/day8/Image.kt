package day8

import kotlin.RuntimeException

class Image(private val height : Int, private val width : Int) {

    private val pixelsPerImage = height * width
    private val pixels = (0 until pixelsPerImage).map { Pixel() }

    fun addLayer(layer : List<Int>) : Image {
        if (layer.size != pixelsPerImage)
            throw RuntimeException("Layer contained invalid number of pixels ${layer.size}")

        layer.forEachIndexed { i, value -> pixels[i].layers.add(value) }

        return this
    }

    override fun toString(): String {
        val output = StringBuilder()
        var i = 0

        (0 until height).forEach { h ->
            (0 until width).forEach { w ->
                output.append(pixels[i].colour())
                i++
            }
            output.append("\n")
        }

        return output.toString()
    }

    class Pixel() {
        val layers = mutableListOf<Int>()

        fun colour() : Colour {
            return layers.foldRight(Colour.TRANSPARENT) { it, pixelColour ->
                when(Colour.fromInt(it)) {
                    Colour.BLACK -> Colour.BLACK
                    Colour.WHITE -> Colour.WHITE
                    Colour.TRANSPARENT -> pixelColour
                }
            }
        }

        enum class Colour {
            BLACK, WHITE, TRANSPARENT;

            companion object {
                fun fromInt(int : Int) : Colour {
                    return when(int) {
                        0 -> BLACK
                        1 -> WHITE
                        2 -> TRANSPARENT
                        else ->
                            throw RuntimeException("Invalid integer supplied for colour $int")
                    }
                }
            }

            override fun toString(): String {
                return when (this) {
                    BLACK -> " "
                    WHITE -> "1"
                    TRANSPARENT -> throw RuntimeException("A transparent pixel has no colour")
                }
            }
        }
    }
}