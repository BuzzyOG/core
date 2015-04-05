package src.ares.core.punish;

import src.ares.core.client.Client;
import src.ares.core.client.OfflineClient;

/**
 * TimedPunishment class is an extension of the Punishment that offers a specific duration for the punishment.
 */
public abstract class TimedPunishment extends Punishment
{
	private String reason;
	private double duration;

	/**
	 * Default Constructor
	 * 
	 * @param name The name of the punishment.
	 * @param reason The reason for the punishment.
	 * @param offender The offender.
	 * @param punisher The punisher.
	 * @param duration The duration of the punishment in minutes.
	 */
	public TimedPunishment(String name, String reason, OfflineClient offender, Client punisher, double duration)
	{
		super(name, reason, offender, punisher);
		this.reason = reason;
		this.duration = duration;
	}

	public String getFormattedDuration()
	{
		if (duration > 0)
		{
			if (duration == 1)
			{
				return duration + " minute";
			}
			else
			{
				return duration + " minutes";
			}
		}
		else
		{
			return "permanent";
		}
	}

	public double getDuration()
	{
		return duration;
	}

	@Override
	public String getReason()
	{
		return reason;
	}
}
