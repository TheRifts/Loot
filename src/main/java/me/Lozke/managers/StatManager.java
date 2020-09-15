package me.Lozke.managers;

import me.Lozke.data.RiftsStat;

import java.util.HashMap;
import java.util.Map;

public class StatManager {

    @SafeVarargs
    public static Map<RiftsStat, Integer> combineStatMaps(Map<RiftsStat, Integer>... statMaps) {
        Map<RiftsStat, Integer> combinedMaps = new HashMap<>();
        for (Map<RiftsStat, Integer> statMap : statMaps) {
            for (Map.Entry<RiftsStat, Integer> stats : statMap.entrySet()) {
                Integer oldValue = combinedMaps.getOrDefault(stats.getKey(), 0);
                Integer combinedValue = oldValue + stats.getValue();
                combinedMaps.put(stats.getKey(), combinedValue);
            }
        }
        return combinedMaps;
    }

}
