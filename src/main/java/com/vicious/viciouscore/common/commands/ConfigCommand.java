package com.vicious.viciouscore.common.commands;

import com.google.common.collect.Lists;
import com.vicious.viciouscore.ViciousCore;
import com.vicious.viciouscore.common.VCoreConfig;
import com.vicious.viciouscore.common.util.configuration.ConfigurationValue;
import com.vicious.viciouscore.common.util.tracking.values.TrackableValue;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.List;


/**
 * Changes configuration values while the game is running if possible.
 */
public class ConfigCommand extends CommandBase {
    private final List<String> alias = Lists.newArrayList("vconfig", "vcfg");

    @Override
    public 
    String getName() {
        return "vconfig";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "vconfig <set/see> <fieldname> <optional:value>)";
    }

    @Override
    public List<String> getAliases() {
        return alias;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        int mode = args[0].equalsIgnoreCase("set") ? 1 : 0;
        String fieldname = args[1];
        VCoreConfig cfg = ViciousCore.CFG;
        Object cfgval;

        if(mode == 0) {
            try {
                cfgval = cfg.getValue(fieldname);
                sender.sendMessage(new TextComponentString(fieldname + " = " + cfgval.toString()));
            } catch(Exception e){
                sender.sendMessage(new TextComponentString(fieldname + " does not exist in the config."));
            }
        }
        else{
            try {
                TrackableValue<?> val = cfg.values.get(fieldname);
                cfgval = val.setFromStringWithUpdate(args[2]);
                sender.sendMessage(new TextComponentString(fieldname + " set to " + cfgval.toString()));
                if(val instanceof ConfigurationValue<?>){
                    if(!((ConfigurationValue<?>) val).canBeModifiedOnRuntime()) sender.sendMessage(new TextComponentString(fieldname + " cannot be modified on runtime, the config has been updated however. Restart the game for changes to apply."));
                }
            } catch(Exception e){
                sender.sendMessage(new TextComponentString(fieldname + " does not exist in the config or you input an incorrect value."));
            }
        }
    }
}