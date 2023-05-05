package com.pandora.backend.config;

import com.github.benmanes.caffeine.cache.Ticker;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.github.benmanes.caffeine.cache.Caffeine.newBuilder;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MINUTES;

@Configuration
@EnableCaching
public class CachingConfiguration {
    @Bean
    public CacheManager cacheManager(Ticker ticker) {
        CaffeineCache bookCache = buildCache("book", ticker, 10, MINUTES);
        CaffeineCache bookCategoryCache = buildCache("book_category", ticker, 1, HOURS);
        CaffeineCache bookAuthorCache = buildCache("book_author", ticker, 1, HOURS);

        SimpleCacheManager manager = new SimpleCacheManager();

        manager.setCaches(Arrays.asList(bookCache, bookCategoryCache, bookAuthorCache));

        return manager;
    }

    private CaffeineCache buildCache(String name, Ticker ticker, long duration, TimeUnit unit) {
        return new CaffeineCache(name, newBuilder()
                .expireAfterWrite(duration, unit)
                .maximumSize(500_000)
                .executor(Executors.newCachedThreadPool())
                .ticker(ticker)
                .build());
    }

    @Bean
    public Ticker ticker() {
        return Ticker.systemTicker();
    }
}
