# ViciousCore
Vicious Core is a powerful mod designed to eliminate the tediousness of many processes in modding Minecraft while keeping everything as server efficient as possible. 
This includes: item and entity rendering and animation, mob spawn detection and handling.
Soon to come:
Structure generation and modification.
Tile Entities and Multiblocks.


Vicious Core depends on CodeChickenLib for model rendering.

# Features:

**modification.MobSpawnModifier
Allows modification of mobs on the first tick after they spawn for the first time.**
To add a modificator execute MobSpawnModifier.entityModificators.put(entity class, consumer)
By doing this, you can modify pretty much anything in the entity.

# Overriding Vanilla item and entity rendering
Make sure to read this entire thing before attempting implementation.

VCore Introduces a bunch of ways you can override Minecraft's default rendering system and adds an easy way of modifying rendering while the game is running. To get started, you will need to understand **IRenderOverride**.
Whenever a living entity is about to be rendered in the game VCore checks a few things in the entity before doing so.
Here's what you need to know
 - If the entity is holding an Item that implements IRenderOverride, VCore will call IRenderOverride.renderEntity(Renderer, EntityLivingBase).
   - When this happens, by default VCore will then get the Item's OverrideConfigurations and use them for many things.

**What are OverrideConfigurations?**
One of the most painstaking processes in modding this god forsaken game is restarting it about a billion times to render a model or entity in the correct way. You have to get the right rotation, scale, translation, etc...
**The goal of OverrideConfigurations is to make this entire process reloadable during runtime. Using OverrideConfigurations will save you LOTS of time. Here's how.**
First of all, you need to register an OverrideConfiguration for your item. This can be done when IRenderOverride#registerRenderers is called. 
Simply write in your Item Class: 
```java
@Override
public void registerRenderers() {
    //Other stuff...
    //heldconfig is usually a private HeldItemOverrideCFG field.
    heldconfig = ClientOverrideConfigurations.createWhenHeldOverride(this);
    //Other stuff...
}
```
**Now on startup or when "ircfg reload" is called, your item's OverrideConfigurations will be automatically generated in the run/resources/vicious/itemrenderoverrides/yourmodid/<itemid> directory.**
The json file there provides 9 fields by default for modifying your item's rendering. These should be self explanatory so I won't delve deeper about that.
Just know a few things.
An OverrideConfiguration must have active set to true to work, as well as any of its transformations that will be ran. We do this inorder to prevent unintentional transformations and to optimize runtime CPU strain.

With that, you should be able to render the item at the correct scale, rotation, and position YOURSELF. You will have to call OverrideConfigurations.getConfiguration(item) to get the configuration. The configuration only provides you transformation data.

**What about entities? Well, OverrideConfigurations has you covered. Note: Currently, OverrideConfigurations only supports overriding rendering when an IRenderOverride Item is held.**
**You can add overrides for specific Models by doing the following in your Item class**
```java
@Override
public void registerRenderers() {
    //Other stuff...
    renderconfig = OverrideConfigurations.create(this);
    renderconfig.addEntityModelOverrider(modelclass extends ModelBase); //EXAMPLE OF COMPLETE CODE renderconfig.addEntityModelOverrider(ModelBiped.class);
    //Other stuff...
}
```
**In the Example, we are overriding the ModelBiped rendering. This will affect vanilla mobs such as Zombies, Skeletons, Pigmen, ... . Once you do this, the OverrideConfigurations for that specific model type will be generated in run/resources/vicious/<yourmodid>-<itemid>/<ModelClassCanonicalName>** 
Each Model part will have its own OverrideConfiguration json file generated. This works the same as the Item variant, just without scale. If you enable one of the parts to have the render be overriden, the entity will be rendered differently while the item is held.
**Note: if a Model class extends another and both modeltypes have render overrides generated, the child class will be prioritized over the parent.**
**Being able to do this in such an easy fashion allows you to quickly create cursed beings such as the one below**

(spider holding gun probably)

**If OverrideConfigurations are stored in my dev env run/resource folder, how do I put the OverrideConfigurations in the mod jar?**
I've got a simple solution for you! Here's the steps to do it.
1. In your mod's resources package, create a directory "assets" if it doesn't already exist.
2. In your run directory, copy the entire "itemrenderoverrides" folder.
3. Paste this in your resource/assets package.
4. In your mod's client preInitializer write this:
```java
OverrideConfigurations.copyFromResources(<yourmodid>,this.getClass());
```
This will put the OverrideConfigurations in the game's resource/vicious/itemrenderoverrides directory when the game starts for the first time. On future runs, the OverrideConfigurations will always load from the resources directory rather than your mod's assets allowing other modders to make changes as they see fit.

**What about for custom entities?**
A few extra steps will need to be taken, first of all, the entity renderer must have a model in its mainModel field. If you or the creator didn't use this, overriding the model won't be possible.
Should this field exist, all parts of the model must have ModelRenderer fields (I.E. in ModelBiped, there is a bipedLeftArm field).
If these fields do not exist, the OverrideConfiguration system won't work as .json files for each part won't be generated.
If all of this exists though, you should be good to get started.

1. The easiest way to do this is to first copy the OverrideModelSpider class. At the very least try to understand it. 
2. Rename the class to something like OverrideModel<modelnameorsomething>. Extend Model<modelnameorsomething>
3. The only thing you should have to modify is the render method. You'll need to copy the render method from the original Model. If you can't do that... good luck.
4. Replace the setRotationAngles method with overrideRotationAngles
5. Add a resetTransformations(boxlist) call to the end of the method if necessary. (Sometimes models don't have their rotations reset on the next render)

The next step is to call in your clientInit:
1. RenderOverrideManager.registerOverrideModel(originalModel,originalRenderer,overriddenModelFunction), it should look something like this: registerOverrideModel(ModelSpider.class, RenderSpider.class, (m)->new OverrideModelSpider((ModelSpider) m))

That's all you need to do to add support for overriding an unsupported model. You should be able to add OverrideConfigurations for that model now!

**IMPORTANT SPECIAL CASES!!!**
There are a few special cases where OverrideConfigurations need some help to work.
1. If the Model class has ModelRenderer arrays, the lengths of those arrays must be provided to the OverrideConfigurator
   To do this, call OverrideConfigurations.putArrayLengths(modelClass, lengths) in your client preInit. The lengths should be inorder of how the arrays appear in the class (line 0 goes before line 1).
   If you fail to do this, the OverrideConfigurations for that model will not be generated properly as they will be assumed to be non-array fields. 
   See ModelSilverfish to see an example of a case of this situation.
   Unfortunately, if the model uses lists of ModelRenderers, VCore does not support modifying lists of renders.
