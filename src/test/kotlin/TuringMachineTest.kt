import TuringMachine.Move
import TuringMachine.Rule
import org.junit.Assert
import org.junit.Test

internal class TuringMachineTest {
    @Test
    fun test1() {
        val turingMachine = TuringMachine(
                rules = listOf(
                        Rule("A", 0, "B", 1, Move.RIGHT),
                        Rule("A", 1, "C", 1, Move.LEFT),
                        Rule("B", 0, "A", 1, Move.LEFT),
                        Rule("B", 1, "B", 1, Move.RIGHT),
                        Rule("C", 0, "B", 1, Move.LEFT),
                        Rule("C", 1, "HALT", 1, Move.RIGHT)
                ),
                initialState = "A",
                lastState = "HALT",
                defaultSymbol = 0
        )
        turingMachine.take(42).forEach { }
        Assert.assertArrayEquals(intArrayOf(1, 1, 1, 1, 1, 1), turingMachine.tape.values.toIntArray())
    }
}