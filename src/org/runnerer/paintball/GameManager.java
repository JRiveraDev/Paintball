package org.runnerer.paintball;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.potion.PotionEffect;
import org.runnerer.core.common.utils.C;
import org.runnerer.core.common.utils.UtilServer;
import org.runnerer.core.scoreboard.ScoreboardSystem;
import org.runnerer.core.update.UpdateType;
import org.runnerer.core.common.utils.F;
import org.runnerer.core.update.events.UpdateEvent;
import org.runnerer.paintball.events.KitChangeEvent;
import org.runnerer.paintball.game.Game;
import org.runnerer.paintball.game.GameState;
import org.runnerer.paintball.kit.Kit;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameManager implements Listener
{
    public static GameManager instance;
    private Game game;

    public CopyOnWriteArrayList<String> spectatorList = new CopyOnWriteArrayList<String>();

    public CopyOnWriteArrayList<String> mapList = new CopyOnWriteArrayList<String>();
    private ScoreboardSystem board;
    private String currentStatus;

    private HashMap<String, Kit> kitHashMap = new HashMap<>();

    public GameManager()
    {
        currentStatus = C.Green + C.Bold + "Waiting for players";
        board = new ScoreboardSystem(currentStatus);

        instance = this;
        Paintball.getInstance().registerEngine(new GameManager());
    }

    public Game getGame()
    {
        return game;
    }

    public void setGame(Game chosenGame)
    {
        game = chosenGame;
    }

    public ScoreboardSystem getBoard()
    {
        return board;
    }

    public void setKit(Player player, Kit kit)
    {
        KitChangeEvent event = new KitChangeEvent(kit);
        Bukkit.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled())
        {
            player.sendMessage(F.main("Kit", "You cannot change/select your kit now."));
            return;
        }

        if (kitHashMap.containsKey(player.getName()))
        {
            kitHashMap.replace(player.getName(), kit);
        } else
        {
            kitHashMap.put(player.getName(), kit);
        }
    }

    public Kit getKit(Player player)
    {
        if (!kitHashMap.containsKey(player.getName())) return null;

        return kitHashMap.get(player.getName());
    }

    public Location getLobbySpawn()
    {
        return new Location(Bukkit.getWorld("world"), 0, 103, 0);
    }

    @EventHandler
    public void preventBlockBurn(BlockBurnEvent event)
    {
        if (game == null) event.setCancelled(true);
    }

    @EventHandler
    public void preventSpread(BlockSpreadEvent event)
    {
        if (game == null) event.setCancelled(true);
    }

    @EventHandler
    public void preventBlockFade(BlockFadeEvent event)
    {
        if (game == null) event.setCancelled(true);
    }

    @EventHandler
    public void preventLeafDecay(LeavesDecayEvent event)
    {
        if (game == null) event.setCancelled(true);
    }

    @EventHandler
    public void preventMobSpawn(CreatureSpawnEvent event)
    {
        if (game == null) event.setCancelled(true);
    }

    @EventHandler
    public void spectatorQuit(PlayerQuitEvent event)
    {
        spectatorList.remove(event.getPlayer());
    }

    @EventHandler
    public void motdModification(ServerListPingEvent event)
    {
        event.setMaxPlayers(getGame().getGameConfig().maxPlayers);

        String info = "";

        if (getGame().getGameState() == GameState.WAITING)
        {
            info = "Recruiting|" + getGame().getGameConfig().minPlayers + "|" + getGame().getGameConfig().maxPlayers;
        } else if (getGame().getGameState() == GameState.PREPARING)
        {
            info = "InProgress|Preparing|" + getGame().getGameConfig().minPlayers + "|" + getGame().getGameConfig().maxPlayers;
        } else if (getGame().getGameState() == GameState.IN_PROGRESS)
        {
            info = "InProgress|" + getGame().getGameConfig().minPlayers + "|" + getGame().getGameConfig().maxPlayers;
        } else if (getGame().getGameState() == GameState.END)
        {
            info = "Ended|" + getGame().getGameConfig().minPlayers + "|" + getGame().getGameConfig().maxPlayers;
        }
        event.setMotd(getGame().getName() + "|" + info);
    }

    public void clear(Player player)
    {
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setFlySpeed(0.1F);

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        player.setFoodLevel(20);
        player.setSaturation(3f);
        player.setExhaustion(0f);
        player.setMaxHealth(20);
        player.setHealth(20);

        player.setLevel(0);
        player.setExp(0);

        player.setGameMode(GameMode.SURVIVAL);

        for (PotionEffect potion : player.getActivePotionEffects())
            player.removePotionEffect(potion.getType());

    }

    public void announce(String message)
    {
        Bukkit.broadcastMessage(F.main("Game", message));
    }

    @EventHandler
    public void preventLogin(PlayerLoginEvent event)
    {
        if (getGame().getPlayers() + 1 > getGame().getGameConfig().maxPlayers)
        {
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            event.getPlayer().sendMessage(C.Red + "This server has been locked to provide best gameplay experience.");
        }
    }

    public void scoreboardSet()
    {
        int i = 11;

        if (getGame().getGameState() == GameState.WAITING)
        {
            if (getGame().getPlayers() >= getGame().getGameConfig().minPlayers)
            {
                board.setTitle(C.Green + C.Bold + "Starting Soon");
            } else
            {
                board.setTitle(C.Green + C.Bold + "Recruiting Players");
            }
            board.add("", i--);
            board.add(C.Gold + C.Bold + "Game", i--);
            board.add(C.White + C.Bold + getGame().getName(), i--);
            board.add("", i--);
            board.add(C.Gray + C.Bold + "Players", i--);
            board.add((UtilServer.getPlayers().size() - spectatorList.size()) + "/" + getGame().getGameConfig().maxPlayers, i--);
            board.add("", i--);
            board.add(C.Red + C.Bold + "Kit", i--);
            board.add("None", i--); // Edit later
            board.add(C.Gray + C.Bold + "", i--);
            board.add("www.runnererswork.com", i--);
        } else
        {
            board.setTitle(getGame().getName());
            getGame().runScoreboard();
        }

        for (Player player : UtilServer.getPlayers()) board.add(player);
    }

    @EventHandler
    public void updateScoreboard(UpdateEvent event)
    {
        if (event.getType() != UpdateType.FAST) return;

        scoreboardSet();
    }

    @EventHandler
    public void joinMessage(PlayerJoinEvent event)
    {
        event.setJoinMessage(C.DGray + "Join> " + event.getPlayer().getName());
    }

    @EventHandler
    public void quitMessage(PlayerQuitEvent event)
    {
        event.setQuitMessage(C.DGray + "Quit> " + event.getPlayer().getName());
    }
}
