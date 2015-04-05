package src.ares.core.punish.type;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import src.ares.core.client.Client;
import src.ares.core.client.OfflineClient;
import src.ares.core.common.util.Chat;
import src.ares.core.punish.PunishmentAuth;
import src.ares.core.punish.TimedPunishment;

public class BanPunishment extends TimedPunishment
{
	public BanPunishment(String reason, OfflineClient offender, Client punisher, double duration)
	{
		super("Ban Punishment", reason, offender, punisher, duration);
	}

	@Override
	public void apply()
	{
		long current = System.currentTimeMillis() / 60000;
		String date = null;

		Player offenderPlayer = Bukkit.getPlayer(getOffender().getName());
		Client offender = null;

		// Check if the player we need is offline.

		if (offenderPlayer != null)
			offender = new Client(offenderPlayer);

		// If the duration of the ban is temporary.

		if (getDuration() >= 0)
		{
			getOffender().getManager().setBanned(current + getDuration(), getReason(), getPunisher().getName());
			date = PunishmentAuth.getInstance().format(getOffender().getManager().getBanDuration());

			if (offender != null)
				offender.kick(ChatColor.RED + "Disconnected from the server", "Banned because of " + ChatColor.YELLOW + getReason() + ChatColor.WHITE + " until " + ChatColor.AQUA + date + ChatColor.WHITE + " by " + Chat.player(getPunisher().getName()) + ".");
		}

		// If the duration of the ban is permanent.

		else
		{
			getOffender().getManager().setBanned(-1, getReason(), getPunisher().getName());

			if (offender != null)
				offender.kick(ChatColor.RED + "Disconnected from the server", "Banned because of " + ChatColor.YELLOW + getReason() + ChatColor.WHITE + " until " + ChatColor.AQUA + "Permanent" + ChatColor.WHITE + " by " + Chat.player(getPunisher().getName()) + ".");
		}

		getOffender().getManager().addBanHistory(getPunisher().getName(), getOffender().getName(), Double.toString(getDuration()) + " minutes", getReason());
		getNotifications().sendToStaff(false, ChatColor.GRAY + getPunisher().getName() + " issued a " + getName() + " on " + getOffender().getName() + " for " + getFormattedDuration() + ".");
	}
}
