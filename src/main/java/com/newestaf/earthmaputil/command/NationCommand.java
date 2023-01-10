package com.newestaf.earthmaputil.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

@CommandAlias("nation|국가")
public class NationCommand extends BaseCommand {

    @Dependency
    private final Plugin plugin;

    public NationCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Default
    @CatchUnknown
    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }




}
