package org.runnerer.paintball;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.runnerer.paintball.engine.GameEngine;
import org.runnerer.paintball.engine.PaintballGame;
import org.runnerer.paintball.lobby.LobbyManager;
import org.runnerer.paintball.spectator.SpectatorManager;

public class Paintball extends JavaPlugin
{
    public static Paintball instance;

    @Override
    public void onEnable()
    {
        instance = this;

        GameManager gameManager = new GameManager();
        registerEngine(new LobbyManager());
        registerEngine(new SpectatorManager());
        registerEngine(new GameEngine());

        gameManager.setGame(new PaintballGame());
    }

    public void registerEngine(Listener listener)
    {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    public static Paintball getInstance()
    {
        return instance;
    }
}
