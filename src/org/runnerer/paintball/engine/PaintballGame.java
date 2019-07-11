package org.runnerer.paintball.engine;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.runnerer.core.common.utils.C;
import org.runnerer.core.common.utils.UtilBlock;
import org.runnerer.core.update.events.UpdateEvent;
import org.runnerer.paintball.GameManager;
import org.runnerer.paintball.game.Game;
import org.runnerer.paintball.game.GameState;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.runnerer.paintball.kit.Kit;

public class PaintballGame extends Game
{

    public PaintballGame()
    {
        super("Paintball", new String[] {C.Gray + "Paint, paint, paint!", C.Gray + "Paint all of the other", C.Gray + "players to ", C.Gray + "become the victor!" });

        getGameConfig().itemPickup = false;
        getGameConfig().itemDrop = false;
        getGameConfig().damagePvP = false;
        getGameConfig().blockPlace = false;
        getGameConfig().blockBreak = false;
        getGameConfig().damageEvP = false;
        getGameConfig().damageOther = true;
        getGameConfig().damage = true;
    }

    @Override
    public void runScoreboard()
    {
        GameManager.instance.getBoard().add("", 2);
        GameManager.instance.getBoard().add("PAINT!", 1);
    }

    @Override
    public void onStart()
    {
        for (Player player : getPlayingPlayers())
        {
            ItemStack itemStack = new ItemStack(Material.IRON_BARDING);
            ItemMeta itemMeta = itemStack.getItemMeta();

            itemMeta.setDisplayName("Paintball Gun");

            itemStack.setItemMeta(itemMeta);
            player.getInventory().addItem(itemStack);
        }
    }

    @EventHandler
    public void onDamageEvent(EntityDamageByEntityEvent event)
    {
        if (getGameState() != GameState.IN_PROGRESS) return;

        if (event.getEntity() instanceof Player)
        {
            Player player = (Player) event.getEntity();

            if (player.getHealth() - event.getDamage() >= 0)
            {
                event.setCancelled(true);
                player.setHealth(20);

                if (getPlayingPlayers().size() >= 1)
                {
                    for (Player winner : getPlayingPlayers())
                    end(winner);
                    return;
                }
                dead(player);
            }
        }
    }

    @EventHandler
    public void preventHealthRegen(EntityRegainHealthEvent event)
    {
        if (event.getRegainReason() == RegainReason.SATIATED)
            event.setCancelled(true);
    }

    @EventHandler
    public void preventTeleport(PlayerTeleportEvent event)
    {
        if (event.getCause() == TeleportCause.ENDER_PEARL)
            event.setCancelled(true);
    }


    @EventHandler
    public void paintEffect(ProjectileHitEvent event)
    {
        if (event.getEntity() instanceof ThrownPotion)
            return;

        double random = Math.random() * 2 + 1;

        byte color = 0;

        if (random == 1)
        {
            color = 3;
        } else
        {
            color = 14;
        }

        Location loc = event.getEntity().getLocation().add(event.getEntity().getVelocity());

        for (Block block : UtilBlock.getNearbyBlocks(loc, 2))
        {
            if (block.getType() != Material.WOOL && block.getType() != Material.STAINED_CLAY)
                continue;

            block.setData(color);
        }
    }

    @EventHandler
    public void shootPaintball(PlayerInteractEvent event)
    {
        if (event.getPlayer().getItemInHand() == null)
            return;

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (event.getPlayer().getItemInHand().getType() != Material.IRON_BARDING)
            return;

        Player player = event.getPlayer();

        Projectile proj = player.launchProjectile(Snowball.class);
        proj.setVelocity(proj.getVelocity().multiply(1));

        event.setCancelled(true);
    }
}
