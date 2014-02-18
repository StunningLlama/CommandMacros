package com.gmail.brandonli2010.CommandMacros;

import org.bukkit.plugin.java.JavaPlugin;

public class CommandMacros extends JavaPlugin {
	private final BookEvent bookListener = new BookEvent(this);

	@Override
	public void onEnable()
	{
		this.saveDefaultConfig();
		this.getServer().getPluginManager().registerEvents(bookListener, this);
	}

	@Override
	public void onDisable() {}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (cmd.getName().equalsIgnoreCase("macro-reload"))
		{
			this.reloadConfig();
			sender.sendMessage("\u00a7aReloaded.");
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("macro-getbook"))
		{
			if (!(sender instanceof Player))
			{
				sender.sendMessage("\u00a7cOnly players can execute this command.");
			}
			Player p = (Player) sender;
			if ((p.getItemInHand() != null) && (p.getItemInHand().getType() == Material.WRITTEN_BOOK))
			{
				p.getItemInHand().setType(Material.BOOK_AND_QUILL);
				sender.sendMessage("\u00a7eUnsigned.");
				return true;
			}
			sender.sendMessage("\u00a7cYou are not holding a written book.");
			return true;
		}
		return false;
	}
}
