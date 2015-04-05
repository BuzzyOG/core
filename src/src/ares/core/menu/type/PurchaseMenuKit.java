package src.ares.core.menu.type;

import src.ares.core.Main;
import src.ares.core.battle.kit.Kit;
import src.ares.core.client.Client;
import src.ares.core.currency.ICurrency;
import src.ares.core.currency.type.GoldCurrency;
import src.ares.core.menu.PurchaseMenu;

public class PurchaseMenuKit extends PurchaseMenu
{
	private final Kit kit;

	public PurchaseMenuKit(Kit sellableKit, GoldCurrency currencyCost)
	{
		super("Purchase Kit", sellableKit.getDisplay(), currencyCost);
		this.kit = sellableKit;
		
		Main.debug("");
		Main.debug("Constructor: " + sellableKit.getName());
		Main.debug("Kit: " + kit.getName());
		Main.debug("");
	}

	@Override
	public void PurchaseProduct(ICurrency currency, Client client)
	{
		client.sendRaw(kit.getName() + " for purchase.");
		// client.getManager().setKitOwned(kit, true);
	}

	@Override
	public boolean PrePaymentChecks(Client client)
	{
		Main.debug("");
		Main.debug("Pre-Payment: " + kit.getName());
		Main.debug("");

		return true;
	}
}
