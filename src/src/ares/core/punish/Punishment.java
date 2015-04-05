package src.ares.core.punish;

import src.ares.core.chat.Notification;
import src.ares.core.client.Client;
import src.ares.core.client.OfflineClient;

public abstract class Punishment
{
	private Notification notification = new Notification();
	private OfflineClient offender;
	private Client punisher;
	private String name;
	private String reason;

	/**
	 * Default Constructor
	 * 
	 * @param name The name of the punishment.
	 * @param reason The reason for the punishment.
	 * @param offender The offender that can be an offline player.
	 * @param punisher The punisher or <b>Swinger</b> of the mute/ban hammer.
	 */
	public Punishment(String name, String reason, OfflineClient offender, Client punisher)
	{
		this.name = name;
		this.offender = offender;
		this.punisher = punisher;
		this.reason = reason;
	}

	/**
	 * This method should be called when the a new punishment needs to be applied.
	 */
	public abstract void apply();

	public Notification getNotifications()
	{
		return notification;
	}

	public String getName()
	{
		return name;
	}

	public OfflineClient getOffender()
	{
		return offender;
	}

	public Client getPunisher()
	{
		return punisher;
	}

	public String getReason()
	{
		return reason;
	}
}
