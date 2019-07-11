package org.runnerer.core.common.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;

public class UtilServer
{

    public static Collection<? extends Player> getPlayers()
    {
        return Bukkit.getOnlinePlayers();
    }
}
