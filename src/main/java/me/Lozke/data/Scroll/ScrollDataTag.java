package me.Lozke.data.Scroll;

import me.Lozke.api.IAddKeyWrapper;
import me.Lozke.data.ARNamespacedKey;
import me.Lozke.data.ItemType;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;

public class ScrollDataTag implements PersistentDataType<PersistentDataContainer, Scroll>, IAddKeyWrapper {

    private JavaPlugin plugin;
    private PersistentDataContainer container;

    private final NamespacedKey DATA_MAP = key("Scroll-Data-Map");
    private final NamespacedKey SUCCESS = key("successPercent");
    private final NamespacedKey DESTROY = key("destroyPercent");
    private final NamespacedKey SKIN = key("skinOrdinal");
    private final NamespacedKey ITEMTYPE = key("itemTypeOrdinal");

    public ScrollDataTag(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public Class<Scroll> getComplexType() {
        return Scroll.class;
    }

    @Override
    public PersistentDataContainer toPrimitive(Scroll scroll, PersistentDataAdapterContext persistentDataAdapterContext) {
        this.container = persistentDataAdapterContext.newPersistentDataContainer();
        addMap(DATA_MAP, scroll.getScrollData());
        addDouble(SUCCESS, scroll.getSuccessPercent());
        addDouble(DESTROY, scroll.getDestroyPercent());
        addInt(SKIN, scroll.getScrollType().ordinal());
        addInt(ITEMTYPE, scroll.getItemTypeToModify().ordinal());
        container.set(ARNamespacedKey.REAL_ITEM.getNamespacedKey(),ARNamespacedKey.REAL_ITEM.getDataType(),true);
        return container;
    }

    @Override
    public Scroll fromPrimitive(PersistentDataContainer container, PersistentDataAdapterContext persistentDataAdapterContext) {
        Scroll scroll = new Scroll();
        scroll.setScrollData(container.get(DATA_MAP, MAP_DATA_TYPE));
        scroll.setSuccessPercent(container.get(SUCCESS, PersistentDataType.DOUBLE));
        scroll.setDestroyPercent(container.get(DESTROY, PersistentDataType.DOUBLE));

        int skinOrdinal = container.get(SKIN, PersistentDataType.INTEGER);
        ScrollType[] scrollTypes = ScrollType.types;
        for (ScrollType scrollType : scrollTypes) {
            if (scrollType.ordinal() == skinOrdinal) {
                scroll.setScrollType(scrollType);
            }
        }

        int itemTypeOrdinal = container.get(ITEMTYPE, PersistentDataType.INTEGER);
        ItemType[] itemTypes = ItemType.types;
        for (ItemType type : itemTypes) {
            if (type.ordinal() == itemTypeOrdinal) {
                scroll.setItemTypeToModify(type);
            }
        }
        return scroll;
    }

    private NamespacedKey key(String string) {
        return new NamespacedKey(plugin, string);
    }

    @Override
    public ScrollDataTag addKey(NamespacedKey namespacedKey, PersistentDataType persistentDataType, Object o) {
        container.set(namespacedKey, persistentDataType,o);
        return this;
    }

    @Override
    public ScrollDataTag addString(NamespacedKey namespacedKey, String s) {
        return addKey(namespacedKey, PersistentDataType.STRING, s);
    }

    @Override
    public ScrollDataTag addByte(NamespacedKey namespacedKey, byte b) {
        return addKey(namespacedKey, PersistentDataType.BYTE, b);
    }

    @Override
    public ScrollDataTag addByteArray(NamespacedKey namespacedKey, byte[] bytes) {
        return addKey(namespacedKey, PersistentDataType.BYTE_ARRAY, bytes);
    }

    @Override
    public ScrollDataTag addInt(NamespacedKey namespacedKey, int i) {
        return addKey(namespacedKey, PersistentDataType.INTEGER, i);
    }

    @Override
    public ScrollDataTag addIntArray(NamespacedKey namespacedKey, int[] ints) {
        return addKey(namespacedKey, PersistentDataType.INTEGER_ARRAY, ints);
    }

    @Override
    public ScrollDataTag addDouble(NamespacedKey namespacedKey, double v) {
        return addKey(namespacedKey, PersistentDataType.DOUBLE, v);
    }

    @Override
    public ScrollDataTag addFloat(NamespacedKey namespacedKey, float v) {
        return addKey(namespacedKey, PersistentDataType.FLOAT, v);
    }

    @Override
    public ScrollDataTag addLong(NamespacedKey namespacedKey, long l) {
        return addKey(namespacedKey, PersistentDataType.LONG, l);
    }

    @Override
    public ScrollDataTag addLongArray(NamespacedKey namespacedKey, long[] longs) {
        return addKey(namespacedKey, PersistentDataType.LONG_ARRAY, longs);
    }

    @Override
    public ScrollDataTag addShort(NamespacedKey namespacedKey, short i) {
        return addKey(namespacedKey, PersistentDataType.SHORT, i);
    }

    @Override
    public ScrollDataTag addTagContainer(NamespacedKey namespacedKey, PersistentDataContainer persistentDataContainer) {
        return addKey(namespacedKey, PersistentDataType.TAG_CONTAINER, persistentDataContainer);
    }

    @Override
    public ScrollDataTag addTagContainerArray(NamespacedKey namespacedKey, PersistentDataContainer[] persistentDataContainers) {
        return addKey(namespacedKey, PersistentDataType.TAG_CONTAINER_ARRAY, persistentDataContainers);
    }

    @Override
    public ScrollDataTag addMap(NamespacedKey namespacedKey, Map map) {
        return addKey(namespacedKey, MAP_DATA_TYPE, map);
    }

    @Override
    public ScrollDataTag addList(NamespacedKey namespacedKey, List list) {
        return addKey(namespacedKey, LIST_DATA_TYPE, list);
    }

    @Override
    public ScrollDataTag addBoolean(NamespacedKey namespacedKey, boolean b) {
        return addKey(namespacedKey, BOOLEAN_DATA_TYPE, b);
    }
}
