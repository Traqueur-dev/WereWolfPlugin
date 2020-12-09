package io.github.ph1lou.werewolfplugin.roles.neutrals;


import io.github.ph1lou.werewolfapi.GetWereWolfAPI;
import io.github.ph1lou.werewolfapi.PlayerWW;
import io.github.ph1lou.werewolfapi.enums.AngelForm;
import io.github.ph1lou.werewolfapi.events.AngelChoiceEvent;
import org.bukkit.Bukkit;

public class GuardianAngel extends Angel {


    public GuardianAngel(GetWereWolfAPI main, PlayerWW pLayerWW, String key) {
        super(main, pLayerWW, key);
        setChoice(AngelForm.GUARDIAN_ANGEL);
        Bukkit.getPluginManager().callEvent(
                new AngelChoiceEvent(getPlayerWW(), AngelForm.GUARDIAN_ANGEL));
    }
}
