package de.comparus.opensource.longmap;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class LongMapImpl<V> implements LongMap<V> {

    private Basket[] baskets;

    private final int DEFAULT_CAPACITY = 16;
    private final static double LOAD_FACTOR = 0.75;

    private Class<V> genericType;

    @SuppressWarnings("unchecked")
    public LongMapImpl() {
        genericType = (Class<V>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        baskets = (Basket[]) Array.newInstance(genericType, DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public LongMapImpl(int capacity){
        genericType = (Class<V>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        baskets = (Basket[]) Array.newInstance(genericType, capacity);
    }


    public V put(long key, V value) {
        int index = getIndexForHashCode(getHashCode(key));
        baskets[index].addEntry(new Entry(key,value));
        return value;
    }

    public V get(long key) {
        int index = getIndexForHashCode(getHashCode(key));
        return baskets[index].findByKey(key);
    }

    public V remove(long key) {
        return null;
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean containsKey(long key) {
        return false;
    }

    public boolean containsValue(V value) {
        return false;
    }

    public long[] keys() {
        return null;
    }

    public V[] values() {
        return null;
    }

    public long size() {
        return 0;
    }

    public void clear() {

    }

    private int getHashCode(long l){
        return  (int)(l ^ l >>> 32);
    }

    private int getIndexForHashCode(int hashCode){
        return hashCode % baskets.length;
    }

    private class Basket {

        private List<Entry> entries = new ArrayList<>();

        private void addEntry(Entry entry){
            entries.add(entry);
        }
        
        private List<Entry> getEntries(){
            return entries;
        }

        private V findByKey(long key) {
            return entries.stream()
                    .filter(entry -> entry.key == key)
                    .findFirst()
                    .map(Entry::getValue)
                    .orElse(null);
        }
    }
    
    private class Entry{
        long key;
        V value;

        private Entry() {
        }

        private Entry(long key, V value) {
            this.key = key;
            this.value = value;
        }

        private long getKey() {
            return key;
        }

        private void setKey(long key) {
            this.key = key;
        }

        private V getValue() {
            return value;
        }

        private void setValue(V value) {
            this.value = value;
        }
    }
}
