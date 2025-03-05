package com.audition.integration;

import com.audition.common.exception.SystemException;
import com.audition.constants.AuditionConstants;
import com.audition.model.AuditionPost;
import com.audition.model.Comments;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class AuditionIntegrationClient {

    private static final Logger logger = LoggerFactory.getLogger(AuditionIntegrationClient.class);

    @Autowired
    private RestTemplate restTemplate;

    public List<AuditionPost> getPosts() {
        // TODO make RestTemplate call to get Posts from https://jsonplaceholder.typicode.com/posts

        try {
            AuditionPost[] posts = restTemplate.getForObject(AuditionConstants.POSTS_API_URL, AuditionPost[].class);

            return posts != null ? Arrays.asList(posts) : Collections.emptyList();
        } catch (Exception e) {
            logger.info("Error occured{}", String.valueOf(e));
            return Collections.emptyList();
        }
    }

    public AuditionPost getPostById(final String id) {
        // TODO get post by post ID call from https://jsonplaceholder.typicode.com/posts/
        String postUrl = UriComponentsBuilder.fromUri(URI.create(AuditionConstants.POSTS_API_URL))
            .path(id)
            .buildAndExpand(id).toUriString();
        System.out.println("posturl" + postUrl);
        try {
            return restTemplate.getForObject(postUrl, AuditionPost.class);
        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("Cannot find a Post with id " + id, "Resource Not Found", 404);
            } else {
                // TODO Find a better way to handle the exception so that the original error message is not lost. Feel free to change this function.
                throw new SystemException(e.getMessage(), e.getMessage(), e.getStatusCode().value());
            }
        }
    }

    // TODO Write a method GET comments for a post from https://jsonplaceholder.typicode.com/posts/{postId}/comments - the comments must be returned as part of the post.
    public List<Comments> getComments(final String id) {

        String commentUrl = UriComponentsBuilder.fromHttpUrl(AuditionConstants.POSTS_API_URL + "{id}" + "/comments")
            .buildAndExpand(id).toUriString();
        try {
            Comments[] comments = restTemplate.getForObject(commentUrl, Comments[].class);
            return Optional.ofNullable(comments).map(Arrays::asList).orElse(Collections.emptyList());
        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("Cannot find a Post with id " + id, "Resource Not Found", 404);
            } else {
                throw new SystemException(e.getMessage(), e.getMessage(), e.getStatusCode().value());
            }
        }
    }

    public List<Comments> getCommentsForPost(final String id) {

        String commentUrl = UriComponentsBuilder.fromUri(URI.create(AuditionConstants.POSTS_API_URL))
            .path("comments")
            .queryParam("postId", id)
            .buildAndExpand(id).toUriString();
        System.out.println("commentUrl" + commentUrl);
        try {
            Comments[] comments = restTemplate.getForObject(commentUrl, Comments[].class);
            return Optional.ofNullable(comments).map(Arrays::asList).orElse(Collections.emptyList());
        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("Cannot find a Post with id " + id, "Resource Not Found", 404);
            } else {
                throw new SystemException(e.getMessage(), e.getMessage(), e.getStatusCode().value());
            }
        }
    }

}
// TODO write a method. GET comments for a particular Post from https://jsonplaceholder.typicode.com/comments?postId={postId}.
// The comments are a separate list that needs to be returned to the API consumers. Hint: this is not part of the AuditionPost pojo.


