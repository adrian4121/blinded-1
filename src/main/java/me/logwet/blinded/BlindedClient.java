package me.logwet.blinded;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.options.Option;
import net.minecraft.network.packet.c2s.play.RecipeBookDataC2SPacket;
import org.apache.logging.log4j.Level;

import java.util.Objects;

public class BlindedClient implements ClientModInitializer {
	private static MinecraftClient MC;
	private static double oldRenderDistance;
	private static double oldFOV;

	public static MinecraftClient getMC() {
        return MC;
    }

	public static void setMC(MinecraftClient mc) {
        MC = mc;
    }

	public static ClientPlayerEntity getClientPlayerEntity() {
        return getMC().player;
    }

	public static void saveOldOptions() {
		oldRenderDistance = Option.RENDER_DISTANCE.get(getMC().options);
		oldFOV = Option.FOV.get(getMC().options);

		Blinded.log(Level.INFO, "Saved Render Distance " + oldRenderDistance + " and FOV " + oldFOV);
	}

	public static void resetOptions() {
		Option.RENDER_DISTANCE.set(getMC().options, oldRenderDistance);
		Option.FOV.set(getMC().options, oldFOV);

		Blinded.log(Level.INFO, "Reset to Render Distance " + oldRenderDistance + " and FOV " + oldFOV);
	}

	private static void openF3() {
		try {
			if (Blinded.config.isF3Enabled()) {
				getMC().options.debugEnabled = true;
				Blinded.log(Level.INFO, "Opened F3 menu");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void openRecipeBook() {
		try {
			if (Blinded.config.isRecipeBookEnabled()) {
				getClientPlayerEntity().getRecipeBook().setGuiOpen(true);
				Objects.requireNonNull(getMC().getNetworkHandler()).sendPacket(
						new RecipeBookDataC2SPacket(true, true, false, false, false, false)
				);
				Blinded.log(Level.INFO, "Opened recipe book");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void onClientJoin() {
		openF3();
		openRecipeBook();
		Blinded.log(Level.INFO, "Finished client side actions");
	}

	@Override
	public void onInitializeClient() {
		Blinded.log(Level.INFO, "Using Blinded v" + Blinded.VERSION + " by logwet!");

		setMC(MinecraftClient.getInstance());

		Blinded.commonConfigHandler();
	}
}
