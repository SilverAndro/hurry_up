package dev.silverandro.hurry_up;

import org.quiltmc.config.api.WrappedConfig;
import org.quiltmc.config.api.annotations.Comment;

public class HurryUpConfig extends WrappedConfig {
    @Comment("How many ticks should be granted when starting")
    public final int startingTicks = (int) (20 * 60 * 1.5);

    @Comment("The max amount of remaining time you can have")
    public final int maxTicks = 20 * 60 * 17;

	@Comment("The amount of time granted on getting an advancement")
	public final int advancementTicks = (int) (20 * 60 * 2.5);

    @Comment("How much extra time to get on death (limited to 2.1x this duration)")
    public final int deathBuffer = 20 * 60 * 2;

    @Comment("Causes players to explode on running out of time")
    public final boolean explode = true;
}
