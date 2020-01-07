package com.anizzzz.redisautocomplete.service;

import com.anizzzz.redisautocomplete.dto.AutocompleteData;
import com.anizzzz.redisautocomplete.repository.AutocompleteKeyRepository;
import com.anizzzz.redisautocomplete.repository.AutocompleteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;

@Service
public class AutocompleteServiceImpl implements AutocompleteRepository {
    private final double min = 0;
    private final double max = 5;
    private final int offset = 60;

    private final StringRedisTemplate stringRedisTemplate;
    private final AutocompleteKeyRepository keyRepository;

    @Autowired
    public AutocompleteServiceImpl(StringRedisTemplate stringRedisTemplate, AutocompleteKeyRepository keyRepository) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.keyRepository = keyRepository;
    }

    @Override
    public List<AutocompleteData> complete(String word) {
        return complete(word, min, max, offset);
    }

    @Override
    public List<AutocompleteData> complete(String word, double min, double max, int offset) {
        Assert.hasLength(word, "Word cannot be empty or null");

        String trimedWord = word.trim();
        int trimedWordLength = trimedWord.length();

        String key = keyRepository.getKey(trimedWord);

        List<AutocompleteData> autocompletes = new ArrayList<>();
        for (int i = trimedWordLength; i < offset; i++) {
            if (autocompletes.size() == offset) break;

            Set<ZSetOperations.TypedTuple<String>> rangeResultsWithScore = stringRedisTemplate
                    .opsForZSet()
                    .reverseRangeByScoreWithScores(key + i, min, max);
            if (rangeResultsWithScore.isEmpty()) continue;

            for (ZSetOperations.TypedTuple<String> typedTuple : rangeResultsWithScore) {
                if (autocompletes.size() == offset) break;

                String value = typedTuple.getValue();
                int minLength = Math.min(value.length(), trimedWordLength);
                if (!value.endsWith(DEFAULT_DELIMITER) || !value.startsWith(trimedWord.substring(0, minLength))) continue;
                autocompletes.add(new AutocompleteData(
                        value.replace(DEFAULT_DELIMITER, "").substring(value.lastIndexOf(SKILLDELIMETER) + SKILLDELIMETER.length()),
                        typedTuple.getScore().intValue()));
            }
        }
        Collections.sort(autocompletes);
        return autocompletes;
    }

    @Override
    public void add(String word) {
        keyRepository.create(word, DEFAULT_DELIMITER);
    }

    @Override
    public double incr(String word) {
        Double score = keyRepository.incr(word, DEFAULT_DELIMITER);
        return score!=null?score:0;
    }

    @Override
    public void clear(String key) {
        stringRedisTemplate.delete(key);
    }
}
