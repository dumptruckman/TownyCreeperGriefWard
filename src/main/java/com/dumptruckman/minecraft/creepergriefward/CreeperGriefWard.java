package com.dumptruckman.minecraft.creepergriefward;

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.object.TownyPermission.ActionType;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * EGG PLACE SPAWNER.  SPAWNER DROP EGG.
 */
public class CreeperGriefWard extends JavaPlugin implements Listener {

    private Towny towny;

    /**
     * Permission to allow player to grief with creepers.
     */
    private static final Permission IGNORE_PERM = new Permission(
            "creepergrief.allow",
            "Allows a user to grief with creepers",
            PermissionDefault.FALSE);

    @Override
    public final void onEnable() {
        PluginManager pm = this.getServer().getPluginManager();
        pm.addPermission(IGNORE_PERM);
        pm.registerEvents(this, this);
        this.towny = (Towny) pm.getPlugin("Towny");
        this.getLogger().info("enabled.");
    }

    @EventHandler
    public void entityExplode(EntityExplodeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!(event.getEntity() instanceof Monster)) {
            return;
        }

        Monster monster = (Monster) event.getEntity();
        if (monster.getTarget() == null
                || !(monster.getTarget() instanceof Player)) {
            return;
        }

        Player player = (Player) monster.getTarget();
        if (player.hasPermission(IGNORE_PERM)) {
            return;
        }

        boolean cancel = false;
        for (Block block : event.blockList()) {
            if (!TownyUniverse.getCachePermissions()
                    .getCachePermission(player, block.getLocation(), ActionType.DESTROY)) {
                cancel = true;
                break;
            }
        }
        if (cancel) {
            event.setCancelled(true);
        }
    }
}
