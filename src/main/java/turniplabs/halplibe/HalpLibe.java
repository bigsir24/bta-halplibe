package turniplabs.halplibe;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.toml.Toml;

public class HalpLibe implements ModInitializer, PreLaunchEntrypoint {
    public static final String MOD_ID = "halplibe";
    public static final String LEGACY_NAMESPACE = "halplibe_legacy";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final boolean isClient = FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT);
    public static final TomlConfigHandler CONFIG;
    static {
        Toml toml = new Toml();
        CONFIG = new TomlConfigHandler(MOD_ID, toml);
    }

    @SuppressWarnings("unused")
    @Deprecated
    public static String addModId(String modId, String name) {
        return modId + "." + name;
    }

    @Override
    public void onInitialize() {
        LOGGER.info("HalpLibe initialized.");
    }

    @Override
    public void onPreLaunch() {

    }
}
