package com.vicious.viciouscore.common.commands;

import com.google.common.collect.Lists;
import com.vicious.viciouscore.common.structure.ViciousCoreStructureSystem;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import java.util.List;

public class CommandStructure extends CommandBase {
    private final List<String> alias = Lists.newArrayList("vcstructure", "vcs");

    @Override
    public String getName() {
        return "vcstructure";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "vstructure <save:load:edit:paste> <name>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public List<String> getAliases() {
        return alias;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(!(sender.getCommandSenderEntity() instanceof EntityPlayer)) return;
        String op1 = args[0];
        String itemname = "";
        if (args.length > 1) itemname = args[1];
        if (op1.equalsIgnoreCase("save")) {
            if(itemname != null) ViciousCoreStructureSystem.saveStructure(itemname,sender.getCommandSenderEntity().getUniqueID());
            else ViciousCoreStructureSystem.saveLoadedStructure(sender.getCommandSenderEntity().getUniqueID());
        }
        else if (op1.equalsIgnoreCase("load")) {
            ViciousCoreStructureSystem.loadStructure(itemname,sender.getCommandSenderEntity().getUniqueID());
        }
        else if (op1.equalsIgnoreCase("paste")) {
            ViciousCoreStructureSystem.pasteStructure(getCommandSenderAsPlayer(sender));
        }
        else if (op1.equalsIgnoreCase("edit")) {
            ViciousCoreStructureSystem.editLoadedStructure((EntityPlayer) sender.getCommandSenderEntity());
        }
    }
}
