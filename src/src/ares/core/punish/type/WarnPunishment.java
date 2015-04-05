package src.ares.core.punish.type;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import src.ares.core.client.Client;
import src.ares.core.client.OfflineClient;
import src.ares.core.punish.Punishment;

public class WarnPunishment extends Punishment
{
	public WarnPunishment(String reason, OfflineClient offender, Client punisher)
	{
		super("Warn Punishment", reason, offender, punisher);
	}

	@Override
	public void apply()
	{
		if (getOffender().getPlayer() == null)
			return;

		Client offender = new Client(Bukkit.getPlayer(getOffender().getName()));

		if (offender != null)
		{
			offender.playSound(Sound.ITEM_BREAK, 1.0F, 1.3F);
			offender.sendMessage("Punish", ChatColor.GRAY + getPunisher().getName() + " warned you for " + getReason() + ".");
			offender.sendMessage("Punish", ChatColor.GRAY + "Remember to follow the rules, have a nice day.");
		}

		getOffender().getManager().addWarnHistory(getPunisher().getName(), getOffender().getName(), getReason());
		getNotifications().sendToStaff(false, ChatColor.GRAY + getPunisher().getName() + " issued a " + getName() + " on " + getOffender().getName() + " for " + getReason() + ".");
	}
}
