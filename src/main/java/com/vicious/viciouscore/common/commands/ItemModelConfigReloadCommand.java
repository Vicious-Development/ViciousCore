package com.vicious.viciouscore.common.commands;

import com.google.common.collect.Lists;
import com.vicious.viciouscore.client.configuration.HeldItemOverrideCFG;
import com.vicious.viciouscore.common.util.VUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import java.util.List;

public class ItemModelConfigReloadCommand extends CommandBase {
    private final List<String> alias = Lists.newArrayList("ircfg");

    @Override
    public
    String getName() {
        return "ircfg";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "ircfg reload <optional:itemname>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public  List<String> getAliases() {
        return alias;
    }

    @Override
    public void execute( MinecraftServer server,  ICommandSender sender, String[] args) throws CommandException {
        String reload = args[0];
        String itemname = "";
        if(args.length > 1) itemname = args[1];
        if(reload.equalsIgnoreCase(reload)){
            if(!VUtil.isEmptyOrNull(itemname)) HeldItemOverrideCFG.read(itemname);
            HeldItemOverrideCFG.readAll();
        }
    }
}