package net.turboangle.fishing;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.turboangle.util.RightClickUtil;

import java.util.Random;

public class FishingManager {

    private int pullTimer = -1;
    private int recastTimer = -1;

    private long biteDetectedTime = -1L;

    private final Random random = new Random();

    private int getGaussianDelay() {
        double meanTicks = 4.0;
        double stdDevTicks = 1.0;
        double delay = meanTicks + (random.nextGaussian() * stdDevTicks);
        return Math.max(1, (int) Math.round(delay));
    }

    public void tick(Minecraft client) {
        if (client.player == null) return;

        boolean hasHookOut = client.player.fishing != null;

        if (hasHookOut) {

            if (FishDetector.hasFishBite(client)) {

                if (pullTimer == -1) {
                    pullTimer = getGaussianDelay();

                    biteDetectedTime = System.currentTimeMillis();
                }
            }

            if (pullTimer > 0) {
                pullTimer--;

                if (pullTimer == 0) {


                    if (biteDetectedTime != -1L) {

                        long delayMs = System.currentTimeMillis() - biteDetectedTime;
                        int delayTicks = (int) Math.round(delayMs / 50.0);

                        client.player.sendSystemMessage(
                                Component.literal(
                                        "§7[TF] Delay : " + delayTicks + " tick"
                                )
                        );

                        biteDetectedTime = -1L;
                    }

                    RightClickUtil.rightClick();
                    recastTimer = getGaussianDelay();
                }
            }

        } else {

            if (recastTimer > 0) {
                recastTimer--;

                if (recastTimer == 0) {
                    RightClickUtil.rightClick();
                }
            }

            pullTimer = -1;
            biteDetectedTime = -1L;
        }
    }
}