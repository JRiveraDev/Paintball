package org.runnerer.paintball.lobby;

import com.avaje.ebean.Update;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.runnerer.core.common.utils.UtilServer;
import org.runnerer.core.update.UpdateType;
import org.runnerer.core.update.events.UpdateEvent;
import org.runnerer.paintball.GameManager;
import org.runnerer.paintball.events.GameStateChangeEvent;
import org.runnerer.paintball.game.GameState;
import org.runnerer.paintball.lobby.countdown.Countdown;

public class LobbyManager implements Listener
{
    private Location spawnLocation;

    public LobbyManager()
    {
       spawnLocation = GameManager.instance.getLobbySpawn();

       World world = Bukkit.getWorld("world");
       world.setTime(6000);
       world.setStorm(false);
       world.setGameRuleValue("doDaylightCycle", "false");

    }

    @EventHandler
    public void lobbyJoin(PlayerJoinEvent event)
    {
        if (GameManager.instance.getGame().getGameState() != GameState.WAITING || GameManager.instance.getGame().getGameState() == GameState.END) return;

        event.getPlayer().teleport(spawnLocation);
    }

    @EventHandler
    public void damagePrevention(EntityDamageByEntityEvent event)
    {
        if (!(event.getEntity() instanceof Player)) return;

        if (GameManager.instance.getGame().getGameState() != GameState.WAITING || GameManager.instance.getGame().getGameState() == GameState.END) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void preventHungerLoss(FoodLevelChangeEvent event)
    {
        if (GameManager.instance.getGame().getGameState() != GameState.WAITING || GameManager.instance.getGame().getGameState() == GameState.END) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void stateChange(GameStateChangeEvent event)
    {
        if (event.getGameState() == GameState.END || event.getGameState() == GameState.PREPARING) return;

        for (Player player : UtilServer.getPlayers())
        {
            player.teleport(spawnLocation);
        }
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent event)
    {

        if (!event.getWorld().equals(Bukkit.getWorld("world")))
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void spectatorBreak(BlockBreakEvent event)
    {
        if (GameManager.instance.getGame().getGameState() != GameState.WAITING || GameManager.instance.getGame().getGameState() == GameState.END) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void spectatorPlace(BlockPlaceEvent event)
    {
        if (GameManager.instance.getGame().getGameState() != GameState.WAITING || GameManager.instance.getGame().getGameState() == GameState.END) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void startCountdown(UpdateEvent event)
    {
        if (event.getType() != UpdateType.SEC) return;

        if (UtilServer.getPlayers().size() >= GameManager.instance.getGame().getGameConfig().minPlayers)
        {
            Countdown.startCountdown();
        }
    }
}
