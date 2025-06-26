package turniplabs.halplibe.helper.model.models;

import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;

import java.util.HashMap;
import java.util.Map;

public class BlockModelMapped<T extends BlockLogic> extends BlockModelStandard<T> implements IconStorage {
    protected final Map<String, IconCoordinate> icons = new HashMap<>();

    public BlockModelMapped(Block<T> block) {
        super(block);
    }

    public void addIcon(String key, String texKey) {
        icons.put(key, TextureRegistry.getTexture(texKey));
    }

    public IconCoordinate getIcon(String key) {
        return icons.get(key);
    }

    @Override
    public void addIconInternal(String modId, String namespaceValue, String texKey) {
        addIcon(namespaceValue, modId + ":block/" + texKey);
    }
}
