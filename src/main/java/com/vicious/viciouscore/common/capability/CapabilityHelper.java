package com.vicious.viciouscore.common.capability;

import com.vicious.viciouscore.ViciousCore;
import com.vicious.viciouscore.common.capability.combined.CombinedItemHandler;
import com.vicious.viciouscore.common.capability.combined.ICombinedCapabilityProvider;
import com.vicious.viciouscore.common.util.identification.ModIdentifiableSupplier;
import com.vicious.viciouscore.common.util.map.AntiConflictHashMap;
import com.vicious.viciouscore.common.util.map.AntiConflictIdentityHashMap;
import com.vicious.viciouslib.util.reflect.deep.DeepReflection;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class CapabilityHelper {
    private static final AntiConflictHashMap<Class<?>, ModIdentifiableSupplier<Capability<?>>> classTokenMap = new AntiConflictHashMap<>();
    private static final AntiConflictIdentityHashMap<Capability<?>, ModIdentifiableSupplier<ICombinedCapabilityProvider<?>>> combinedProviderSuppliers = new AntiConflictIdentityHashMap<>();
    static {
        classConvertsTo(IItemHandler.class,ForgeCapabilities.ITEM_HANDLER,ViciousCore.MODID);
        classConvertsTo(IEnergyStorage.class,ForgeCapabilities.ENERGY,ViciousCore.MODID);
        classConvertsTo(IFluidHandler.class,ForgeCapabilities.FLUID_HANDLER,ViciousCore.MODID);
        classConvertsTo(IFluidHandlerItem.class,ForgeCapabilities.FLUID_HANDLER_ITEM,ViciousCore.MODID);
        registerCombinedCapabilityProvider(ForgeCapabilities.ITEM_HANDLER, CombinedItemHandler::new,ViciousCore.MODID);
    }

    /**
     * Registers a class to capability token converter for the provided class.
     */
    public static <T> void classConvertsTo(Class<T> cls, Capability<T> token, String modid){
        classTokenMap.putOrThrow(cls,new ModIdentifiableSupplier<>(()->token,modid));
    }

    /**
     * Registers a combined capability provider builder for the provided token.
     */
    public static <T> void registerCombinedCapabilityProvider(Capability<T> token, Supplier<ICombinedCapabilityProvider<?>> combinedCapabilityBuilder, String modid){
        combinedProviderSuppliers.putOrThrow(token,new ModIdentifiableSupplier<>(combinedCapabilityBuilder,modid));
    }

    /**
     * Gets the capability token for a provided object based on its class.
     */
    @Nullable
    public static Capability<?> getTokenFor(Object o){
        return DeepReflection.cycleAndExecute(o.getClass(),(cls)->{
            if(classTokenMap.containsKey(cls)){
                return classTokenMap.get(cls).get();
            }
            return null;
        });
    }

    /**
     * Creates a new combined provider for the token type.
     */
    @SuppressWarnings("unchecked")
    public static <T> ICombinedCapabilityProvider<T> createCombinedProvider(Capability<T> token){
        if(combinedProviderSuppliers.containsKey(token)){
            return (ICombinedCapabilityProvider<T>) combinedProviderSuppliers.get(token).get();
        }
        else{
            throw new IllegalStateException("Attempted to create an unregistered combined capability provider.");
        }
    }
}
