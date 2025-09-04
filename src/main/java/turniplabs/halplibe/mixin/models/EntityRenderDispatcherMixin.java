package turniplabs.halplibe.mixin.models;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.render.EntityRendererDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turniplabs.halplibe.helper.ModelHelper;
import turniplabs.halplibe.util.ModelEntrypoint;

import java.util.Map;

@Mixin(value = EntityRendererDispatcher.class, remap = false)
public abstract class EntityRenderDispatcherMixin {
    @Shadow @Final private Map<Class<?>, EntityRenderer<?>> renderers;

    @Inject(method = "reload", at = @At(value = "TAIL"))
    private void addQueuedModels(CallbackInfo ci){
        ModelHelper.entityRenderers = renderers;
        ModelHelper.entityRenderDispatcher = EntityRendererDispatcher.class.cast(this);
        FabricLoader.getInstance().getEntrypoints("initModels", ModelEntrypoint.class).forEach(e -> e.initEntityModels(ModelHelper.entityRenderDispatcher));
    }
}
