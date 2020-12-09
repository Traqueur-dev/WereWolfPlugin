package io.github.ph1lou.werewolfplugin.listeners.scenarios;

import io.github.ph1lou.werewolfapi.GetWereWolfAPI;
import io.github.ph1lou.werewolfapi.ListenerManager;
import io.github.ph1lou.werewolfapi.WereWolfAPI;
import io.github.ph1lou.werewolfapi.enums.UniversalMaterial;
import io.github.ph1lou.werewolfapi.versions.VersionUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CutClean extends ListenerManager {


    public CutClean(GetWereWolfAPI main) {
        super(main);
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {

        WereWolfAPI game = main.getWereWolfAPI();
        Block block = event.getBlock();
        Location loc = new Location(block.getWorld(), block.getLocation().getBlockX() + 0.5, block.getLocation().getBlockY() + 0.5, block.getLocation().getBlockZ() + 0.5);


        Material currentItemType = VersionUtils.getVersionUtils().getItemInHand(event.getPlayer()).getType();

        switch (block.getType()) {

            case COAL_ORE:

                if (!currentItemType.equals(Material.DIAMOND_PICKAXE) && !currentItemType.equals(Material.IRON_PICKAXE) && !currentItemType.equals(Material.STONE_PICKAXE) && !currentItemType.equals(UniversalMaterial.GOLDEN_PICKAXE.getType()) && !currentItemType.equals(UniversalMaterial.WOODEN_PICKAXE.getType())) {
                    return;
                }
                block.getWorld().spawn(loc, ExperienceOrb.class).setExperience(event.getExpToDrop());
                block.setType(Material.AIR);
                block.getWorld().dropItem(loc, new ItemStack(Material.TORCH, 4));
                break;


            case IRON_ORE:

                if (!currentItemType.equals(Material.DIAMOND_PICKAXE) && !currentItemType.equals(Material.IRON_PICKAXE) && !currentItemType.equals(Material.STONE_PICKAXE)) {
                    return;
                }
                block.getWorld().spawn(loc, ExperienceOrb.class).setExperience(game.getConfig().getScenarioValues().get(io.github.ph1lou.werewolfapi.enums.ScenariosBase.XP_BOOST.getKey()) ? (int) (game.getConfig().getXpBoost() / 100f) : 1);
                block.setType(Material.AIR);
                block.getWorld().dropItem(loc, new ItemStack(Material.IRON_INGOT, 1));
                break;

            case GOLD_ORE:
                if (!currentItemType.equals(Material.DIAMOND_PICKAXE) && !currentItemType.equals(Material.IRON_PICKAXE)) {
                    return;
                }
                block.getWorld().spawn(loc, ExperienceOrb.class).setExperience(game.getConfig().getScenarioValues().get(io.github.ph1lou.werewolfapi.enums.ScenariosBase.XP_BOOST.getKey()) ? (int) (game.getConfig().getXpBoost() / 100f) : 1);
                block.setType(Material.AIR);
                block.getWorld().dropItem(loc, new ItemStack(Material.GOLD_INGOT, 1));
                break;

            default:
                break;
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {

        List<ItemStack> loots = event.getDrops();

        for (int i = loots.size() - 1; i >= 0; --i) {
            ItemStack is = loots.get(i);
            if (is == null) {
                return;
            }
            UniversalMaterial material = UniversalMaterial.ofType(is.getType());
            if (material == null) return;

            switch (material) {
                case RAW_BEEF:
                    loots.remove(i);
                    loots.add(new ItemStack(UniversalMaterial.COOKED_BEEF.getType()));
                    break;

                case RAW_PORK:
                    loots.remove(i);
                    loots.add(new ItemStack(UniversalMaterial.COOKED_PORKCHOP.getType()));
                    break;

                case RAW_CHICKEN:
                    loots.remove(i);
                    loots.add(new ItemStack(UniversalMaterial.COOKED_CHICKEN.getType()));
                    break;

                case RAW_MUTTON:
                    loots.remove(i);
                    loots.add(new ItemStack(UniversalMaterial.COOKED_MUTTON.getType()));
                    break;

                case RAW_RABBIT:
                    loots.remove(i);
                    loots.add(new ItemStack(UniversalMaterial.COOKED_RABBIT.getType()));
                    break;
                default:

            }
        }
    }
}


