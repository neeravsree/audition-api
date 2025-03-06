package com.audition.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.audition.common.exception.SystemException;
import com.audition.constants.AuditionConstants;
import com.audition.model.AuditionPost;
import com.audition.model.Comments;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class AuditionIntegrationClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AuditionIntegrationClient auditionIntegrationClient;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPosts() {
        AuditionPost[] posts = new AuditionPost[]{
            new AuditionPost(1, 1, "Title 1", "Description 1"),
            new AuditionPost(2, 2, "Title 2", "Description 2")
        };
        when(restTemplate.getForObject(AuditionConstants.POSTS_API_URL, AuditionPost[].class))
            .thenReturn(posts);
        List<AuditionPost> result = auditionIntegrationClient.getPosts();
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(restTemplate).getForObject(AuditionConstants.POSTS_API_URL, AuditionPost[].class);
    }


    @Test
    void testGetPosts_ReturnsEmptyListWhenExceptionOccurs() {
        when(restTemplate.getForObject(AuditionConstants.POSTS_API_URL, AuditionPost[].class))
            .thenThrow(new RuntimeException("An error occurred"));
        List<AuditionPost> result = auditionIntegrationClient.getPosts();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    @Test
    void testGetPostsReturnsEmptyList() {
        when(restTemplate.getForObject(AuditionConstants.POSTS_API_URL, AuditionPost[].class))
            .thenReturn(null);
        List<AuditionPost> result = auditionIntegrationClient.getPosts();
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(restTemplate).getForObject(AuditionConstants.POSTS_API_URL, AuditionPost[].class);
    }

    @Test
    void testGetPostById() {
        String postId = "1";
        AuditionPost post = new AuditionPost(1, 1, "Title 1", "Description 1");
        String postUrl = AuditionConstants.POSTS_API_URL + postId;
        when(restTemplate.getForObject(postUrl, AuditionPost.class)).thenReturn(post);
        AuditionPost result = auditionIntegrationClient.getPostById(postId);
        assertNotNull(result);
        verify(restTemplate).getForObject(postUrl, AuditionPost.class);
    }

    @Test
    void testGetPostByIdThrowsSystemException() {
        String postId = "1";
        String postUrl = AuditionConstants.POSTS_API_URL + postId;
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND);
        when(restTemplate.getForObject(postUrl, AuditionPost.class)).thenThrow(exception);
        SystemException thrown = assertThrows(SystemException.class, () -> {
            auditionIntegrationClient.getPostById(postId);
        });
        assertEquals("Cannot find a Post with id 1", thrown.getMessage());
        verify(restTemplate).getForObject(postUrl, AuditionPost.class);
    }

    @Test
    void testGetPostById_ThrowsSystemExceptionForOtherHttpClientErrorException() {
        String postId = "1";
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request");
        when(restTemplate.getForObject(anyString(), eq(AuditionPost.class)))
            .thenThrow(exception);
        SystemException thrownException = assertThrows(SystemException.class, () -> {
            auditionIntegrationClient.getPostById(postId);
        });
        assertEquals("400 Bad Request", thrownException.getMessage());
        assertEquals(400, thrownException.getStatusCode());
    }


    @Test
    void testGetComments() {
        String postId = "1";
        Comments[] comments = new Comments[]{
            new Comments(1, 1, "xyz", "xyz@sydney.com", "comment 1"),
            new Comments(2, 1, "abc", "abc@sydney.com", "comment 2")
        };
        String commentUrl = AuditionConstants.POSTS_API_URL + postId + "/comments";
        when(restTemplate.getForObject(commentUrl, Comments[].class)).thenReturn(comments);

        List<Comments> result = auditionIntegrationClient.getComments(postId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(restTemplate).getForObject(commentUrl, Comments[].class);
    }

    @Test
    void testGetCommentsForPost() {
        String postId = "1";
        Comments[] comments = new Comments[]{
            new Comments(1, 1, "xyz", "xyz@sydney.com", "comment 1")
        };
        String commentUrl = AuditionConstants.POSTS_API_URL + "comments?postId=" + postId;
        when(restTemplate.getForObject(commentUrl, Comments[].class)).thenReturn(comments);
        List<Comments> result = auditionIntegrationClient.getCommentsForPost(postId);
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(restTemplate).getForObject(commentUrl, Comments[].class);
    }

    @Test
    void testGetComments_ThrowsSystemException_WhenHttpClientErrorExceptionIsNotFound() {
        String postId = "1";
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not Found");
        when(restTemplate.getForObject(anyString(), eq(Comments[].class)))
            .thenThrow(exception);
        SystemException thrownException = assertThrows(SystemException.class, () -> {
            auditionIntegrationClient.getComments(postId);
        });
        assertEquals("Cannot find a comments with postId 1", thrownException.getMessage());
        assertEquals(404, thrownException.getStatusCode());
    }

    @Test
    void testGetComments_ThrowsSystemException_WhenHttpClientErrorExceptionIsOtherError() {
        String postId = "1";
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request");
        when(restTemplate.getForObject(anyString(), eq(Comments[].class)))
            .thenThrow(exception);
        SystemException thrownException = assertThrows(SystemException.class, () -> {
            auditionIntegrationClient.getComments(postId);
        });
        assertEquals("400 Bad Request", thrownException.getMessage());
        assertEquals(400, thrownException.getStatusCode());
    }

    @Test
    void testGetCommentsForPost_ThrowsSystemException_WhenHttpClientErrorExceptionIsOtherError() {
        String postId = "1";
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request");

        when(restTemplate.getForObject(anyString(), eq(Comments[].class)))
            .thenThrow(exception);

        SystemException thrownException = assertThrows(SystemException.class, () -> {
            auditionIntegrationClient.getCommentsForPost(postId);
        });

        assertEquals("400 Bad Request", thrownException.getMessage());
        assertEquals(400, thrownException.getStatusCode());
    }


    @Test
    void testGetCommentsForPostThrowsSystemException() {
        String postId = "1";
        String commentUrl = AuditionConstants.POSTS_API_URL + "comments?postId=" + postId;
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND);
        when(restTemplate.getForObject(commentUrl, Comments[].class)).thenThrow(exception);

        SystemException thrown = assertThrows(SystemException.class, () -> {
            auditionIntegrationClient.getCommentsForPost(postId);
        });

        assertEquals("Cannot find a comments with postId 1", thrown.getMessage());
        verify(restTemplate).getForObject(commentUrl, Comments[].class);
    }
}
