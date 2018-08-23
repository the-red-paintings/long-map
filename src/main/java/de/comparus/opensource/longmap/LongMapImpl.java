package de.comparus.opensource.longmap;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class LongMapImpl<V> implements LongMap<V> {

    private Basket[] baskets;

    private final int capacity;

    private final int DEFAULT_CAPACITY = 16;
    private final static double LOAD_FACTOR = 0.75;

    private Class<? extends  V> genericType;



    public LongMapImpl() {
        capacity = DEFAULT_CAPACITY;
    }

    @SuppressWarnings("unchecked")
    public LongMapImpl(int capacity){
        this.capacity = capacity;
    }


    public V put(long key, V value) {
        if (genericType == null)
            genericType = (Class<? extends V>) value.getClass();
        if (baskets == null)
            baskets = (Basket[]) Array.newInstance(Basket.class, capacity);
        int index = getIndexForHashCode(getHashCode(key));
        if (baskets[index] == null){
            baskets[index] = new Basket();
        }
        baskets[index].addEntry(new Entry(key,value));

        return value;
    }

    public V get(long key) {
        int index = getIndexForHashCode(getHashCode(key));
        Optional<Basket> basket = Optional.ofNullable(baskets[index]);
        return basket.map(b -> b.findByKey(key)).orElse(null);
    }

    public V remove(long key) {
        return null;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean containsKey(long key) {
        return Arrays.stream(baskets)
                .anyMatch(basket -> Optional.ofNullable(basket.findByKey(key))
                        .isPresent());
    }

    public boolean containsValue(V value) {
        return Arrays.stream(baskets).
                anyMatch(basket -> Optional.ofNullable(basket.finByValue(value))
                        .isPresent());
    }

    public long[] keys() {
        return null;
    }

    public V[] values() {
        List<V> values = Arrays.stream(baskets)
                .flatMap(basket -> basket.getEntries()
                        .stream())
                .map(entry -> entry.value)
                .collect(Collectors.toList());
        return values.toArray(((V[]) Array.newInstance(genericType, values.size())));
    }

    public long size() {
        long size = 0;
        for (Basket basket : baskets){
            if (basket != null){
                size += basket.getEntries().size();
            }
        }
        return size;
    }

    public void clear() {

    }

    private int getHashCode(long l){
        return  (int)(l ^ l >>> 32);
    }

    private int getIndexForHashCode(int hashCode){
        return hashCode % baskets.length;
    }

    private void calcSixe(){
        int critical_size = (int) (capacity * LOAD_FACTOR);

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

        private Long finByValue(V value){
            return entries.stream()
                    .filter(entry -> entry.value.equals(value))
                    .findFirst()
                    .map(Entry::getKey)
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
