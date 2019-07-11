package org.runnerer.paintball.spectator;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.runnerer.core.common.utils.F;
import org.runnerer.paintball.GameManager;
import org.runnerer.paintball.game.GameState;

public class SpectatorCommand implements CommandExecutor
{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!(sender instanceof Player)) return false;

        if (cmd.getName().equalsIgnoreCase("spec") || cmd.getName().equalsIgnoreCase("spectate"))
        {
            Player player = (Player) sender;

            if (GameManager.instance.getGame().getGameState() != GameState.WAITING)
            {
                player.sendMessage(F.main("Game", "Wait until you're in the lobby to set or remove yourself as a spectator."));
                return true;
            }

            if (GameManager.instance.spectatorList.contains(player.getName()))
            {
                GameManager.instance.spectatorList.remove(player.getName());
                player.sendMessage(F.main("Game", "You are no longer spectating." ));
            } else
            {
                GameManager.instance.spectatorList.add(player.getName());
                player.sendMessage(F.main("Game", "You are now spectating." ));
            }
        }
        return true;
    }
}
