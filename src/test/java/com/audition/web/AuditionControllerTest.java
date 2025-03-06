package com.audition.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.audition.model.AuditionPost;
import com.audition.model.Comments;
import com.audition.service.AuditionService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AuditionControllerTest {

    @Mock
    private AuditionService auditionService;

    @InjectMocks
    private AuditionController auditionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetPostsWithUserIdFilter() {
        List<AuditionPost> posts = Arrays.asList(
            new AuditionPost(1, 1, "Title 1", "Description 1"),
            new AuditionPost(2, 1, "Title 2", "Description 2")
        );
        when(auditionService.getPosts()).thenReturn(posts);
        List<AuditionPost> postsResult = auditionController.getPosts(1);
        assertEquals(1, postsResult.size());
        assertEquals(1, postsResult.get(0).getId());
        verify(auditionService).getPosts();
    }

    @Test
    void testGetPostByIdValid() {
        String postId = "1";
        AuditionPost post = new AuditionPost(1, 1, "Title 1", "Description 1");
        when(auditionService.getPostById(postId)).thenReturn(post);

        ResponseEntity<?> response = auditionController.getPosts(postId);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        AuditionPost returnedPost = (AuditionPost) response.getBody();
        assertNotNull(returnedPost);
        assertEquals(postId, String.valueOf(returnedPost.getId()));
        verify(auditionService).getPostById(postId);
    }

    @Test
    void testGetPostByIdInvalid() {
        String postId = "null";
        String errorMessage = "Post ID must not be empty";

        ResponseEntity<?> response = auditionController.getPosts(postId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }

    @Test
    void testGetCommentsForPost() {
        String postId = "1";
        List<Comments> comments = Arrays.asList(
            new Comments(1, 1, "User1", "user1@example.com", "Great post!"),
            new Comments(2, 1, "User2", "user2@example.com", "Nice post!")
        );
        when(auditionService.getComments(postId)).thenReturn(comments);

        List<Comments> commentsResult = auditionController.getComments(postId);

        assertEquals(2, commentsResult.size());
        assertEquals("User1", commentsResult.get(0).getName());
        assertEquals("User2", commentsResult.get(1).getName());
        verify(auditionService).getComments(postId);
    }

    @Test
    void testGetCommentsForPostWithRequestParam() {
        String postId = "1";
        List<Comments> comments = Arrays.asList(
            new Comments(1, 1, "User1", "user1@example.com", "Great post!"),
            new Comments(2, 1, "User2", "user2@example.com", "Nice post!")
        );
        when(auditionService.getComments(postId)).thenReturn(comments);
        List<Comments> commentsResult = auditionController.getCommentsForPost(postId);
        assertEquals(2, commentsResult.size());
        assertEquals("User1", commentsResult.get(0).getName());
        assertEquals("User2", commentsResult.get(1).getName());
        verify(auditionService).getComments(postId);
    }
}
