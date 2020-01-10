package mt

import mt.TuringMachine.Move.*
import mt.TuringMachine.Rule
import java.util.*

fun getEmptyTm() = TuringMachine(
        rules = mutableListOf(),
        tape = tape(),
        defaultSymbol = "S0",
        initialPosition = 1,
        initialState = "q1",
        lastState = "q0"
)

fun getTest1() = TuringMachine(
        rules = mutableListOf(
                Rule("q1", "S0", "q0", "1", LEFT),
                Rule("q1", "0", "q3", "1", RIGHT),
                Rule("q1", "1", "q1", "2", RIGHT),
                Rule("q1", "2", "q3", "0", RIGHT),

                Rule("q2", "S0", "q0", "2", NONE),
                Rule("q2", "0", "q2", "2", RIGHT),
                Rule("q2", "1", "q1", "0", RIGHT),
                Rule("q2", "2", "q2", "1", RIGHT),

                Rule("q3", "S0", "q0", "0", RIGHT),
                Rule("q3", "0", "q2", "2", LEFT),
                Rule("q3", "1", "q3", "2", NONE),
                Rule("q3", "2", "q1", "2", LEFT)
        ),
        tape = tape("S0", "0", "0", "1", "1", "2", "2", "1", "1", "0", "S0"),
        defaultSymbol = "S0",
        initialPosition = 1,
        initialState = "q1",
        lastState = "q0"
)

fun getTest2() = TuringMachine(
        rules = mutableListOf(
                Rule("q1", "S0", "q0", "1", LEFT),
                Rule("q1", "0", "q3", "1", RIGHT),
                Rule("q1", "1", "q1", "2", RIGHT),
                Rule("q1", "2", "q3", "0", RIGHT),

                Rule("q2", "S0", "q0", "2", NONE),
                Rule("q2", "0", "q2", "2", RIGHT),
                Rule("q2", "1", "q1", "0", RIGHT),
                Rule("q2", "2", "q2", "1", RIGHT),

                Rule("q3", "S0", "q0", "0", RIGHT),
                Rule("q3", "0", "q2", "2", LEFT),
                Rule("q3", "1", "q3", "2", NONE),
                Rule("q3", "2", "q1", "2", LEFT)
        ),
        tape = tape("S0", "0", "1", "2", "0", "1", "2", "0", "1", "2", "S0"),
        defaultSymbol = "S0",
        initialPosition = 1,
        initialState = "q1",
        lastState = "q0"
)

fun getTest3() = TuringMachine(
        rules = mutableListOf(
                Rule("q1", "S0", "q0", "1", LEFT),
                Rule("q1", "0", "q3", "1", RIGHT),
                Rule("q1", "1", "q1", "2", RIGHT),
                Rule("q1", "2", "q3", "0", RIGHT),

                Rule("q2", "S0", "q2", "2", NONE),
                Rule("q2", "0", "q2", "2", RIGHT),
                Rule("q2", "1", "q1", "0", RIGHT),
                Rule("q2", "2", "q2", "1", RIGHT),

                Rule("q3", "S0", "q0", "0", RIGHT),
                Rule("q3", "0", "q2", "2", LEFT),
                Rule("q3", "1", "q3", "2", NONE),
                Rule("q3", "2", "q1", "2", LEFT)
        ),
        tape = tape("S0", "0", "0", "1", "1", "2", "2", "1", "1", "0", "S0"),
        defaultSymbol = "S0",
        initialPosition = 1,
        initialState = "q1",
        lastState = "q0"
)

fun getTest4() = TuringMachine(
        rules = mutableListOf(
                Rule("q1", "S0", "q0", "1", LEFT),
                Rule("q1", "0", "q3", "1", RIGHT),
                Rule("q1", "1", "q1", "2", RIGHT),
                Rule("q1", "2", "q3", "0", RIGHT),

                Rule("q2", "S0", "q2", "0", LEFT),
                Rule("q2", "0", "q2", "2", RIGHT),
                Rule("q2", "1", "q1", "0", RIGHT),
                Rule("q2", "2", "q2", "1", RIGHT),

                Rule("q3", "S0", "q0", "0", RIGHT),
                Rule("q3", "0", "q2", "2", LEFT),
                Rule("q3", "1", "q3", "2", NONE),
                Rule("q3", "2", "q1", "2", LEFT)
        ),
        tape = tape("S0", "0", "0", "1", "1", "2", "2", "1", "1", "0", "S0"),
        defaultSymbol = "S0",
        initialPosition = 1,
        initialState = "q1",
        lastState = "q0"
)

fun getTest5() = TuringMachine(
        rules = mutableListOf(
                Rule("q1", "S0", "q0", "1", LEFT),
                Rule("q1", "0", "q3", "1", RIGHT),
                Rule("q1", "1", "q1", "2", RIGHT),
                Rule("q1", "2", "q3", "0", RIGHT),

                Rule("q2", "S0", "q0", "2", NONE),
                Rule("q2", "0", "q2", "2", RIGHT),
                Rule("q2", "1", "q1", "0", RIGHT),
                Rule("q2", "2", "q2", "1", RIGHT),

                Rule("q3", "S0", "q0", "0", RIGHT),
                Rule("q3", "0", "q2", "2", LEFT),
                Rule("q3", "1", "q3", "2", NONE),
                Rule("q3", "2", "q1", "2", LEFT)
        ),
        tape = tape("S0", "0", "1", "2", "0", "1", "2", "0", "1", "2", "S0"),
        defaultSymbol = "S0",
        initialPosition = 1,
        initialState = "q1",
        lastState = "q0"
)

val tests = listOf(::getTest1, ::getTest2, ::getTest3, ::getTest4, ::getTest5)

private fun <S> tape(vararg symbol: S, indexOfFirst: Int = 0): TreeMap<Int, S> {
    var index = indexOfFirst
    return symbol.associateByTo(TreeMap()) { index++ }
}