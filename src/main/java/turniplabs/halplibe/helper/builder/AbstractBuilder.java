package turniplabs.halplibe.helper.builder;

import net.minecraft.core.data.tag.ITaggable;
import net.minecraft.core.data.tag.Tag;
import net.minecraft.core.item.IItemConvertible;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import turniplabs.halplibe.helper.ItemBuilder;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbstractBuilder<A extends ITaggable<A>, I, C> {
    private final @NotNull String builderPrefix;
    public final @NotNull String modID;
    protected @NotNull String formattableKey = "%s";
    protected @NotNull String formattableID = "%s";
    protected @NotNull String[] convertersKey = new String[0];
    protected @NotNull String[] convertersID = new String[0];
    // Huh?
    // interface ITaggable<E extends ITaggable<E>>
    protected @Nullable Tag<A>[] tags = null;
    protected @Nullable String namespaceID = null;
    protected @Nullable String translationKey = null;
    protected int id = -1;
    protected @Nullable Counter counter = null;
    protected final @Nullable Counter.Type counterType;
    protected @Nullable Supplier<@NotNull IItemConvertible> statParentSupplier = null;

    protected AbstractBuilder(@NotNull String builderPrefix, @Nullable Counter.Type counterType, @NotNull String modID) {
        this.builderPrefix = builderPrefix;
        this.modID = modID;
        this.counterType = counterType;
    }

    protected AbstractBuilder(@NotNull AbstractBuilder<A, I, C> other) {
        this.builderPrefix = other.builderPrefix;
        this.modID = other.modID;
        this.formattableKey = other.formattableKey;
        this.formattableID = other.formattableID;
        // This is fine for Strings, but this produces a shallow copy
        this.convertersKey = Arrays.copyOf(other.convertersKey, other.convertersKey.length);
        this.convertersID = Arrays.copyOf(other.convertersID, other.convertersID.length);
        this.tags = other.tags == null ? null : Arrays.copyOf(other.tags, other.tags.length);

        //These should probably not be copied?
        this.namespaceID = other.namespaceID;
        this.translationKey = other.translationKey;
        this.id = other.id;

        //Pass by reference
        this.counter = other.counter;
        this.counterType = other.counterType;
        this.statParentSupplier = other.statParentSupplier;
    }
    protected abstract <T extends AbstractBuilder<A,I,C>> T copy();
    protected abstract @NotNull <T extends A> T buildInternal(@NotNull I supplier);

    @SuppressWarnings("UnnecessaryLocalVariable")
    public final @NotNull <T extends A> T build(String translationKey, String namespaceID, int id, @NotNull I supplier) {
        this.translationKey = this.modID + "." + translationKey;
        this.namespaceID = this.modID + this.builderPrefix + namespaceID;
        this.id = id;
        if (counter != null) this.id = counter.next();

        T t = buildInternal(supplier);
        // withTags is not part of the interface for some reason

        return t;
    }

    @SuppressWarnings("unused")
    public final @NotNull <T extends A> T build(String translationKey, String namespaceID, @NotNull I supplier) {
        if (id <= -1) throw new NullPointerException("Numeric ID cannot be null. Use id(1234) to assign, or autoID(yourCounter) to set automatically.");

        return build(translationKey, namespaceID, id, supplier);
    }

    @SuppressWarnings("unused")
    public final @NotNull <T extends A> T build(String mergedKey, @NotNull I supplier) {
        if (id <= -1) throw new NullPointerException("Numeric ID cannot be null. Use id(1234) to assign, or autoID(yourCounter) to set automatically.");

        return build(mergedKey, id, supplier);
    }

    @SuppressWarnings("unused")
    public final @NotNull <T extends A> T build(String mergedKey, int id, @NotNull I supplier) {
        this.id = id;
        return build(mergedKey.replaceAll("_", "."), mergedKey, id, supplier);
    }

    @SuppressWarnings("unused")
    public final @NotNull <T extends A> T build(int id, @NotNull I supplier) {
        throwInvalidLength(convertersID, 1, "Namespace ID cannot be null. Use name(\"some_name\") to assign.");
        throwInvalidLength(convertersKey, 1, "Translation key cannot be null. Use key(\"some.key\") to assign.");

        return build(String.format(formattableKey, (Object[]) convertersKey), String.format(formattableID, (Object[]) convertersID), id, supplier);
    }

    @SuppressWarnings("unused")
    public final @NotNull <T extends A> T build(@NotNull I supplier) {
        if (id <= -1) throw new NullPointerException("Numeric ID cannot be null. Use id(1234) to assign, or autoID(yourCounter) to set automatically.");

        return build(id, supplier);
    }

    public <T> void throwInvalidLength(T[] arr, int minLength, String message) {
        if (arr.length < minLength) throw new NullPointerException(message);
    }

    protected final void validateFormat(@NotNull String format, @NotNull String name) {
        Objects.requireNonNull(format, String.format("%s was null.", name));
        if (!format.contains("%s")) throw new IllegalArgumentException(String.format("%s must contain at least one '%s' format specifier.", name, "%s"));
    }


    @SuppressWarnings({"unused", "unchecked"})
    public C setKeyFormat(@NotNull String keyFormat) {
        validateFormat(keyFormat, "Translation key format");
        AbstractBuilder<A, I, C> builder = this.copy();
        builder.formattableKey = keyFormat;
        return (C) builder;
    }

    @SuppressWarnings({"unused", "unchecked"})
    public C key(@NotNull String... key) {
        AbstractBuilder<A, I, C> builder = this.copy();
        builder.convertersKey = key;
        return (C) builder;
    }

    @SuppressWarnings({"unused", "unchecked"})
    public C setIDFormat(@NotNull String IDFormat) {
        validateFormat(IDFormat, "Namespace ID format");
        AbstractBuilder<A, I, C> builder = this.copy();
        builder.formattableID = IDFormat;
        return (C) builder;
    }

    @SuppressWarnings({"unused", "unchecked"})
    public C name(@NotNull String... name) {
        AbstractBuilder<A, I, C> builder = this.copy();
        builder.convertersID = name;
        return (C) builder;
    }

    @SuppressWarnings({"unused", "unchecked"})
    public C mergedKey(@NotNull String... mergedKey) {
        AbstractBuilder<A, I, C> builder = this.copy();
        builder.convertersKey = mergedKey;
        builder.convertersID = mergedKey;
        return (C) builder;
    }

    @SuppressWarnings({"unused", "unchecked"})
    public C id(int id) {
        if (id < 0 || id > Short.MAX_VALUE) throw new IllegalArgumentException("Numeric ID must be within [0, 32767]");
        AbstractBuilder<A, I, C> builder = this.copy();
        builder.id = id;
        return (C) builder;
    }

    /**
     * Overrides all previous tags with the ones provided
     * @return Copy of {@link ItemBuilder}
     */
    @SafeVarargs
    @SuppressWarnings({"unused", "unchecked"})
    public final C setTags(Tag<A>... tags) {
        AbstractBuilder<A, I, C> builder = this.copy();
        builder.tags = tags;
        return (C) builder;
    }

    /**
     * Adds provided tags to previously specified tags
     * @return Copy of {@link ItemBuilder}
     */
    @SafeVarargs
    @SuppressWarnings({"unused", "unchecked"})
    public final C addTags(Tag<A>... tags) {
        AbstractBuilder<A, I, C> builder = this.copy();
        builder.tags = ArrayUtils.addAll(this.tags, tags);
        return (C) builder;
    }

    @SuppressWarnings({"unused", "unchecked"})
    public final C autoID(Counter counter) {
        counter.checkType(counterType);
        AbstractBuilder<A, I, C>builder = this.copy();
        builder.counter = counter; // Passed by ref
        return (C) builder;
    }

    /**
     * Sets the stat parent of the ItemConvertible. All statistics will be logged
     * to the parent.
     * @param statParentSupplier the stat parent supplier
     * @return a copy of BlockBuilder
     */
    @SuppressWarnings({"unused", "unchecked"})
    public final C setStatParent(@NotNull Supplier<@NotNull IItemConvertible> statParentSupplier) {
        AbstractBuilder<A, I, C> builder = this.copy();
        builder.statParentSupplier = statParentSupplier;
        return (C) builder;
    }

    public void ifTrue(@Nullable Boolean bool, Runnable runnable) {
        if (Boolean.TRUE.equals(bool)) runnable.run();
    }

    public <T> void ifNotNull(T t, Consumer<T> consumer) {
        if (t != null) consumer.accept(t);
    }
}
