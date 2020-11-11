package io.github.ph1lou.werewolfplugin.game;


import io.github.ph1lou.werewolfapi.PlayerWW;
import io.github.ph1lou.werewolfapi.WereWolfAPI;
import io.github.ph1lou.werewolfapi.enumlg.RolesBase;
import io.github.ph1lou.werewolfapi.enumlg.StatePlayer;
import io.github.ph1lou.werewolfapi.rolesattributs.Roles;
import io.github.ph1lou.werewolfplugin.Main;
import io.github.ph1lou.werewolfplugin.roles.villagers.Villager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public class PlayerLG implements PlayerWW {

    private StatePlayer state = StatePlayer.ALIVE;
    private Roles role;
    private final List<UUID> lovers = new ArrayList<>();
    private Boolean kit = false;
    private Boolean announceCursedLoversAFK = false;
    private Boolean announceLoversAFK = false;
    private Boolean thief = false;
    private Boolean revealAmnesiacLover = false;
    private final List<ItemStack> itemsDeath = new ArrayList<>();
    private UUID cursedLovers = null;
    private UUID amnesiacLoverUUID = null;
    private transient Location spawn;
    private int deathTime = 0;
    private int disconnectedTime = 0;
    private int lostHeart = 0;
    private int kill = 0;
    private final List<UUID> killer = new ArrayList<>();
    private String name;


    public PlayerLG(Main main, WereWolfAPI game, Player player) {
        this.spawn = player.getWorld().getSpawnLocation();
        this.role = new Villager(main, game,
                player.getUniqueId(),
                RolesBase.VILLAGER.getKey());
        this.name = player.getName();
    }


	@Override
	public void setItemDeath(ItemStack[] itemsDeath) {
		this.itemsDeath.addAll(Arrays.asList(itemsDeath));
	}

	@Override
	public List<ItemStack> getItemDeath() {
		return this.itemsDeath;
	}

	@Override
	public void clearItemDeath() {
		this.itemsDeath.clear();
	}

	@Override
	public void setState(StatePlayer state) {
		this.state=state;
	}

	@Override
	public boolean isState(StatePlayer state) {
		return(this.state==state);
	}

	@Override
	public void addOneKill() {
		this.kill +=1;
	}

	@Override
	public int getNbKill() {
		return(this.kill);
	}

	@Override
	public void addKLostHeart(int k) {
		this.lostHeart +=k;
	}

	@Override
	public int getLostHeart() {
		return(this.lostHeart);
	}


	@Override
	public void setKit(Boolean kit) {
		this.kit=kit;
	}

	@Override
	public Boolean hasKit() {		
		return(this.kit);
	}


	@Override
	public void setRole(Roles role) {
		this.role = role;
	}

	@Override
	public Roles getRole() {
		return (this.role);
	}

    @Override
    public Boolean isKey(String role) {
        return (role.equals(this.role.getKey()));
    }

	@Override
	public void setSpawn(Location spawn) {
		this.spawn=spawn;
	}

	@Override
	public Location getSpawn() {
		return(this.spawn);
	}

	@Override
	public void addLover(UUID uuid) {
		this.lovers.add(uuid);
	}

	@Override
	public void clearLovers() {
		this.lovers.clear();
	}

	@Override
	public List<UUID> getLovers() {
		return (this.lovers);
	}

	@Override
	public void addKiller(UUID killerUUID) {
		this.killer.add(killerUUID);
	}

	@Override
	public List<UUID> getKillers() {
		return (this.killer);
	}

	@Override
	public void setDeathTime(Integer deathTime) {
		this.deathTime =deathTime;
	}

	@Override
	public int getDeathTime() {
		return(this.deathTime);
	}


	@Override
	public void clearLostHeart() {
		this.lostHeart = 0;
	}

	@Override
	public void removeLover(UUID uuid) {
		this.lovers.remove(uuid);
	}

	@Override
	public UUID getCursedLovers() {
		return cursedLovers;
	}

	@Override
	public void setCursedLover(UUID uuid) {
		this.cursedLovers = uuid;
	}

	@Override
	public Boolean getAnnounceCursedLoversAFK() {
		return announceCursedLoversAFK;
	}

	@Override
	public void setAnnounceCursedLoversAFK(Boolean announceCursedLoversAFK) {
		this.announceCursedLoversAFK = announceCursedLoversAFK;
	}

    @Override
    public Boolean getAnnounceLoversAFK() {
        return announceLoversAFK;
    }

    @Override
    public void setAnnounceLoversAFK(Boolean announceLoversAFK) {
        this.announceLoversAFK = announceLoversAFK;
    }

    @Nullable
    @Override
    public UUID getLastKiller() {
        return this.killer.isEmpty() ? null : this.killer.get(this.killer.size() - 1);
    }

    @Override
    public void setThief(Boolean thief) {
        this.thief = thief;
    }

	@Override
	public Boolean isThief() {
		return(this.thief);
	}

	@Override
	public UUID getAmnesiacLoverUUID() {
		return amnesiacLoverUUID;
	}

	@Override
	public void setAmnesiacLoverUUID(UUID uuid) {
		this.amnesiacLoverUUID = uuid;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

    @Override
    public Boolean getRevealAmnesiacLover() {
        return revealAmnesiacLover;
    }

    @Override
    public void setRevealAmnesiacLover(Boolean revealAmnesiacLover) {
        this.revealAmnesiacLover = revealAmnesiacLover;
    }

    @Override
    public StatePlayer getState() {
        return state;
    }

    @Override
    public int getDisconnectedTime() {
        return disconnectedTime;
    }

    @Override
    public void setDisconnectedTime(int disconnectedTime) {
        this.disconnectedTime = disconnectedTime;
    }
}

