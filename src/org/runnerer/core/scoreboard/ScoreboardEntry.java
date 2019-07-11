package org.runnerer.core.scoreboard;

import com.google.common.base.Splitter;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

import java.util.Iterator;

public class ScoreboardEntry
{
    private String key;
    private ScoreboardSystem scoreboard;
    private String name;
    private Team team;
    private Score score;
    private int value;

    private String origName;
    private int count;

    public ScoreboardEntry(String key, ScoreboardSystem scoreboard, int value) {
        this.key = key;
        this.scoreboard = scoreboard;
        this.value = value;
        this.count = 0;
    }

    public ScoreboardEntry(String key, ScoreboardSystem scoreboard, int value, String origName, int count) {
        this.key = key;
        this.scoreboard = scoreboard;
        this.value = value;
        this.origName = origName;
        this.count = count;
    }

    public String getKey() {
        return key;
    }

    public ScoreboardSystem getScoreboard() {
        return scoreboard;
    }

    public String getName() {
        return name;
    }

    public Team getTeam() {
        return team;
    }

    public Score getScore() {
        return score;
    }

    public int getValue() {
        return score != null ? (value = score.getScore()) : value;
    }

    public void setValue(int value) {
        if (!score.isScoreSet()) {
            score.setScore(-1);
        }

        score.setScore(value);
    }

    public void update(String newName) {
        int value = getValue();
        if (origName != null && newName.equals(origName)) {
            // String oldName = newName;
            for (int i = 0; i < count; i++) {
                newName = ChatColor.RESET + newName;
            }
        } else if (newName.equals(name)) {
            return;
        }

        create(newName);
        setValue(value);
    }

    void remove() {
        if (score != null) {
            score.getScoreboard().resetScores(score.getEntry());
        }

        if (team != null) {
            team.unregister();
        }
    }

    private void create(String name) {
        this.name = name;
        remove();

        if (name.length() <= 16) {
            int value = getValue();
            score = scoreboard.getObjective().getScore(name);
            score.setScore(value);
            return;
        }

        team = scoreboard.getScoreboard().registerNewTeam("sb-" + scoreboard.getTeamId());
        Iterator<String> iterator = Splitter.fixedLength(16).split(name).iterator();
        if (name.length() > 16)
            team.setPrefix(iterator.next());
        String entry = iterator.next();
        score = scoreboard.getObjective().getScore(entry);
        if (name.length() > 32)
            team.setSuffix(iterator.next());

        team.addEntry(entry);
    }

}