package me.Lozke.items.Scroll;

import me.Lozke.LootPlugin;
import me.Lozke.data.ItemType;
import me.Lozke.utils.NamespacedKeyWrapper;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ScrollDataTag implements PersistentDataType<PersistentDataContainer, ScrollData> {

    public  static final NamespacedKey DATA_TAG = key("scroll-data");
    private        final NamespacedKey DATA_MAP = key("Scroll-Data-Map");
    private        final NamespacedKey SUCCESS = key("successPercent");
    private        final NamespacedKey DESTROY = key("destroyPercent");
    private        final NamespacedKey SKIN = key("skinOrdinal");
    private        final NamespacedKey ITEMTYPE = key("itemTypeOrdinal");

    @Override
    public Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public Class<ScrollData> getComplexType() {
        return ScrollData.class;
    }

    @Override
    public PersistentDataContainer toPrimitive(ScrollData scroll, PersistentDataAdapterContext persistentDataAdapterContext) {
        NamespacedKeyWrapper wrapper = new NamespacedKeyWrapper(persistentDataAdapterContext.newPersistentDataContainer());
        wrapper.addMap(DATA_MAP, scroll.getScrollData());
        wrapper.addDouble(SUCCESS, scroll.getSuccessPercent());
        wrapper.addDouble(DESTROY, scroll.getDestroyPercent());
        wrapper.addInt(SKIN, scroll.getScrollType().ordinal());
        wrapper.addInt(ITEMTYPE, scroll.getItemTypeToModify().ordinal());
        return wrapper.getDataContainer();
    }

    @Override
    public ScrollData fromPrimitive(PersistentDataContainer container, PersistentDataAdapterContext persistentDataAdapterContext) {
        NamespacedKeyWrapper wrapper = new NamespacedKeyWrapper(container);
        ScrollData scroll = new ScrollData();
        scroll.setScrollData(wrapper.getMap(DATA_MAP));
        scroll.setSuccessPercent(wrapper.getDouble(SUCCESS));
        scroll.setDestroyPercent(wrapper.getDouble(DESTROY));

        int skinOrdinal = wrapper.getInt(SKIN);
        ScrollType[] scrollTypes = ScrollType.types;
        scroll.setScrollType(scrollTypes[skinOrdinal]);

        int itemTypeOrdinal = wrapper.getInt(ITEMTYPE);
        ItemType[] itemTypes = ItemType.types;
        scroll.setItemTypeToModify(itemTypes[itemTypeOrdinal]);
        return scroll;
    }

    private static NamespacedKey key(String string) {
        return new NamespacedKey(LootPlugin.getPluginInstance(), string);
    }
}
