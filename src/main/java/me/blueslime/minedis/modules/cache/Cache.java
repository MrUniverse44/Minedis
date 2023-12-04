package me.blueslime.minedis.modules.cache;

import java.util.*;

public abstract class Cache<K, V> {
    private final Map<K, V> map;

    public Cache(Map<K, V> initialMap) {
        this.map = initialMap;
    }

    public void add(K key, V value) {
        map.computeIfAbsent(key, F -> value);
    }

    public void set(K key, V value) {
        map.put(key, value);
    }

    public void remove(K key) {
        map.remove(key);
    }

    public V get(K key) {
        return map.get(key);
    }

    public V getAndRemove(K key) {
        return map.remove(key);
    }

    public V get(K key, V value) {
        add(key, value);
        return map.get(key);
    }

    public List<V> getValues() {
        return new ArrayList<>(map.values());
    }

    @SuppressWarnings("unused")
    public List<K> getKeys() {
        return new ArrayList<>(map.keySet());
    }

    public boolean contains(K key) {
        return map.containsKey(key);
    }

    public Set<Map.Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    public Map<K, V> toMap() {
        return map;
    }

    public void clear() {
        map.clear();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public int size() {
        return map.size();
    }
}
