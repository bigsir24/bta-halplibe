package turniplabs.halplibe.helper.builder;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.material.Material;

@FunctionalInterface
public interface BlockLogicSupplier {
    BlockLogic create(Block<?> block, Material material);
}
