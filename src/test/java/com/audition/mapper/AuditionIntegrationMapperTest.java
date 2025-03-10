package com.audition.mapper;

import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostWithComments;
import com.audition.model.Comments;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuditionIntegrationMapperTest {

    private AuditionIntegrationMapper auditionIntegrationMapper;

    @BeforeEach
    void setUp() {
        auditionIntegrationMapper = new AuditionIntegrationMapper();
    }

    @Test
    void testMapperWithValidPostAndComments() {
        AuditionPost post = new AuditionPost();
        post.setId(1);
        post.setTitle("Test Title");
        post.setBody("Test Body");
        Comments comment1 = Mockito.mock(Comments.class);
        Comments comment2 = Mockito.mock(Comments.class);
        List<Comments> comments = List.of(comment1, comment2);
        AuditionPostWithComments result = auditionIntegrationMapper.mapper(post, comments);
        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals(1L, result.getId());
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Body", result.getBody());
        assertEquals(2, result.getComments().size());
    }

    @Test
    void testMapperWithNullComments() {
        AuditionPost post = new AuditionPost();
        post.setId(1);
        post.setTitle("Title");
        post.setBody("Body");
        List<Comments> comments = null;
        AuditionPostWithComments result = auditionIntegrationMapper.mapper(post, comments);
        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals(1L, result.getId());
        assertEquals("Title", result.getTitle());
        assertEquals("Body", result.getBody());
        assertEquals(0, result.getComments().size());
    }

    @Test
    void testMapperWithEmptyComments() {
        AuditionPost post = new AuditionPost();
        post.setId(2);
        post.setTitle("Empty Comments Title");
        post.setBody("Empty Comments Body");
        List<Comments> comments = List.of();
        AuditionPostWithComments result = auditionIntegrationMapper.mapper(post, comments);
        assertNotNull(result);
        assertEquals(2L, result.getUserId());
        assertEquals(2L, result.getId());
        assertEquals("Empty Comments Title", result.getTitle());
        assertEquals("Empty Comments Body", result.getBody());
        assertEquals(0, result.getComments().size());
    }

    @Test
    void testMapperWithNullTitleAndBody() {
        AuditionPost post = new AuditionPost();
        post.setId(3);
        post.setTitle(null);
        post.setBody(null);
        List<Comments> comments = List.of(new Comments());
        AuditionPostWithComments result = auditionIntegrationMapper.mapper(post, comments);
        assertNotNull(result);
        assertEquals(3L, result.getUserId());
        assertEquals(3L, result.getId());
        assertEquals("", result.getTitle());
        assertEquals("", result.getBody());
        assertEquals(1, result.getComments().size());
    }
}
