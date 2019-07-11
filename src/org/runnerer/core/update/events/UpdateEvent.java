package org.runnerer.core.update.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.runnerer.core.update.UpdateType;

public class UpdateEvent
        extends Event
{

    private static final HandlerList handlers = new HandlerList();
    private UpdateType Type;

    public UpdateEvent(UpdateType updateType)
    {
        this.Type = updateType;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }

    public UpdateType getType()
    {
        return this.Type;
    }

    public HandlerList getHandlers()
    {
        return handlers;
    }
}

