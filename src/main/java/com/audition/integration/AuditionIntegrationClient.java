package com.audition.integration;

import com.audition.common.exception.SystemException;
import com.audition.constants.AuditionConstants;
import com.audition.mapper.AuditionIntegrationMapper;
import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostWithComments;
import com.audition.model.Comments;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class AuditionIntegrationClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    AuditionIntegrationMapper auditionIntegrationMapper;

    public List<AuditionPost> getPosts() {
            AuditionPost[] posts = makeRestCall(AuditionConstants.POSTS_API_URL, AuditionPost[].class);
            return posts != null ? Arrays.asList(posts) : Collections.emptyList();
    }

    public AuditionPost getPostById(final String postId) {
        String postUrl = UriComponentsBuilder.fromUri(URI.create(AuditionConstants.POSTS_API_URL))
            .path(postId)
            .buildAndExpand(postId).toUriString();
        return makeRestCall(postUrl, AuditionPost.class);
    }

    public AuditionPostWithComments getPostComments(final String postId) {
        String commentUrl = UriComponentsBuilder.fromHttpUrl(AuditionConstants.POSTS_API_URL + "{id}/" + AuditionConstants.COMMENTS)
            .buildAndExpand(postId).toUriString();
        AuditionPost auditionPost=getPostById(postId);
        Optional.ofNullable(auditionPost)
            .orElseThrow(() -> new SystemException(AuditionConstants.POST_NOT_FOUND, AuditionConstants.POST_ID_NOT_FOUND + postId , HttpStatus.NOT_FOUND.value()));
        Comments[] comments = makeRestCall(commentUrl, Comments[].class);
        List<Comments> commentsForPost=Optional.ofNullable(comments).map(Arrays::asList).orElse(Collections.emptyList());
        return auditionIntegrationMapper.mapper(auditionPost,commentsForPost);

    }

    public List<Comments> getComments(final String id) {
        String commentUrl = UriComponentsBuilder.fromUri(URI.create(AuditionConstants.COMMENTS_API_URL))
            .queryParam(AuditionConstants.POSTID, id)
            .buildAndExpand(id).toUriString();
        Comments[] comments = makeRestCall(commentUrl, Comments[].class);
        return Optional.ofNullable(comments).map(Arrays::asList).orElse(Collections.emptyList());
    }

    private <T> T makeRestCall(String url, Class<T> responseType) {
        try {
            return restTemplate.getForObject(url, responseType);
        } catch (final HttpClientErrorException e) {
            handleHttpClientErrorException(e);
        }
        return null;
    }

    private void handleHttpClientErrorException(HttpClientErrorException e) {
        if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new SystemException(AuditionConstants.RESOURCE_NOT_FOUND, e.getMessage(), e.getStatusCode().value());
        } else {
            throw new SystemException(e.getMessage(), e.getMessage(), e.getStatusCode().value());
        }
    }
}


