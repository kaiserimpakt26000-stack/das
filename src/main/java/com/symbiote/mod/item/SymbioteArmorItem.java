package com.symbiote.mod.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;

public class SymbioteArmorItem extends ArmorItem {

    public SymbioteArmorItem(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }

    public boolean isChestplate() {
        return this.type == Type.CHESTPLATE;
    }
}
