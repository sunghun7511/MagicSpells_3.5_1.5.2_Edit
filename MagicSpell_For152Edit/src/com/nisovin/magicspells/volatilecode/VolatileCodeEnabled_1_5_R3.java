package com.nisovin.magicspells.volatilecode;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import net.minecraft.server.v1_5_R3.DataWatcher;
import net.minecraft.server.v1_5_R3.EntityCreature;
import net.minecraft.server.v1_5_R3.EntityEnderDragon;
import net.minecraft.server.v1_5_R3.EntityFallingBlock;
import net.minecraft.server.v1_5_R3.EntityFireworks;
import net.minecraft.server.v1_5_R3.EntityHuman;
import net.minecraft.server.v1_5_R3.EntityLiving;
import net.minecraft.server.v1_5_R3.EntityOcelot;
import net.minecraft.server.v1_5_R3.EntitySmallFireball;
import net.minecraft.server.v1_5_R3.EntityTNTPrimed;
import net.minecraft.server.v1_5_R3.EntityVillager;
import net.minecraft.server.v1_5_R3.EntityWitch;
import net.minecraft.server.v1_5_R3.MobEffect;
import net.minecraft.server.v1_5_R3.NBTBase;
import net.minecraft.server.v1_5_R3.NBTTagCompound;
import net.minecraft.server.v1_5_R3.NBTTagList;
import net.minecraft.server.v1_5_R3.Packet;
import net.minecraft.server.v1_5_R3.Packet103SetSlot;
import net.minecraft.server.v1_5_R3.Packet24MobSpawn;
import net.minecraft.server.v1_5_R3.Packet28EntityVelocity;
import net.minecraft.server.v1_5_R3.Packet29DestroyEntity;
import net.minecraft.server.v1_5_R3.Packet34EntityTeleport;
import net.minecraft.server.v1_5_R3.Packet38EntityStatus;
import net.minecraft.server.v1_5_R3.Packet40EntityMetadata;
import net.minecraft.server.v1_5_R3.Packet43SetExperience;
import net.minecraft.server.v1_5_R3.Packet60Explosion;
import net.minecraft.server.v1_5_R3.Packet62NamedSoundEffect;
import net.minecraft.server.v1_5_R3.Packet63WorldParticles;
import net.minecraft.server.v1_5_R3.PathfinderGoal;
import net.minecraft.server.v1_5_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_5_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_5_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_5_R3.PotionBrewer;
import net.minecraft.server.v1_5_R3.Vec3D;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_5_R3.CraftServer;
import org.bukkit.craftbukkit.v1_5_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftFallingSand;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftTNTPrimed;
import org.bukkit.craftbukkit.v1_5_R3.inventory.CraftItemStack;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.nisovin.magicspells.MagicSpells;
import com.nisovin.magicspells.util.BoundingBox;
import com.nisovin.magicspells.util.DisguiseManager;
import com.nisovin.magicspells.util.MagicConfig;

public class VolatileCodeEnabled_1_5_R3 implements VolatileCodeHandle {
	EntityLiving bossBarEntity;
	Field[] packet63Fields;

	private static NBTTagCompound getTag(ItemStack item) {
		if (item instanceof CraftItemStack) {
			try {
				Field field = CraftItemStack.class.getDeclaredField("handle");
				field.setAccessible(true);
				return ((net.minecraft.server.v1_5_R3.ItemStack) field
						.get(item)).tag;
			} catch (Exception ex) {
			}
		}
		return null;
	}

	private static ItemStack setTag(ItemStack item, NBTTagCompound tag) {
		CraftItemStack craftItem = null;
		if (item instanceof CraftItemStack) {
			craftItem = (CraftItemStack) item;
		} else {
			craftItem = CraftItemStack.asCraftCopy(item);
		}
		net.minecraft.server.v1_5_R3.ItemStack nmsItem = null;
		try {
			Field field = CraftItemStack.class.getDeclaredField("handle");
			field.setAccessible(true);
			nmsItem = (net.minecraft.server.v1_5_R3.ItemStack) field.get(item);
		} catch (Exception ex) {
		}
		if (nmsItem == null) {
			nmsItem = CraftItemStack.asNMSCopy((ItemStack) craftItem);
		}
		nmsItem.tag = tag;
		try {
			Field field = CraftItemStack.class.getDeclaredField("handle");
			field.setAccessible(true);
			field.set(craftItem, nmsItem);
		} catch (Exception ex2) {
		}
		return (ItemStack) craftItem;
	}

	public VolatileCodeEnabled_1_5_R3() {
		packet63Fields = new Field[9];
		try {
			packet63Fields[0] = Packet63WorldParticles.class
					.getDeclaredField("a");
			packet63Fields[1] = Packet63WorldParticles.class
					.getDeclaredField("b");
			packet63Fields[2] = Packet63WorldParticles.class
					.getDeclaredField("c");
			packet63Fields[3] = Packet63WorldParticles.class
					.getDeclaredField("d");
			packet63Fields[4] = Packet63WorldParticles.class
					.getDeclaredField("e");
			packet63Fields[5] = Packet63WorldParticles.class
					.getDeclaredField("f");
			packet63Fields[6] = Packet63WorldParticles.class
					.getDeclaredField("g");
			packet63Fields[7] = Packet63WorldParticles.class
					.getDeclaredField("h");
			packet63Fields[8] = Packet63WorldParticles.class
					.getDeclaredField("i");
			for (int i = 0; i <= 8; ++i) {
				packet63Fields[i].setAccessible(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		(bossBarEntity = (EntityLiving) new EntityEnderDragon(
				((CraftWorld) Bukkit.getWorlds().get(0)).getHandle()))
				.setCustomNameVisible(false);
		bossBarEntity.getDataWatcher().watch(0, (Object) (byte) 32);
	}

	@Override
	public void addPotionGraphicalEffect(LivingEntity entity, int color,
			int duration) {
		final EntityLiving el = ((CraftLivingEntity) entity).getHandle();
		final DataWatcher dw = el.getDataWatcher();
		dw.watch(7, (Object) color);
		if (duration > 0) {
			MagicSpells.scheduleDelayedTask(new Runnable() {
				@Override
				public void run() {
					int c = 0;
					if (!el.effects.isEmpty()) {
						c = PotionBrewer.a((Collection<?>) el.effects.values());
					}
					dw.watch(7, (Object) c);
				}
			}, duration);
		}
	}

	@Override
	public void entityPathTo(LivingEntity creature, LivingEntity target) {
		EntityCreature entity = ((CraftCreature) creature).getHandle();
		entity.pathEntity = entity.world.findPath(entity,
				((CraftLivingEntity) target).getHandle(), 16.0f, true, false,
				false, false);
	}

	@Override
	public void sendFakeSlotUpdate(Player player, int slot, ItemStack item) {
		net.minecraft.server.v1_5_R3.ItemStack nmsItem;
		if (item != null) {
			nmsItem = CraftItemStack.asNMSCopy(item);
		} else {
			nmsItem = null;
		}
		Packet103SetSlot packet = new Packet103SetSlot(0, (short) slot + 36,
				nmsItem);
		((CraftPlayer) player).getHandle().playerConnection
				.sendPacket((Packet) packet);
	}

	@Override
	public void toggleLeverOrButton(Block block) {
	}

	@Override
	public void pressPressurePlate(Block block) {
		block.setData((byte) (block.getData() ^ 0x1));
	}

	@Override
	public boolean simulateTnt(Location target, LivingEntity source,
			float explosionSize, boolean fire) {
		EntityTNTPrimed e = new EntityTNTPrimed(
				((CraftWorld) target.getWorld()).getHandle(), target.getX(),
				target.getY(), target.getZ(),
				((CraftLivingEntity) source).getHandle());
		CraftTNTPrimed c = new CraftTNTPrimed((CraftServer) Bukkit.getServer(),
				e);
		ExplosionPrimeEvent event = new ExplosionPrimeEvent(
				(org.bukkit.entity.Entity) c, explosionSize, fire);
		Bukkit.getServer().getPluginManager().callEvent((Event) event);
		return event.isCancelled();
	}

	@Override
	public boolean createExplosionByPlayer(Player player, Location location,
			float size, boolean fire, boolean breakBlocks) {
		return !((CraftWorld) location.getWorld()).getHandle().createExplosion(
				((CraftPlayer) player).getHandle(), location.getX(),
				location.getY(), location.getZ(), size, fire, breakBlocks).wasCanceled;
	}

	@Override
	public void playExplosionEffect(Location location, float size) {
		Packet60Explosion packet = new Packet60Explosion(location.getX(),
				location.getY(), location.getZ(), size,
				(List<?>) new ArrayList<Object>(), (Vec3D) null);
		for (Player player : location.getWorld().getPlayers()) {
			if (player.getLocation().distanceSquared(location) < 2500.0) {
				((CraftPlayer) player).getHandle().playerConnection
						.sendPacket((Packet) packet);
			}
		}
	}

	@Override
	public void setExperienceBar(Player player, int level, float percent) {
		Packet43SetExperience packet = new Packet43SetExperience(percent,
				player.getTotalExperience(), level);
		((CraftPlayer) player).getHandle().playerConnection
				.sendPacket((Packet) packet);
	}

	@Override
	public Fireball shootSmallFireball(Player player) {
		net.minecraft.server.v1_5_R3.World w = ((CraftWorld) player.getWorld())
				.getHandle();
		Location playerLoc = player.getLocation();
		Vector loc = player.getEyeLocation().toVector()
				.add(player.getLocation().getDirection().multiply(10));
		double d0 = loc.getX() - playerLoc.getX();
		double d2 = loc.getY() - (playerLoc.getY() + 1.5);
		double d3 = loc.getZ() - playerLoc.getZ();
		EntitySmallFireball entitysmallfireball = new EntitySmallFireball(w,
				(EntityLiving) ((CraftPlayer) player).getHandle(), d0, d2, d3);
		entitysmallfireball.locY = playerLoc.getY() + 1.5;
		w.addEntity(entitysmallfireball);
		return (Fireball) entitysmallfireball.getBukkitEntity();
	}

	@Override
	public void setTarget(LivingEntity entity, LivingEntity target) {
		if (entity instanceof Creature) {
			((Creature) entity).setTarget(target);
		}
		((EntityLiving) ((CraftLivingEntity) entity).getHandle())
				.setGoalTarget(((CraftLivingEntity) target).getHandle());
	}

	@Override
	public void playSound(Location location, String sound, float volume,
			float pitch) {
		((CraftWorld) location.getWorld()).getHandle().makeSound(
				location.getX(), location.getY(), location.getZ(), sound,
				volume, pitch);
	}

	@Override
	public void playSound(Player player, String sound, float volume, float pitch) {
		Location loc = player.getLocation();
		Packet62NamedSoundEffect packet = new Packet62NamedSoundEffect(sound,
				loc.getX(), loc.getY(), loc.getZ(), volume, pitch);
		((CraftPlayer) player).getHandle().playerConnection
				.sendPacket((Packet) packet);
	}

	@Override
	public ItemStack addFakeEnchantment(ItemStack item) {
		if (!(item instanceof CraftItemStack)) {
			item = (ItemStack) CraftItemStack.asCraftCopy(item);
		}
		NBTTagCompound tag = getTag(item);
		if (tag == null) {
			tag = new NBTTagCompound();
		}
		if (!tag.hasKey("ench")) {
			tag.set("ench", (NBTBase) new NBTTagList());
		}
		return setTag(item, tag);
	}

	@Override
	public void setFallingBlockHurtEntities(FallingBlock block, float damage,
			int max) {
		EntityFallingBlock efb = ((CraftFallingSand) block).getHandle();
		try {
			Field field = EntityFallingBlock.class
					.getDeclaredField("hurtEntities");
			field.setAccessible(true);
			field.setBoolean(efb, true);
			field = EntityFallingBlock.class.getDeclaredField("fallHurtAmount");
			field.setAccessible(true);
			field.setFloat(efb, damage);
			field = EntityFallingBlock.class.getDeclaredField("fallHurtMax");
			field.setAccessible(true);
			field.setInt(efb, max);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void playEntityAnimation(final Location location,
			EntityType entityType, final int animationId, boolean instant) {
		EntityLiving entity;
		if (entityType == EntityType.VILLAGER) {
			entity = (EntityLiving) new EntityVillager(
					((CraftWorld) location.getWorld()).getHandle());
		} else if (entityType == EntityType.WITCH) {
			entity = (EntityLiving) new EntityWitch(
					((CraftWorld) location.getWorld()).getHandle());
		} else if (entityType == EntityType.OCELOT) {
			entity = (EntityLiving) new EntityOcelot(
					((CraftWorld) location.getWorld()).getHandle());
		} else {
			entity = null;
		}
		if (entity == null) {
			return;
		}
		entity.setPosition(location.getX(), instant ? location.getY() : -5.0,
				location.getZ());
		((CraftWorld) location.getWorld()).getHandle().addEntity(entity);
		entity.addEffect(new MobEffect(14, 40));
		if (instant) {
			((CraftWorld) location.getWorld()).getHandle()
					.broadcastEntityEffect(entity, (byte) animationId);
			entity.getBukkitEntity().remove();
		} else {
			final EntityLiving el = entity;
			entity.setPosition(location.getX(), location.getY(),
					location.getZ());
			MagicSpells.scheduleDelayedTask(new Runnable() {
				@Override
				public void run() {
					((CraftWorld) location.getWorld()).getHandle()
							.broadcastEntityEffect(el, (byte) animationId);
					el.getBukkitEntity().remove();
				}
			}, 8);
		}
	}

	@Override
	public void createFireworksExplosion(Location location, boolean flicker,
			boolean trail, int type, int[] colors, int[] fadeColors,
			int flightDuration) {
		net.minecraft.server.v1_5_R3.ItemStack item = new net.minecraft.server.v1_5_R3.ItemStack(
				401, 1, 0);
		NBTTagCompound tag = item.tag;
		if (tag == null) {
			tag = new NBTTagCompound();
		}
		NBTTagCompound explTag = new NBTTagCompound();
		explTag.setByte("Flicker", (byte) (byte) (flicker ? 1 : 0));
		explTag.setByte("Trail", (byte) (byte) (trail ? 1 : 0));
		explTag.setByte("Type", (byte) type);
		explTag.setIntArray("Colors", colors);
		explTag.setIntArray("FadeColors", fadeColors);
		NBTTagCompound fwTag = new NBTTagCompound();
		fwTag.setByte("Flight", (byte) flightDuration);
		NBTTagList explList = new NBTTagList();
		explList.add((NBTBase) explTag);
		fwTag.set("Explosions", (NBTBase) explList);
		tag.set("Fireworks", (NBTBase) fwTag);
		item.tag = tag;
		EntityFireworks fireworks = new EntityFireworks(
				((CraftWorld) location.getWorld()).getHandle(),
				location.getX(), location.getY(), location.getZ(), item);
		((CraftWorld) location.getWorld()).getHandle().addEntity(fireworks);
		if (flightDuration == 0) {
			((CraftWorld) location.getWorld()).getHandle()
					.broadcastEntityEffect(fireworks, (byte) 17);
			fireworks.die();
		}
	}

	@Override
	public void playParticleEffect(Location location, String name,
			float spreadHoriz, float spreadVert, float speed, int count,
			int radius, float yOffset) {
		Packet63WorldParticles packet = new Packet63WorldParticles();
		try {
			packet63Fields[0].set(packet, name);
			packet63Fields[1].setFloat(packet, (float) location.getX());
			packet63Fields[2].setFloat(packet, (float) location.getY()
					+ yOffset);
			packet63Fields[3].setFloat(packet, (float) location.getZ());
			packet63Fields[4].setFloat(packet, spreadHoriz);
			packet63Fields[5].setFloat(packet, spreadVert);
			packet63Fields[6].setFloat(packet, spreadHoriz);
			packet63Fields[7].setFloat(packet, speed);
			packet63Fields[8].setInt(packet, count);
			int rSq = radius * radius;
			for (Player player : location.getWorld().getPlayers()) {
				if (player.getLocation().distanceSquared(location) <= rSq) {
					((CraftPlayer) player).getHandle().playerConnection
							.sendPacket((Packet) packet);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void playDragonDeathEffect(Location location) {
		EntityEnderDragon dragon = new EntityEnderDragon(
				((CraftWorld) location.getWorld()).getHandle());
		dragon.setPositionRotation(location.getX(), location.getY(),
				location.getZ(), location.getYaw(), 0.0f);
		Packet24MobSpawn packet24 = new Packet24MobSpawn((EntityLiving) dragon);
		Packet38EntityStatus packet25 = new Packet38EntityStatus(dragon.id,
				(byte) 3);
		final Packet29DestroyEntity packet26 = new Packet29DestroyEntity(
				new int[] { dragon.getBukkitEntity().getEntityId() });
		BoundingBox box = new BoundingBox(location, 64.0);
		final List<Player> players = new ArrayList<Player>();
		for (Player player : location.getWorld().getPlayers()) {
			if (box.contains((org.bukkit.entity.Entity) player)) {
				players.add(player);
				((CraftPlayer) player).getHandle().playerConnection
						.sendPacket((Packet) packet24);
				((CraftPlayer) player).getHandle().playerConnection
						.sendPacket((Packet) packet25);
			}
		}
		MagicSpells.scheduleDelayedTask(new Runnable() {
			@Override
			public void run() {
				for (Player player : players) {
					if (player.isValid()) {
						((CraftPlayer) player).getHandle().playerConnection
								.sendPacket((Packet) packet26);
					}
				}
			}
		}, 250);
	}

	@Override
	public void setKiller(LivingEntity entity, Player killer) {
		((CraftLivingEntity) entity).getHandle().killer = (EntityHuman) ((CraftPlayer) killer)
				.getHandle();
	}

	@Override
	public DisguiseManager getDisguiseManager(MagicConfig config) {
		if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
			return new DisguiseManager_1_5_R3_ProtocolLib(config);
		}
		return new DisguiseManagerEmpty(config);
	}

	@Override
	public ItemStack addAttributes(ItemStack item, String[] names,
			String[] types, double[] amounts, int[] operations) {
		if (!(item instanceof CraftItemStack)) {
			item = (ItemStack) CraftItemStack.asCraftCopy(item);
		}
		NBTTagCompound tag = getTag(item);
		NBTTagList list = new NBTTagList();
		for (int i = 0; i < names.length; ++i) {
			if (names[i] != null) {
				NBTTagCompound attr = new NBTTagCompound();
				attr.setString("Name", names[i]);
				attr.setString("AttributeName", types[i]);
				attr.setDouble("Amount", amounts[i]);
				attr.setInt("Operation", operations[i]);
				UUID uuid = UUID.randomUUID();
				attr.setLong("UUIDLeast", uuid.getLeastSignificantBits());
				attr.setLong("UUIDMost", uuid.getMostSignificantBits());
				list.add((NBTBase) attr);
			}
		}
		tag.set("AttributeModifiers", (NBTBase) list);
		setTag(item, tag);
		return item;
	}

	@Override
	public void addEntityAttribute(LivingEntity entity, String attribute,
			double amount, int operation) {
		((CraftLivingEntity) entity).getHandle();

	}

	@Override
	public void removeAI(LivingEntity entity) {
		try {
			EntityLiving ev = (EntityLiving) ((CraftLivingEntity) entity)
					.getHandle();
			Field goalsField = EntityLiving.class
					.getDeclaredField("goalSelector");
			goalsField.setAccessible(true);
			PathfinderGoalSelector goals = (PathfinderGoalSelector) goalsField
					.get(ev);
			Field listField = PathfinderGoalSelector.class
					.getDeclaredField("b");
			listField.setAccessible(true);
			List<?> list = (List<?>) listField.get(goals);
			list.clear();
			listField = PathfinderGoalSelector.class.getDeclaredField("c");
			listField.setAccessible(true);
			list = (List<?>) listField.get(goals);
			list.clear();
			goals.a(0, (PathfinderGoal) new PathfinderGoalFloat(ev));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addAILookAtPlayer(LivingEntity entity, int range) {
		try {
			EntityLiving ev = (EntityLiving) ((CraftLivingEntity) entity)
					.getHandle();
			Field goalsField = EntityLiving.class
					.getDeclaredField("goalSelector");
			goalsField.setAccessible(true);
			PathfinderGoalSelector goals = (PathfinderGoalSelector) goalsField
					.get(ev);
			goals.a(1, (PathfinderGoal) new PathfinderGoalLookAtPlayer(ev,
					(Class<?>) EntityHuman.class, (float) range, 1.0f));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setBossBar(Player player, String title, double percent) {
		if (percent <= 0.0) {
			percent = 0.001;
		}
		bossBarEntity.setCustomName(ChatColor.translateAlternateColorCodes('&',
				title));
		bossBarEntity.getDataWatcher().watch(6,
				(Object) (float) (percent * 200.0));
		Location l = player.getLocation();
		bossBarEntity.setLocation(l.getX(), l.getY() - 300.0, l.getZ(), 0.0f,
				0.0f);
		Packet29DestroyEntity packetDestroy = new Packet29DestroyEntity(
				new int[] { bossBarEntity.id });
		Packet24MobSpawn packetSpawn = new Packet24MobSpawn(
				(EntityLiving) bossBarEntity);
		Packet34EntityTeleport packetTeleport = new Packet34EntityTeleport(
				bossBarEntity);
		Packet28EntityVelocity packetVelocity = new Packet28EntityVelocity(
				bossBarEntity.id, 1.0, 0.0, 1.0);
		((CraftPlayer) player).getHandle().playerConnection
				.sendPacket((Packet) packetDestroy);
		((CraftPlayer) player).getHandle().playerConnection
				.sendPacket((Packet) packetSpawn);
		((CraftPlayer) player).getHandle().playerConnection
				.sendPacket((Packet) packetTeleport);
		((CraftPlayer) player).getHandle().playerConnection
				.sendPacket((Packet) packetVelocity);
	}

	@Override
	public void updateBossBar(Player player, String title, double percent) {
		if (percent <= 0.0) {
			percent = 0.001;
		}
		bossBarEntity.setCustomName(ChatColor.translateAlternateColorCodes('&',
				title));
		bossBarEntity.getDataWatcher().watch(6,
				(Object) (float) (percent * 200.0));
		Location l = player.getLocation();
		bossBarEntity.setLocation(l.getX(), l.getY() - 300.0, l.getZ(), 0.0f,
				0.0f);
		Packet40EntityMetadata packetData = new Packet40EntityMetadata(
				bossBarEntity.id, bossBarEntity.getDataWatcher(), true);
		Packet34EntityTeleport packetTeleport = new Packet34EntityTeleport(
				bossBarEntity);
		Packet28EntityVelocity packetVelocity = new Packet28EntityVelocity(
				bossBarEntity.id, 1.0, 0.0, 1.0);
		((CraftPlayer) player).getHandle().playerConnection
				.sendPacket((Packet) packetData);
		((CraftPlayer) player).getHandle().playerConnection
				.sendPacket((Packet) packetTeleport);
		((CraftPlayer) player).getHandle().playerConnection
				.sendPacket((Packet) packetVelocity);
	}

	@Override
	public void removeBossBar(Player player) {
		Packet29DestroyEntity packetDestroy = new Packet29DestroyEntity(
				new int[] { bossBarEntity.id });
		((CraftPlayer) player).getHandle().playerConnection
				.sendPacket((Packet) packetDestroy);
	}

	@Override
	public void saveSkinData(Player player, String name) {

	}

	@Override
	public ItemStack setUnbreakable(ItemStack item) {
		if (!(item instanceof CraftItemStack)) {
			item = (ItemStack) CraftItemStack.asCraftCopy(item);
		}
		NBTTagCompound tag = getTag(item);
		if (tag == null) {
			tag = new NBTTagCompound();
		}
		tag.setByte("Unbreakable", (byte) 1);
		return setTag(item, tag);
	}

	@Override
	public void setArrowsStuck(LivingEntity entity, int count) {

	}
	
	@Override
	public void playParticleEffect(Location location, String name,
			float spreadX, float spreadY, float spreadZ, float speed,
			int count, int radius, float yOffset) {
		Packet63WorldParticles packet = new Packet63WorldParticles();
		try {
			packet63Fields[0].set(packet, name);
			packet63Fields[1].setFloat(packet, (float) location.getX());
			packet63Fields[2].setFloat(packet, (float) location.getY()
					+ yOffset);
			packet63Fields[3].setFloat(packet, (float) location.getZ());
			packet63Fields[4].setFloat(packet, spreadX);
			packet63Fields[5].setFloat(packet, spreadY);
			packet63Fields[6].setFloat(packet, spreadZ);
			packet63Fields[7].setFloat(packet, speed);
			packet63Fields[8].setInt(packet, count);
			int rSq = radius * radius;
			for (Player player : location.getWorld().getPlayers()) {
				if (player.getLocation().distanceSquared(location) <= rSq) {
					((CraftPlayer) player).getHandle().playerConnection
							.sendPacket((Packet) packet);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ItemStack hideTooltipCrap(ItemStack item) {
		if (!(item instanceof CraftItemStack)) {
			item = CraftItemStack.asCraftCopy(item);
		}
		NBTTagCompound tag = getTag(item);
		if (tag == null) {
			tag = new NBTTagCompound();
		}
		tag.setInt("HideFlags", 63);
		setTag(item, tag);
		return item;
	}

	@Override
	public void resetEntityAttributes(LivingEntity entity) {
		try {
			EntityLiving e = ((CraftLivingEntity)entity).getHandle();
			Field field = EntityLiving.class.getDeclaredField("c");
			field.setAccessible(true);
			field.set(e, null);
			Method method = null;
			Class<?> clazz = e.getClass();
			while (clazz != null) {
				try {
					method = clazz.getDeclaredMethod("aW");
					break;
				} catch (NoSuchMethodException e1) {
				    clazz = clazz.getSuperclass();
				}
			}
			if (method != null) {
				method.setAccessible(true);
				method.invoke(e);
			} else {
				throw new Exception("No method aW found on " + e.getClass().getName());
			}
		} catch (Exception e) {
			MagicSpells.handleException(e);
		}
	}

	@Override
	public void setNoAIFlag(LivingEntity entity) {
		((CraftLivingEntity)entity).getHandle().getDataWatcher().watch(15, Byte.valueOf((byte)1));
		((CraftLivingEntity)entity).getHandle().getDataWatcher().watch(4, Byte.valueOf((byte)1));
	}

	@Override
	public void sendTitleToPlayer(Player player, String title, String subtitle,
			int fadeIn, int stay, int fadeOut) {
	}

	@Override
	public void sendActionBarMessage(Player player, String message) {
	}

	@Override
	public void setTabMenuHeaderFooter(Player player, String header,
			String footer) {
	}

	@Override
	public void setClientVelocity(Player player, Vector velocity) {
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(new Packet28EntityVelocity(player.getEntityId(), velocity.getX(), velocity.getY(), velocity.getZ()));
	}

	@Override
	public double getAbsorptionHearts(LivingEntity entity) {
		return 0;
	}

	@Override
	public void setOffhand(Player player, ItemStack item) {
		
	}

	@Override
	public ItemStack getOffhand(Player player) {
		return null;
	}

	@Override
	public void showItemCooldown(Player player, ItemStack item, int duration) {
		
	}

	@Override
	public void playSound(Player player, Sound sound, float volume, float pitch) {
		Location loc = player.getLocation();
		Packet62NamedSoundEffect packet = new Packet62NamedSoundEffect(sound.name(),
				loc.getX(), loc.getY(), loc.getZ(), volume, pitch);
		((CraftPlayer) player).getHandle().playerConnection
				.sendPacket((Packet) packet);
		
	}
}
