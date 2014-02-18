package com.gmail.brandonli2010.CommandMacros;

import org.bukkit.plugin.java.JavaPlugin;

public class CommandMacros extends JavaPlugin {
	private final BookEvent bookListener = new BookEvent(this);
	public void onEnable()
	{
		this.saveDefaultConfig();
		this.getServer().getPluginManager().registerEvents(bookListener, this);
	}
	public void onDisable()
	{
	}
}
