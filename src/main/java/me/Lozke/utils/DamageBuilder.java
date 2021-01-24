package me.Lozke.utils;

import me.Lozke.data.*;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public class DamageBuilder {

    private int baseDamage;
    private WeaponType weaponType;
    private Map<RiftsStat, Integer> statBuffs = new HashMap<>();
    private Map<RiftsStat, Integer> statDebuffs = new HashMap<>();

    public DamageBuilder setAttacker(RiftsEntity entity) {
        if (entity instanceof RiftsPlayer) {
            RiftsPlayer player = (RiftsPlayer) entity;
            baseDamage = player.getDamage();
            weaponType = WeaponType.getWeaponType(Bukkit.getPlayer(player.getUuid()).getEquipment().getItemInMainHand());
            statBuffs = player.getEquipmentContainer().getCombinedStats();
        }
        else if (entity instanceof RiftsMob) {
            RiftsMob mob = (RiftsMob) entity;
            baseDamage = mob.getDamage();
            weaponType = WeaponType.getWeaponType(mob.getEntity().getEquipment().getItemInMainHand());
            statBuffs = mob.getBaseStats();
        }
        return this;
    }

    public DamageBuilder setDefender(RiftsEntity entity) {
        if (entity instanceof RiftsPlayer) {
            statDebuffs = ((RiftsPlayer) entity).getEquipmentContainer().getCombinedStats();
        }
        else if (entity instanceof RiftsMob) {
            statDebuffs = ((RiftsMob) entity).getBaseStats();
        }
        return this;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public DamageBuilder setBaseDamage(int newBaseDamage) {
        this.baseDamage = newBaseDamage;
        return this;
    }

    public DamageBuilder modifyDamageBy(int modifyAmount) {
        baseDamage += modifyAmount;
        return this;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public DamageBuilder setWeaponType(WeaponType type) {
        this.weaponType = type;
        return this;
    }

    public DamageBuilder setDamageStatBuffs(Map<RiftsStat, Integer> statMap) {
        this.statBuffs = statMap;
        return this;
    }

    public DamageBuilder setDamageStatDebuffs(Map<RiftsStat, Integer> statMap) {
        this.statDebuffs = statMap;
        return this;
    }

    private int buildElementalDamage() {
        int elementalDmg = 0;
        elementalDmg += statBuffs.getOrDefault(RiftsStat.FIRE_DAMAGE, 0);
        elementalDmg += statBuffs.getOrDefault(RiftsStat.ICE_DAMAGE, 0);
        elementalDmg += statBuffs.getOrDefault(RiftsStat.POISON_DAMAGE, 0);

        float elementalResistance = statDebuffs.getOrDefault(RiftsStat.ELEMENTAL_RESISTANCE, 0);
        elementalResistance = (elementalResistance > 1.0) ? elementalResistance / 100 : elementalResistance;
        elementalDmg = (int) (elementalDmg - (elementalDmg * elementalResistance));

        return elementalDmg;
    }

    private int buildBaseDamageReduction() {
        int defense = statDebuffs.getOrDefault(RiftsStat.DEFENSE, 0);
        return baseDamage * (defense/(defense + 1000));
    }

    private int buildBonusWeaponDamage() {
        if (weaponType == null) {
            return 0;
        }
        switch (weaponType) {
            case SWORD:
                return (int) Math.round(baseDamage * (statBuffs.getOrDefault(RiftsStat.VIT, 0) * 0.0003));
            case AXE:
            case POLEARM:
                return (int) Math.round(baseDamage * (statBuffs.getOrDefault(RiftsStat.STR, 0) * 0.0003));
            /*
            case TRIDENT:
                return (int) Math.round(baseDamage * (statBuffs.getOrDefault(RiftsStat.STR, 0) * 0.0003));
            case STAVE:
                return (int) Math.round(baseDamage * (statBuffs.getOrDefault(RiftsStat.INT, 0) * 0.0003));
            case BOW:
            case CROSSBOW:
                return (int) Math.round(baseDamage * (statBuffs.getOrDefault(RiftsStat.DEX, 0) * 0.0003));
             */
            default:
                return 0;
        }
    }

    public int buildDamage() {
        int finalDamage = baseDamage - buildBaseDamageReduction();
        finalDamage += buildElementalDamage();
        finalDamage += buildBonusWeaponDamage();
        finalDamage += statBuffs.getOrDefault(RiftsStat.PURE_DAMAGE, 0);
        return finalDamage;
    }
}
