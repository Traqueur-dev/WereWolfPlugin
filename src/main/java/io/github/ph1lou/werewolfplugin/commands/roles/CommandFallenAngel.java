package io.github.ph1lou.werewolfplugin.commands.roles;


import io.github.ph1lou.werewolfapi.Commands;
import io.github.ph1lou.werewolfapi.PlayerWW;
import io.github.ph1lou.werewolfapi.WereWolfAPI;
import io.github.ph1lou.werewolfapi.enums.AngelForm;
import io.github.ph1lou.werewolfapi.enums.RolesBase;
import io.github.ph1lou.werewolfapi.events.AngelChoiceEvent;
import io.github.ph1lou.werewolfapi.rolesattributs.AngelRole;
import io.github.ph1lou.werewolfapi.rolesattributs.Roles;
import io.github.ph1lou.werewolfplugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CommandFallenAngel implements Commands {


    private final Main main;

    public CommandFallenAngel(Main main) {
        this.main = main;
    }

    @Override
    public void execute(Player player, String[] args) {

        WereWolfAPI game = main.getWereWolfAPI();
        UUID uuid = player.getUniqueId();
        PlayerWW playerWW = game.getPlayerWW(uuid);

        if (playerWW == null) return;

        Roles role = playerWW.getRole();

        if (!((AngelRole) role).isChoice(AngelForm.ANGEL)) {
            player.sendMessage(game.translate("werewolf.check.power"));
            return;
        }

        ((AngelRole) role).setChoice(AngelForm.FALLEN_ANGEL);
        Bukkit.getPluginManager().callEvent(new AngelChoiceEvent(playerWW, AngelForm.FALLEN_ANGEL));
        player.sendMessage(game.translate("werewolf.role.angel.angel_choice_perform", game.translate(RolesBase.FALLEN_ANGEL.getKey())));
    }
}
