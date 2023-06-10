package com.vicious.viciouscore.mixin.vanilla;

import com.vicious.viciouscore.common.util.server.IPretranslatable;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Arrays;
import java.util.List;

@Mixin(TranslatableContents.class)
public abstract class MixinTranslatableContents implements IPretranslatable {
    @Shadow protected abstract void decompose();

    @Shadow private List<FormattedText> decomposedParts;

    @Shadow @Final private Object[] args;

    @Shadow @Final private String key;

    @Override
    public Component translate() {
        decompose();
        StringBuilder txt = new StringBuilder();
        for (FormattedText decomposedPart : decomposedParts) {
            txt.append(decomposedPart.getString());
        }
        return Component.literal(txt.toString());
    }
}
