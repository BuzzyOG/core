package src.ares.core.punish.type;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import src.ares.core.Main;
import src.ares.core.chat.Notification;
import src.ares.core.client.Client;
import src.ares.core.client.OfflineClient;
import src.ares.core.common.util.Chat;
import src.ares.core.punish.Punishment;

public class KickPunishment extends Punishment
{
	public KickPunishment(String reason, OfflineClient offender, Client punisher)
	{
		super("Kick Punishment", reason, offender, punisher);
	}

	@Override
	public void apply()
	{
		if (getOffender().getPlayer() == null)
			return;

		final Client offender = new Client(Bukkit.getPlayer(getOffender().getName()));

		offender.playLocationSound(Sound.ENDERDRAGON_GROWL, 1.0F, 1.0F);

		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				offender.kick(ChatColor.RED + "" + ChatColor.BOLD + "Kicked from the server", getReason());

				Notification manager = new Notification();

				if (getOffender() != null && getPunisher() != null)
					manager.sendToPlayers(true, ChatColor.RED + "" + ChatColor.BOLD + getName(), Chat.player(getOffender().getName()) + " was kicked by " + Chat.player(getPunisher().getName()) + " because of " + Chat.tool(getReason()));
			}
		}, 10);
	}
}
