package com.symbiote.mod.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class SymbioteDataImpl implements SymbioteData, INBTSerializable<CompoundTag> {

    public static final int MAX_LEVEL = 10;

    private boolean bonded = false;
    private int level = 1;
    private int xp = 0;
    private int selectedAbilityIndex = 0;

    @Override
    public boolean isBonded() { return bonded; }

    @Override
    public void setBonded(boolean bonded) { this.bonded = bonded; }

    @Override
    public int getLevel() { return level; }

    @Override
    public void setLevel(int level) { this.level = Math.max(1, Math.min(MAX_LEVEL, level)); }

    @Override
    public int getXp() { return xp; }

    @Override
    public void setXp(int xp) { this.xp = Math.max(0, xp); }

    @Override
    public boolean addXp(int amount) {
        if (!bonded || level >= MAX_LEVEL) return false;
        this.xp += amount;
        boolean leveledUp = false;
        while (level < MAX_LEVEL && xp >= xpToNextLevel()) {
            xp -= xpToNextLevel();
            level++;
            leveledUp = true;
        }
        return leveledUp;
    }

    @Override
    public int getSelectedAbilityIndex() { return selectedAbilityIndex; }

    @Override
    public void setSelectedAbilityIndex(int index) { this.selectedAbilityIndex = index; }

    @Override
    public int getMaxLevel() { return MAX_LEVEL; }

    @Override
    public int xpToNextLevel() {
        return level * 100;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("bonded", bonded);
        tag.putInt("level", level);
        tag.putInt("xp", xp);
        tag.putInt("selected", selectedAbilityIndex);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        this.bonded = tag.getBoolean("bonded");
        this.level = tag.getInt("level");
        this.xp = tag.getInt("xp");
        this.selectedAbilityIndex = tag.getInt("selected");
        if (this.level < 1) this.level = 1;
    }
}
