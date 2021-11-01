package com.vicious.viciouscore.common.util.configuration;

import java.util.HashMap;
import java.util.Map;

//NYI
public class ConfigCommands {
    public static void init(Config cfg){
       /* Map<String, TrackableValue<?>> options = new HashMap<>(cfg.values);
        CommandSpec set = CommandSpec.builder()
                .description(Text.of("Sets a config option"))
                .arguments(GenericArguments.choices(Text.of("optionname"), options), GenericArguments.string(Text.of("value")))
                .executor((src, ctx) -> {
                    ConfigurationValue<?> val = ctx.requireOne("optionname");
                    try {
                        val.setFromStringWithUpdate(ctx.requireOne("value"));
                    } catch(Exception e){
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }
                    src.sendMessage(Text.of(TextColors.AQUA, TextStyles.BOLD, "Set ", val.name, " to ", TextColors.BLUE, val.getStopValue().toString()));
                    if(!(val).canBeModifiedOnRuntime()){
                        src.sendMessage(Text.of(TextColors.GOLD, TextStyles.BOLD, val.name, " does not update while the server is running. To apply the update, restart the server."));
                    }
                    else src.sendMessage(Text.of(TextColors.AQUA, TextStyles.BOLD, "Set ", val.name, " to ", TextColors.BLUE, val.value().toString()));
                    return CommandResult.success();
                }).build();
        CommandSpec option = CommandSpec.builder()
                .description(Text.of("Tells you an option's value"))
                .arguments(GenericArguments.choices(Text.of("optionname"), options))
                .child(set,"set")
                .executor((src, ctx) -> {
                    ConfigurationValue<?> val = ctx.requireOne("optionname");
                    src.sendMessage(Text.of(TextColors.AQUA, TextStyles.BOLD, val.name, TextColors.DARK_AQUA, " = ", TextColors.BLUE, val.value().toString(), " : ", TextStyles.RESET, val.description));
                    return CommandResult.success();
                }).build();
        CommandSpec config = CommandSpec.builder()
                .description(Text.of("Discord linking."))
                .permission(Config.INSTANCE.adminStaffPermission.value())
                .child(option,"option")
                .build();
        Sponge.getCommandManager().register(FrozenSuite.getPlugin(), config,"viciouscfg");*/
    }
}
