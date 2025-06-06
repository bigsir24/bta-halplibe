package turniplabs.halplibe.helper.model;

import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Side;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.helper.model.extras.BlockModelExtended;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BlockModelBuilder extends ModelBuilder<Block<?>, BlockModel<?>, BlockModelBuilder>{
    private final Map<Side, String> sideMap = new HashMap<>();
	//private final List<String> extendedMap = new ArrayList<>();
	private int renderLayer = 0;
	//private final List<String> counterStringList = new ArrayList<>();
	//private final List<Integer> countList = new ArrayList<>();

    public BlockModelBuilder(@NotNull String modID, @NotNull BlockModelDispatcher dispatcher) {
        super(modID, dispatcher);
    }

	/*private int getCount(int index) {
		if (countList.isEmpty()) return 0;

		return index < 0 || index >= countList.size() ? countList.get(countList.size() - 1) : countList.get(index);
	}*/

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

	/*
	@SuppressWarnings("unused")
	public BlockModelBuilder extMapping(String stringFormat) {
		this.extendedMap.add(stringFormat);
		return this;
	}

	@SuppressWarnings("unused")
	public BlockModelBuilder extMappingCounter(String stringFormat) {
		if (!stringFormat.contains("%d")) {
			HalpLibe.LOGGER.warn("'{}' should contain %d conversion specifier!", stringFormat);
			return this;
		}

		this.counterStringList.add(stringFormat);
		return this;
	}*/

	@SuppressWarnings("unused")
	public BlockModelBuilder onLayer(int layer) {
		this.renderLayer = layer;
		return this;
	}

	/*

	**Count is cleared after every {@link ModelBuilder#build(Object)} call.
	 * @param count the number of iterations
	 * @return the builder *
	@SuppressWarnings("unused")
	public BlockModelBuilder count(int count) {
		this.countList.add(count);
		return this;
	}

	** See {@link BlockModelBuilder#count(int)} *
	@SuppressWarnings("unused")
	public BlockModelBuilder count(int count1, int count2) {
		this.countList.add(count1);
		this.countList.add(count2);
		return this;
	}

	** See {@link BlockModelBuilder#count(int)} *
	@SuppressWarnings("unused")
	public BlockModelBuilder count(int @NotNull ... count) {
		for (int i = 0; i < count.length; i++) {
			countList.add(count[i]);
		}
		return this;
	}
	*/

    @Override
    protected void onBuild(@NotNull Block<?> block, @NotNull BlockModel<?> model, @NotNull String namespaceValue) {
		//Check errors
		/*if (model instanceof BlockModelExtended) {
			for (String format : this.extendedMap) {
				String formatted = modID + ":block/" + String.format(format, namespaceValue);
				((BlockModelExtended<?>)model).addIcon(formatted); //TODO extract to ModelBuilder
			}

			// Add an arbitrary amount of textures to the extended block model
			// Supports differing amounts (awesome_tex_1-3; awesome_tex_other_1-6)
			for (int i = 0; i < counterStringList.size(); i++) {
				String counterString = counterStringList.get(i);
				int count = getCount(i);

				for (int j = 0; j < count; j++) {
					String formatted = modID + ":block/" + String.format(counterString, namespaceValue, i + 1);
					((BlockModelExtended<?>)model).addIcon(formatted);
				}
			}

			// Clear the count list
			countList.clear();
		}else if (!this.extendedMap.isEmpty()) {
			HalpLibe.LOGGER.warn("Model builder of '{}' is using extended mappings, but Model does not extend BlockModelExtended!", block.namespaceId());
		}*/

        for (Map.Entry<Side, String> entry : this.sideMap.entrySet()) {
            String formatted = modID + ":block/" + String.format(entry.getValue(), namespaceValue);

            if (model instanceof BlockModelStandard) { //TODO: Log error for non-standard models
                ((BlockModelStandard<?>) model).setTex(0, formatted, entry.getKey());
				((BlockModelStandard<?>) model).renderLayer = this.renderLayer;
            }
        }
    }
}
