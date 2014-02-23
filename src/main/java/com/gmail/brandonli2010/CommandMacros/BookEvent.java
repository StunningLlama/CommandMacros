package com.gmail.brandonli2010.CommandMacros;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
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
					event.setCancelled(true);
					if (thisbook.getAuthor().equals("@console") | thisbook.getAuthor().equals("@server"))
					{
						if (plugin.getConfig().getBoolean("doconsolebooks"))
						{
							plugin.executeconsolebook(thisbook);
						}
						return;
					}
					if (plugin.getConfig().getBoolean("selectors") & thisbook.getAuthor().startsWith("@"))
					{
						if (thisbook.getAuthor().equals("@p"))
						{
							Double shortestd = Double.POSITIVE_INFINITY;
							Player finalp = null;
							for (Player p : Bukkit.getOnlinePlayers())
							{
								if (event.getBlock().getWorld().equals(p.getWorld()) && (event.getBlock().getLocation().distanceSquared(p.getLocation()) < shortestd))
								{
									shortestd = event.getBlock().getLocation().distanceSquared(p.getLocation());
									finalp = p;
								}
							}
							if (finalp != null)
							{
								plugin.executeBook(finalp, thisbook);
							}
						}
						if (thisbook.getAuthor().equals("@a"))
						{
							for (Player p : Bukkit.getOnlinePlayers())
							{
								plugin.executeBook(p, thisbook);
							}
						}
						if (thisbook.getAuthor().equals("@r"))
						{
							if (Bukkit.getOnlinePlayers().length > 0)
							{
								Random r = new Random();
								plugin.executeBook(Bukkit.getOnlinePlayers()[r.nextInt(Bukkit.getOnlinePlayers().length)], thisbook);
							}
						}
						return;
					}
					else
					{
						Player sender = Bukkit.getPlayer(thisbook.getAuthor());
						if (sender == null)
						{
							return;
						}
						plugin.executeBook(sender, thisbook);
						return;
					}
				}
			}
		}
	}
}
