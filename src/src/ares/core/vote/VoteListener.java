package src.ares.core.vote;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;

import src.ares.core.Main;
import src.ares.core.chat.Notification;
import src.ares.core.client.Client;
import src.ares.core.client.OfflineClient;
import src.ares.core.common.Module;
import src.ares.core.common.util.Chat;
import src.ares.core.currency.type.GoldCurrency;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

public class VoteListener extends Module
{
	private Notification notification = new Notification();

	public VoteListener()
	{
		super("Vote");
	}

	@EventHandler
	public void onVote(VotifierEvent event)
	{
		Vote vote = event.getVote();
		Client online = new Client(Bukkit.getPlayer(vote.getUsername()));
		OfflineClient offline = new OfflineClient(vote.getUsername());
		String rarity = "";
		Reward reward = new Reward(offline);
		int amount = reward.goldReward(100, 500);

		Main.log("Received vote from " + vote.getUsername() + " and reward of " + amount + " Gold", Level.INFO);

		if (amount <= 200)
		{
			rarity = ChatColor.GREEN + "Common";

			if (online != null)
				online.playSound(Sound.HORSE_ARMOR, 1.0F, 1.3F);
		}
		else if (amount <= 400 && amount > 200)
		{
			rarity = ChatColor.RED + "Fortune";

			if (online != null)
				online.playSound(Sound.NOTE_PLING, 1.0F, 1.5F);
		}
		else if (amount > 400)
		{
			rarity = ChatColor.LIGHT_PURPLE + "Jackpot";

			if (online != null)
				online.playSound(Sound.LEVEL_UP, 1.0F, 1.5F);
		}

		notification.sendToPlayers(false, getModuleName(), Chat.player(vote.getUsername()) + " voted and hit a " + rarity + ChatColor.WHITE + " amount of " + Chat.gold(new GoldCurrency(amount).getFormatted()) + ".");
	}
}
