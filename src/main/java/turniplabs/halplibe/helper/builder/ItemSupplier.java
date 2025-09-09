package turniplabs.halplibe.helper.builder;

import net.minecraft.core.item.Item;

@FunctionalInterface
public interface ItemSupplier {
    Item create(String translationKey, String namespaceID, int id);
}
