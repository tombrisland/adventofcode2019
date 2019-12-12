package common.intcode

import common.intcode.IntCode.Code.*
import java.lang.RuntimeException

fun Boolean.toInt() = if (this) 1 else 0
fun Boolean.toLong() = toInt().toLong()

class Program(instructions: List<Long>) {

    private val memory = instructions.toMutableList()
    private val inputs = mutableListOf<Long>()

    var isExited = false
    private var instructionPointer = 0L
    private var relativeBase = 0L

    constructor(program: List<Long>, phase: Int) : this(program) {
        inputs.add(phase.toLong())
    }

    fun execute() : Long {
        var output: Long?

        do {
            val instr = nextInstruction()

//            println("$instr $relativeBase")

            if (instr.outputIndex != null) {
                expandMemory(instr.outputIndex!!.toLong())
            }

            instructionPointer += instr.intCode.value.size()

            output = handleInstruction(instr)

            if (isExited || output != null) {
                return output ?: inputs.last()
            }

        } while (instructionPointer < memory.size)

        throw RuntimeException("Program didn't produce an output")
    }

    fun execute(input: Long): Long {
        inputs.add(input)
        return execute()
    }

    private fun handleInstruction(inst: Instruction) : Long? {
        var output : Long? = null

        when (inst.intCode.value) {
            ADD -> {
                memory[inst.outputIndex!!] = inst.p1() + inst.p2()
            }
            MUL -> {
                memory[inst.outputIndex!!] = inst.p1() * inst.p2()
            }
            INP -> {
                memory[inst.outputIndex!!] = removeFirst(inputs)
            }
            OUT -> {
                output = inst.p1()
            }
            JNZ -> {
                if (inst.p1() != 0L) instructionPointer = inst.p2()
            }
            JIZ -> {
                if (inst.p1() == 0L) instructionPointer = inst.p2()
            }
            LES -> {
                memory[inst.outputIndex!!] = (inst.p1() < inst.p2()).toLong()
            }
            EQU -> {
                memory[inst.outputIndex!!] = (inst.p1() == inst.p2()).toLong()
            }
            RBS -> {
                relativeBase += inst.p1()
            }
            HLT -> {
                isExited = true
            }
        }

        return output
    }

    private fun removeFirst(list : MutableList<Long>) : Long {
        return list.removeAt(0)
    }

    private fun nextInstruction() : Instruction {
        return try {
            val remaining = memory.size - (instructionPointer + 3)

            val input = if (remaining > 4) {
                memory.subList(instructionPointer.toInt(), (instructionPointer + 4).toInt())
            } else {
                memory.subList(instructionPointer.toInt(), memory.size)
            }

            Instruction(memory, relativeBase, input)
        } catch (e : InvalidMemoryException) {
            expandMemory(e.index)
            nextInstruction()
        }
    }

    private fun expandMemory(index : Long) {
        while (memory.size <= index) {
            memory.add(0)
        }
    }

    override fun toString(): String {
        return "Program(inputs=$inputs, isExited=$isExited)"
    }
}
