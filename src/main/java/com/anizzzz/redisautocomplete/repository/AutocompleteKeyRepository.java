package com.anizzzz.redisautocomplete.repository;

public interface AutocompleteKeyRepository {
    void create(String word, String identifier);

    Double incr(String word, String identifier);

    String getKey(String word);
}
