package me.Lozke.data;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class MapDataType implements PersistentDataType<byte[], Map> {

    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public Class<Map> getComplexType() {
        return Map.class;
    }

    @Override
    public byte[] toPrimitive(Map map, PersistentDataAdapterContext persistentDataAdapterContext) {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        try {
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(map);
        } catch (IOException ignored) {
        }
        return byteOut.toByteArray();
    }

    @Override
    public Map fromPrimitive(byte[] bytes, PersistentDataAdapterContext persistentDataAdapterContext) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            ByteArrayInputStream byteIn = new ByteArrayInputStream(bytes);
            ObjectInputStream in = new ObjectInputStream(byteIn);
            map = (HashMap<String, Object>) in.readObject();
        } catch (IOException | ClassNotFoundException ignored) {
        }
        return map;
    }
}
