package src.ares.core.punish.type;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import src.ares.core.client.Client;
import src.ares.core.client.OfflineClient;
import src.ares.core.punish.TimedPunishment;

public class MutePunishment extends TimedPunishment
{
	public MutePunishment(String reason, OfflineClient offender, Client punisher, double duration)
	{
		super("Mute Punishment", reason, offender, punisher, duration);
	}

	@Override
	public void apply()
	{
		Player offenderPlayer = Bukkit.getPlayer(getOffender().getName());
		long current = System.currentTimeMillis() / 60000;
		Client offender = null;

		// Check if the player we need is offline.

		if (offenderPlayer != null)
			offender = new Client(offenderPlayer);

		if (getDuration() >= 0)
		{
			getOffender().getManager().setMute(current + getDuration(), getReason(), getPunisher().getName());

			if (offender != null)
				offender.sendMessage("Punish", ChatColor.GRAY + getPunisher().getName() + " muted you for " + getDuration() + " minutes because of " + getReason() + ".");
		}
		else
		{
			getOffender().getManager().setMute(-1, getReason(), getPunisher().getName());

			if (offender != null)
				offender.sendMessage("Punish", ChatColor.GRAY + getPunisher().getName() + " muted you permanently because of " + getReason() + ".");
		}

		getOffender().getManager().addMuteHistory(getPunisher().getName(), getOffender().getName(), Double.toString(getDuration()) + " minutes", getReason());
		getNotifications().sendToStaff(false, ChatColor.GRAY + getPunisher().getName() + " issued a " + getName() + " on " + getOffender().getName() + " for " + getFormattedDuration() + ".");
	}
}
