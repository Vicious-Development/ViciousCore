package com.vicious.viciouscore.client.render;

import com.vicious.viciouscore.client.render.entity.model.IOverrideModel;
import com.vicious.viciouscore.client.render.entity.model.multimob.OverrideModelBiped;
import com.vicious.viciouscore.client.render.entity.model.multimob.OverrideModelIllager;
import com.vicious.viciouscore.client.render.entity.model.multimob.OverrideModelSpider;
import com.vicious.viciouscore.client.render.entity.model.multimob.OverrideModelVillager;
import com.vicious.viciouscore.client.render.entity.model.singlemob.OverrideModelPlayer;
import com.vicious.viciouscore.client.render.entity.model.singlemob.aggressive.*;
import com.vicious.viciouscore.client.render.entity.model.singlemob.passive.*;
import com.vicious.viciouscore.common.util.reflect.Reflection;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.entity.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class RenderOverrideManager {
    private static Map<Class<? extends Render>, Function<ModelBase, ModelBase>> overrideMap = new HashMap<>();
    private static Map<Class<? extends Render>, Class<? extends ModelBase>> renderMap = new HashMap<>();
    static {
        registerOverrideModel(ModelBat.class, RenderBat.class, (m) -> new OverrideModelBat((ModelBat) m));
        registerOverrideModel(ModelBlaze.class, RenderBlaze.class, (m) -> new OverrideModelBlaze((ModelBlaze) m));
        registerOverrideModel(ModelChicken.class, RenderChicken.class, (m) -> new OverrideModelChicken((ModelChicken) m));
        registerOverrideModel(ModelCow.class, RenderCow.class, (m) -> new OverrideModelCow((ModelCow) m));
        registerOverrideModel(ModelCreeper.class, RenderCreeper.class, (m) -> new OverrideModelCreeper((ModelCreeper) m));
        registerOverrideModel(ModelDragon.class, RenderDragon.class, (m) -> new OverrideModelDragon((ModelDragon) m));
        registerOverrideModel(ModelEnderMite.class, RenderEndermite.class, (m) -> new OverrideModelEnderMite((ModelEnderMite) m));
        registerOverrideModel(ModelGhast.class, RenderGhast.class, (m) -> new OverrideModelGhast((ModelGhast) m));
        registerOverrideModel(ModelHorse.class, RenderHorse.class, (m) -> new OverrideModelHorse((ModelHorse) m));
        registerOverrideModel(ModelOcelot.class, RenderOcelot.class, (m) -> new OverrideModelOcelot((ModelOcelot) m));
        registerOverrideModel(ModelPig.class, RenderPig.class, (m) -> new OverrideModelPig((ModelPig) m));
        registerOverrideModel(ModelSilverfish.class, RenderSilverfish.class, (m) -> new OverrideModelSilverfish((ModelSilverfish) m));
        registerOverrideModel(ModelSkeleton.class, RenderSkeleton.class, (m) -> new OverrideModelSkeleton((ModelSkeleton) m));
        registerOverrideModel(ModelWither.class, RenderWither.class, (m) -> new OverrideModelWither((ModelWither) m));
        registerOverrideModel(ModelWolf.class, RenderWolf.class, (m) -> new OverrideModelWolf((ModelWolf) m));

        //Multientity
        registerOverrideModel(ModelBiped.class, RenderBiped.class, (m) -> new OverrideModelBiped((ModelBiped) m));
        registerOverrideModel(ModelIllager.class, RenderVindicator.class, (m) -> new OverrideModelIllager((ModelIllager) m));
        registerOverrideModel(ModelIllager.class, RenderEvoker.class, (m) -> new OverrideModelIllager((ModelIllager) m));
        registerOverrideModel(ModelIllager.class, RenderIllusionIllager.class, (m) -> new OverrideModelIllager((ModelIllager) m));
        registerOverrideModel(ModelPlayer.class, RenderPlayer.class, (m) -> new OverrideModelPlayer((ModelPlayer) m));
        registerOverrideModel(ModelSpider.class, RenderSpider.class, (m) -> new OverrideModelSpider((ModelSpider) m));
        registerOverrideModel(ModelVillager.class, RenderVillager.class, (m) -> new OverrideModelVillager((ModelVillager) m));

    }
    /**
     * Used to allow custom mob animations. Works on anything with a nonNull mainModel.
     * We need to do this because vanilla by default applies rotations on doRender() that need to be overriden.
     * @param entityRenderer
     * @return
     */
    public static IOverrideModel overrideModel(RenderLivingBase<?> entityRenderer) {
        //Override the entity models so we can apply changes.
        if (!(entityRenderer.getMainModel() instanceof IOverrideModel)) {
            ModelBase model = entityRenderer.getMainModel();
            Class<?> clazz = entityRenderer.getClass();
            while(clazz != null) {
                try {
                    model = overrideMap.get(clazz).apply(model);
                    break;
                } catch(Exception e){
                    clazz = clazz.getSuperclass();
                }
            }
            Reflection.setField(entityRenderer, model, "mainModel");
        }
        return (IOverrideModel) entityRenderer.getMainModel();
    }

    /**
     * Call this to register a models overrider. Parameters should look something like this:
     * registerOverrideModel(ModelSpider.class, RenderSpider.class, (m)->new OverrideModelSpider((ModelSpider) m))
     */
    public static void registerOverrideModel(Class<? extends ModelBase> modelClass, Class<? extends Render> renderClass, Function<ModelBase,ModelBase> overrideModelFunction){
        overrideMap.put(renderClass,overrideModelFunction);
        renderMap.put(renderClass,modelClass);
    }
    public static <T extends ModelBase> Class<T> getRenderModel(Class<?> clazz){
        Class<T> mdl = null;
        while(clazz != null && mdl == null) {
            mdl = (Class<T>) renderMap.get(clazz);
            clazz = clazz.getSuperclass();
        }
        return mdl;
    }
}
