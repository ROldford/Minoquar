package utils;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UtiltitiesTest {
    @Test
    public void testIterateSimultaneously() {
        List<String> strings = new ArrayList<>(Arrays.asList("one", "two", "three"));
        List<Integer> integers = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<String> results = new ArrayList<>();
        Utilities.iterateSimultaneously(
                strings, integers,
                (String string, Integer integer) -> results.add(String.format("%s %d", string, integer)));
        assertEquals("one 1", results.get(0));
        assertEquals("two 2", results.get(1));
        assertEquals("three 3", results.get(2));
    }

    @Test
    public void testIsEven() {
        assertTrue(Utilities.isEven(-2));
        assertFalse(Utilities.isEven(-1));
        assertTrue(Utilities.isEven(0));
        assertFalse(Utilities.isEven(1));
        assertTrue(Utilities.isEven(2));
    }
}
