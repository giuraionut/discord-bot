package com.discordbot.babybot.reddit;

import com.discordbot.babybot.reddit.entities.reddit_post.RedditPost;
import com.discordbot.babybot.reddit.reddit_configuration.RedditInfoAPI;
import com.discordbot.babybot.reddit.reddit_configuration.RedditLoginInfo;
import com.discordbot.babybot.reddit.reddit_configuration.RedditRequestResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Data
public class RedditAPI {
    private RedditInfoAPI redditInfoAPI;
    private RedditLoginInfo redditLoginInfo;

    private String getAuthTokenAccount() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(redditInfoAPI.getApiUsername(), redditInfoAPI.getApiPassword());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.put("User-Agent",
                Collections.singletonList(redditInfoAPI.getUserAgent()));
        String body = "grant_type=password&username=" + redditLoginInfo.getUsername() + "&password=" + redditLoginInfo.getPassword();
        HttpEntity<String> request
                = new HttpEntity<>(body, headers);
        String authUrl = "https://www.reddit.com/api/v1/access_token";
        ResponseEntity<String> response = restTemplate.postForEntity(
                authUrl, request, String.class);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        try {
            map.putAll(mapper
                    .readValue(response.getBody(), new TypeReference<Map<String, Object>>() {
                    }));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return String.valueOf(map.get("access_token"));
    }

    private String getAuthTokenDefault() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(redditInfoAPI.getApiUsername(), redditInfoAPI.getApiPassword());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.put("User-Agent",
                Collections.singletonList(redditInfoAPI.getUserAgent()));
        String body = "grant_type=client_credentials";
        HttpEntity<String> request
                = new HttpEntity<>(body, headers);
        String authUrl = "https://www.reddit.com/api/v1/access_token";
        ResponseEntity<String> response = restTemplate.postForEntity(
                authUrl, request, String.class);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        try {
            map.putAll(mapper
                    .readValue(response.getBody(), new TypeReference<Map<String, Object>>() {
                    }));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return String.valueOf(map.get("access_token"));
    }

    private RedditRequestResponse selfSavedPosts(int limit, String after) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String authToken = getAuthTokenAccount();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.put("User-Agent",
                Collections.singletonList(redditInfoAPI.getUserAgent()));
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        String url = "https://oauth.reddit.com/user/"
                + redditLoginInfo.getUsername() + "/saved/new.json?limit=" + limit + "&after=" + after;

        ResponseEntity<String> response
                = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        RedditRequestResponse redditRequestResponse = new RedditRequestResponse();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(response.getBody());
            redditRequestResponse.setData(node.get("data").get("children"));
            redditRequestResponse.setAfter(node.get("data").get("after"));
        } catch (JsonProcessingException ex) {
            System.out.println(ex);
        }
        return redditRequestResponse;
    }

    public List<RedditPost> requestSelfSavedPosts(int chunkSize, int chunks) {
        List<RedditPost> posts = new ArrayList<>();
        if (chunks * chunkSize <= 100) {
            RedditRequestResponse savedPosts = selfSavedPosts(chunks * chunkSize, null);
            JsonNode childrens = savedPosts.getData();
            posts.addAll(new RedditPost().getPostFromNode(childrens));
        } else {
            RedditRequestResponse savedPosts = selfSavedPosts(chunkSize, null);
            JsonNode childrens = savedPosts.getData();
            String after = savedPosts.getAfter();
            posts.addAll(new RedditPost().getPostFromNode(childrens));
            var chunksObtained = 1;
            var noOfRequests = 1;
            while (!savedPosts.getAfter().equals("null")) {
                noOfRequests++; // increase the number of requests
                chunksObtained++; // increase the number of obtained chunks
                if (chunksObtained > chunks) { // if we got all the chunks we stop
                    break;
                }
                if (noOfRequests > 50) { // if we made more than 50 requests, take a break
                    try {
                        TimeUnit.SECONDS.sleep(60); // 1-minute break
                        noOfRequests = 0; // reset the no of requests
                    } catch (InterruptedException ex) {
                        System.out.println(ex);
                    }
                }
                savedPosts = selfSavedPosts(chunkSize, after);
                childrens = savedPosts.getData();
                after = savedPosts.getAfter();
                posts.addAll(new RedditPost().getPostFromNode(childrens));
            }
        }
        return posts;
    }

    private RedditRequestResponse getPostsFromSubReddit(String subRedditName, int limit, String after) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String authToken = getAuthTokenDefault();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.put("User-Agent",
                Collections.singletonList(redditInfoAPI.getUserAgent()));
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        String url = "https://oauth.reddit.com/r/"
                + subRedditName + "/new.json?limit=" + limit + "&after=" + after;

        ResponseEntity<String> response
                = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        RedditRequestResponse redditRequestResponse = new RedditRequestResponse();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(response.getBody());
            redditRequestResponse.setData(node.get("data").get("children"));
            redditRequestResponse.setAfter(node.get("data").get("after"));
        } catch (JsonProcessingException ex) {
            System.out.println(ex);
        }
        return redditRequestResponse;
    }


    public List<RedditPost> requestSubRedditPosts(String subRedditName, int chunkSize, int chunks) {
        List<RedditPost> posts = new ArrayList<>();
        if (chunks * chunkSize <= 100) {
            RedditRequestResponse postsFromSubreddit = getPostsFromSubReddit(subRedditName, chunks * chunkSize, null);
            JsonNode childrens = postsFromSubreddit.getData();
            posts.addAll(new RedditPost().getPostFromNode(childrens));
        } else {
            RedditRequestResponse postsFromSubreddit = getPostsFromSubReddit(subRedditName, chunkSize, null);
            JsonNode childrens = postsFromSubreddit.getData();
            String after = postsFromSubreddit.getAfter();
            posts.addAll(new RedditPost().getPostFromNode(childrens));
            var chunksObtained = 1;
            var noOfRequests = 1;
            while (!postsFromSubreddit.getAfter().equals("null")) {
                noOfRequests++; // increase the number of requests
                chunksObtained++; // increase the number of obtained chunks
                if (chunksObtained > chunks) { // if we got all the chunks we stop
                    break;
                }
                if (noOfRequests > 50) { // if we made more than 50 requests, take a break
                    try {
                        TimeUnit.SECONDS.sleep(60); // 1-minute break
                        noOfRequests = 0; // reset the no of requests
                    } catch (InterruptedException ex) {
                        System.out.println(ex);
                    }
                }
                postsFromSubreddit = getPostsFromSubReddit(subRedditName, chunkSize, after);
                childrens = postsFromSubreddit.getData();
                after = postsFromSubreddit.getAfter();
                posts.addAll(new RedditPost().getPostFromNode(childrens));
            }
        }
        return posts;
    }

}