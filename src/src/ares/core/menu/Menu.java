package src.ares.core.menu;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import src.ares.core.Main;
import src.ares.core.common.Module;
import src.ares.core.common.crafted.CraftedItemStack;
import src.ares.core.common.util.Chat;

/**
 * The Menu Class creates a graphical in-game inventory (GUI) that appears as a popup on a specific player.
 * This inventory may contain ItemStacks that are clickable to trigger a series of events.
 */
public abstract class Menu extends Module
{
	private boolean dependOnEvents;
	private HashMap<Integer, ItemStack> displays = new HashMap<>();
	private ItemStack display;
	private Material material;
	private String name;
	private int slots;
	private Inventory inventory;
	private boolean forceClose = true;

	/**
	 * Default Constructor
	 * 
	 * @param menuDisplay The material of the display.
	 * @param menuName The name of the menu.
	 * @param menuSlots The slots of the menu, must be multiple of 9.
	 */
	public Menu(Material menuDisplay, String menuName, int menuSlots)
	{
		super("Menu");
		this.display = new CraftedItemStack(menuDisplay, menuName).build();

		this.material = menuDisplay;
		this.name = menuName;
		this.slots = menuSlots;
		this.inventory = Bukkit.createInventory(null, slots, name);

		Main.debug("Creating " + menuName + " with " + menuSlots + " slots.");
	}

	/**
	 * Apply checks when a specific display is clicked.
	 * 
	 * @param item The display of the ItemStack.
	 * @param player The player to retrieve data from.
	 */
	protected abstract void InventoryClick(InventoryAction action, ItemStack item, Player player);

	/**
	 * Everything that should be added in the inventory.
	 * 
	 * @param player The player to retrieve data from.
	 */
	protected abstract void InventoryConstruct(Player player);

	/**
	 * No material Constructor
	 * 
	 * @param menuName The name of the menu.
	 * @param menuSlots The slots of the menu, must be multiple of 9.
	 */
	public Menu(String menuName, int menuSlots)
	{
		this(Material.STONE, menuName, menuSlots);
	}

	/**
	 * Adds a display (ItemStack) to the Inventory.
	 * 
	 * @param slot The slot of the item.
	 * @param item The display of the item.
	 */
	protected void AddDisplay(int slot, ItemStack item)
	{
		displays.put(slot, item);
	}

	/**
	 * Removes a display (ItemStack) from the Inventory.
	 * 
	 * @param slot The slot of the item.
	 */
	protected void RemoveDisplay(int slot)
	{
		if (displays.containsKey(slot))
		{
			displays.remove(slot);
		}
	}

	/**
	 * Builds the Menu page for the player.
	 * 
	 * @param player The player to target.
	 */
	private void BuildPage(Player player)
	{
		inventory = Bukkit.createInventory(null, slots, name);

		InventoryConstruct(player);

		for (int slot : displays.keySet())
		{
			inventory.setItem(slot, displays.get(slot));
		}
	}

	/**
	 * Shows the Menu page to the player.
	 * 
	 * @param player The player to show the Menu.
	 */
	public void ShowPage(Player player)
	{
		try
		{
			BuildPage(player);
			player.openInventory(inventory);
		}
		catch (Exception e)
		{
			player.sendMessage(Chat.format(getModuleName(), ChatColor.RED + "" + ChatColor.BOLD + "There was a problem showing the menu page."));
			e.printStackTrace();
		}
	}

	@EventHandler
	public void EventOpenInventory(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		ItemStack item = event.getItem();

		// Checks if the item does not exist.

		if (item == null || item.getType() == Material.AIR)
			return;

		// Checks if the item is the one we need.

		if (item.getType() != this.material || !item.hasItemMeta() || !item.getItemMeta().getDisplayName().contains(this.display.getItemMeta().getDisplayName()))
			return;

		event.setCancelled(true);

		player.closeInventory();
		ShowPage(player);
	}

	@EventHandler
	public void EventUseDisplay(InventoryClickEvent event)
	{
		Player player = (Player) event.getWhoClicked();
		ItemStack item = event.getCurrentItem();
		Inventory inventory = event.getInventory();
		InventoryAction action = event.getAction();

		// Checks if the inventory has the correct name.

		if (inventory.getName() != this.name)
			return;

		// Checks if the item is the one we need.

		if (item == null || item.getType() == Material.AIR)
			return;

		// Checks if the name of the inventory is correct.

		if (player.getOpenInventory().getTitle() != name)
			return;

		event.setCancelled(true);

		if (forceClose)
			player.closeInventory();

		InventoryClick(action, item, player);

		if (dependOnEvents)
			Destroy();
	}

	@EventHandler
	public void EventCloseInventory(InventoryCloseEvent event)
	{
		Player player = (Player) event.getPlayer();
		Inventory inventory = event.getInventory();

		if (inventory.getName() != this.name)
			return;
		if (!inventory.equals(this.inventory))
			return;
		if (!player.getOpenInventory().getTopInventory().equals(this.inventory))
			return;
		if (inventory.getViewers().size() > 1)
			return;
		if (!inventory.getViewers().get(0).equals(player))
			return;

		if (dependOnEvents)
			Destroy();
	}

	protected void DependOnEvents(boolean dependOnEvents)
	{
		this.dependOnEvents = dependOnEvents;
	}

	protected void SetForceClosed(boolean dependOnEvents)
	{
		this.forceClose = dependOnEvents;
	}

	protected void Destroy()
	{
		Main.debug("Destroying");
		unregisterEvents();
		displays.clear();
		display = null;
		material = null;
		name = null;
		inventory = null;

		try
		{
			finalize();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}

	public ItemStack getDisplay()
	{
		return display;
	}

	public HashMap<Integer, ItemStack> getDisplays()
	{
		return displays;
	}

	public Inventory getInventory()
	{
		return inventory;
	}

	public Material getMaterial()
	{
		return material;
	}

	public String getName()
	{
		return name;
	}

	public int getSlots()
	{
		return slots;
	}

	protected void PlayErrorSound(Player player)
	{
		player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1.0F, 1.0F);
	}

	protected void PlayFailureSound(Player player)
	{
		player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 0.5F);
	}

	protected void PlaySuccessSound(Player player)
	{
		player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 1.5F);
	}
}
