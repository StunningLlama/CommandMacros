package com.gmail.brandonli2010.CommandMacros;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.BookMeta;

public class BookEvent implements Listener {
	private final CommandMacros plugin;
	public BookEvent(CommandMacros instance) {
		plugin = instance;
	}
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if ((event.getAction() == Action.LEFT_CLICK_AIR) | (event.getAction() == Action.LEFT_CLICK_BLOCK))
		{
			if (event.getItem() != null)
			{
				if (event.getItem().getType() == Material.WRITTEN_BOOK)
				{
					BookMeta thisbook = (BookMeta) event.getItem().getItemMeta();
					if ((thisbook.getTitle() != null) && thisbook.getTitle().equals(plugin.getConfig().getString("bookname")))
					{
						if (!event.getPlayer().hasPermission("commandmacros.macro"))
						{
							event.getPlayer().sendMessage("\u00a7cYou dont have permissions to use a macro.");
							return;
						}
						if ((thisbook.getPageCount() > plugin.getConfig().getInt("maxcommands")) & (!(plugin.getConfig().getInt("maxcommands") == 0)))
						{
							event.getPlayer().sendMessage("\u00a7cThere cannot be more than " + plugin.getConfig().getInt("maxcommands") + "commands in a book.");
							return;
						}
						event.setCancelled(true);
						plugin.executeBook(event.getPlayer(), thisbook);
					}
				}
			}
		}
	}

	@EventHandler
	public void onBlockDispense(BlockDispenseEvent event)
	{
		if (!plugin.getConfig().getBoolean("enabledispensers"))
		{
			return;
		}
		if (event.getItem() != null)
		{
			if (event.getItem().getType() == Material.WRITTEN_BOOK)
			{
				BookMeta thisbook = (BookMeta) event.getItem().getItemMeta();
				if ((thisbook.getTitle() != null) && thisbook.getTitle().equals(plugin.getConfig().getString("bookname")))
				{
					if (thisbook.getAuthor().equals("@console"))
					{
						if (plugin.getConfig().getBoolean("doconsolebooks"))
						{
							event.setCancelled(true);
							plugin.executeconsolebook(thisbook);
						}
						return;
					}
					Player sender = Bukkit.getPlayer(thisbook.getAuthor());
					if (sender == null)
					{
						return;
					}
					if (!sender.hasPermission("commandmacros.macro"))
					{
						sender.sendMessage("\u00a7cYou dont have permissions to use a macro.");
						return;
					}
					if ((thisbook.getPageCount() > plugin.getConfig().getInt("maxcommands")) & (!(plugin.getConfig().getInt("maxcommands") == 0)))
					{
						sender.sendMessage("\u00a7cThere cannot be more than " + plugin.getConfig().getInt("maxcommands") + " commands in a book.");
						return;
					}
					event.setCancelled(true);
					plugin.executeBook(sender, thisbook);
				}
			}
		}
	}
}
