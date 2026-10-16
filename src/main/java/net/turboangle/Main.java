package net.turboangle;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.turboangle.fishing.FishingManager;
import net.minecraft.client.Minecraft;

public class Main implements ModInitializer, ClientModInitializer {

	public static final FishingManager FISHING_MANAGER = new FishingManager();

	@Override
	public void onInitialize() {}

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register((Minecraft client) -> {
			if (client.player == null || client.level == null) return;
			FISHING_MANAGER.tick(client);
		});
	}
}