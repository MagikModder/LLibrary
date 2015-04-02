package net.ilexiconn.llibrary.client.render;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Map;

/**
 * Render helper class for basic render operations and the IModelExtension.
 *
 * @author iLexiconn & Gegy1000
 */
@SideOnly(Side.CLIENT)
public class RenderHelper
{
    private static Map<Class<? extends ModelBase>, List<IModelExtension>> modelExtensions = Maps.newHashMap();

    /**
     * Registers the given IModelExtension.
     *
     * @param modelExtension
     */
    public static void registerModelExtension(IModelExtension modelExtension)
    {
        registerModelExtension(ModelBiped.class, modelExtension);
    }

    /**
     * Registers the given IModelExtension to a specific model.
     *
     * @param modelClazz
     * @param modelExtension
     */
    private static void registerModelExtension(Class<? extends ModelBase> modelClazz, IModelExtension modelExtension)
    {
        List<IModelExtension> extensionsForModel = modelExtensions.get(modelClazz);

        if (extensionsForModel == null)
        {
            extensionsForModel = Lists.newArrayList();
        }

        extensionsForModel.add(modelExtension);

        modelExtensions.put(modelClazz, extensionsForModel);
    }

    /**
     * @param clazz
     * @returns a list of ModelExtensions for the given model class.
     */
    public static List<IModelExtension> getModelExtensionsFor(Class<? extends ModelBase> clazz)
    {
        return modelExtensions.get(clazz);
    }
}
