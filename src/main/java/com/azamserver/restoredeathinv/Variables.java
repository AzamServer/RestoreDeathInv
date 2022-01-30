
/*
File Name: Variables.java
Part of package: com.azamserver.restoredeathinv
Description: This file stores all the variables needed across multiple files
*/

// Declare package name
package com.azamserver.restoredeathinv;

// Import all needed libraries
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;

// Start java class
public class Variables
{
    // Create an ArrayList variable to store snapshot of a player's inventory of right before they died
    public static final ArrayList<ArrayList<ItemStack>> inventories = new ArrayList<ArrayList<ItemStack>>();

    // Create an ArrayList variable to store player's IGN
    public static final ArrayList<String> playerList = new ArrayList<String>();
}
