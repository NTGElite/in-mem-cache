package lrumap;

import doublylinkedlist.DoublyLinkedList;
import doublylinkedlist.Node;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.HashMap;
import java.util.Optional;

@Getter
@Setter
public class LruCache<K, V> {
    private int capacity;

    private DoublyLinkedList<K> dll = new DoublyLinkedList<>();

    private HashMap<K, ImmutablePair<V, Node<K>>> cache = new HashMap<>();

    public LruCache() {
    }

    public LruCache(final int capacity) {
        this.capacity = capacity;
    }

    public Optional<V> get(final K key) {
        if (cache.containsKey(key)) {
            dll.delete(cache.get(key).getValue());
            dll.append(key);
            cache.get(key).setValue(dll.end().getPrev());

            return Optional.of(cache.get(key).getKey());
        }

        return Optional.empty();
    }

    public void set(final K key, final V value) {
        if (!cache.containsKey(key) && cache.size() == capacity) {
            cache.remove(dll.begin().getValue());
            dll.delete(dll.begin());
        } else if (cache.containsKey(key)) {
            dll.delete(cache.get(key).getValue());
        }
        dll.append(key);
        cache.put(key, ImmutablePair.of(value, dll.end().getPrev()));
    }

    public void delete(final K key) {
        if (cache.containsKey(key)) {
            dll.delete(cache.get(key).getRight());
            cache.remove(key);
        }
    }
}
