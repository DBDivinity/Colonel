package com.dbdivinity.colonel.command;

import com.dbdivinity.colonel.util.ModInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import java.util.List;

public class ForgeCommand extends CommandBase {
    //TODO: Integrated and Dedicated Command Separation
    private static final Commands commands = new Commands(Commands.CommandSelection.ALL);

    @Override
    public String getCommandName() {
        return ModInfo.baseCommand;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            commands.performCommand(sender, "help");
        }
        commands.performCommand(sender, String.join(" ", args));
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
