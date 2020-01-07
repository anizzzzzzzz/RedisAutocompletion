package com.anizzzz.redisautocomplete.controller;

import com.anizzzz.redisautocomplete.dto.AutocompleteData;
import com.anizzzz.redisautocomplete.dto.Skill;
import com.anizzzz.redisautocomplete.repository.AutocompleteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AutoRestController {
    private final AutocompleteRepository autocompleteRepository;

    @Autowired
    public AutoRestController(AutocompleteRepository autocompleteRepository) {
        this.autocompleteRepository = autocompleteRepository;
    }

    @PostMapping("/autocomplete")
    public List<String> autoComplete(@RequestBody Skill skill){
        if(!skill.getSkill().isEmpty())
            return autocompleteRepository.complete(skill.getSkill()).stream().map(AutocompleteData::getValue).collect(Collectors.toList());
        else
            return Collections.emptyList();
    }

    @PostMapping("/increment")
    public ResponseEntity<?> increment(@RequestBody Skill skill){
        return ResponseEntity.ok(autocompleteRepository.incr(skill.getSkill()));
    }
}
