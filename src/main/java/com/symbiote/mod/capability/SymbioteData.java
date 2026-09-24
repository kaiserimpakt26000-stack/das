package com.symbiote.mod.capability;

/**
 * Per-player data tracking whether a symbiote is bonded to them,
 * its level, XP, and which active ability is currently selected.
 */
public interface SymbioteData {

    boolean isBonded();
    void setBonded(boolean bonded);

    int getLevel();
    void setLevel(int level);

    int getXp();
    void setXp(int xp);

    /** Adds XP, handling level-ups (returns true if a level-up happened). */
    boolean addXp(int amount);

    int getSelectedAbilityIndex();
    void setSelectedAbilityIndex(int index);

    int getMaxLevel();

    /** XP required to go from the current level to the next one. */
    int xpToNextLevel();
}
