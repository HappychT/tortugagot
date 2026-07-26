package got.client.model.customarmor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public final class CustomArmorModelCache {
    private static final Map<String, ModelBiped> BODY_MODELS = new HashMap<>();
    private static final Map<String, ModelBiped> HELMET_MODELS = new HashMap<>();
    private static final ModelBiped LEGGINGS_MODEL = new CustomArmorLeggingsModel();
    private static final ModelBiped BOOTS_MODEL = new CustomArmorBootsModel();

    private CustomArmorModelCache() {
    }

    public static ModelBiped getBodyModel(String prefix) {
        return getModel(prefix, false);
    }

    public static ModelBiped getHelmetModel(String prefix) {
        return getModel(prefix, true);
    }

    public static ModelBiped getLeggingsModel() {
        return LEGGINGS_MODEL;
    }

    public static ModelBiped getBootsModel() {
        return BOOTS_MODEL;
    }

    private static ModelBiped getModel(String prefix, boolean helmet) {
        Map<String, ModelBiped> cache = helmet ? HELMET_MODELS : BODY_MODELS;
        if (cache.containsKey(prefix)) {
            return cache.get(prefix);
        }

        String suffix = helmet ? "_helmet" : "_body";
        String className = "got.client.model.customarmor." + prefix + suffix;
        ModelBiped model = null;

        try {
            Class<?> modelClass = Class.forName(className);
            Constructor<?> ctor = modelClass.getDeclaredConstructor();
            ctor.setAccessible(true);
            Object instance = ctor.newInstance();
            if (instance instanceof ModelBiped) {
                model = (ModelBiped) instance;
            }
        } catch (Exception ignored) {
        }

        cache.put(prefix, model);
        return model;
    }
}
