package deathstep.deathStep10;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;


public final class DeathStep10 extends JavaPlugin implements Listener {

    /* DeathStep plugin created by Via. */
    /* Feel free to improve, use and enjoy it. */


    /* All our death-blocks. */
    private HashSet<Material> deathBlocks = new HashSet<>();
    /* By default, server is turned on (ofc) */
    private boolean isPluginEnabled = true;

    @Override
    public void onEnable() {
        /* Saving configuration by default, if it not exist. */
        saveDefaultConfig();
        /* Load config. */
        loadConfig();


        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info(ChatColor.GREEN + "Deathstep is turned on. Enjoy!");

    }

    @Override
    public void onDisable() {
        /* Disabling, logging. */
        getLogger().info(ChatColor.RED + "Deathstep is turned off. Have a nice day!");

    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        /* Looking if boolean is true. (by default = true) */
        if (isPluginEnabled) {
            /* Checking if the entered command is "off" */
            if(command.getName().equalsIgnoreCase("off")) {
                /* Disabling plugin. */
                getServer().getPluginManager().disablePlugin(this);
                /* Printing it to our user. */
                sender.sendMessage(ChatColor.RED + "Deathstep is turned off.\nTo turn on, you need to restart server.\n" + ChatColor.GREEN + "Have a nice day!");
                /* Flagging isPluginEnabled as false. (idk why, this is dumb but ok) */
                isPluginEnabled = false;

            }

            /* Checking if the command isn't off, cause we've got only 1 command */
            else if (!command.getName().equalsIgnoreCase("off")) {
                /* Idiotic chat colors but idc */
                sender.sendMessage("Command isn't found." + ChatColor.YELLOW + "Available commands: \n" + ChatColor.GOLD + "/off - turns off the plugin.");
            }

        }



        /* In the end always returning true. */
        return true;

    }

    private void loadConfig() {
        /* Initializing config through FileConfiguration class. */
        FileConfiguration config = getConfig();

        /* Loading death-blocks configuration into that List. */
        List<String> blockNames = config.getStringList("death-blocks");

        /* We're detecting our block and converting it into MATERIAL. */

        for (String block : blockNames) {
            try {
                /* Adding our block. */
                deathBlocks.add(Material.valueOf(block.toUpperCase()));

                /* Catching exception, calling getLogger to print it. */
            } catch (IllegalArgumentException e) {
                getLogger().severe(ChatColor.RED + "Invalid blocks! " + blockNames);
            }
        }
    }


    /* Player position's handler. */

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

        /* Determine block under the player by event callback, specifying it into Material. */
        Material blockUnderPlayer = event.getPlayer().getLocation().subtract(0, 1, 0).getBlock().getType();


        /* If our <Hash> contains called block (blockUnderPlayer) */
        if(deathBlocks.contains(blockUnderPlayer)) {
            /* Killing player, tracing to him message. */
            event.getPlayer().setHealth(0.0);
            event.getPlayer().sendMessage("Stepped on: " + ChatColor.RED + blockUnderPlayer);
        }
    }
}



