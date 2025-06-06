package turniplabs.halplibe.helper.model;

import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.item.Item;
import org.jetbrains.annotations.NotNull;

import static turniplabs.halplibe.HalpLibe.LOGGER;

public final class ItemModelBuilder extends ModelBuilder<Item, ItemModel, ItemModelBuilder> {
    private boolean full3D = false;
    private boolean fullbright = false;
    private boolean rotateWhenRendering = false;
    private boolean pointInFrontOfPlayer = false;
    private @NotNull String stringFormat = "%s";
	//private final List<String> extendedMap = new ArrayList<>();
    public ItemModelBuilder(@NotNull String modID, @NotNull ItemModelDispatcher dispatcher) {
        super(modID, dispatcher);
    }

    @SuppressWarnings("unused")
    public ItemModelBuilder withMapping(@NotNull String stringFormat) {
        this.stringFormat = stringFormat;
        return this;
    }

    /*
	@SuppressWarnings("unused")
	public ItemModelBuilder extMapping(@NotNull String stringFormat) {
		this.extendedMap.add(stringFormat);
		return this;
	}*/

    @SuppressWarnings("unused")
    public ItemModelBuilder withFull3D() {
        this.full3D = true;
        return this;
    }

    @SuppressWarnings("unused")
    public ItemModelBuilder withFullbright() {
        this.fullbright = true;
        return this;
    }

    @SuppressWarnings("unused")
    public ItemModelBuilder setPointForward() {
        this.pointInFrontOfPlayer = true;
        return this;
    }

    @SuppressWarnings("unused")
    public ItemModelBuilder rotateWhenRendering() {
        this.rotateWhenRendering = true;
        return this;
    }

    @Override
    protected void onBuild(@NotNull Item block, @NotNull ItemModel model, @NotNull String namespaceValue) {

		/*if (model instanceof ItemModelExtended)  {
			for (String format : this.extendedMap) {
				String formatted = modID + ":item/" + String.format(format, namespaceValue);
				((ItemModelExtended)model).addIcon(formatted); //TODO extract to ModelBuilder
			}
		}else if (!this.extendedMap.isEmpty()) {
            HalpLibe.LOGGER.warn("Model builder of '{}' is using extended mappings, but Model does not extend ItemModelExtended!", block.namespaceID);
        }*/

        if (model instanceof ItemModelStandard) {//TODO log non-standard warning
			String formatted = modID + ":item/" + String.format(this.stringFormat, namespaceValue);
            ItemModelStandard modelStd = ((ItemModelStandard) model);
            if (this.pointInFrontOfPlayer) modelStd.setPointInfrontOfPlayer();
            if (this.full3D) modelStd.setFull3D();
            if (this.fullbright) modelStd.setFullBright();
            if (this.rotateWhenRendering) modelStd.setRotateWhenRendering();
            modelStd.icon = TextureRegistry.getTexture(formatted);
        }else {
            LOGGER.warn("Item model ({}) of '{}' does not extend ItemModelStandard/ItemModelExtended!", model.getClass().getSimpleName(), block.namespaceID);
        }
    }
}
