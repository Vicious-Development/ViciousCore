package com.vicious.viciouscore.common.data.structures;

import com.vicious.viciouscore.ViciousCore;
import com.vicious.viciouscore.common.capability.VCCapabilityProvider;
import com.vicious.viciouscore.common.capability.exposer.ICapabilityExposer;
import com.vicious.viciouscore.common.data.DataAccessor;
import com.vicious.viciouscore.common.data.IVCNBTSerializable;
import com.vicious.viciouscore.common.data.autogen.annotation.ReadOnly;
import com.vicious.viciouscore.common.data.implementations.SyncableArrayHashSet;
import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Values stored in this exposer will be exposed to the same capability exposers when permitted.
 * The exposed locations are saved this way.
 */
public class ExposableSyncableCompound extends SyncableCompound{
    @ReadOnly
    private final SyncableArrayHashSet<ExpositionRef> exposedTo = new SyncableArrayHashSet<>("exposedTo",ExpositionRef::new);
    private final VCCapabilityProvider capabilityProvider;
    private boolean hasInitialized = false;
    public ExposableSyncableCompound(String key, VCCapabilityProvider provider) {
        super(key);
        this.capabilityProvider=provider;
    }

    @Override
    public void deserializeNBT(CompoundTag tag, DataAccessor sender) {
        super.deserializeNBT(tag, sender);
        if(!hasInitialized){
            hasInitialized=true;
            for (ExpositionRef expositionRef : exposedTo.value) {
                expose(expositionRef.ref);
            }
        }
    }

    public void expose(Object o){
        exposedTo.value.add(new ExpositionRef(o));
        //Ensure the exposer exists.
        capabilityProvider.allowExposure(o);
        ICapabilityExposer exposer = capabilityProvider.getExposer(o);
        for (SyncableValue<?> syncableValue : values()) {
            exposer.expose(syncableValue);
        }
    }
    public void conceil(Object o){
        exposedTo.value.remove(new ExpositionRef(o));
        ICapabilityExposer exposer = capabilityProvider.getExposer(o);
        for (SyncableValue<?> syncableValue : values()) {
            exposer.conceal(syncableValue);
        }
    }

    /**
     * IMPORTANT! THIS SERIALIZER MUST PUT ONE AND ONLY ONE NBT VALUE INTO THE COMPOUNDTAG PROVIDED!
     * The key must be the object's canonical classname.
     */
    public static void addSerializer(Class<?> cls, BiConsumer<Object, CompoundTag> serializer){
        ExpositionRef.serializers.put(cls,serializer);
    }
    /**
     * IMPORTANT! THIS DESERIALIZER WILL ONLY RECEIVE ONE KEY-TAG PAIR!
     */
    public static void addDeserializer(Class<?> cls, Function<CompoundTag,Object> deserializer){
        ExpositionRef.deserializers.put(cls,deserializer);
    }
    private static class ExpositionRef implements IVCNBTSerializable{
        private static final Map<Class<?>, BiConsumer<Object,CompoundTag>> serializers = new HashMap<>();
        private static final Map<Class<?>, Function<CompoundTag,Object>> deserializers = new HashMap<>();

        private Object ref;

        public ExpositionRef(){}
        public ExpositionRef(Object ref){
            this.ref=ref;
        }
        public void set(Object o){
            this.ref=o;
        }

        @Override
        public void serializeNBT(CompoundTag tag, DataAccessor destination) {
            if(ref.getClass().isEnum()) {
                tag.putInt(ref.getClass().getCanonicalName(), ((Enum<?>) ref).ordinal());
            }
            else{
                serializers.get(ref.getClass()).accept(ref,tag);
            }
        }

        @Override
        public void deserializeNBT(CompoundTag tag, DataAccessor sender) {
            for (String key : tag.getAllKeys()) {
                try {
                    Class<?> cls = Class.forName(key);
                    if(cls.isEnum()){
                        set(cls.getEnumConstants()[tag.getInt(key)]);
                    }
                    else{
                        set(deserializers.get(cls).apply(tag));
                    }
                    break;
                } catch (ClassNotFoundException e) {
                    ViciousCore.logger.error("Could not find class for provided data value. \nThis is a developer's fault and will cause data loss! Whose is it? I don't know.");
                    e.printStackTrace();
                }
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ExpositionRef that = (ExpositionRef) o;
            return Objects.equals(ref, that.ref);
        }

        @Override
        public int hashCode() {
            return Objects.hash(ref);
        }
    }
}
