package day8

import common.Utils

object Main {

    private const val INPUT = "/day8/input.txt"

    private const val IMAGE_WIDTH = 25
    private const val IMAGE_HEIGHT = 6

    private const val PIXELS_PER_IMAGE = IMAGE_HEIGHT * IMAGE_WIDTH

    @JvmStatic
    fun main(args: Array<String>) {
        val lines = Utils.getFileFromResource(INPUT).readLines()

        val numbers = lines.reduce { acc, s -> acc + s }.map { it.toString().toInt() }

        val layers = numbers
                .mapIndexed { i, n -> Pair(Math.floorDiv(i, PIXELS_PER_IMAGE), n) }
                .groupBy { it.first }

        val lowestZeros = layers[layers
                .map { layer -> Pair(layer.key, layer.value.count { it.second == 0 }) }
                .minBy { it.second }!!.first]

        val partOne = lowestZeros!!.count { it.second == 2 } * lowestZeros.count { it.second == 1 }

        println("In the layer with the lowest number of 0s, the count of 1s multiplied by 2s was $partOne")

        val initialImage = Image(height = IMAGE_HEIGHT, width = IMAGE_WIDTH)

        val image = layers.values.fold(initialImage) { image, layer ->
            image.addLayer(layer.map { it.second })
        }

        println(image)
    }
}