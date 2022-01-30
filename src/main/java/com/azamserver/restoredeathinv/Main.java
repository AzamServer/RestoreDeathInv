
/*
File Name: Main.java
Part of package: com.azamserver.restoredeathinv
Description: This file is the root of the plugin and tells the plugins what to do on enabling and disabling
*/

// Declare package name
package com.azamserver.restoredeathinv;

// Import all needed libraries
import org.bukkit.plugin.java.JavaPlugin;
import com.azamserver.restoredeathinv.Commands.RestoreInv;

// Start java class
public final class Main extends JavaPlugin
{
    // Code will run on plugin enable
    @Override
    public void onEnable()
    {
        // Allow plugin to run code on player death
        getServer().getPluginManager().registerEvents(new Events(), this);

        // Allow command "/restoreinv" to work
        getServer().getPluginCommand("restoreinv").setExecutor(new RestoreInv(this));
    }

    // Code will run on plugin disable
    @Override
    public void onDisable()
    {
        // Since there isn't anything for the plugin to do on disable, this has been left empty
    }
}
