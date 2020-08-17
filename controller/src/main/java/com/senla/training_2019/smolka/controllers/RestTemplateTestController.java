package com.senla.training_2019.smolka.controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/redir")
public class RestTemplateTestController {

    @PostMapping
    public ResponseEntity<String> postMethod(@RequestParam(value = "id") Integer id, @RequestParam(value = "body") String body, @RequestParam(value = "title") String title) {
        RestTemplate restTemplate = new RestTemplate();
        String rBody = "{ \"title\" : \""+title+"\", \"userId\" : "+id+", \"body\" : \""+body+"\" }";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> request = new HttpEntity<>(rBody, headers);
        String resourceUrl = "https://jsonplaceholder.typicode.com/posts";
        return restTemplate.postForEntity(resourceUrl, request, String.class);
    }

    @GetMapping
    public ResponseEntity<String> getMapping(@RequestParam(value = "id") Integer id) {
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "https://jsonplaceholder.typicode.com/todos/"+id.toString();
        return restTemplate.getForEntity(resourceUrl, String.class);
    }
}
