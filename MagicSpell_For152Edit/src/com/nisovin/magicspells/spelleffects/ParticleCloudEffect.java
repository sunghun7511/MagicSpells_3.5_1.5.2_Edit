package com.nisovin.magicspells.spelleffects;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

class ParticleCloudEffect extends SpellEffect {
	float radius = 5f;
	float radiusPerTick = 0f;
	int duration = 60;
	int color = 0xFF0000;
	float yOffset = 0F;

	@Override
	public void loadFromString(String string) {
	}

	@Override
	public void loadFromConfig(ConfigurationSection config) {
		radius = (float)config.getDouble("radius", radius);
		radiusPerTick = (float)config.getDouble("radius-per-tick", radiusPerTick);
		duration = config.getInt("duration", duration);
		color = config.getInt("color", color);
		yOffset = (float)config.getDouble("y-offset", yOffset);
	}

	@Override
	public void playEffectLocation(Location location) {
	}
	
}
