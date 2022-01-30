
/*
File Name: Events.java
Part of package: com.azamserver.restoredeathinv
Description: This file alerts the plugin what to do on a player's death
*/

// Declare package name
package com.azamserver.restoredeathinv;

// Import all needed libraries
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;

// Start java class
public class Events implements Listener
{
    // Run code on a player's death
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        // If player died, check if player's previous snapshot of inventory is already taken and stored
        if(Variables.playerList.contains(event.getEntity().getName()))
        {
            // If player's previous snapshot of inventory is already taken and stored, delete old snapshot
            Variables.inventories.remove(Variables.playerList.indexOf(event.getEntity().getName()));
            Variables.playerList.remove(event.getEntity().getName());
        }

        // Create new snapshot of player inventory
        final ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        for(ItemStack item : event.getEntity().getInventory())
        {
            if (item != null)
                items.add(item);
        }

        // Store new snapshot and player's IGN to database
        Variables.inventories.add(items);
        Variables.playerList.add(event.getEntity().getName());
    }
}
