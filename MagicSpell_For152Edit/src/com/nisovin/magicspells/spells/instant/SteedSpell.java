package com.nisovin.magicspells.spells.instant;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.nisovin.magicspells.spelleffects.EffectPosition;
import com.nisovin.magicspells.spells.InstantSpell;
import com.nisovin.magicspells.util.MagicConfig;
import com.nisovin.magicspells.util.Util;

public class SteedSpell extends InstantSpell {

	Random random = new Random();
	
	Map<String, Integer> mounted = new HashMap<String, Integer>();
	
	EntityType type;
	
	ItemStack armor;
	
	String strAlreadyMounted;
	
	public SteedSpell(MagicConfig config, String spellName) {
		super(config, spellName);
		
		type = Util.getEntityType(getConfigString("type", "horse"));
		
		strAlreadyMounted = getConfigString("str-already-mounted", "You are already mounted!");
	}

	@Override
	public PostCastAction castSpell(Player player, SpellCastState state, float power, String[] args) {
		if (state == SpellCastState.NORMAL) {
			if (player.getVehicle() != null) {
				sendMessage(player, strAlreadyMounted);
				return PostCastAction.ALREADY_HANDLED;
			}
			Entity entity = player.getWorld().spawnEntity(player.getLocation(), type);
			entity.setPassenger(player);
			playSpellEffects(EffectPosition.CASTER, player);
			mounted.put(player.getName(), entity.getEntityId());
		}
		return PostCastAction.HANDLE_NORMALLY;
	}
	
	@EventHandler
	void onDamage(EntityDamageEvent event) {
		if (mounted.containsValue(event.getEntity().getEntityId())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	void onDeath(PlayerDeathEvent event) {
		if (mounted.containsKey(event.getEntity().getName()) && event.getEntity().getVehicle() != null) {
			mounted.remove(event.getEntity().getName());
			Entity vehicle = event.getEntity().getVehicle();
			vehicle.eject();
			vehicle.remove();
		}
	}
	
	@EventHandler
	void onQuit(PlayerQuitEvent event) {
		if (mounted.containsKey(event.getPlayer().getName()) && event.getPlayer().getVehicle() != null) {
			mounted.remove(event.getPlayer().getName());
			Entity vehicle = event.getPlayer().getVehicle();
			vehicle.eject();
			vehicle.remove();
		}
	}
	
	@Override
	public void turnOff() {
		for (String name : mounted.keySet()) {
			@SuppressWarnings("deprecation")
			Player player = Bukkit.getPlayerExact(name);
			if (player != null && player.getVehicle() != null) {
				player.getVehicle().eject();
			}
		}
		mounted.clear();
	}

}
