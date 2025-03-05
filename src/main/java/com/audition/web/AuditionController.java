package com.audition.web;

import com.audition.model.AuditionPost;
import com.audition.model.Comments;
import com.audition.service.AuditionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuditionController {

    @Autowired
    AuditionService auditionService;

    // TODO Add a query param that allows data filtering. The intent of the filter is at developers discretion.
    @RequestMapping(value = "/posts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AuditionPost> getPosts(@RequestParam(required = false) Integer userId) {

        // TODO Add logic that filters response data based on the query param
        List<AuditionPost> allPosts = auditionService.getPosts();
        if (userId != null) {
            allPosts = allPosts.stream().filter(post -> post.getUserId() == userId).toList();
        }
        return allPosts;
    }

    @RequestMapping(value = "/posts/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPosts(@PathVariable("id") final String postId) {

        if (!StringUtils.hasText(postId) || "null".equals(postId)) {
            return new ResponseEntity<>("Post ID must not be empty", HttpStatus.BAD_REQUEST);
        }
        final AuditionPost auditionPosts = auditionService.getPostById(postId);
        return ResponseEntity.ok(auditionPosts);
    }

    // TODO Add additional methods to return comments for each post. Hint: Check https://jsonplaceholder.typicode.com/
    @GetMapping("/posts/{postId}/comments")
    public List<Comments> getComments(@PathVariable("postId") final String postId) {

        return auditionService.getComments(postId);
    }

    @GetMapping("/posts/comments")
    public List<Comments> getCommentsForPost(@RequestParam("postId") final String postId) {

        return auditionService.getComments(postId);
    }

}
