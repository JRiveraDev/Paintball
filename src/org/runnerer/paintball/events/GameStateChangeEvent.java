package org.runnerer.paintball.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.runnerer.paintball.game.GameState;

public class GameStateChangeEvent extends Event
{

    private static final HandlerList handlers = new HandlerList();
    private GameState state;

    private boolean cancelled = false;

    public GameStateChangeEvent(GameState gameState)
    {
        this.state = gameState;
    }

    public HandlerList getHandlers()
    {
        return handlers;
    }

    public GameState getGameState()
    {
        return this.state;
    }

    public void setCancelled(boolean cancel)
    {
        cancelled = cancel;
    }

    public boolean isCancelled()
    {
        return cancelled;
    }
}
