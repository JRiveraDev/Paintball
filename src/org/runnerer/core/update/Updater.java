package org.runnerer.core.update;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.runnerer.paintball.Paintball;

import java.text.DecimalFormat;

public class Updater
{


    private int updater;
    private DecimalFormat format = new DecimalFormat("0.00");

    public Updater()
    {
        this.updater = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin) Paintball.getInstance(), (Runnable) this, 0L, 1L);
    }

    public void Disable()
    {
        Bukkit.getScheduler().cancelTask(this.updater);
    }
}

