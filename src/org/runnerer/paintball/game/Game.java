package org.runnerer.paintball.game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.runnerer.core.common.utils.C;
import org.runnerer.core.common.utils.F;
import org.runnerer.core.common.utils.UtilServer;
import org.runnerer.paintball.GameManager;
import org.runnerer.paintball.events.GameStateChangeEvent;
import org.runnerer.paintball.spectator.SpectatorManager;
import org.runnerer.paintball.Paintball;
import org.runnerer.paintball.config.GameOptions;

import java.util.ArrayList;

public abstract class Game
{
    private String name;
    private String[] description;

    private GameState gameState;

    private GameOptions gameConfig;

    public Game(String gameName, String[] gameDescription)
    {
        name = gameName;
        description = gameDescription;

        gameConfig = new GameOptions();
    }

    public void setState(GameState state)
    {
        GameStateChangeEvent event = new GameStateChangeEvent(state);
        Bukkit.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled())
        {
            System.out.print("Cannot change game state!");
            return;
        }

        gameState = state;
    }

    public GameState getGameState()
    {
        return gameState;
    }

    public GameOptions getGameConfig()
    {
        return gameConfig;
    }

    public String getName()
    {
        return name;
    }

    public String[] getDescription()
    {
        return description;
    }

    public Location getSpectatorLocation()
    {
        // CHANGE.
        return new Location(getMap(), 0, 120, 0);
    }

    public Location getSpawnLocations()
    {
        // CHANGE.
        return new Location(getMap(), 0, 69, 0);
    }

    public void start()
    {
        for (Player player : UtilServer.getPlayers())
        {
            GameManager.instance.clear(player);
            setState(GameState.PREPARING);
            if (GameManager.instance.spectatorList.contains(player))
            {
                player.sendMessage(F.main("Spectator", "You are spectating this game!"));
                SpectatorManager.startAsSpectator(player);
            } else
            {
                player.teleport(getSpawnLocations());
            }
            player.sendMessage(C.Yellow + C.Bold + getName());

            for (String descriptionString : getDescription())
            {
                player.sendMessage(C.Gray + descriptionString);
            }

            Paintball.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Paintball.getInstance(), new Runnable()
            {
                public void run()
                {
                    onStart();
                    setState(GameState.IN_PROGRESS);
                }
            }, 200L);
        }
    }

    public void end(Player winner)
    {
        for (Player player : UtilServer.getPlayers())
        {
            for (Player other : UtilServer.getPlayers())
            {
                if (other != player) return;
                other.hidePlayer(player);
            }
            setState(GameState.END);
            GameManager.instance.clear(player);
            player.setFlySpeed(0.1f);
            player.setAllowFlight(false);
            player.setFlying(false);
            player.sendMessage("");
            player.sendMessage("");

            if (winner == null)
            {
                player.sendMessage(C.Yellow + C.Bold + "No one" + C.White + C.Bold + " won the game. :(");
            } else
            {
                player.sendMessage(C.Yellow + C.Bold + winner.getName() + C.White + C.Bold + " won the game!");
            }

            player.sendMessage("");
            player.sendMessage("");

            Paintball.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Paintball.getInstance(), new Runnable()
            {
                public void run()
                {
                        for (Player other : UtilServer.getPlayers())
                        {
                            if (other != player) return;
                            other.showPlayer(player);
                        }

                    player.teleport(GameManager.instance.getLobbySpawn());
                    setState(GameState.WAITING);
                }
            }, 200L);
        }
    }

    public void dead(Player player)
    {
        player.sendMessage(F.main("Death", "You lost! :("));
        SpectatorManager.setSpectator(player);
    }

    public int getPlayers()
    {
        return UtilServer.getPlayers().size() - GameManager.instance.spectatorList.size();
    }

    public ArrayList<Player> getPlayingPlayers()
    {
        ArrayList<Player> gamePlayers = new ArrayList<>();
        for (Player player : UtilServer.getPlayers())
        {
            if (GameManager.instance.spectatorList.size() != 0)
            {
                for (String spectators : GameManager.instance.spectatorList)
                {
                    Player spectator = Bukkit.getPlayer(spectators);

                    if (player != spectator)
                    {
                        gamePlayers.add(player);
                    }
                }
            } else
            {
                gamePlayers.add(player);
            }
        }

        if (gamePlayers.size() == 0) return null;

        return gamePlayers;
    }

    public World getMap()
    {
        return Bukkit.getWorld("Paintball_HighRise");
    }

    public abstract void runScoreboard();

    public abstract void onStart();
}
