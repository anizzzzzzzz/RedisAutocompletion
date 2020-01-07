package com.anizzzz.redisautocomplete.service;

import com.anizzzz.redisautocomplete.repository.AutocompleteKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Service
public class AutocompleteKeyServiceImpl implements AutocompleteKeyRepository {
    private static final String DELIMITER = ":";
    private static final String PREFIX = "autocomplete" + DELIMITER;
    private static final String SKILLDELIMETER = "____";

    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public AutocompleteKeyServiceImpl(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void create(String word, String identifier) {
        Assert.hasLength(word, "Word cannot be empty or null");
        String trimmedWord = word.trim();
        for(String ngram:createNGram(trimmedWord)){
            String generatedKey = generateKey(getPrefix(ngram), ngram.length());
            if(!hasKey(generatedKey, ngram + SKILLDELIMETER + trimmedWord, identifier)) {
                if(ngram.equalsIgnoreCase(trimmedWord))
                    stringRedisTemplate.opsForZSet()
                            .add(generatedKey, createValue(trimmedWord, ngram) + identifier, 1);
                else
                    stringRedisTemplate.opsForZSet()
                            .add(generatedKey, createValue(trimmedWord, ngram) + identifier, 0);
                /*for(int index = 1; index<ngram.length(); index++)
                    stringRedisTemplate.opsForZSet()
                            .add(generatedKey, createValue(trimmedWord, ngram.substring(0, index)), 0);*/
            }
        }
    }

    @Override
    public Double incr(final String word, final String identifier) {
        Assert.hasLength(word, "Word cannot be empty or null");
        String trimmedWord =  word.trim();
        for(String ngram:createNGram(trimmedWord)) {
            String generatedKey = generateKey(getPrefix(ngram), ngram.length());
            stringRedisTemplate.opsForZSet().incrementScore(generatedKey, createValue(trimmedWord, ngram) + identifier, 1);
        }
        return stringRedisTemplate.opsForZSet()
                .score(generateKey(getPrefix(trimmedWord).toLowerCase(), trimmedWord.length()),
                        createValue(trimmedWord, trimmedWord.toLowerCase()) + identifier);
    }

    @Override
    public String getKey(String word) {
        String firstLetter = getPrefix(word);
        return generateKeyWithoutLength(firstLetter);
    }

    private String createValue(String original, String ngramWord){
        return ngramWord + SKILLDELIMETER + original;
    }

    private String generateKey(final String firstLetter, int length){
        return generateKeyWithoutLength(firstLetter) + length;
    }

    private String generateKeyWithoutLength(final String firstLetter){
        return PREFIX + firstLetter + DELIMITER;
    }

    private boolean hasKey(final String key, final String word, final String identifier){
        Double exist = stringRedisTemplate.opsForZSet().score(key, word.trim() + identifier);
        return exist != null;
    }

    private String getPrefix(final String word){
        return word.substring(0, 1);
    }

    private List<String> createNGram(String line){
        String[] arr = line.split(" ");
        List<String> ngram = new ArrayList<>();
        if(arr.length > 1){
            for(int i = 0; i< arr.length; i++){
                StringBuilder curr = new StringBuilder(arr[i]);
                for(int j=i+1; j<arr.length; j++){
                    curr.append(" ").append(arr[j]);
                }
                ngram.add(curr.toString().toLowerCase());
            }
        }
        else{
            ngram.add(line.toLowerCase());
        }
        return ngram;
    }
}
