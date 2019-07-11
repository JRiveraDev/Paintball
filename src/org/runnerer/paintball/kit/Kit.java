package org.runnerer.paintball.kit;

import org.bukkit.Material;

public abstract class Kit
{
    private String name;
    private String[] description;
    private Material material;

    public Kit(String name, String[] description, Material displayMaterial)
    {
        this.name = name;
        this.description = description;
        this.material = displayMaterial;
    }

    public String getName()
    {
        return name;
    }

    public String[] getDescription()
    {
        return description;
    }

    public Material getDisplayMaterial()
    {
        return material;
    }

    public abstract void giveItems();
}
