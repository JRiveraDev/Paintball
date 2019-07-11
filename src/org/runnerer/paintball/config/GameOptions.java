package org.runnerer.paintball.config;

public class GameOptions
{
    public int minPlayers = 4;
    public int maxPlayers = 20;

    public boolean blockBreak = false;
    public boolean blockPlace = false;

    public boolean damage = true;
    public boolean damagePvP = false;
    public boolean damageEvP = false;
    public boolean damageOther = false;

    public boolean itemDrop = false;
    public boolean itemPickup = false;

    public void revertFlags()
    {
        blockBreak = false;
        blockPlace = false;
        damage = true;
        damagePvP = false;
        damageEvP = false;
        damageOther = true;
        itemPickup = false;
        itemDrop = false;
    }
}
