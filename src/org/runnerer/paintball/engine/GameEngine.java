package org.runnerer.paintball.engine;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.runnerer.paintball.GameManager;
import org.runnerer.paintball.game.GameState;

public class GameEngine implements Listener
{
    @EventHandler
    public void blockBreak(BlockBreakEvent event)
    {
        if (GameManager.instance.getGame().getGameState() == GameState.WAITING) return;

        if (!GameManager.instance.getGame().getGameConfig().blockBreak)
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void blockPlace(BlockPlaceEvent event)
    {
        if (GameManager.instance.getGame().getGameState() == GameState.WAITING) return;

        if (!GameManager.instance.getGame().getGameConfig().blockPlace)
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void placeWhilePreparing(BlockPlaceEvent event)
    {
        if (GameManager.instance.getGame().getGameState() != GameState.PREPARING) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void breakWhilePreparing(BlockBreakEvent event)
    {
        if (GameManager.instance.getGame().getGameState() != GameState.PREPARING) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void hitWhilePreparing(EntityDamageByEntityEvent event)
    {
        if (GameManager.instance.getGame().getGameState() != GameState.PREPARING) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void moveWhilePreparing(PlayerMoveEvent event)
    {
        if (GameManager.instance.getGame().getGameState() != GameState.PREPARING) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void dropWhilePreparing(PlayerDropItemEvent event)
    {
        if (GameManager.instance.getGame().getGameState() != GameState.PREPARING) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void pickupWhilePreparing(PlayerPickupItemEvent event)
    {
        if (GameManager.instance.getGame().getGameState() != GameState.PREPARING) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void damage(EntityDamageByEntityEvent event)
    {
        if (GameManager.instance.getGame().getGameState() == GameState.WAITING) return;

        if (!GameManager.instance.getGame().getGameConfig().damage)
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void damagePVP(EntityDamageByEntityEvent event)
    {
        if (GameManager.instance.getGame().getGameState() == GameState.WAITING) return;

        if (!GameManager.instance.getGame().getGameConfig().damagePvP)
        {
            if (!(event.getEntity() instanceof Player)) return;
            if (!(event.getDamager() instanceof Player)) return;

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void damageEVP(EntityDamageByEntityEvent event)
    {
        if (GameManager.instance.getGame().getGameState() == GameState.WAITING) return;

        if (!GameManager.instance.getGame().getGameConfig().damageEvP)
        {
            if (event.getEntity() instanceof Player) return;
            if (!(event.getDamager() instanceof Player)) return;

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void damageOther(EntityDamageByEntityEvent event)
    {
        if (GameManager.instance.getGame().getGameState() == GameState.WAITING) return;

        if (!GameManager.instance.getGame().getGameConfig().damageOther)
        {
            if (!(event.getCause() == EntityDamageEvent.DamageCause.FALL)) return;
            if (!(event.getDamager() instanceof Player)) return;

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void dropItem(PlayerDropItemEvent event)
    {
        if (GameManager.instance.getGame().getGameState() == GameState.WAITING) return;

        if (!GameManager.instance.getGame().getGameConfig().itemDrop)
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void pickupItem(PlayerPickupItemEvent event)
    {
        if (GameManager.instance.getGame().getGameState() == GameState.WAITING) return;

        if (!GameManager.instance.getGame().getGameConfig().itemPickup)
        {
            event.setCancelled(true);
        }
    }
}
