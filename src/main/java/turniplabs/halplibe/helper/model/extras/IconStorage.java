package turniplabs.halplibe.helper.model.extras;

public interface IconStorage {

    /**Internal method used to add textures to models
     * @param modId self-explanatory
     * @param namespaceValue the cleaned namespace value, mainly meant to be used as a key in maps. <br>
     *                       Example: minecraft:block/ore_coal -> ore_coal
     *
     * @param texKey the formatted texture key */
    void addIconInternal(String modId, String namespaceValue, String texKey);
}
