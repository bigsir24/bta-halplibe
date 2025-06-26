package turniplabs.halplibe.helper.model;

import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.util.dispatch.Dispatcher;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.helper.model.models.IconStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class ModelBuilder<O, M, B> {
    protected final String modID;
    protected final Dispatcher<O, M> dispatcher;
    protected Function<O, M> modelSupplier;
	protected Consumer<M> modelConsumer;

    private final List<String> extendedMap = new ArrayList<>();
    private final List<String> counterStringList = new ArrayList<>();
    private final List<Integer> countList = new ArrayList<>();

    protected ModelBuilder(String modID, Dispatcher<O, M> dispatcher) {
        this.dispatcher = dispatcher;
        this.modID = modID;
    }

    public abstract B copy();

    protected B copyToInternal(ModelBuilder<O, M, B> newBuilder) {
        newBuilder.modelSupplier = modelSupplier;
        newBuilder.modelConsumer = modelConsumer;
        newBuilder.extendedMap.addAll(extendedMap);
        newBuilder.counterStringList.addAll(counterStringList);
        newBuilder.countList.addAll(countList);
        return (B) newBuilder;
    }

    /**Method used to get a BlockModelBuilder, equivalent to using the constructor
     * @return new BlockModelBuilder */
    public static BlockModelBuilder block(@NotNull String modID) {
        return new BlockModelBuilder(modID, BlockModelDispatcher.getInstance());
    }

    /**Method used to get a ItemModelBuilder, equivalent to using the constructor
     * @return new BlockModelBuilder */
    public static ItemModelBuilder item(@NotNull String modID) {
        return new ItemModelBuilder(modID, ItemModelDispatcher.getInstance());
    }

    /**Method used to get a BlockModelBuilder, equivalent to using the constructor
     * @return new BlockModelBuilder */
    @SuppressWarnings({"unchecked", "unused"})
    public B buildsModel(@NotNull Function<O, M> modelSupplier) {
        this.modelSupplier = modelSupplier;
        return (B) this;
    }

    /**Method to run after model building has completed (all textures assigned). */
	@SuppressWarnings({"unchecked", "unused"})
	public B postBuild(@NotNull Consumer<M> modelConsumer) {
		this.modelConsumer = modelConsumer;
		return (B) this;
	}

    protected abstract void onBuild(@NotNull O block, @NotNull M model, @NotNull String namespaceValue);

    private int getCount(int index) {
        if (countList.isEmpty()) return 0;

        return index < 0 || index >= countList.size() ? countList.get(countList.size() - 1) : countList.get(index);
    }

    private void onBuildExt(M model, String namespaceValue) {
        if (!(model instanceof IconStorage) || extendedMap.isEmpty()) return;
        //TODO maybe warning when using ext but no data in maps?

        IconStorage extModel = (IconStorage) model;

        // Add icons from ext list to the model
        for (String str : this.extendedMap) {
            String formatted = String.format(str, namespaceValue);
            extModel.addIconInternal(modID, namespaceValue, formatted);
        }

        if(counterStringList.isEmpty()) return;

        // Add numbered icons from ext list to the model
        for (int i = 0; i < counterStringList.size(); i++) {
            int index = getCount(i);

            String formatted = String.format(counterStringList.get(i), namespaceValue, index);
            extModel.addIconInternal(modID, namespaceValue, formatted);
        }
    }

    /**<p><b><i>!!! To be used with Models implementing IconStorage only !!!</b></i><br></p>
     * <p>Adds a formattable string to the texture look-up list</p>
     * Example: %s_my_texture_key
     *
     * @param stringFormat the string to format
     * @return this*/
    @SuppressWarnings({"unchecked", "unused"})
    public B extMapping(String stringFormat) {
        this.extendedMap.add(stringFormat);
        return (B) this;
    }

    /**<p><b><i>!!! To be used with Models implementing IconStorage only !!!</b></i><br></p>
     * <p>Adds a formattable string to the counting texture look-up list.</p>
     * Example: %s_my_texture_key_%d
     *
     * @param stringFormat the string to format
     * @return this*/
    @SuppressWarnings({"unchecked", "unused"})
    public B extMappingCounter(String stringFormat) {
        if (!stringFormat.contains("%d")) {
            HalpLibe.LOGGER.warn("'{}' should contain %d conversion specifier!", stringFormat);
            return (B) this;
        }

        this.counterStringList.add(stringFormat);
        return (B) this;
    }

    /**Count of textures to be indexed.
     *
     * <p>If there are more countable texture keys than assigned counts, strings that have
     * no count mapping will use the last count in the list.</p>
     *
     * <p>Clear the count list by calling {@link ModelBuilder#clearCount()}.</p>
     *
     * @param count the number of iterations
     * @return this */
    @SuppressWarnings({"unchecked", "unused"})
    public B count(int count) {
        this.countList.add(count);
        return (B) this;
    }

    /** @see ModelBuilder#count(int)  */
    @SuppressWarnings({"unchecked", "unused"})
    public B count(int count1, int count2) {
        this.countList.add(count1);
        this.countList.add(count2);
        return (B) this;
    }

    /** @see ModelBuilder#count(int) */
    @SuppressWarnings({"unchecked", "unused"})
    public B count(int @NotNull ... count) {
        for (int i = 0; i < count.length; i++) {
            countList.add(count[i]);
        }
        return (B) this;
    }

    /**<p>Clears the count list. This does not affect the assigned countable texture keys.</p>
     * @return this */
    @SuppressWarnings({"unchecked", "unused"})
    public B clearCount() {
        countList.clear();
        return (B) this;
    }

    /**<p>Creates the model and assigns it to the target.</p>
     * @param target the owner of the model
     * @param key the key to use when replacing %s format specifiers (by default the namespace id of the target is used) */
    @SuppressWarnings({"unchecked", "UnusedReturnValue"})
	public B build(@NotNull O target, @NotNull String key) {
		M model = this.modelSupplier.apply(target);

        this.onBuildExt(model, key);
		this.onBuild(target, model, key);

		if (this.modelConsumer != null) this.modelConsumer.accept(model);

		this.dispatcher.addDispatch(target, model);

        return (B) this;
	}

    /**Creates the model and assigns it to the target.
     * @param target the owner of the model */
    @SuppressWarnings("unused")
    public B build(@NotNull O target) {
        NamespaceObject object = (NamespaceObject) target;
        String namespaceValue = object.halplibe$cleanValue();
		return build(target, namespaceValue);
    }
}
