package utils;

import java.util.Iterator;
import java.util.function.BiConsumer;

public final class Utilities {
    private Utilities() {}

    // EFFECTS: iterates through two Iterable collections of the same size simultaneously
    //          Biconsumer takes in a lambda, allowing actions to be taken on the corresponding items from each list
    // Based on code by nix9 (https://stackoverflow.com/users/5073678/nix9)
    // and eugene82 (https://stackoverflow.com/users/1882010/eugene82)
    // found on StackOverflow at https://stackoverflow.com/a/37612232
    public static <T1, T2> void iterateSimultaneously(Iterable<T1> c1, Iterable<T2> c2, BiConsumer<T1, T2> consumer) {
        Iterator<T1> i1 = c1.iterator();
        Iterator<T2> i2 = c2.iterator();
        while (i1.hasNext() && i2.hasNext()) {
            consumer.accept(i1.next(), i2.next());
        }
    }

    // EFFECTS: returns true if given int is even
    public static boolean isEven(int i) {
        return i % 2 == 0;
    }
}
