package io.github.ph1lou.werewolfplugin.listeners.scenarios;

import io.github.ph1lou.werewolfapi.GetWereWolfAPI;
import io.github.ph1lou.werewolfapi.ListenerManager;
import io.github.ph1lou.werewolfapi.WereWolfAPI;
import io.github.ph1lou.werewolfapi.enums.StateGame;
import io.github.ph1lou.werewolfapi.enums.TimersBase;
import io.github.ph1lou.werewolfapi.events.StopEvent;
import io.github.ph1lou.werewolfapi.versions.VersionUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class DiamondLimit extends ListenerManager {

    final Map<String, Integer> diamondPerPlayer = new HashMap<>();

    public DiamondLimit(GetWereWolfAPI main) {
        super(main);
    }


    @EventHandler
    public void onGameStop(StopEvent event) {
        diamondPerPlayer.clear();
    }


    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {


        WereWolfAPI game = main.getWereWolfAPI();
        if (game.isState(StateGame.LOBBY)) return;

        String playerName = event.getPlayer().getName();
        Block block = event.getBlock();

        if (!block.getType().equals(Material.DIAMOND_ORE)) return;

        Location loc = new Location(block.getWorld(),
                block.getLocation().getBlockX() + 0.5,
                block.getLocation().getBlockY() + 0.5,
                block.getLocation().getBlockZ() + 0.5);

        if (game.getConfig().getTimerValues().get(TimersBase.DIGGING.getKey()) < 0) {
            block.getWorld().spawn(loc, ExperienceOrb.class).setExperience(event.getExpToDrop());
            block.setType(Material.AIR);
            return;
        }

        if (!VersionUtils.getVersionUtils()
                .getItemInHand(event.getPlayer())
                .getType().equals(Material.DIAMOND_PICKAXE) &&
                !VersionUtils.getVersionUtils()
                        .getItemInHand(event.getPlayer())
                        .getType().equals(Material.IRON_PICKAXE)) {
            return;
        }
        if (diamondPerPlayer.getOrDefault(playerName, 0) >=
                game.getConfig().getDiamondLimit()) {
            block.getWorld().dropItem(loc, new ItemStack(Material.GOLD_INGOT, 1));
            block.getWorld().spawn(loc, ExperienceOrb.class).setExperience(event.getExpToDrop());
            block.setType(Material.AIR);
        }
        diamondPerPlayer.put(playerName, diamondPerPlayer.getOrDefault(playerName, 0) + 1);
    }
}
