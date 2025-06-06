package turniplabs.halplibe.mixin.namespace;

import net.minecraft.core.item.Item;
import net.minecraft.core.util.collection.NamespaceID;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import turniplabs.halplibe.helper.model.NamespaceObject;

@Mixin(value = Item.class, remap = false)
public abstract class ItemMixin implements NamespaceObject {
	@Shadow
	@Final
	public NamespaceID namespaceID;

	@Override
	public NamespaceID halplibe$id() {
		return this.namespaceID;
	}

	@Override
	public String halplibe$cleanValue() {
		return this.namespaceID.value().replaceFirst("item/", "");
	}
}

