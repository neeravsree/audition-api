package com.audition.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostWithComments;
import com.audition.model.Comments;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class AuditionServiceTest {

    @Mock
    private AuditionIntegrationClient auditionIntegrationClient;

    @InjectMocks
    private AuditionService auditionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPosts() {
        List<AuditionPost> expectedPosts = Arrays.asList(
            new AuditionPost(1, 1, "Title 1", "Description 1"),
            new AuditionPost(2, 2, "Title 2", "Description 2")
        );
        when(auditionIntegrationClient.getPosts()).thenReturn(expectedPosts);
        List<AuditionPost> actualPosts = auditionService.getPosts();
        assertEquals(expectedPosts.size(), actualPosts.size());
        assertEquals(expectedPosts.get(0).getId(), actualPosts.get(0).getId());
        assertEquals(expectedPosts.get(1).getTitle(), actualPosts.get(1).getTitle());
        verify(auditionIntegrationClient).getPosts();
    }

    @Test
    void testGetPostById() {
        String postId = "1";
        AuditionPost expectedPost = new AuditionPost(1, 1, "Title 1", "Description 1");
        when(auditionIntegrationClient.getPostById(postId)).thenReturn(expectedPost);
        AuditionPost actualPost = auditionService.getPostById(postId);
        assertNotNull(actualPost);
        assertEquals(expectedPost.getId(), actualPost.getId());
        assertEquals(expectedPost.getTitle(), actualPost.getTitle());
        verify(auditionIntegrationClient).getPostById(postId);
    }

    @Test
    void testGetComments() {
        String postId = "1";
        List<Comments> expectedComments = Arrays.asList(
            new Comments(1, 1, "xyz", "xyz@sydney.com", "comment 1"),
            new Comments(2, 1, "abc", "abc@sydney.com", "comment 2")
        );
        when(auditionIntegrationClient.getComments(postId)).thenReturn(expectedComments);
        List<Comments> actualComments = auditionService.getComments(postId);
        assertEquals(expectedComments.size(), actualComments.size());
        assertEquals(expectedComments.get(0).getId(), actualComments.get(0).getId());
        assertEquals(expectedComments.get(1).getBody(), actualComments.get(1).getBody());
        verify(auditionIntegrationClient).getComments(postId);
    }

    @Test
    void testGetCommentsForPost() {
        String postId = "1";
        List<Comments> expectedComments = Arrays.asList(
            new Comments(1, 1, "xyz", "xyz@sydney.com", "comment 1"),
            new Comments(2, 1, "abc", "abc@sydney.com", "comment 2")
        );
        when(auditionIntegrationClient.getComments(postId)).thenReturn(expectedComments);
        List<Comments> actualComments = auditionService.getComments(postId);
        assertEquals(expectedComments.size(), actualComments.size());
        assertEquals(expectedComments.get(0).getId(), actualComments.get(0).getId());
        assertEquals(expectedComments.get(1).getBody(), actualComments.get(1).getBody());
        verify(auditionIntegrationClient).getComments(postId);
    }

    @Test
    public void testGetPostComments() {
        String postId = "1";
        when(auditionIntegrationClient.getPostComments(anyString())).thenReturn(getAuditionPostWithComments());
        AuditionPostWithComments result = auditionService.getPostComments(postId);
        assertEquals(Integer.valueOf(postId), result.getId());
        assertEquals("Test Post", result.getTitle());
        assertEquals(2, result.getComments().size());
        assertEquals("Great post!", result.getComments().get(0).getBody());
        assertEquals("Nice post!", result.getComments().get(1).getBody());
    }

    private static AuditionPostWithComments getAuditionPostWithComments() {
        List<Comments> comments = Arrays.asList(
            new Comments(1, 1, "User1", "user1@example.com", "Great post!"),
            new Comments(1, 1, "User2", "user2@example.com", "Nice post!")
        );
        AuditionPostWithComments mockPostWithComments = new AuditionPostWithComments();
        mockPostWithComments.setId(1);
        mockPostWithComments.setUserId(1);
        mockPostWithComments.setTitle("Test Post");
        mockPostWithComments.setBody("This is a test post body.");
        mockPostWithComments.setComments(comments);
        return mockPostWithComments;
    }
}
