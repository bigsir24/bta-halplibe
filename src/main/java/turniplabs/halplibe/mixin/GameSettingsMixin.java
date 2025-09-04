package turniplabs.halplibe.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.options.components.BooleanOptionComponent;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.option.OptionBoolean;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turniplabs.halplibe.util.OptionsInitEntrypoint;

import java.io.File;

@Mixin(value = GameSettings.class, remap = false)
public abstract class GameSettingsMixin {
    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameSettings;loadOptions()V"))
    private static void initOptions(CallbackInfo ci) {
        FabricLoader.getInstance().getEntrypoints("initOptions", OptionsInitEntrypoint.class).forEach(OptionsInitEntrypoint::initOptions);
    }
}
