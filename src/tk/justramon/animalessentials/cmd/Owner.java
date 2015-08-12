package tk.justramon.animalessentials.cmd;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;

import tk.justramon.animalessentials.core.AnimalEssentials;
import tk.justramon.animalessentials.util.Utilities;

public class Owner implements IAECommand,Listener
{
	private static boolean waiting = false;
	private static String playerName;
	public static Plugin plugin;

	@Override
	public void exe(Plugin pl, final Player p, Command cmd, String[] args) throws IOException
	{
		if(waiting)
		{
			Utilities.sendChatMessage(p, "A player is currently checking the owner of an animal and the magic invisible ownerchecking device can't handle that much. Please try again later.");
			return;
		}

		plugin = pl;
		playerName = p.getName();
		Utilities.sendChatMessage(p, "Please rightclick the animal you want to check the owner of.");
		waiting = true;
		Bukkit.getScheduler().runTaskLater(AnimalEssentials.instance, new Runnable(){
			@Override
			public void run()
			{
				if(waiting)
				{
					waiting = false;
					Utilities.sendChatMessage(p, "You ran out of time to select an animal to check the owner of. Use /()/ae owner()/ to start again.");
				}
			}
		}, 10L * 20); //10 seconds * 20 (server ticks/second)
	}

	@EventHandler
	public void onPlayerInteractEntity(final PlayerInteractEntityEvent event)
	{
		if(waiting && event.getPlayer().getName().equals(playerName))
		{
			final Entity entity = event.getRightClicked();

			if(!Utilities.isAnimal(entity) || !(entity instanceof Tameable))
			{
				Utilities.sendChatMessage(event.getPlayer(), "You can't check the owner of this mob, it's " + Utilities.aN(entity.getType().getName()) + " /()" + entity.getType().getName() + "()/ and not a tameable animal.");
				event.setCancelled(true);
				return;
			}

			if(((Tameable)entity).isTamed())
			{
			if(((Tameable)entity).getOwner().getName().equals(playerName))
				Utilities.sendChatMessage(event.getPlayer(), "This /()" + entity.getName() + "()/ is owned by /()you()/.");
			else
				Utilities.sendChatMessage(event.getPlayer(), "This /()" + entity.getName() + "()/ is owned by /()" + ((Tameable)entity).getOwner().getName() + "()/.");
			}
			else
			{
				Utilities.sendChatMessage(event.getPlayer(), "This /()" + entity.getName() + "()/ is not tamed.");
				return;
			}
			
			waiting = false;
			Bukkit.getScheduler().cancelTasks(plugin);
			event.setCancelled(true);
		}
	}

	@Override
	public String getAlias()
	{
		return "owner";
	}

	@Override
	public boolean isConsoleCommand()
	{
		return false;
	}

	@Override
	public String[] getHelp()
	{
		return new String[]{
				"Shows the owner of the right-clicked animal."
		};
	}

	@Override
	public String getPermission()
	{
		return "aess.owner";
	}

	@Override
	public List<Integer> allowedArgLengths()
	{
		return Arrays.asList(new Integer[]{1}); // /ae owner
	}

	@Override
	public String getSyntax()
	{
		return "";
	}
}