package org.runnerer.paintball.map;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;

public class MapHelper
{
	public static void unloadMap(String mapname)
	{
		if (Bukkit.getServer().unloadWorld(Bukkit.getServer().getWorld(mapname), false))
		{
			Bukkit.getLogger().info("Successfully reset " + mapname);
		} else
		{

		}
	}

	public static void loadMap(String mapname)
	{
		Bukkit.getServer().createWorld(new WorldCreator(mapname));
	}

	public static void mapRollback(String mapname)
	{
		UnloadMap(mapname);
		LoadMap(mapname);
	}
}
