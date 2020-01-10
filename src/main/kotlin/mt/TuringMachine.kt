package mt

import java.util.*

class TuringMachine<S, C>(
        val rules: MutableList<Rule<S, C>>,
        initialState: S,
        val lastState: S,
        initialPosition: Int = 0,
        val defaultSymbol: C,
        val tape: TreeMap<Int, C> = TreeMap()
) : Iterator<Unit> {
    var currentPosition = initialPosition; private set
    var currentState = initialState; private set
    val currentRule: Rule<S, C>? get() = findNextRule()
    var stepsCount = 0; private set

    private fun findNextRule(): Rule<S, C>? {
        val currentSymbol = tape[currentPosition] ?: defaultSymbol
        return rules.find { it.state == currentState && it.tapeSymbol == currentSymbol }
    }

    override fun hasNext() = currentState != lastState && currentRule != null

    override fun next() {
        if (!hasNext())
            throw NoSuchElementException("No rule for state")
        val rule = currentRule!!
        tape[currentPosition] = rule.writeSymbol
        currentPosition += rule.move.delta
        val nextState = rule.nextState
        currentState = nextState
//        currentRule = findNextRule()
        ++stepsCount
    }

    data class Rule<out S, out V>(
            val state: S,
            val tapeSymbol: V,
            val nextState: S,
            val writeSymbol: V,
            val move: Move
    ) {
        fun _toString() = " $nextState $writeSymbol $move "
    }

    enum class Move(val delta: Int) {
        LEFT(-1) {
            override fun toString() = "L"
        },
        NONE(0) {
            override fun toString() = "C"
        },
        RIGHT(1) {
            override fun toString() = "R"
        }
    }
}