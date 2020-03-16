package utils;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UtilitiesTest {
    @Test
    public void testIterateSimultaneouslySameSizeLists() {
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
    public void testIterateSimultaneouslyDifferentSizeLists() {
        List<String> strings = new ArrayList<>(Arrays.asList("one", "two", "three"));
        List<Integer> integersShort = new ArrayList<>(Arrays.asList(1, 2));
        List<Integer> integersLong = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        List<String> results1 = new ArrayList<>();
        Utilities.iterateSimultaneously(
                strings, integersShort,
                (String string, Integer integer) -> results1.add(String.format("%s %d", string, integer)));
        assertEquals(2, results1.size());
        assertEquals("one 1", results1.get(0));
        assertEquals("two 2", results1.get(1));
        List<String> results2 = new ArrayList<>();
        Utilities.iterateSimultaneously(
                strings, integersLong,
                (String string, Integer integer) -> results2.add(String.format("%s %d", string, integer)));
        assertEquals(3, results2.size());
        assertEquals("one 1", results2.get(0));
        assertEquals("two 2", results2.get(1));
        assertEquals("three 3", results2.get(2));
    }

    @Test
    public void testIsEven() {
        assertTrue(Utilities.isEven(-2));
        assertFalse(Utilities.isEven(-1));
        assertTrue(Utilities.isEven(0));
        assertFalse(Utilities.isEven(1));
        assertTrue(Utilities.isEven(2));
    }

    @Test
    public void testDivideRoundUp() {
        assertEquals(-1, Utilities.divideRoundUp(-10, 10));
        assertEquals(0, Utilities.divideRoundUp(-9, 10));
        assertEquals(0, Utilities.divideRoundUp(-1, 10));
        assertEquals(0, Utilities.divideRoundUp(0, 10));
        assertEquals(1, Utilities.divideRoundUp(1, 10));
        assertEquals(1, Utilities.divideRoundUp(9, 10));
        assertEquals(1, Utilities.divideRoundUp(10, 10));
        assertEquals(2, Utilities.divideRoundUp(11, 10));
    }
}
