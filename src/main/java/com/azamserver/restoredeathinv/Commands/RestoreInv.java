
/*
File Name: RestoreInv.java
Part of package: com.azamserver.restoredeathinv
Description: This file alerts the plugin on what to do when a player issues the command "/restoreinv"
*/

// Declare package name
package com.azamserver.restoredeathinv.Commands;

// Import all needed libraries
import com.azamserver.restoredeathinv.Main;
import com.azamserver.restoredeathinv.Variables;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.Collection;

// Start java class
public class RestoreInv implements CommandExecutor
{
    // Declare all needed variables
    private final Main main;
    private static final String messageStart = "" + ChatColor.BOLD + "" + ChatColor.GREEN + "[" + ChatColor.RED + "RestoreInv" + ChatColor.GREEN + "]: ";

    // This constructor allows Main.java to access the command "/restoreinv"
    public RestoreInv(Main main)
    {
        // Set all needed variables
        this.main = main;
    }

    // This method alerts the plugins on what to do when the command "/restoreinv" is run
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        // Check if "/restoreinv" command has arguments
        if(args.length == 0)
        {
            // If "/restoreinv" command does not have arguments, check who sent command
            if(sender instanceof ConsoleCommandSender)
            {
                // If command was sent by console, alert console that command cannot be executed
                sender.sendMessage(messageStart + "Console cannot restore its own inventory as it does not have one");
                return true;
            }
            else if(sender.isOp() || sender.hasPermission("restoreinv.ask"))
            {
                // If "/xptransfer" command was sent by an entity that has correct perms, check if command sender has a death inventory
                if(Variables.playerList.contains(sender.getName()))
                {
                    // If command sender has a death inventory, alert console and players with correct perms that command sender wants to restore their inventory
                    Bukkit.getServer().getConsoleSender().sendMessage(messageStart + "Player \"" + sender.getName() + "\" wants to restore their inventory");
                    Bukkit.getServer().getConsoleSender().sendMessage(messageStart + "Please issue command \"/restoreinv accept " + sender.getName() + "\" to restore their inventory");
                    Bukkit.getServer().getConsoleSender().sendMessage(messageStart + "Please beware that this command is case-sensitive");
                    Collection<? extends Player> allPlayers = Bukkit.getOnlinePlayers();
                    for(Player p : allPlayers)
                    {
                        if(p.isOp() || p.hasPermission("restoreinv.accept"))
                        {
                            p.sendMessage(messageStart + "Player \"" + sender.getName() + "\" wants to restore their inventory");
                            p.sendMessage(messageStart + "Please issue command \"/restoreinv accept " + sender.getName() + "\" to restore their inventory");
                            p.sendMessage(messageStart + "Please beware that this command is case-sensitive");
                        }
                    }

                    // Alert command sender that their request has been sent
                    sender.sendMessage(messageStart + "Your request to restore your inventory has been sent");
                    return true;
                }
                else
                {
                    // If command sender does not have a death inventory, alert them that they do not have a death inventory
                    sender.sendMessage(messageStart + "You do not have an inventory to restore");
                    return true;
                }
            }
            else
            {
                // If "/xptransfer" command was sent by an entity that does not have correct perms, alert command sender that they do not have the correct perms
                sender.sendMessage(messageStart + "You do not have the permission to run this command");
                return true;
            }
        }
        else if(args.length == 2)
        {
            // If "/restoreinv" command has 2 arguments, check if command sender has correct perms
            if(sender instanceof ConsoleCommandSender || sender.isOp() || sender.hasPermission("restoreinv.accept") || sender.hasPermission("restoreinv.decline"))
            {
                // If command sender has correct perms, check the first argument sent with command
                if (args[0].equals("accept"))
                {
                    // If the first argument sent with command was "accept", recheck if command sender has correct perms
                    if(sender instanceof ConsoleCommandSender || sender.isOp() || sender.hasPermission("restoreinv.accept"))
                    {
                        // If command sender has correct perms, check if database contains specified player's death inventory
                        if(Variables.playerList.contains(args[1]))
                        {
                            // If database contains specified player's death inventory, drop all items in their current inventory
                            for (ItemStack item : Bukkit.getPlayer(args[1]).getInventory().getContents())
                            {
                                if (item != null)
                                    Bukkit.getPlayer(args[1]).getLocation().clone().getWorld().dropItemNaturally(Bukkit.getPlayer(args[1]).getLocation().clone(), item.clone());
                            }
                            Bukkit.getPlayer(args[1]).getInventory().clear();

                            // Restore the specified player's inventory using snapshot
                            for(ItemStack item : Variables.inventories.get(Variables.playerList.indexOf(args[1])))
                                Bukkit.getPlayer(args[1]).getInventory().addItem(item);

                            // Delete the snapshot
                            Variables.inventories.remove(Variables.playerList.indexOf(args[1]));
                            Variables.playerList.remove(args[1]);

                            // Alert command sender that they have restored the specified player's inventory
                            sender.sendMessage(messageStart + "You have restored Player \"" + args[1] + "\"'s inventory");
                            Bukkit.getPlayer(args[1]).sendMessage(messageStart + "Your inventory has been restored to right before you died");
                            return true;
                        }
                        else
                        {
                            // If database does not contain specified player's death inventory, alert command sender that the database does not contain specified player's death inventory
                            sender.sendMessage(messageStart + "Player \"" + args[1] + "\" does not exist in the database");
                            sender.sendMessage(messageStart + "Please make sure the player has died, that the IGN is case-sensitive, and that the player's restore hasn't already been declined");
                            return true;
                        }
                    }
                    else
                    {
                        // If command sender does not have correct perms, alert command sender that they do not have the correct perms
                        sender.sendMessage(messageStart + "You do not have the permission to run this command");
                        return true;
                    }
                }
                else if (args[0].equals("decline"))
                {
                    // If the first argument sent with command was "decline", recheck if command sender has correct perms
                    if(sender instanceof ConsoleCommandSender || sender.isOp() || sender.hasPermission("restoreinv.decline"))
                    {
                        // If command sender has correct perms, check if database contains specified player's death inventory
                        if(Variables.playerList.contains(args[1]))
                        {
                            // If database contains specified player's death inventory, delete snapshot of specified player's death inventory
                            Variables.inventories.remove(Variables.playerList.indexOf(args[1]));
                            Variables.playerList.remove(args[1]);

                            // Alert specified player that the snapshot of their death inventory has been deleted
                            sender.sendMessage(messageStart + "Player \"" + args[1] + "\" can no longer request to restore their inventory for their previous death");

                            // Alert command sender that they have deleted the snapshot of specified player's death inventory
                            Bukkit.getPlayer(args[1]).sendMessage(messageStart + "You can no longer ask to restore your inventory for your previous death");
                            return true;
                        }
                        else
                        {
                            // If database does not contain specified player's death inventory, alert command sender that the database does not contain specified player's death inventory
                            sender.sendMessage(messageStart + "Player \"" + args[1] + "\" does not exist in the database");
                            sender.sendMessage(messageStart + "Please make sure the player has died, that the IGN is case-sensitive, and that the player's restore hasn't already been declined");
                            return true;
                        }
                    }
                    else
                    {
                        // If command sender does not have correct perms, alert command sender that they do not have the correct perms
                        sender.sendMessage(messageStart + "You do not have the permission to run this command");
                        return true;
                    }
                }
                else
                {
                    // If the first argument is invalid, alert command sender that the first argument is invalid
                    sender.sendMessage(messageStart + "\"" + args[0] + "\" is not a valid argument");
                    return true;
                }
            }
            else
            {
                // If command sender does not have correct perms, alert command sender that they do not have the correct perms
                sender.sendMessage(messageStart + "You do not have the permission to run this command");
                return true;
            }
        }
        else
        {
            // If the command has 1 or 3+ arguments, check who sent command
            if(sender instanceof ConsoleCommandSender || sender.isOp() || sender.hasPermission("restoreinv.helpMenu"))
            {
                // If command sender does have perms to run command, display a help menu
                sender.sendMessage(messageStart + "Help Menu");
                sender.sendMessage(messageStart + "/restoreinv : Ask OP to restore your inventory after death");
                sender.sendMessage(messageStart + "/restoreinv accept <playerIGN> : Restore a specified player's inventory");
                sender.sendMessage(messageStart + "/restoreinv decline <playerIGN> : Decline to restore a specified player's inventory and disallow them to ask for another restore");
                return true;
            }
            else
            {
                // If command sender does not have perms to run command, alert the command sender that they do not have the correct perms to execute the command
                sender.sendMessage(messageStart + "You do not have the permission to run this command");
                return true;
            }
        }
    }
}
