package com.gmail.brandonli2010.CommandMacros;

import java.util.logging.Logger;

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
					if (thisbook.getTitle().equals(plugin.getConfig().getString("bookname")))
					{
						if (!event.getPlayer().hasPermission("commandmacros.macro"))
						{
							event.getPlayer().sendMessage("\u00a7cYou dont have permissions to use a macro.");
							return;
						}
						if ((thisbook.getPageCount() > plugin.getConfig().getInt("maxcommands")) & (!(plugin.getConfig().getInt("maxcommands") == 0)))
						{
							event.getPlayer().sendMessage("\u00a7cThere cannot be more than " + plugin.getConfig().getInt("maxcommands") + "commannds on a book.");
							return;
						}
						event.setCancelled(true);
						Logger cmdlog = plugin.getLogger();
						String pname = event.getPlayer().getName();
						Boolean cmdE = plugin.getConfig().getBoolean("commandsenabled");
						Boolean chatE = plugin.getConfig().getBoolean("chatenabled");
						for (int ind = 1; ind <= thisbook.getPageCount(); ind ++)
						{
							if (thisbook.getPage(ind).startsWith("/"))
							{
								if (cmdE)
								{
									cmdlog.info(pname + " issued server command " + thisbook.getPage(ind));
									event.getPlayer().chat(thisbook.getPage(ind));
								}
							}
							else if (chatE)
							{
								event.getPlayer().chat(thisbook.getPage(ind));
							}
						}
					}
				}
			}
		}
	}
}
