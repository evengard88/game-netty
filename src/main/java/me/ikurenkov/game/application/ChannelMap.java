package me.ikurenkov.game.application;

import io.netty.channel.Channel;
import io.netty.util.internal.PlatformDependent;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ChannelMap implements Map<String, Channel> {

    @Override
    public int size() {
        return obj.size();
    }

    @Override
    public boolean isEmpty() {
        return obj.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return obj.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return obj.containsValue(value);
    }

    @Override
    public Channel get(Object key) {
        return obj.get(key);
    }

    @Override
    public Channel put(String key, Channel value) {
        return obj.put(key, value);
    }

    @Override
    public Channel remove(Object key) {
        return obj.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Channel> m) {
        obj.putAll(m);
    }

    @Override
    public void clear() {
        obj.clear();
    }

    @Override
    public Set<String> keySet() {
        return obj.keySet();
    }

    @Override
    public Collection<Channel> values() {
        return obj.values();
    }

    @Override
    public Set<Entry<String, Channel>> entrySet() {
        return obj.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return obj.equals(o);
    }

    @Override
    public int hashCode() {
        return obj.hashCode();
    }

    @Override
    public Channel getOrDefault(Object key, Channel defaultValue) {
        return obj.getOrDefault(key, defaultValue);
    }

    @Override
    public void forEach(BiConsumer<? super String, ? super Channel> action) {
        obj.forEach(action);
    }

    @Override
    public void replaceAll(BiFunction<? super String, ? super Channel, ? extends Channel> function) {
        obj.replaceAll(function);
    }

    @Override
    public Channel putIfAbsent(String key, Channel value) {
        return obj.putIfAbsent(key, value);
    }

    @Override
    public boolean remove(Object key, Object value) {
        return obj.remove(key, value);
    }

    @Override
    public boolean replace(String key, Channel oldValue, Channel newValue) {
        return obj.replace(key, oldValue, newValue);
    }

    @Override
    public Channel replace(String key, Channel value) {
        return obj.replace(key, value);
    }

    @Override
    public Channel computeIfAbsent(String key, Function<? super String, ? extends Channel> mappingFunction) {
        return obj.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public Channel computeIfPresent(String key, BiFunction<? super String, ? super Channel, ? extends Channel> remappingFunction) {
        return obj.computeIfPresent(key, remappingFunction);
    }

    @Override
    public Channel compute(String key, BiFunction<? super String, ? super Channel, ? extends Channel> remappingFunction) {
        return obj.compute(key, remappingFunction);
    }

    @Override
    public Channel merge(String key, Channel value, BiFunction<? super Channel, ? super Channel, ? extends Channel> remappingFunction) {
        return obj.merge(key, value, remappingFunction);
    }

    public static <K, V> Map<K, V> of() {
        return Map.of();
    }

    public static <K, V> Map<K, V> of(K k1, V v1) {
        return Map.of(k1, v1);
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2) {
        return Map.of(k1, v1, k2, v2);
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        return Map.of(k1, v1, k2, v2, k3, v3);
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return Map.of(k1, v1, k2, v2, k3, v3, k4, v4);
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        return Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6);
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        return Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7);
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
        return Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8);
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
        return Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9);
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
        return Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10);
    }

    @SafeVarargs
    public static <K, V> Map<K, V> ofEntries(Entry<? extends K, ? extends V>... entries) {
        return Map.ofEntries(entries);
    }

    public static <K, V> Entry<K, V> entry(K k, V v) {
        return Map.entry(k, v);
    }

    public static <K, V> Map<K, V> copyOf(Map<? extends K, ? extends V> map) {
        return Map.copyOf(map);
    }

    final Map<String, Channel> obj = PlatformDependent.newConcurrentHashMap();

}
