package turniplabs.halplibe.helper.builder;

import org.jetbrains.annotations.Range;

public class Counter {
    public short value = 0;
    public final Type type;

    private Counter(Type type, short value) {
        this.type = type;
        this.value = value;
    }

    public static Counter block(@Range(from = 0, to = 16383) int value) {
        return new Counter(Type.BLOCK, (short) value);
    }
    public static Counter block() {
        return block(Type.BLOCK.min);
    }
    public static Counter item(@Range(from = 16384, to = 32767) int value) {
        return new Counter(Type.ITEM, (short) value);
    }
    public static Counter item() {
        return block(Type.ITEM.min);
    }
    public int next() {
        return value++;
    }

    public void checkType(Type expected) {
        if (type != expected) {
            throw new IllegalArgumentException(String.format("Expected %s counter but %s was given.", expected, type));
        }
    }

    public enum Type {
        BLOCK(0, 16383),
        ITEM(16384, 32767);
        final int min, max;

        Type(int min, int max) {
            this.min = min;
            this.max = max;
        }
    }
}
