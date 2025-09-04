package turniplabs.halplibe.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.texture.stitcher.AtlasStitcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.image.BufferedImage;

@Mixin(value = AtlasStitcher.class,remap = false)
public abstract class AtlasStitcherMixin {

    @Shadow private int atlasWidth;

    @Shadow private int atlasHeight;

    @WrapOperation(method = "init", at = @At(value = "NEW", target = "(III)Ljava/awt/image/BufferedImage;", ordinal = 0))
    public BufferedImage init(int width, int height, int imageType, Operation<BufferedImage> original){
        int size = Math.max(height, width);
        return new BufferedImage(size, size, imageType);
    }

    @Inject(method = "init", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/texture/stitcher/AtlasStitcher;atlasWidth:I", ordinal = 1))
    public void changeSize(CallbackInfo ci) {
        this.atlasWidth = this.atlasHeight = Math.max(this.atlasWidth, this.atlasHeight);
    }
}
