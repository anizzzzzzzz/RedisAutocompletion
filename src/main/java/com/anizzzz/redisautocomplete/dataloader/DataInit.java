package com.anizzzz.redisautocomplete.dataloader;

import com.anizzzz.redisautocomplete.repository.AutocompleteRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DataInit implements ApplicationRunner {
    private final AutocompleteRepository autocompleteRepository;

    @Autowired
    public DataInit(AutocompleteRepository autocompleteRepository) {
        this.autocompleteRepository = autocompleteRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        readLines(System.getProperty("user.dir") + "/data/skill_dictionary.txt")
                .forEach(autocompleteRepository::add);
    }

    private List<String> readLines(String path) {
        try (FileInputStream inputStream = new FileInputStream(path)) {
            return Arrays.stream(IOUtils.toString(inputStream, "UTF-8").split("\n"))
                    .map(String::trim).collect(Collectors.toList());
        } catch (IOException ex) {
            return Collections.emptyList();
        }
    }
}
