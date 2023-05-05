package com.example.server.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.example.server.model.Comment;
import com.example.server.model.MarvelCharacter;
import com.example.server.repository.CommentRepository;

@Service
public class MarvelApiService {

    private static final String BASE_URL = "https://gateway.marvel.com/v1/public/characters";
    private static final String PUBLIC_KEY = "a6f3079176d1e0ddb0c27502ee475e6c";
    private static final String PRIVATE_KEY = "763ccc0cf8ff5c1db6f2bef93ef2c9d1d5da4b79";

    @Autowired
    private RedisTemplate<String, MarvelCharacter> redisTemplate;

    @Autowired
    private CommentRepository commentRepository;

    public List<MarvelCharacter> searchCharacters(String nameStartsWith) {
        String ts = Long.toString(Instant.now().toEpochMilli());
        String hash = generateHash(ts, PRIVATE_KEY, PUBLIC_KEY);
        
        String url = BASE_URL + "?apikey=" + PUBLIC_KEY + "&ts=" + ts + "&hash=" + hash;
        if (nameStartsWith != null) {
            url += "&nameStartsWith=" + nameStartsWith;
        }
        
        RestTemplate restTemplate = new RestTemplate();
        String jsonResponse = restTemplate.getForObject(url, String.class);

        // Deserialize the response into a list of MarvelCharacter objects
        List<MarvelCharacter> characters = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode resultsNode = rootNode.path("data").path("results");
            for (JsonNode characterNode : resultsNode) {
                MarvelCharacter character = objectMapper.treeToValue(characterNode, MarvelCharacter.class);
                characters.add(character);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error parsing response", e);
        }

        // Save characters to Redis and set expiration to 1 hour
        ValueOperations<String, MarvelCharacter> valueOps = redisTemplate.opsForValue();
        for (MarvelCharacter character : characters) {
            valueOps.set("character_" + character.getId(), character, 1, TimeUnit.HOURS);
        }

        return characters;
    }

    public MarvelCharacter getCharacter(int characterId) {
        String cacheKey = "character_" + characterId;
    
        // Check if character exists in Redis cache
        if (redisTemplate.hasKey(cacheKey)) {
            System.out.println("Using Redis");
            MarvelCharacter character = redisTemplate.opsForValue().get(cacheKey);
            character.setComments(getComments(characterId)); // Fetch comments and set them in the character object
            return character;
        }

        System.out.println("Using API");

        String ts = Long.toString(Instant.now().toEpochMilli());
        String hash = generateHash(ts, PRIVATE_KEY, PUBLIC_KEY);
    
        String url = BASE_URL + "/" + characterId + "?apikey=" + PUBLIC_KEY + "&ts=" + ts + "&hash=" + hash;
        RestTemplate restTemplate = new RestTemplate();
        String jsonResponse = restTemplate.getForObject(url, String.class);
    
        MarvelCharacter character;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode characterNode = rootNode.path("data").path("results").get(0);
            character = objectMapper.treeToValue(characterNode, MarvelCharacter.class);
        } catch (IOException e) {
            throw new RuntimeException("Error parsing response", e);
        }
    
        // Save character to Redis cache and set expiration to 1 hour
        redisTemplate.opsForValue().set(cacheKey, character, 1, TimeUnit.HOURS);
    
        character.setComments(getComments(characterId)); // Fetch comments and set them in the character object
        return character;
    }

    public List<Comment> getComments(int characterId) {
        System.out.println("Comment from Mongodb");
        return commentRepository.findTop10ByCharacterIdOrderByTimestampDesc(characterId);
    }

    public Comment addComment(int characterId, Comment comment) {
        comment.setCharacterId(characterId);
        System.out.println("Saved Comment");
        return commentRepository.save(comment);
    }

    private static String generateHash(String ts, String privateKey, String publicKey) {
        try {
            String valueToHash = ts + privateKey + publicKey;
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(valueToHash.getBytes(StandardCharsets.UTF_8));
    
            try (Formatter formatter = new Formatter()) {
                for (byte b : hashBytes) {
                    formatter.format("%02x", b);
                }
                return formatter.toString();
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating hash", e);
        }
    }
}