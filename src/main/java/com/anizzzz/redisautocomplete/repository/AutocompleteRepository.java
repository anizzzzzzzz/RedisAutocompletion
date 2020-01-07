package com.anizzzz.redisautocomplete.repository;

import com.anizzzz.redisautocomplete.dto.AutocompleteData;

import java.util.List;

public interface AutocompleteRepository {
    String DEFAULT_DELIMITER = "§";
    String SKILLDELIMETER = "____";

    List<AutocompleteData> complete(final String word);

    List<AutocompleteData> complete(final String word, final double min, final double max, final int offset);

    void add(final String word);

    double incr(final String word);

    void clear(final String key);
}
