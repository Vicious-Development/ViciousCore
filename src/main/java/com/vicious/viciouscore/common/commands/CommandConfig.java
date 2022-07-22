package com.vicious.viciouscore.common.commands;

/* TODO: REIMPL
 * Changes configuration values while the game is running if possible.

public class CommandConfig extends Command {
    private final List<String> alias = Lists.newArrayList("vconfig", "vcfg");

    @Override
    public 
    String getName() {
        return "vconfig";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "vconfig <reload/set/see> <fieldname> <optional:value>)";
    }

    @Override
    public List<String> getAliases() {
        return alias;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        int mode = args[0].equalsIgnoreCase("set") ? 1 : (args[0].equalsIgnoreCase("reload") ? 2 : 0);
        String fieldname = args[1];
        VCoreConfig cfg = VCoreConfig.getInstance();
        Object cfgval;

        if(mode == 0) {
            try {
                cfgval = cfg.getValue(fieldname);
                sender.sendMessage(new TextComponentString(fieldname + " = " + cfgval.toString()));
            } catch(Exception e){
                sender.sendMessage(new TextComponentString(fieldname + " does not exist in the config."));
            }
        }
        else if (mode == 2){
            ViciousConfigManager.reload();
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
 */