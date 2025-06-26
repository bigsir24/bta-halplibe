package turniplabs.halplibe.helper.model;

import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Side;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.model.models.BlockModelMapped;

import java.util.HashMap;
import java.util.Map;

public final class BlockModelBuilder extends ModelBuilder<Block<?>, BlockModel<?>, BlockModelBuilder>{
    private final Map<Side, String> sideMap = new HashMap<>();
	private int renderLayer = 0;

    public BlockModelBuilder(@NotNull String modID, @NotNull BlockModelDispatcher dispatcher) {
        super(modID, dispatcher);
    }

    public BlockModelBuilder copy() {
        BlockModelBuilder newBuilder = copyToInternal(new BlockModelBuilder(modID, (BlockModelDispatcher) dispatcher));
        newBuilder.sideMap.putAll(sideMap);
        newBuilder.renderLayer = renderLayer;
        return newBuilder;
    }

	@SuppressWarnings("unused")
	public BlockModelBuilder withMapping(String stringFormat, int side) {
		return withMapping(stringFormat, Side.getSideById(side));
	}

	@SuppressWarnings("unused")
	public BlockModelBuilder withMapping(String stringFormat, int... sides) {
		for (int sideIndex : sides) {
			this.sideMap.put(Side.getSideById(sideIndex), stringFormat);
		}
		return this;
	}

    @SuppressWarnings("unused")
    public BlockModelBuilder withMapping(String stringFormat, Side side) {
        this.sideMap.put(side, stringFormat);
        return this;
    }

    @SuppressWarnings("unused")
    public BlockModelBuilder withMapping(String stringFormat, Side... sides) {
        for (Side side : sides) {
            this.sideMap.put(side, stringFormat);
        }
        return this;
    }

    @SuppressWarnings("unused")
    public BlockModelBuilder withMapping(String stringFormat) {
        for (Side side : Side.sides) {
            this.sideMap.put(side, stringFormat);
        }
        return this;
    }

	@SuppressWarnings("unused")
	public BlockModelBuilder onLayer(int layer) {
		this.renderLayer = layer;
		return this;
	}

    @Override
    protected void onBuild(@NotNull Block<?> block, @NotNull BlockModel<?> model, @NotNull String namespaceValue) {
        for (Map.Entry<Side, String> entry : this.sideMap.entrySet()) {
            String formatted = modID + ":block/" + String.format(entry.getValue(), namespaceValue);

            if (model instanceof BlockModelStandard) { //TODO: Log error for non-standard models
                ((BlockModelStandard<?>) model).setTex(0, formatted, entry.getKey());
				((BlockModelStandard<?>) model).renderLayer = this.renderLayer;
            }
        }
    }
}
