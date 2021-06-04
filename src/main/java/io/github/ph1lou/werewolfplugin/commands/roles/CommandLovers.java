package io.github.ph1lou.werewolfplugin.commands.roles;

import io.github.ph1lou.werewolfapi.ICommand;
import io.github.ph1lou.werewolfapi.ILover;
import io.github.ph1lou.werewolfapi.IPlayerWW;
import io.github.ph1lou.werewolfapi.WereWolfAPI;
import io.github.ph1lou.werewolfapi.enums.LoverType;
import io.github.ph1lou.werewolfapi.enums.Sound;
import io.github.ph1lou.werewolfapi.enums.StatePlayer;
import io.github.ph1lou.werewolfapi.events.lovers.DonEvent;
import io.github.ph1lou.werewolfplugin.roles.lovers.AmnesiacLover;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class CommandLovers implements ICommand {

    @Override
    public void execute(WereWolfAPI game, Player player, String[] args) {

        String playerName = player.getName();
        UUID uuid = player.getUniqueId();
        IPlayerWW playerWW = game.getPlayerWW(uuid).orElse(null);

        if (playerWW == null) return;

        if (playerWW.getLovers().isEmpty()) {
            playerWW.sendMessageWithKey("werewolf.role.lover.not_in_pairs");
            return;
        }

        int heart;

        try {
            heart = Integer.parseInt(args[0]);
        } catch (NumberFormatException ignored) {
            playerWW.sendMessageWithKey("werewolf.check.number_required");
            return;
        }

        if (heart >= 100) {
            playerWW.sendMessageWithKey("werewolf.role.lover.100");
            return;
        }

        if (args.length == 1) {

            playerWW.getLovers().stream()
                    .filter(loverAPI1 -> !loverAPI1.isKey(LoverType.CURSED_LOVER.getKey()))
                    .filter(loverAPI1 -> !loverAPI1.isKey(LoverType.AMNESIAC_LOVER.getKey()) || ((AmnesiacLover) loverAPI1).isRevealed())
                    .forEach(loverAPI1 -> {
                        double health = player.getHealth() * heart / 100f;
                        AtomicReference<Double> temp = new AtomicReference<>((double) 0);

                        double don = health / (float) (loverAPI1.getLovers().size() - 1);

                        loverAPI1.getLovers()
                                .stream()
                                .filter(playerWW1 -> !playerWW.equals(playerWW1))
                                .filter(playerWW1 -> playerWW1.isState(StatePlayer.ALIVE))
                                .forEach(playerWW1 -> {
                                    Player playerCouple = Bukkit.getPlayer(playerWW1.getUUID());

                                    if (playerCouple != null) {

                                        if (playerWW1.getMaxHealth() - playerCouple.getHealth() >= don) {
                                            DonEvent donEvent = new DonEvent(playerWW, playerWW1, heart);
                                            Bukkit.getPluginManager().callEvent(donEvent);

                                            if (!donEvent.isCancelled()) {
                                                playerCouple.setHealth(playerCouple.getHealth() + don);
                                                temp.updateAndGet(v -> v + don);
                                                playerCouple.sendMessage(game.translate("werewolf.role.lover.received", heart, playerName));
                                                playerWW.sendMessageWithKey("werewolf.role.lover.complete", Sound.PORTAL, heart, playerCouple.getName());
                                            } else {
                                                playerWW.sendMessageWithKey("werewolf.check.cancel");
                                            }
                                        } else {
                                            playerWW.sendMessageWithKey("werewolf.role.lover.too_many_heart", playerCouple.getName());
                                        }
                                    }
                                });

                        player.setHealth(player.getHealth() - temp.get());
                    });
        }
        else {
            if (args[1].equals(playerName)) {
                playerWW.sendMessageWithKey("werewolf.check.not_yourself");
                return;
            }
            Player playerCouple = Bukkit.getPlayer(args[1]);

            if (playerCouple == null) {
                playerWW.sendMessageWithKey("werewolf.check.offline_player");
                return;
            }

            UUID argUUID = playerCouple.getUniqueId();
            IPlayerWW playerWW1 = game.getPlayerWW(argUUID).orElse(null);

            if (playerWW1 == null) return;

            if (!playerWW1.isState(StatePlayer.ALIVE)) {
                playerWW.sendMessageWithKey("werewolf.check.offline_player");
                return;
            }

            double don = player.getHealth() * heart / 100f;

            Optional<? extends ILover> ILover = playerWW.getLovers().stream()
                    .filter(loverAPI1 -> !loverAPI1.isKey(LoverType.CURSED_LOVER.getKey()))
                    .filter(loverAPI1 -> loverAPI1.getLovers().contains(playerWW1))
                    .filter(loverAPI1 -> !loverAPI1.isKey(LoverType.AMNESIAC_LOVER.getKey()) || ((AmnesiacLover) loverAPI1).isRevealed())
                    .findFirst();

            if (ILover.isPresent()) {
                ILover.ifPresent(loverAPI1 -> {

                    if (playerWW1.getMaxHealth() - playerCouple.getHealth() >= heart) {

                        DonEvent donEvent = new DonEvent(playerWW, playerWW1, heart);
                        Bukkit.getPluginManager().callEvent(donEvent);

                        if (!donEvent.isCancelled()) {
                            playerCouple.setHealth(playerCouple.getHealth() + don);
                            player.setHealth(player.getHealth() - don);
                            playerWW1.sendMessageWithKey("werewolf.role.lover.received", heart, playerName);
                            playerWW.sendMessageWithKey("werewolf.role.lover.complete", Sound.PORTAL, heart, playerCouple.getName());
                        } else {
                            playerWW.sendMessageWithKey("werewolf.check.cancel");
                        }
                    } else {
                        playerWW.sendMessageWithKey("werewolf.role.lover.too_many_heart", playerCouple.getName());
                    }
                });
            } else {
                playerWW.sendMessageWithKey("werewolf.role.lover.not_lover");
            }

        }
    }
}
