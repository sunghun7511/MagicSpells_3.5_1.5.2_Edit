package com.nisovin.magicspells.volatilecode;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_5_R3.ChunkCoordinates;
import net.minecraft.server.v1_5_R3.DataWatcher;
import net.minecraft.server.v1_5_R3.EntityAgeable;
import net.minecraft.server.v1_5_R3.EntityBat;
import net.minecraft.server.v1_5_R3.EntityBlaze;
import net.minecraft.server.v1_5_R3.EntityBoat;
import net.minecraft.server.v1_5_R3.EntityCaveSpider;
import net.minecraft.server.v1_5_R3.EntityChicken;
import net.minecraft.server.v1_5_R3.EntityCow;
import net.minecraft.server.v1_5_R3.EntityCreeper;
import net.minecraft.server.v1_5_R3.EntityEnderDragon;
import net.minecraft.server.v1_5_R3.EntityEnderman;
import net.minecraft.server.v1_5_R3.EntityFallingBlock;
import net.minecraft.server.v1_5_R3.EntityGhast;
import net.minecraft.server.v1_5_R3.EntityGiantZombie;
import net.minecraft.server.v1_5_R3.EntityHuman;
import net.minecraft.server.v1_5_R3.EntityIronGolem;
import net.minecraft.server.v1_5_R3.EntityItem;
import net.minecraft.server.v1_5_R3.EntityLiving;
import net.minecraft.server.v1_5_R3.EntityMagmaCube;
import net.minecraft.server.v1_5_R3.EntityMushroomCow;
import net.minecraft.server.v1_5_R3.EntityOcelot;
import net.minecraft.server.v1_5_R3.EntityPig;
import net.minecraft.server.v1_5_R3.EntityPigZombie;
import net.minecraft.server.v1_5_R3.EntityPlayer;
import net.minecraft.server.v1_5_R3.EntitySheep;
import net.minecraft.server.v1_5_R3.EntitySilverfish;
import net.minecraft.server.v1_5_R3.EntitySkeleton;
import net.minecraft.server.v1_5_R3.EntitySlime;
import net.minecraft.server.v1_5_R3.EntitySnowman;
import net.minecraft.server.v1_5_R3.EntitySpider;
import net.minecraft.server.v1_5_R3.EntitySquid;
import net.minecraft.server.v1_5_R3.EntityTracker;
import net.minecraft.server.v1_5_R3.EntityVillager;
import net.minecraft.server.v1_5_R3.EntityWitch;
import net.minecraft.server.v1_5_R3.EntityWither;
import net.minecraft.server.v1_5_R3.EntityWolf;
import net.minecraft.server.v1_5_R3.EntityZombie;
import net.minecraft.server.v1_5_R3.ItemStack;
import net.minecraft.server.v1_5_R3.Packet;
import net.minecraft.server.v1_5_R3.Packet20NamedEntitySpawn;
import net.minecraft.server.v1_5_R3.Packet23VehicleSpawn;
import net.minecraft.server.v1_5_R3.Packet24MobSpawn;
import net.minecraft.server.v1_5_R3.Packet28EntityVelocity;
import net.minecraft.server.v1_5_R3.Packet29DestroyEntity;
import net.minecraft.server.v1_5_R3.Packet31RelEntityMove;
import net.minecraft.server.v1_5_R3.Packet32EntityLook;
import net.minecraft.server.v1_5_R3.Packet33RelEntityMoveLook;
import net.minecraft.server.v1_5_R3.Packet34EntityTeleport;
import net.minecraft.server.v1_5_R3.Packet35EntityHeadRotation;
import net.minecraft.server.v1_5_R3.Packet39AttachEntity;
import net.minecraft.server.v1_5_R3.Packet40EntityMetadata;
import net.minecraft.server.v1_5_R3.Packet5EntityEquipment;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_5_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_5_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.nisovin.magicspells.MagicSpells;
import com.nisovin.magicspells.spells.targeted.DisguiseSpell;
import com.nisovin.magicspells.util.DisguiseManager;
import com.nisovin.magicspells.util.MagicConfig;

public class DisguiseManager_1_5_R3_ProtocolLib extends DisguiseManager {

	protected ProtocolManager protocolManager;
	protected PacketAdapter packetListener;

	public DisguiseManager_1_5_R3_ProtocolLib(MagicConfig config) {
		super(config);
	}

	@Override
	protected void cleanup() {
		protocolManager
				.removePacketListener((com.comphenix.protocol.events.PacketListener) packetListener);
	}

	private Entity getEntity(Player player, DisguiseSpell.Disguise disguise) {
		EntityType entityType = disguise.getEntityType();
		boolean flag = disguise.getFlag();
		int var = disguise.getVar1();
		Location location = player.getLocation();
		Entity entity = null;
		float yOffset = 0.0f;
		net.minecraft.server.v1_5_R3.World world = ((CraftWorld) location
				.getWorld()).getHandle();
		String name = disguise.getNameplateText();
		if (name == null || name.isEmpty()) {
			name = player.getName();
		}
		if (entityType == EntityType.PLAYER) {
			entity = new EntityHuman(world) {
				@Override
				public void sendMessage(String s) {

				}

				public boolean a(int arg0, String arg1) {
					return false;
				}

				@Override
				public ChunkCoordinates b() {
					return null;
				}
			}.getBukkitEntity();
			yOffset = -1.5f;
		} else if (entityType == EntityType.ZOMBIE) {
			entity = (Entity) new EntityZombie(world);
			if (flag) {
				((EntityZombie) entity).setBaby(true);
			}
			if (var == 1) {
				((EntityZombie) entity).setVillager(true);
			}
		} else if (entityType == EntityType.SKELETON) {
			entity = (Entity) new EntitySkeleton(world);
			if (flag) {
				((EntitySkeleton) entity).setSkeletonType(1);
			}
		} else if (entityType == EntityType.IRON_GOLEM) {
			entity = (Entity) new EntityIronGolem(world);
		} else if (entityType == EntityType.SNOWMAN) {
			entity = (Entity) new EntitySnowman(world);
		} else if (entityType == EntityType.CREEPER) {
			entity = (Entity) new EntityCreeper(world);
			if (flag) {
				((EntityCreeper) entity).setPowered(true);
			}
		} else if (entityType == EntityType.SPIDER) {
			entity = (Entity) new EntitySpider(world);
		} else if (entityType == EntityType.CAVE_SPIDER) {
			entity = (Entity) new EntityCaveSpider(world);
		} else if (entityType == EntityType.WOLF) {
			entity = (Entity) new EntityWolf(world);
			((EntityAgeable) entity).setAge(flag ? -24000 : 0);
			if (var > 0) {
				((EntityWolf) entity).setTamed(true);
				((EntityWolf) entity).setOwnerName(player.getName());
				((EntityWolf) entity).setCollarColor(var);
			}
		} else if (entityType == EntityType.OCELOT) {
			entity = (Entity) new EntityOcelot(world);
			((EntityAgeable) entity).setAge(flag ? -24000 : 0);
			if (var == -1) {
				((EntityOcelot) entity).setCatType(random.nextInt(4));
			} else if (var >= 0 && var < 4) {
				((EntityOcelot) entity).setCatType(var);
			}
		} else if (entityType == EntityType.BLAZE) {
			entity = (Entity) new EntityBlaze(world);
		} else if (entityType == EntityType.GIANT) {
			entity = (Entity) new EntityGiantZombie(world);
		} else if (entityType == EntityType.ENDERMAN) {
			entity = (Entity) new EntityEnderman(world);
		} else if (entityType == EntityType.SILVERFISH) {
			entity = (Entity) new EntitySilverfish(world);
		} else if (entityType == EntityType.WITCH) {
			entity = (Entity) new EntityWitch(world);
		} else if (entityType == EntityType.VILLAGER) {
			entity = (Entity) new EntityVillager(world);
			if (flag) {
				((EntityVillager) entity).setAge(-24000);
			}
			((EntityVillager) entity).setProfession(var);
		} else if (entityType == EntityType.PIG_ZOMBIE) {
			entity = (Entity) new EntityPigZombie(world);
		} else if (entityType == EntityType.SLIME) {
			entity = (Entity) new EntitySlime(world);
			((CraftEntity) entity).getHandle().getDataWatcher()
					.watch(16, (Object) (byte) 2);

		} else if (entityType == EntityType.MAGMA_CUBE) {
			entity = (Entity) new EntityMagmaCube(world);
			((CraftEntity) entity).getHandle().getDataWatcher()
					.watch(16, (Object) (byte) 2);
		} else if (entityType == EntityType.BAT) {
			entity = (Entity) new EntityBat(world);
			((CraftEntity) entity).getHandle().getDataWatcher()
					.watch(16, (Object) (byte) 0);
		} else if (entityType == EntityType.CHICKEN) {
			entity = (Entity) new EntityChicken(world);
			((EntityAgeable) entity).setAge(flag ? -24000 : 0);
		} else if (entityType == EntityType.COW) {
			entity = (Entity) new EntityCow(world);
			((EntityAgeable) entity).setAge(flag ? -24000 : 0);
		} else if (entityType == EntityType.MUSHROOM_COW) {
			entity = (Entity) new EntityMushroomCow(world);
			((EntityAgeable) entity).setAge(flag ? -24000 : 0);
		} else if (entityType == EntityType.PIG) {
			entity = (Entity) new EntityPig(world);
			((EntityAgeable) entity).setAge(flag ? -24000 : 0);
			if (var == 1) {
				((EntityPig) entity).setSaddle(true);
			}
		} else if (entityType == EntityType.SHEEP) {
			entity = (Entity) new EntitySheep(world);
			((EntityAgeable) entity).setAge(flag ? -24000 : 0);
			if (var == -1) {
				((EntitySheep) entity).setColor(random.nextInt(16));
			} else if (var >= 0 && var < 16) {
				((EntitySheep) entity).setColor(var);
			}
		} else if (entityType == EntityType.SQUID) {
			entity = (Entity) new EntitySquid(world);
		} else if (entityType == EntityType.GHAST) {
			entity = (Entity) new EntityGhast(world);
		} else if (entityType == EntityType.WITHER) {
			entity = (Entity) new EntityWither(world);
		}

		else if (entityType == EntityType.ENDER_DRAGON) {
			entity = (Entity) new EntityEnderDragon(world);
		} else if (entityType == EntityType.FALLING_BLOCK) {
			int id = disguise.getVar1();
			int data = disguise.getVar2();
			int a = id > 0 ? id : 0;
			try {
				if (!Material.getMaterial(id).isBlock())
					a = 0;
			} catch (Exception ex) {
				a = 0;
			}
			entity = (Entity) new EntityFallingBlock(world, 0.0, 0.0, 0.0, a,
					(data > 15) ? 0 : data);
		} else if (entityType == EntityType.DROPPED_ITEM) {
			int id = disguise.getVar1();
			int data = disguise.getVar2();
			entity = (Entity) new EntityItem(world);
			((EntityItem) entity).setItemStack(new ItemStack((id > 1 ? id : 0),
					1, data));
		}
		if (entity != null) {
			String nameplateText = disguise.getNameplateText();
			if (entity instanceof EntityLiving && nameplateText != null
					&& !nameplateText.isEmpty()) {
				((EntityLiving) entity).setCustomName(nameplateText);
				((EntityLiving) entity).setCustomNameVisible(disguise
						.alwaysShowNameplate());
			}
			((CraftEntity) entity).getHandle().setPositionRotation(
					location.getX(), location.getY() + yOffset,
					location.getZ(), location.getYaw(), location.getPitch());
			return entity;
		}
		return null;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onArmSwing(PlayerAnimationEvent event) {
		final Player p = event.getPlayer();
		final int entityId = p.getEntityId();
		if (isDisguised(p)) {
			DisguiseSpell.Disguise disguise = getDisguise(p);
			EntityType entityType = disguise.getEntityType();
			EntityPlayer entityPlayer = ((CraftPlayer) p).getHandle();
			if (entityType == EntityType.IRON_GOLEM) {
				((CraftWorld) p.getWorld()).getHandle().broadcastEntityEffect(
						((CraftEntity) p).getHandle(), (byte) 4);
			} else if (entityType == EntityType.WITCH) {
				((CraftWorld) p.getWorld()).getHandle().broadcastEntityEffect(
						((CraftEntity) p).getHandle(), (byte) 15);
			} else if (entityType == EntityType.VILLAGER) {
				((CraftWorld) p.getWorld()).getHandle().broadcastEntityEffect(
						((CraftEntity) p).getHandle(), (byte) 13);
			} else if (entityType == EntityType.BLAZE
					|| entityType == EntityType.SPIDER
					|| entityType == EntityType.GHAST) {
				final DataWatcher dw = entityPlayer.getDataWatcher();
				dw.a(0, (Object) (byte) 0);
				dw.a(1, (Object) (short) 300);
				dw.a(16, (Object) (byte) 1);
				broadcastPacket(p, 40, (Packet) new Packet40EntityMetadata(
						entityId, dw, true));
				Bukkit.getScheduler().scheduleSyncDelayedTask(
						(Plugin) MagicSpells.plugin, (Runnable) new Runnable() {
							@Override
							public void run() {
								dw.watch(16, (Object) (byte) 0);
								DisguiseManager_1_5_R3_ProtocolLib.this
										.broadcastPacket(
												p,
												40,
												(Packet) new Packet40EntityMetadata(
														entityId, dw, true));
							}
						}, 10L);
			} else if (entityType == EntityType.WITCH) {
				final DataWatcher dw = entityPlayer.getDataWatcher();
				dw.a(0, (Object) (byte) 0);
				dw.a(1, (Object) (short) 300);
				dw.a(21, (Object) (byte) 1);
				broadcastPacket(p, 40, (Packet) new Packet40EntityMetadata(
						entityId, dw, true));
				Bukkit.getScheduler().scheduleSyncDelayedTask(
						(Plugin) MagicSpells.plugin, (Runnable) new Runnable() {
							@Override
							public void run() {
								dw.watch(21, (Object) (byte) 0);
								DisguiseManager_1_5_R3_ProtocolLib.this
										.broadcastPacket(
												p,
												40,
												(Packet) new Packet40EntityMetadata(
														entityId, dw, true));
							}
						}, 10L);
			} else if (entityType == EntityType.CREEPER && !disguise.getFlag()) {
				final DataWatcher dw = entityPlayer.getDataWatcher();
				dw.a(0, (Object) (byte) 0);
				dw.a(1, (Object) (short) 300);
				dw.a(17, (Object) (byte) 1);
				broadcastPacket(p, 40, (Packet) new Packet40EntityMetadata(
						entityId, dw, true));
				Bukkit.getScheduler().scheduleSyncDelayedTask(
						(Plugin) MagicSpells.plugin, (Runnable) new Runnable() {
							@Override
							public void run() {
								dw.watch(17, (Object) (byte) 0);
								DisguiseManager_1_5_R3_ProtocolLib.this
										.broadcastPacket(
												p,
												40,
												(Packet) new Packet40EntityMetadata(
														entityId, dw, true));
							}
						}, 10L);
			} else if (entityType == EntityType.WOLF) {
				final DataWatcher dw = entityPlayer.getDataWatcher();
				dw.a(0, (Object) (byte) 0);
				dw.a(1, (Object) (short) 300);
				dw.a(16, (Object) (byte) (p.isSneaking() ? 3 : 2));
				broadcastPacket(p, 40, (Packet) new Packet40EntityMetadata(
						entityId, dw, true));
				Bukkit.getScheduler().scheduleSyncDelayedTask(
						(Plugin) MagicSpells.plugin, (Runnable) new Runnable() {
							@Override
							public void run() {
								dw.watch(
										16,
										(Object) (byte) (p.isSneaking() ? 1 : 0));
								DisguiseManager_1_5_R3_ProtocolLib.this
										.broadcastPacket(
												p,
												40,
												(Packet) new Packet40EntityMetadata(
														entityId, dw, true));
							}
						}, 10L);
			} else if (entityType == EntityType.SLIME
					|| entityType == EntityType.MAGMA_CUBE) {
				final DataWatcher dw = entityPlayer.getDataWatcher();
				dw.a(0, (Object) (byte) 0);
				dw.a(1, (Object) (short) 300);
				dw.a(16, (Object) (byte) (p.isSneaking() ? 2 : 3));
				broadcastPacket(p, 40, (Packet) new Packet40EntityMetadata(
						entityId, dw, true));
				Bukkit.getScheduler().scheduleSyncDelayedTask(
						(Plugin) MagicSpells.plugin, (Runnable) new Runnable() {
							@Override
							public void run() {
								dw.watch(
										16,
										(Object) (byte) (p.isSneaking() ? 1 : 2));
								DisguiseManager_1_5_R3_ProtocolLib.this
										.broadcastPacket(
												p,
												40,
												(Packet) new Packet40EntityMetadata(
														entityId, dw, true));
							}
						}, 10L);
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onSneak(PlayerToggleSneakEvent event) {
		DisguiseSpell.Disguise disguise = getDisguise(event.getPlayer());
		if (disguise == null) {
			return;
		}
		EntityType entityType = disguise.getEntityType();
		EntityPlayer entityPlayer = ((CraftPlayer) event.getPlayer())
				.getHandle();
		Player p = event.getPlayer();
		int entityId = p.getEntityId();
		if (entityType == EntityType.WOLF) {
			if (event.isSneaking()) {
				DataWatcher dw = entityPlayer.getDataWatcher();
				dw.a(0, (Object) (byte) 0);
				dw.a(1, (Object) (short) 300);
				dw.a(16, (Object) (byte) 1);
				broadcastPacket(p, 40, (Packet) new Packet40EntityMetadata(
						entityId, dw, true));
			} else {
				DataWatcher dw = entityPlayer.getDataWatcher();
				dw.a(0, (Object) (byte) 0);
				dw.a(1, (Object) (short) 300);
				dw.a(16, (Object) (byte) 0);
				broadcastPacket(p, 40, (Packet) new Packet40EntityMetadata(
						entityId, dw, true));
			}
		} else if (entityType == EntityType.ENDERMAN) {
			if (event.isSneaking()) {
				DataWatcher dw = entityPlayer.getDataWatcher();
				dw.a(0, (Object) (byte) 0);
				dw.a(1, (Object) (short) 300);
				dw.a(18, (Object) (byte) 1);
				broadcastPacket(p, 40, (Packet) new Packet40EntityMetadata(
						entityId, dw, true));
			} else {
				DataWatcher dw = entityPlayer.getDataWatcher();
				dw.a(0, (Object) (byte) 0);
				dw.a(1, (Object) (short) 300);
				dw.a(18, (Object) (byte) 0);
				broadcastPacket(p, 40, (Packet) new Packet40EntityMetadata(
						entityId, dw, true));
			}
		} else if (entityType == EntityType.SLIME
				|| entityType == EntityType.MAGMA_CUBE) {
			if (event.isSneaking()) {
				DataWatcher dw = entityPlayer.getDataWatcher();
				dw.a(0, (Object) (byte) 0);
				dw.a(1, (Object) (short) 300);
				dw.a(16, (Object) (byte) 1);
				broadcastPacket(p, 40, (Packet) new Packet40EntityMetadata(
						entityId, dw, true));
			} else {
				DataWatcher dw = entityPlayer.getDataWatcher();
				dw.a(0, (Object) (byte) 0);
				dw.a(1, (Object) (short) 300);
				dw.a(16, (Object) (byte) 2);
				broadcastPacket(p, 40, (Packet) new Packet40EntityMetadata(
						entityId, dw, true));
			}
		} else if (entityType == EntityType.SHEEP && event.isSneaking()) {
			p.playEffect(EntityEffect.SHEEP_EAT);
		}
	}

	@Override
	protected void sendDestroyEntityPackets(Player disguised) {
		sendDestroyEntityPackets(disguised, disguised.getEntityId());
	}

	@Override
	protected void sendDestroyEntityPackets(Player disguised, int entityId) {
		Packet29DestroyEntity packet29 = new Packet29DestroyEntity(
				new int[] { entityId });
		EntityTracker tracker = ((CraftWorld) disguised.getWorld()).getHandle().tracker;
		tracker.a(((CraftPlayer) disguised).getHandle(), (Packet) packet29);
	}

	private void broadcastPacket(Player disguised, int packetId, Packet packet) {
		PacketContainer con = new PacketContainer(packetId, (Object) packet);
		for (Player player : protocolManager
				.getEntityTrackers((org.bukkit.entity.Entity) disguised)) {
			if (player.isValid()) {
				try {
					protocolManager.sendServerPacket(player, con, false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void sendDisguisedSpawnPacket(Player viewer, Player disguised,
			DisguiseSpell.Disguise disguise, Entity entity) {
		if (entity == null) {
			entity = getEntity(disguised, disguise);
		}
		if (entity != null) {
			List<Packet> packets = getPacketsToSend(disguised, disguise, entity);
			if (packets != null && packets.size() > 0) {
				EntityPlayer ep = ((CraftPlayer) viewer).getHandle();
				try {
					for (Packet packet : packets) {
						if (packet instanceof Packet40EntityMetadata) {
							protocolManager.sendServerPacket(viewer,
									new PacketContainer(40, (Object) packet),
									false);
						} else if (packet instanceof Packet20NamedEntitySpawn) {
							protocolManager.sendServerPacket(viewer,
									new PacketContainer(20, (Object) packet),
									false);
						} else {
							ep.playerConnection.sendPacket(packet);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	protected void sendDisguisedSpawnPackets(Player disguised,
			DisguiseSpell.Disguise disguise) {
		Entity entity = getEntity(disguised, disguise);
		if (entity != null) {
			List<Packet> packets = getPacketsToSend(disguised, disguise, entity);
			if (packets != null && packets.size() > 0) {
				EntityTracker tracker = ((CraftWorld) disguised.getWorld())
						.getHandle().tracker;
				for (Packet packet : packets) {
					if (packet instanceof Packet40EntityMetadata) {
						broadcastPacket(disguised, 40, packet);
					} else if (packet instanceof Packet40EntityMetadata) {
						broadcastPacket(disguised, 20, packet);
					} else {
						tracker.a(((CraftPlayer) disguised).getHandle(), packet);
					}
				}
			}
		}
	}

	private List<Packet> getPacketsToSend(Player disguised,
			DisguiseSpell.Disguise disguise, Entity entity) {
		List<Packet> packets = new ArrayList<Packet>();
		if (entity instanceof EntityHuman) {
			Packet20NamedEntitySpawn packet20 = new Packet20NamedEntitySpawn(
					(EntityHuman) entity);
			packet20.a = disguised.getEntityId();
			packets.add((Packet) packet20);
			addEquipmentPackets(disguised, packets);
		} else if (entity instanceof EntityLiving) {
			Packet24MobSpawn packet21 = new Packet24MobSpawn(
					(EntityLiving) entity);
			packet21.a = disguised.getEntityId();
			if (dragons.contains(disguised.getEntityId())) {
				int dir = packet21.i + 128;
				if (dir > 127) {
					dir -= 256;
				}
				packet21.i = (byte) dir;
				packet21.j = (byte) 0;
				packet21.k = (byte) 1;
			}
			packets.add((Packet) packet21);
			Packet40EntityMetadata packet22 = new Packet40EntityMetadata(
					disguised.getEntityId(), ((CraftEntity) entity).getHandle()
							.getDataWatcher(), false);
			packets.add((Packet) packet22);
			if (dragons.contains(disguised.getEntityId())) {
				Packet28EntityVelocity packet23 = new Packet28EntityVelocity(
						disguised.getEntityId(), 0.15, 0.0, 0.15);
				packets.add((Packet) packet23);
			}
			if (disguise.getEntityType() == EntityType.ZOMBIE
					|| disguise.getEntityType() == EntityType.SKELETON) {
				addEquipmentPackets(disguised, packets);
			}
		} else if (entity instanceof EntityFallingBlock) {
			Packet23VehicleSpawn packet24 = new Packet23VehicleSpawn(
					((CraftEntity) entity).getHandle(), 70, disguise.getVar1()
							| (byte) disguise.getVar2() << 16);
			packet24.a = disguised.getEntityId();
			packets.add((Packet) packet24);
		} else if (entity instanceof EntityItem) {
			Packet23VehicleSpawn packet24 = new Packet23VehicleSpawn(
					((CraftEntity) entity).getHandle(), 2, 1);
			packet24.a = disguised.getEntityId();
			packets.add((Packet) packet24);
			Packet40EntityMetadata packet22 = new Packet40EntityMetadata(
					disguised.getEntityId(), ((CraftEntity) entity).getHandle()
							.getDataWatcher(), true);
			packets.add((Packet) packet22);
		}
		if (disguise.isRidingBoat()) {
			EntityBoat boat = new EntityBoat(
					((CraftWorld) entity.getWorld()).getHandle());
			int boatEntId;
			if (mounts.containsKey(disguised.getEntityId())) {
				boatEntId = mounts.get(disguised.getEntityId());
				boat.id = boatEntId;
			} else {
				boatEntId = boat.id;
				mounts.put(disguised.getEntityId(), boatEntId);
			}
			boat.setPositionRotation(disguised.getLocation().getX(), disguised
					.getLocation().getY(), disguised.getLocation().getZ(),
					disguised.getLocation().getYaw(), 0.0f);
			Packet23VehicleSpawn packet25 = new Packet23VehicleSpawn(boat, 1);
			packets.add((Packet) packet25);
			Packet39AttachEntity packet26 = new Packet39AttachEntity();
			packet26.a = disguised.getEntityId();
			packet26.b = boatEntId;
			packets.add((Packet) packet26);
		}
		if (disguised.getPassenger() != null) {
			Packet39AttachEntity packet27 = new Packet39AttachEntity();
			packet27.a = disguised.getPassenger().getEntityId();
			packet27.b = disguised.getEntityId();

			packets.add((Packet) packet27);
		}
		if (disguised.getVehicle() != null) {
			Packet39AttachEntity packet27 = new Packet39AttachEntity();
			packet27.a = disguised.getEntityId();
			packet27.b = disguised.getVehicle().getEntityId();
			packets.add((Packet) packet27);
		}
		return packets;
	}

	private void addEquipmentPackets(Player disguised, List<Packet> packets) {
		org.bukkit.inventory.ItemStack inHand = disguised.getItemInHand();
		if (inHand != null && inHand.getType() != Material.AIR) {
			Packet5EntityEquipment packet5 = new Packet5EntityEquipment(
					disguised.getEntityId(), 0,
					CraftItemStack.asNMSCopy(inHand));
			packets.add((Packet) packet5);
		}
		org.bukkit.inventory.ItemStack helmet = disguised.getInventory()
				.getHelmet();
		if (helmet != null && helmet.getType() != Material.AIR) {
			Packet5EntityEquipment packet6 = new Packet5EntityEquipment(
					disguised.getEntityId(), 4,
					CraftItemStack.asNMSCopy(helmet));
			packets.add((Packet) packet6);
		}
		org.bukkit.inventory.ItemStack chestplate = disguised.getInventory()
				.getChestplate();
		if (chestplate != null && chestplate.getType() != Material.AIR) {
			Packet5EntityEquipment packet7 = new Packet5EntityEquipment(
					disguised.getEntityId(), 3,
					CraftItemStack.asNMSCopy(chestplate));
			packets.add((Packet) packet7);
		}
		org.bukkit.inventory.ItemStack leggings = disguised.getInventory()
				.getLeggings();
		if (leggings != null && leggings.getType() != Material.AIR) {
			Packet5EntityEquipment packet8 = new Packet5EntityEquipment(
					disguised.getEntityId(), 2,
					CraftItemStack.asNMSCopy(leggings));
			packets.add((Packet) packet8);
		}
		org.bukkit.inventory.ItemStack boots = disguised.getInventory()
				.getBoots();
		if (boots != null && boots.getType() != Material.AIR) {
			Packet5EntityEquipment packet9 = new Packet5EntityEquipment(
					disguised.getEntityId(), 1, CraftItemStack.asNMSCopy(boots));
			packets.add((Packet) packet9);
		}
	}

	@Override
	protected void sendPlayerSpawnPackets(Player player) {
		Packet20NamedEntitySpawn packet20 = new Packet20NamedEntitySpawn(
				(EntityHuman) ((CraftPlayer) player).getHandle());
		EntityTracker tracker = ((CraftWorld) player.getWorld()).getHandle().tracker;
		tracker.a(((CraftPlayer) player).getHandle(), (Packet) packet20);
	}

	class PacketListener extends PacketAdapter {
		public PacketListener() {
			super((Plugin) MagicSpells.plugin, ConnectionSide.SERVER_SIDE,
					ListenerPriority.NORMAL, new Integer[] { 20, 5, 31, 33, 32,
							40, 34, 35 });
		}

		public void onPacketSending(PacketEvent event) {
			final Player player = event.getPlayer();
			PacketContainer packetContainer = event.getPacket();
			Packet packet = (Packet) packetContainer.getHandle();
			if (packet instanceof Packet20NamedEntitySpawn) {
				final String name = ((Packet20NamedEntitySpawn) packet).b;
				final DisguiseSpell.Disguise disguise = DisguiseManager_1_5_R3_ProtocolLib.this.disguises
						.get(name.toLowerCase());
				if (player != null && disguise != null) {
					Bukkit.getScheduler().scheduleSyncDelayedTask(
							(Plugin) MagicSpells.plugin,
							(Runnable) new Runnable() {
								@Override
								public void run() {
									Player disguised = Bukkit.getPlayer(name);
									if (disguised != null) {
										DisguiseManager_1_5_R3_ProtocolLib.this
												.sendDisguisedSpawnPacket(
														player, disguised,
														disguise, null);
									}
								}
							}, 0L);
					event.setCancelled(true);
				}
			} else if (DisguiseManager_1_5_R3_ProtocolLib.this.hideArmor
					&& packet instanceof Packet5EntityEquipment) {
				Packet5EntityEquipment eq = (Packet5EntityEquipment) packet;
				if (eq.b > 0
						&& DisguiseManager_1_5_R3_ProtocolLib.this.disguisedEntityIds
								.containsKey(eq.a)) {
					event.setCancelled(true);
				}
			} else if (packet instanceof Packet31RelEntityMove) {
				Packet31RelEntityMove mov = (Packet31RelEntityMove) packet;
				int entId = mov.a;
				if (DisguiseManager_1_5_R3_ProtocolLib.this.mounts
						.containsKey(entId)) {

					Packet31RelEntityMove newpacket = new Packet31RelEntityMove(
							(int) DisguiseManager_1_5_R3_ProtocolLib.this.mounts
									.get(entId), mov.b, mov.c, mov.d);
					((CraftPlayer) player).getHandle().playerConnection
							.sendPacket((Packet) newpacket);
				}
			} else if (packet instanceof Packet40EntityMetadata) {
				Packet40EntityMetadata pac = (Packet40EntityMetadata) packet;
				int entId = pac.a;
				DisguiseSpell.Disguise disguise = DisguiseManager_1_5_R3_ProtocolLib.this.disguisedEntityIds
						.get(entId);
				if (disguise != null
						&& disguise.getEntityType() != EntityType.PLAYER) {
					event.setCancelled(true);
				}
			} else if (packet instanceof Packet33RelEntityMoveLook) {
				Packet33RelEntityMoveLook pac = (Packet33RelEntityMoveLook) packet;
				int entId = pac.a;
				if (DisguiseManager_1_5_R3_ProtocolLib.this.dragons
						.contains(entId)) {

					PacketContainer clone = packetContainer.deepClone();
					clone.getHandle();
					int dir = pac.e + 128;
					if (dir > 127) {
						dir -= 256;
					}
					pac.e = (byte) dir;
					event.setPacket(clone);
					Packet28EntityVelocity packet2 = new Packet28EntityVelocity(
							entId, 0.15, 0.0, 0.15);
					((CraftPlayer) event.getPlayer()).getHandle().playerConnection
							.sendPacket((Packet) packet2);
				} else if (DisguiseManager_1_5_R3_ProtocolLib.this.mounts
						.containsKey(entId)) {
					PacketContainer clone = packetContainer.deepClone();
					Packet33RelEntityMoveLook newpacket2 = (Packet33RelEntityMoveLook) clone
							.getHandle();
					newpacket2.a = DisguiseManager_1_5_R3_ProtocolLib.this.mounts
							.get(entId);
					((CraftPlayer) event.getPlayer()).getHandle().playerConnection
							.sendPacket((Packet) newpacket2);
				}
			} else if (packet instanceof Packet32EntityLook) {
				Packet32EntityLook orig = (Packet32EntityLook) packet;
				int entId = orig.a;
				if (DisguiseManager_1_5_R3_ProtocolLib.this.dragons
						.contains(entId)) {
					PacketContainer clone = packetContainer.deepClone();
					clone.getHandle();
					int dir = orig.e + 128;
					if (dir > 127) {
						dir -= 256;
					}
					orig.e = (byte) dir;
					event.setPacket(clone);
					Packet28EntityVelocity packet2 = new Packet28EntityVelocity(
							entId, 0.15, 0.0, 0.15);
					((CraftPlayer) event.getPlayer()).getHandle().playerConnection
							.sendPacket((Packet) packet2);
				} else if (DisguiseManager_1_5_R3_ProtocolLib.this.mounts
						.containsKey(entId)) {
					PacketContainer clone = packetContainer.deepClone();
					Packet32EntityLook newpacket3 = (Packet32EntityLook) clone
							.getHandle();
					newpacket3.a = DisguiseManager_1_5_R3_ProtocolLib.this.mounts
							.get(entId);
					((CraftPlayer) event.getPlayer()).getHandle().playerConnection
							.sendPacket((Packet) newpacket3);
				}
			} else if (packet instanceof Packet34EntityTeleport) {
				Packet34EntityTeleport orig = (Packet34EntityTeleport) packet;
				int entId = orig.a;
				if (DisguiseManager_1_5_R3_ProtocolLib.this.dragons
						.contains(entId)) {
					PacketContainer clone = packetContainer.deepClone();
					Packet34EntityTeleport newpacket4 = (Packet34EntityTeleport) clone
							.getHandle();
					int dir = orig.e + 128;
					if (dir > 127) {
						dir -= 256;
					}
					orig.e = (byte) dir;
					newpacket4.e = (byte) dir;
					event.setPacket(clone);
					Packet28EntityVelocity packet2 = new Packet28EntityVelocity(
							entId, 0.15, 0.0, 0.15);
					((CraftPlayer) event.getPlayer()).getHandle().playerConnection
							.sendPacket((Packet) packet2);
				} else if (DisguiseManager_1_5_R3_ProtocolLib.this.mounts
						.containsKey(entId)) {
					PacketContainer clone = packetContainer.deepClone();
					Packet34EntityTeleport newpacket4 = (Packet34EntityTeleport) clone
							.getHandle();
					newpacket4.a = DisguiseManager_1_5_R3_ProtocolLib.this.mounts
							.get(entId);
					((CraftPlayer) event.getPlayer()).getHandle().playerConnection
							.sendPacket((Packet) newpacket4);
				}
			} else if (packet instanceof Packet35EntityHeadRotation) {
				Packet35EntityHeadRotation orig = (Packet35EntityHeadRotation) packet;
				int entId = orig.a;

				if (DisguiseManager_1_5_R3_ProtocolLib.this.dragons
						.contains(entId)) {
					event.setCancelled(true);
				}
			}
		}
	}
}
