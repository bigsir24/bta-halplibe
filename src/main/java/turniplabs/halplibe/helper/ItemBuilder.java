package turniplabs.halplibe.helper;

import net.minecraft.core.item.Item;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import turniplabs.halplibe.helper.builder.AbstractBuilder;
import turniplabs.halplibe.helper.builder.Counter;
import turniplabs.halplibe.helper.builder.ItemSupplier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class ItemBuilder extends AbstractBuilder<Item, ItemSupplier, ItemBuilder> {
    private static final String PREFIX = ":item/";
    private @Nullable String overrideKey = null;
    private @Nullable Integer stackSize = null;
    private @Nullable Integer maxDamage = null;
    private @Nullable Supplier<Item> containerItemSupplier = null; //Why is this a supplier?

    @SuppressWarnings("unused")
    public ItemBuilder(String modId){
        super(PREFIX, Counter.Type.ITEM, modId);
    }

    private ItemBuilder(ItemBuilder other){
        super(other);
        this.overrideKey = other.overrideKey;
        this.stackSize = other.stackSize;
        this.maxDamage = other.maxDamage;
        this.containerItemSupplier = other.containerItemSupplier;
    }

    /**
     * Sets the key to the built {@link Item}, for example if you set the key "gem.sapphire" the actual key ingame will be "item.mod_id.gem.sapphire"
     * @param key Override translation key for the {@link Item}
     * @return Copy of {@link ItemBuilder}
     */
    @Deprecated
    @SuppressWarnings({"unused"})
    public ItemBuilder setKey(String key){
        ItemBuilder builder = this.copy();
        builder.overrideKey = key;
        return builder;
    }

    /**
     * Sets stack size for the built {@link Item}, will override any class default stacksizes
     * @param stackSize Stack size of the {@link Item}
     * @return Copy of {@link ItemBuilder}
     */
    @SuppressWarnings({"unused"})
    public ItemBuilder setStackSize(int stackSize){
        ItemBuilder builder = this.copy();
        builder.stackSize = stackSize;
        return builder;
    }

    /**
     * Sets max durability for the built {@link Item}, will override any class default max damage values.
     * Probably only really affects tool classes.
     * @param maxDamage Max durability of the {@link Item}
     * @return Copy of {@link ItemBuilder}
     */
    @SuppressWarnings({"unused"})
    public ItemBuilder setMaxDamage(int maxDamage){
        ItemBuilder builder = this.copy();
        builder.maxDamage = maxDamage;
        return builder;
    }

    /**
     * Sets the container item for the built item. For example {@code Items.BUCKET_MILK} uses the container item {@code Items.BUCKET}
     * @param itemSupplier Supplies the {@link Item} to set as the container item
     * @return Copy of {@link ItemBuilder}
     */
    @SuppressWarnings({"unused"})
    public ItemBuilder setContainerItem(@NotNull Supplier<Item> itemSupplier){
        ItemBuilder builder = this.copy();
        builder.containerItemSupplier = itemSupplier;
        return builder;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected  <T extends AbstractBuilder<Item, ItemSupplier, ItemBuilder>> T copy() {
        return (T) new ItemBuilder(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T extends Item> @NotNull T buildInternal(@NotNull ItemSupplier supplier) {
        T item = (T) supplier.create(this.translationKey, this.namespaceID, this.id);

        ifNotNull(stackSize, item::setMaxStackSize);
        // Maybe actually do lazy init since it's already a supplier?
        if(containerItemSupplier != null) item.setContainerItem(containerItemSupplier.get());
        ifNotNull(maxDamage, item::setMaxDamage);
        ifNotNull(tags, item::withTags);
        // Not assigned in AbstractBuilder since it's not part of any interface
        // And this is probably the least hacky way
        ifNotNull(statParentSupplier, item::setStatParent);

        return item;
    }

    /**
     * Applies the builder configuration to the supplied item.
     * @param item Input item object
     * @return Returns the input item after builder settings are applied to it.
     * @deprecated Use {@link ItemBuilder#build(String, String, int, ItemSupplier)} or {@link ItemBuilder#build(String, int, ItemSupplier)}
     */
    @Deprecated
    @SuppressWarnings("unused")
    public <T extends Item> T build(T item){
        buildInternal((a,b,c) -> item);

        List<String> tokens;

        if (overrideKey != null){
            tokens = Arrays.stream(overrideKey.split("\\.")).collect(Collectors.toList());
        } else {
            tokens = Arrays.stream(item.getKey().split("\\.")).collect(Collectors.toList());
        }

        List<String> newTokens = new ArrayList<>();
        newTokens.add(modID);
        newTokens.addAll(tokens.subList(1, tokens.size()));

        item.setKey(StringUtils.join(newTokens, "."));

        return item;
    }

}
