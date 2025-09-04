package turniplabs.halplibe.helper;

import net.minecraft.core.data.tag.Tag;
import net.minecraft.core.item.Item;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public final class ItemBuilder implements Cloneable {
    private final String modId;
    @Nullable
    private String overrideKey = null;
    @Nullable
    private String textureKey = null;
    @Nullable
    private Tag<Item>[] tags = null;
    private Integer stackSize = null;
    private Integer maxDamage = null;
    @Nullable
    private Supplier<Item> containerItemSupplier = null;
    private boolean compactKeys = false;
    public ItemBuilder(String modId){
        this.modId = modId;
    }
    @Override
    public ItemBuilder clone() {
        try {
            // none of the fields are mutated so this should be fine
            return (ItemBuilder) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /**
     * Sets the key to the built {@link Item}, for example if you set the key "gem.sapphire" the actual key ingame will be "item.<modid>.gem.sapphire"
     * @param key Override translation key for the {@link Item}
     * @return @return Copy of {@link ItemBuilder}
     */
    @SuppressWarnings({"unused"})
    public ItemBuilder setKey(String key){
        ItemBuilder builder = this.clone();
        builder.overrideKey = key;
        return builder;
    }
    /**
     * Sets stack size for the built {@link Item}, will override any class default stacksizes
     * @param stackSize Stack size of the {@link Item}
     * @return @return Copy of {@link ItemBuilder}
     */
    @SuppressWarnings({"unused"})
    public ItemBuilder setStackSize(int stackSize){
        ItemBuilder builder = this.clone();
        builder.stackSize = stackSize;
        return builder;
    }

    /**
     * Sets max durability for the built {@link Item}, will override any class default max damage values.
     * Probably only really affects tool classes.
     * @param maxDamage Max durability of the {@link Item}
     * @return @return Copy of {@link ItemBuilder}
     */
    @SuppressWarnings({"unused"})
    public ItemBuilder setMaxDamage(int maxDamage){
        ItemBuilder builder = this.clone();
        builder.maxDamage = maxDamage;
        return builder;
    }

    /**
     * Sets the container item for the built item. For example {@code Item.bucketMilk} uses the container item {@code Item.bucket}
     * @param itemSupplier Supplies the {@link Item} to set as the container item
     * @return @return Copy of {@link ItemBuilder}
     */
    @SuppressWarnings({"unused"})
    public ItemBuilder setContainerItem(Supplier<Item> itemSupplier){
        ItemBuilder builder = this.clone();
        builder.containerItemSupplier = itemSupplier;
        return builder;
    }

    /**
     * Overrides all previous tags with the ones provided
     * @return @return Copy of {@link ItemBuilder}
     */
    @SafeVarargs
    @SuppressWarnings({"unused"})
    public final ItemBuilder setTags(Tag<Item>... tags) {
        ItemBuilder itemBuilder = this.clone();
        itemBuilder.tags = tags;
        return itemBuilder;
    }

    /**
     * Adds provided tags to previously specified tags
     * @return @return Copy of {@link ItemBuilder}
     */
    @SafeVarargs
    @SuppressWarnings({"unused"})
    public final ItemBuilder addTags(Tag<Item>... tags) {
        ItemBuilder itemBuilder = this.clone();
        itemBuilder.tags = ArrayUtils.addAll(this.tags, tags);
        return itemBuilder;
    }

    /**
     * Allows {@link ItemBuilder#build(String, String, int, ItemSupplier)} to
     * append {@code "MOD_ID:item/"} and {@code "MOD_ID."} to namespace ID and translation
     * key respectively.
     * @return Copy of {@link ItemBuilder}
     */
    @SuppressWarnings("unused")
    public ItemBuilder compactKeys() {
        ItemBuilder itemBuilder = this.clone();
        this.compactKeys = true;
        return itemBuilder;
    }

    @SuppressWarnings("unused")
    public <T extends Item> T build(String translationKey, String namespaceID, int id, ItemSupplier<T> itemSupplier){
        String prefix = modId + ":item/";
        T item = itemSupplier.create(modId + "." + translationKey, prefix + namespaceID, id);

        return build(item);
    }

    /**
     * Applies the builder configuration to the supplied item.
     * @param item Input item object
     * @return Returns the input item after builder settings are applied to it.
     */
    @SuppressWarnings("unused")
    public <T extends Item> T build(T item){
        if (tags != null) {
            item.withTags(tags);
        }

        if (stackSize != null){
            item.setMaxStackSize(stackSize);
        }

        if (containerItemSupplier != null){
            item.setContainerItem(containerItemSupplier.get());
        }

        if (maxDamage != null){
            item.setMaxDamage(maxDamage);
        }

        return item;
    }

    public interface ItemSupplier<T> {
        T create(String translationKey, String namespaceID, int id);
    }

}
