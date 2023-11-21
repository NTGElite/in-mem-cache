package cache;

import lombok.Builder;
import lrumap.LruCache;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

public class InMemCache<K, V> {
    private final long timeToLive;

    private final LruCache<K, CacheObject<V>> lruCache;

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private InMemCache() {
        timeToLive = 0;
        lruCache = new LruCache<>();
    }

    public InMemCache(final long timeToLive,
                      final long cacheTimerInterval,
                      final int size) {
        this.timeToLive = timeToLive;
        this.lruCache = new LruCache<>(size);

        if (this.timeToLive > 0 && cacheTimerInterval > 0) {
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            final Runnable timerInterval = this::cleanUp;

            scheduler.scheduleAtFixedRate(
                    timerInterval,
                    10,
                    this.timeToLive,
                    TimeUnit.SECONDS
            );
        }
    }

    @Builder
    public static <K, V> InMemCache<K, V> buildeCache(final long timeToLive,
                                                      final long cacheTimerInterval,
                                                      final int size) {
        return new InMemCache<>(timeToLive, cacheTimerInterval, size);
    }

    public void put(final K key,
                    final V value) {
        lock.writeLock().lock();

        try {
            lruCache.set(key, new CacheObject<>(value));
        } finally {
            lock.writeLock().unlock();
        }
    }

    public V get(final K key) {
        lock.readLock().lock();

        try {
            return lruCache.get(key).map(vCacheObject -> {
                vCacheObject.lastAccessed = System.currentTimeMillis();

                return vCacheObject.value;
            }).orElse(null);
        } finally {
            lock.readLock().unlock();
        }
    }

    private void cleanUp() {
        lock.writeLock().lock();

        try {
            long now = System.currentTimeMillis();

            List<K> keysToBeEvicted = lruCache.getCache().values().stream()
                    .filter(item -> item.getLeft().lastAccessed + now > timeToLive)
                    .map(item -> item.getRight().getValue())
                    .collect(Collectors.toUnmodifiableList());

            keysToBeEvicted.forEach(lruCache::delete);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private static class CacheObject<V> {
        private long lastAccessed = System.currentTimeMillis();
        private final V value;

        private CacheObject(final V value) {
            this.value = value;
        }
    }
}
