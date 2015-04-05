package src.ares.core.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

import src.ares.core.client.Client;
import src.ares.core.common.crafted.CraftedItemStack;
import src.ares.core.currency.ICurrency;

public abstract class PurchaseMenu extends Menu
{
	private final ItemStack item;
	private ICurrency currency;

	private Material cancelBlock = Material.REDSTONE_BLOCK;
	private String cancelButton = ChatColor.RED + "" + ChatColor.BOLD + "Cancel";

	private Material purchaseBlock = Material.EMERALD_BLOCK;
	private String purchaseButton = ChatColor.GREEN + "" + ChatColor.BOLD + "Purchase";

	public PurchaseMenu(String menuName, ItemStack sellableItem, ICurrency currencyType)
	{
		super(Material.STONE, menuName, 54);

		item = sellableItem;
		currency = currencyType;

		DependOnEvents(true);
	}

	private void addPurchaseButtons()
	{
		String cost = ChatColor.WHITE + "" + ChatColor.BOLD + currency.getFormatted();

		AddDisplay(29, new CraftedItemStack(purchaseBlock, purchaseButton, cost).build());
		AddDisplay(28, new CraftedItemStack(purchaseBlock, purchaseButton, cost).build());
		AddDisplay(27, new CraftedItemStack(purchaseBlock, purchaseButton, cost).build());
		AddDisplay(36, new CraftedItemStack(purchaseBlock, purchaseButton, cost).build());
		AddDisplay(37, new CraftedItemStack(purchaseBlock, purchaseButton, cost).build());
		AddDisplay(38, new CraftedItemStack(purchaseBlock, purchaseButton, cost).build());
		AddDisplay(45, new CraftedItemStack(purchaseBlock, purchaseButton, cost).build());
		AddDisplay(46, new CraftedItemStack(purchaseBlock, purchaseButton, cost).build());
		AddDisplay(47, new CraftedItemStack(purchaseBlock, purchaseButton, cost).build());
	}
	
	private void addCancelButtons()
	{
		AddDisplay(33, new CraftedItemStack(cancelBlock, cancelButton).build());
		AddDisplay(34, new CraftedItemStack(cancelBlock, cancelButton).build());
		AddDisplay(35, new CraftedItemStack(cancelBlock, cancelButton).build());
		AddDisplay(42, new CraftedItemStack(cancelBlock, cancelButton).build());
		AddDisplay(43, new CraftedItemStack(cancelBlock, cancelButton).build());
		AddDisplay(44, new CraftedItemStack(cancelBlock, cancelButton).build());
		AddDisplay(51, new CraftedItemStack(cancelBlock, cancelButton).build());
		AddDisplay(52, new CraftedItemStack(cancelBlock, cancelButton).build());
		AddDisplay(53, new CraftedItemStack(cancelBlock, cancelButton).build());
	}

	@Override
	public void InventoryClick(InventoryAction action, ItemStack item, Player player)
	{
		Client client = new Client(player);

		if (item.getType() == purchaseBlock)
		{
			// Purchase success
			
			if (PrePaymentChecks(client))
			{
				if (client.removeCurrency(currency, true))
				{
					PlaySuccessSound(player);
					PlayVillagerSuccess(player);

					PurchaseProduct(currency, client);
				}

				// Purchase failure

				else
				{
					PlayErrorSound(player);
				}
			}
		}
		else if (item.getType() == cancelBlock)
		{
			PlayVillagerFailure(player);
		}

		client.unload();
	}

	@Override
	public void InventoryConstruct(Player player)
	{
		AddDisplay(13, item);

		addPurchaseButtons();
		addCancelButtons();
	}

	protected void PlayVillagerFailure(Player player)
	{
		player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
	}

	protected void PlayVillagerSuccess(Player player)
	{
		player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1.0F, 1.0F);
	}

	public abstract void PurchaseProduct(ICurrency currency, Client client);

	public abstract boolean PrePaymentChecks(Client client);
}
