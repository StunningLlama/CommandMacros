package com.gmail.brandonli2010.CommandMacros;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandMacros extends JavaPlugin {
	private final BookEvent bookListener = new BookEvent(this);

	public String FirstToken(String str)
	{
		StringBuilder toreturn = new StringBuilder("");
		if (str.length() > 1)
		{
			for (int si = 1; si < str.length(); si++)
			{
				if (str.charAt(si) == ' ')
				{
					break;
				}
				else
				{
					toreturn.append(str.charAt(si));
				}
			}
		}
		return toreturn.toString();
	}

	public void executeBook(Player p, BookMeta pages)
	{
		Logger cmdlog = getLogger();
		String pname = p.getName();
		Boolean cmdE = getConfig().getBoolean("commandsenabled");
		Boolean chatE = getConfig().getBoolean("chatenabled");
		Command cmd;
		for (int ind = 1; ind <= pages.getPageCount(); ind ++)
		{
			if (pages.getPage(ind).startsWith("/"))
			{
				if (cmdE)
				{
					cmd = Bukkit.getPluginCommand(FirstToken(pages.getPage(ind)));
					if ((cmd != null) && getConfig().getStringList("disabledcommands").contains(cmd.getName()))
					{
						p.sendMessage("\u00a7cCommandMacros: Command was cancelled because it is disabled: " + pages.getPage(ind));
						cmdlog.info("Cancelled server command being executed by " + p.getName() + ": " + pages.getPage(ind));
					}
					else
					{
						cmdlog.info(pname + " issued server command " + pages.getPage(ind));
						p.chat(pages.getPage(ind));
					}
				}
			}
			else if (chatE)
			{
				p.chat(pages.getPage(ind));
			}
		}
	}

	public void executeconsolebook(BookMeta pages)
	{
		Logger cmdlog = getLogger();
		Command cmd;
		for (int ind = 1; ind <= pages.getPageCount(); ind ++)
		{
			cmd = Bukkit.getPluginCommand(FirstToken(pages.getPage(ind)));
			if ((cmd != null) && getConfig().getStringList("disabledcommands").contains(cmd.getName()))
			{
				cmdlog.info("Cancelled server command being executed by CONSOLE: " + pages.getPage(ind));
			}
			else
			{
				cmdlog.info("CONSOLE issued server command " + pages.getPage(ind));
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), pages.getPage(ind));
			}
		}
	}

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
		if (cmd.getName().equalsIgnoreCase("macro"))
		{
			if (!(sender instanceof Player))
			{
				sender.sendMessage("\u00a7cOnly players can execute this command.");
			}
			Player p = (Player) sender;
			if ((p.getItemInHand() != null) && (p.getItemInHand().getType() == Material.WRITTEN_BOOK))
			{
				BookMeta thisbook = (BookMeta) p.getItemInHand().getItemMeta();
				if (thisbook.getTitle().equals(getConfig().getString("bookname")))
				{
					if ((thisbook.getPageCount() > getConfig().getInt("maxcommands")) & (!(getConfig().getInt("maxcommands") == 0)))
					{
						sender.sendMessage("\u00a7cThere cannot be more than " + getConfig().getInt("maxcommands") + " commands in a book.");
					}
					executeBook(p, thisbook);
				}
			}
			return true;
		}
		return false;
	}
}
