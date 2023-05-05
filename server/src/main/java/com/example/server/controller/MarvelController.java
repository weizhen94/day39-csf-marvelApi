package com.example.server.controller;

import com.example.server.model.Comment;
import com.example.server.model.MarvelCharacter;
import com.example.server.service.MarvelApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MarvelController {

    @Autowired
    private MarvelApiService marvelApiService;

    @GetMapping("/characters")
    public List<MarvelCharacter> searchCharacters(@RequestParam(value = "nameStartsWith", required = false) String nameStartsWith) {
        return marvelApiService.searchCharacters(nameStartsWith);
    }

    @GetMapping("/character/{characterId}")
    public MarvelCharacter getCharacter(@PathVariable("characterId") int characterId) {
        return marvelApiService.getCharacter(characterId);
    }

    @GetMapping("/character/{characterId}/comments")
    public List<Comment> getComments(@PathVariable("characterId") int characterId) {
        return marvelApiService.getComments(characterId);
    }

    @PostMapping("/character/{characterId}/comments")
    public Comment addComment(@PathVariable("characterId") int characterId, @RequestBody Comment comment) {
        return marvelApiService.addComment(characterId, comment);
    }
}

