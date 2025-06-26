package turniplabs.halplibe.helper.model.models;

import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.item.Item;

import java.util.HashMap;
import java.util.Map;

public class ItemModelMapped extends ItemModelStandard implements IconStorage {
    protected final Map<String, IconCoordinate> icons = new HashMap<>();

    public ItemModelMapped(Item item, String namespace) {
        super(item, namespace);
    }

    public void addIcon(String key, String texKey) {
        icons.put(key, TextureRegistry.getTexture(texKey));
    }

    public IconCoordinate getIcon(String key) {
        return icons.get(key);
    }

    @Override
    public void addIconInternal(String modId, String namespaceValue, String texKey) {
        addIcon(namespaceValue, modId + ":item/" + texKey);
    }
}
