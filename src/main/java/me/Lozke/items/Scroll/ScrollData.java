package me.Lozke.items.Scroll;

import me.Lozke.data.ARNamespacedKey;
import me.Lozke.data.ItemType;
import me.Lozke.utils.Items;
import me.Lozke.utils.Logger;
import me.Lozke.utils.NamespacedKeyWrapper;
import me.Lozke.utils.Text;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ScrollData implements Serializable {

    private Map<Modifier, Object> scrollDataMap;
    private double successPercent;
    private double destroyPercent;
    private ScrollType scrollType;
    private ItemType itemType;

    private transient ItemStack stack;

    public ScrollData(ItemStack stack) {
        this.stack = stack;
        this.scrollDataMap = new HashMap<>();
    }

    public ScrollData() {
        this(new ItemStack(Material.MAP));
    }

    public void setScrollData(Map<Modifier, Object> scrollData) {
        this.scrollDataMap = scrollData;
    }

    public Map<Modifier, Object> getScrollData() {
        return scrollDataMap;
    }

    public void addModifier(Modifier modifier, double amount) {
        if (amount % 1 == 0) { //int
            scrollDataMap.put(modifier, (int) amount);
        }
        else { //double
            scrollDataMap.put(modifier, amount);
        }
    }

    public void setSuccessPercent(double percent) {
        this.successPercent = (percent > 1.0) ? percent / 100 : percent;
    }

    public double getSuccessPercent() {
        return successPercent;
    }

    public void setDestroyPercent(double percent) {
        this.destroyPercent = (percent > 1.0) ? percent / 100 : percent;
    }

    public double getDestroyPercent() {
        return destroyPercent;
    }

    public void setItemTypeToModify(ItemType itemType) {
        this.itemType = itemType;
    }

    public ItemType getItemTypeToModify() {
        return itemType;
    }

    public void setScrollType(ScrollType scrollType) {
        this.scrollType = scrollType;
        Items.setCustomModelData(stack, scrollType.ordinal() + 1);
    }

    public ScrollType getScrollType() {
        return scrollType;
    }

    public void formatName() {
        String name = scrollType.name();
        name = "&f" + name.substring(0, 1) + name.substring(1).toLowerCase() + " Scroll";

        Items.formatItem(stack, name);
    }

    public void formatLore() {
        String[] lines = new String[scrollDataMap.size()];
        int i = 0;
        for (Modifier modifier : scrollDataMap.keySet()) {
            Object val = scrollDataMap.get(modifier);
            if (val instanceof Integer) { //int
                lines[i] = Text.colorize("&f" + modifier.getFormat() + " +" + val);
            }
            else if (val instanceof Double) { //double
                lines[i] = Text.colorize("&f" + modifier.getFormat() + " +" + (int) ((double) val * 100) + "%");
            }
            else {
                Logger.log("Could not format " + modifier + " with value " + scrollDataMap.get(modifier) + " (Invalid Number)");
            }
            i++;
        }

        Items.setLore(stack, lines);
    }

    public void updateFormat() {
        formatName();
        formatLore();
    }

    public ItemStack getItem() {
        updateFormat();
        new NamespacedKeyWrapper(stack)
                .addKey(ScrollDataTag.DATA_TAG, new ScrollDataTag(), this)
                .addKey(ARNamespacedKey.REAL_ITEM);
        return stack;
    }
}
