package com.vmoizan.brushplugin.utils;

import org.bukkit.FluidCollisionMode;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.function.Predicate;

/**
 * Utils function on player
 */
public class PlayerUtils {

    /**
     * Permet d'avoir le block que le joueur regarde
     * @param p : le joueur
     * @param range : la range maximum
     * @return Une location
     */
    public static Block getLookingBlock(Player p, int range){
        double yaw = p.getLocation().getYaw();
        double pitch = p.getLocation().getPitch();
        World world = p.getWorld();
        Predicate<Entity> predicate = entity -> entity == p;
        RayTraceResult rayTraceResult = world.rayTrace(p.getEyeLocation(), getVectorForRotation(pitch, yaw), range, FluidCollisionMode.ALWAYS, false, range, predicate);
        return rayTraceResult.getHitBlock();
    }

    /**
     * Creates a Vec3 using the pitch and yaw of the entities rotation.
     */
    public static Vector getVectorForRotation(double pitch, double yaw)
    {
        double f = Math.cos(-yaw * 0.017453292F - (float)Math.PI);
        double f1 = Math.sin(-yaw * 0.017453292F - (float)Math.PI);
        double f2 = -Math.cos(-pitch * 0.017453292F);
        double f3 = Math.sin(-pitch * 0.017453292F);
        return new Vector(f1 * f2, f3, f * f2);
    }
}
