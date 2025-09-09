package turniplabs.halplibe.helper;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicFire;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.material.MaterialColor;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.sound.BlockSound;
import net.minecraft.core.sound.BlockSounds;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import turniplabs.halplibe.helper.builder.AbstractBuilder;
import turniplabs.halplibe.helper.builder.BlockLogicSupplier;
import turniplabs.halplibe.helper.builder.Counter;
import turniplabs.halplibe.mixin.accessors.BlockAccessor;
import turniplabs.halplibe.mixin.accessors.BlocksAccessor;
import turniplabs.halplibe.util.registry.IdSupplier;
import turniplabs.halplibe.util.registry.RunLengthConfig;
import turniplabs.halplibe.util.registry.RunReserves;
import turniplabs.halplibe.util.toml.Toml;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class BlockBuilder extends AbstractBuilder<Block<?>, BlockLogicSupplier, BlockBuilder> {
    private static final String PREFIX = ":block/";
    private @Nullable Float hardness = null;
    private @Nullable Float blastResistance = null;
    private @Nullable Integer lightEmission = null;
    private @Nullable Integer lightOpacity = null;
    private @Nullable Float slipperiness = 0.6F;
    private @Nullable Boolean immovable = null;
    private @Nullable Boolean useInternalLight = null;
    private @Nullable Boolean visualUpdateOnMetadata = null;
    private @Nullable Boolean tickOnLoad = null;
    private @Nullable Boolean infiniburn = null;
    private int @Nullable [] flammability = null;
    private @NotNull BlockSound blockSound = BlockSounds.DEFAULT;
    private @Nullable BlockLambda<ItemBlock<?>> customBlockItem = null;
    private @Nullable Supplier<TileEntity> entitySupplier = null;
    private @NotNull Material material = Material.stone;
    private @Nullable MaterialColor colorMaterial = null;
    private @Nullable Float particleGravity = null;
    private @Nullable Boolean trackStats = null;

    @SuppressWarnings("unused")
    public BlockBuilder(@NotNull String modId) {
        super(PREFIX, Counter.Type.BLOCK, modId);
    }

    private BlockBuilder(BlockBuilder other) {
        super(other);
        this.hardness = other.hardness;
        this.blastResistance = other.blastResistance;
        this.lightEmission = other.lightEmission;
        this.lightOpacity = other.lightOpacity;
        this.slipperiness = other.slipperiness;
        this.immovable = other.immovable;
        this.useInternalLight = other.useInternalLight;
        this.visualUpdateOnMetadata = other.visualUpdateOnMetadata;
        this.tickOnLoad = other.tickOnLoad;
        this.infiniburn = other.infiniburn;
        this.flammability = other.flammability == null ? null : Arrays.copyOf(other.flammability, other.flammability.length);
        this.blockSound = other.blockSound;
        this.customBlockItem = other.customBlockItem;
        this.entitySupplier = other.entitySupplier;
        this.material = other.material;
        this.colorMaterial = other.colorMaterial;
        this.particleGravity = other.particleGravity;
        this.trackStats = other.trackStats;
    }

    /**
     * Sets the block to be a TileEntity Block which creates the provided tile entities on placement
     * @param tileEntitySupplier supplier of TileEntity instances for the block to create when placed
     * @return @return Copy of {@link ItemBuilder}
     */
    @SuppressWarnings("unused")
    public BlockBuilder setTileEntity(@Nullable Supplier<TileEntity> tileEntitySupplier) {
        BlockBuilder builder = this.copy();
        builder.entitySupplier = tileEntitySupplier;
        return builder;
    }

    /**
     * Sets how long it takes to break the block.
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setHardness(float hardness) {
        BlockBuilder blockBuilder = this.copy();
        blockBuilder.hardness = hardness;
        return blockBuilder;
    }

    /**
     * Sets the block's resistance against explosions.
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setResistance(float resistance) {
        BlockBuilder blockBuilder = this.copy();
        blockBuilder.blastResistance = resistance;
        return blockBuilder;
    }

    /**
     * Sets the block's light emitting capacity.
     *
     * @param luminance ranges from 0 to 15
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setLuminance(int luminance) {
        BlockBuilder blockBuilder = this.copy();
        blockBuilder.lightEmission = luminance;
        return blockBuilder;
    }

    /**
     * Sets the block's ability for light to pass through it.<br>
     * Block light and sunlight (once it encounters a non-transparent block) decreases
     * its intensity by 1 every block travelled.<br>
     * Therefore, when passing through a block with opacity 1, it will actually decrease by 2.
     *
     * @param lightOpacity ranges from 0 to 15
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setLightOpacity(int lightOpacity) {
        BlockBuilder blockBuilder = this.copy();
        blockBuilder.lightOpacity = lightOpacity;
        return blockBuilder;
    }

    /**
     * Sets the block's slipperiness, 0.6 is default, 0.98 is ice.
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setSlipperiness(float slipperiness) {
        BlockBuilder blockBuilder = this.copy();
        blockBuilder.slipperiness = slipperiness;
        return blockBuilder;
    }

    /**
     * Sets the block's flammability.
     *
     * @param chanceToCatchFire how likely it is for the block to catch fire
     *                          non-destructively
     * @param chanceToDegrade   how likely it is for the block to burn itself
     *                          to ash and disappear
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setFlammability(int chanceToCatchFire, int chanceToDegrade) {
        BlockBuilder blockBuilder = this.copy();
        blockBuilder.flammability = new int[]{chanceToCatchFire, chanceToDegrade};
        return blockBuilder;
    }

    /**
     * Makes a block unable to be moved by pistons.
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setImmovable() {
        BlockBuilder blockBuilder = this.copy();
        blockBuilder.immovable = true;
        return blockBuilder;
    }

    /**
     * Makes a block unable to be broken.
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setUnbreakable() {
        BlockBuilder blockBuilder = this.copy();
        blockBuilder.hardness = -1.0f;
        return blockBuilder;
    }

    /**
     * Makes fire burn indefinitely on top of the block.
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setInfiniburn() {
        BlockBuilder blockBuilder = this.copy();
        blockBuilder.infiniburn = true;
        return blockBuilder;
    }

    /**
     * Makes a block's interior faces get light from the block's position.<br>
     * Used for things like slabs, stairs, layers and various other non-full
     * blocks that allow light to pass through them.
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setUseInternalLight() {
        BlockBuilder blockBuilder = this.copy();
        blockBuilder.useInternalLight = true;
        return blockBuilder;
    }

    /**
     * Makes the block receive a visual update when the metadata of that block changes.
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setVisualUpdateOnMetadata() {
        BlockBuilder blockBuilder = this.copy();
        blockBuilder.visualUpdateOnMetadata = true;
        return blockBuilder;
    }

    /**
     * Makes the block receive a tick update when the game loads the chunk the block is in.
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setTickOnLoad() {
        BlockBuilder blockBuilder = this.copy();
        blockBuilder.tickOnLoad = true;
        return blockBuilder;
    }
    /**
     * Makes the block receive a tick update when the game loads the chunk the block is in.
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setTicking(boolean ticking) {
        BlockBuilder blockBuilder = this.copy();
        blockBuilder.tickOnLoad = ticking;
        return blockBuilder;
    }

    /**
     * Sets the block's sound when walking over and breaking it.<br>
     * Example code:
     * <pre>{@code
     *     public static final Block exampleBlock = new BlockBuilder(MOD_ID)
     *          .setBlockSound(BlockSounds.WOOD)
     *          .build(new Block("example.block", 4000, Material.wood));
     * }</pre>
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setBlockSound(@NotNull BlockSound blockSound) {
        Objects.requireNonNull(blockSound, "Block sound cannot be null.");
        BlockBuilder blockBuilder = this.copy();
        blockBuilder.blockSound = blockSound;
        return blockBuilder;
    }

    /**
     * Sets the block's item used to place the block.<br>
     * Example code:
     * <pre>{@code
     *     public static final Block customSlab = new BlockBuilder(MOD_ID)
     *          .setBlockItem(BlockItemSlab::new)
     *          .build(new BlockSlab(Block.dirt, 4003));
     * }</pre>
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setBlockItem(@Nullable BlockLambda<ItemBlock<?>> customBlockItem) {
        BlockBuilder blockBuilder = this.copy();
        blockBuilder.customBlockItem = customBlockItem;
        return blockBuilder;
    }

    /**
     *
     * @param material the material, {@link Material#stone} by default
     * @return a copy of BlockBuilder
     */
    @SuppressWarnings("unused")
    public BlockBuilder setMaterial(@NotNull Material material) {
        BlockBuilder blockBuilder = this.copy();
        blockBuilder.material = material;
        return blockBuilder;
    }

    /**
     * Sets the material color of the block. This color is used to render the block on a map.
     * @param colorMaterial the color material, the color of the block material by default
     * @return a copy of BlockBuilder
     */
    @SuppressWarnings("unused")
    public BlockBuilder setColorMaterial(@NotNull MaterialColor colorMaterial) {
        BlockBuilder blockBuilder = this.copy();
        blockBuilder.colorMaterial = colorMaterial;
        return blockBuilder;
    }

    /**
     * Sets the gravity factor of the block breaking particle, which is multiplied by its gravitational constant (0.04)
     * before accelerating the particle. <br>
     * The default value is 1.0F.
     * @param particleGravity the gravity factor
     * @return a copy of BlockBuilder
     */
    @SuppressWarnings("unused")
    public BlockBuilder setParticleGravity(float particleGravity) {
        BlockBuilder blockBuilder = this.copy();
        blockBuilder.particleGravity = particleGravity;
        return blockBuilder;
    }

    /**
     * Enables/disables stat tracking. Tracking is enabled by default.
     * @return a copy of BlockBuilder
     */
    @SuppressWarnings("unused")
    public BlockBuilder setStatTracking(boolean trackStats) {
        BlockBuilder blockBuilder = this.copy();
        blockBuilder.trackStats = trackStats;
        return blockBuilder;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected  <T extends AbstractBuilder<Block<?>, BlockLogicSupplier, BlockBuilder>> T copy() {
        return (T) new BlockBuilder(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T extends Block<?>> @NotNull T buildInternal(@NotNull BlockLogicSupplier supplier) {
        // This should never happen, but it should suppress the warning at least
        Objects.requireNonNull(this.translationKey, "Translation key was null.");
        Objects.requireNonNull(this.namespaceID, "NamespaceID was null.");

        T block = (T) Blocks.register(this.translationKey, this.namespaceID, this.id, (b) -> supplier.create(b, material));

        ifNotNull(hardness, block::withHardness);
        ifNotNull(blastResistance, block::withBlastResistance);
        ifNotNull(tickOnLoad, block::setTicking);
        block.withSound(blockSound);
        block.withOverrideColor(colorMaterial);

        if (slipperiness != null) block.friction = slipperiness;
        if (particleGravity != null) block.blockParticleGravity = particleGravity;
        // Not assigned in AbstractBuilder since it's not part of any interface
        // And this is probably the least hacky way
        ifNotNull(statParentSupplier, block::setStatParent);
        if (trackStats != null) block.enableStats = trackStats;

        ifNotNull(lightOpacity, block::withLightBlock);
        ifNotNull(useInternalLight, block::withLitInteriorSurface);
        ifNotNull(lightEmission, block::withLightEmission);

        ifTrue(immovable, block::withImmovableFlagSet);
        ifTrue(infiniburn, () -> block.withTags(BlockTags.INFINITE_BURN));
        ifTrue(visualUpdateOnMetadata, block::withDisabledNeighborNotifyOnMetadataChange);

        ifNotNull(tags, block::withTags);
        ifNotNull(entitySupplier, block::withEntity);

        if (flammability != null) {
            BlockLogicFire.setFlammable(block, flammability[0], flammability[1]);
        }

        if (customBlockItem != null) {
            block.setBlockItem(() -> customBlockItem.run(block));
        }

        if (BlocksAccessor.hasInit()) {
            block.init();

            Item item = block.blockItemSupplier.get();
            if (((BlockAccessor)(Object)block).getStatParent() != null) {
                item.setStatParent(((BlockAccessor)(Object)block).getStatParent());
            }
            Item.itemsList[item.id] = item;


            block.getLogic().initializeBlock();
            BlocksAccessor.cacheBlock(block);
        }

        return block;
    }

    @SuppressWarnings({"unused", "unchecked"})
    public <T extends BlockLogic> @NotNull Block<T> build(@NotNull String translationKey, @NotNull String namespaceID, int id, net.minecraft.core.block.BlockLogicSupplier<T> supplier) {
        return build(translationKey, namespaceID, id, (b, m) -> supplier.get((Block<T>) b));
    }
    
    @FunctionalInterface
    public interface BlockLambda<T> {
        T run(Block<?> block);
    }
    public static class Registry{
        public static int highestVanilla;

        private static final RunReserves reserves = new RunReserves(
                Registry::findOpenIds,
                Registry::findLength
        );

        /**
         * Should be called in a runnable scheduled with {@link IdSupplierHelper#scheduleRegistry(boolean, Runnable)}
         * @param count the amount of needed blocks for the mod
         * @return the first available slot to register in
         */
        public static int findOpenIds(int count) {
            int run = 0;
            for (int i = highestVanilla; i < Blocks.blocksList.length; i++) {
                if (Blocks.blocksList[i] == null && !reserves.isReserved(i)) {
                    if (run >= count)
                        return (i - run);
                    run++;
                } else {
                    run = 0;
                }
            }
            return -1;
        }

        public static int findLength(int id, int terminate) {
            int run = 0;
            for (int i = id; i < Blocks.blocksList.length; i++) {
                if (Blocks.blocksList[i] == null && !reserves.isReserved(i)) {
                    run++;
                    if (run >= terminate) return terminate;
                } else {
                    return run;
                }
            }
            return run;
        }

        /**
         * Allows halplibe to automatically figure out where to insert the runs
         * @param modId     an identifier for the mod, can be anything, but should be something the user can identify
         * @param runs      a toml object representing configured registry runs
         * @param neededIds the number of needed ids
         *                  if this changes after the mod has been configured (i.e. mod updated and now has more blocks) it'll find new, valid runs to put those blocks into
         * @param function  the function to run for registering items
         */
        public static void reserveRuns(String modId, Toml runs, int neededIds, Consumer<IdSupplier> function) {
            RunLengthConfig cfg = new RunLengthConfig(runs, neededIds);
            cfg.register(reserves);
            IdSupplierHelper.scheduleSmartRegistry(
                    () -> {
                        IdSupplier supplier = new IdSupplier(modId, reserves, cfg, neededIds);
                        function.accept(supplier);
                        supplier.validate();
                    }
            );
        }
    }
}
