package com.audition.web;

import com.audition.common.exception.SystemException;
import com.audition.constants.AuditionConstants;
import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostWithComments;
import com.audition.model.Comments;
import com.audition.service.AuditionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Tag(
    name = "CRUD REST APIs for Audition API",
    description = "CRUD REST APIs for Audition API"
)
@RestController
@Validated
@AllArgsConstructor
public class AuditionController {

    private AuditionService auditionService;

    @Operation(
        summary = "Get API for returning all posts or filter based on userId",
        description = "REST API for returning all posts or filter based on userId"
    )
    @RequestMapping(value = "/posts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AuditionPost> getPosts(@RequestParam(required = false) Integer userId) {

        List<AuditionPost> allPosts = auditionService.getPosts();
        return Optional.ofNullable(userId)
            .map(usrIdForPost -> allPosts.stream()
                .filter(post -> Optional.of(post.getUserId())
                    .map(userIdFromPost -> userIdFromPost.equals(usrIdForPost))
                    .orElse(false))
                .collect(Collectors.toList()))
            .orElse(allPosts);

    }

    @Operation(
        summary = "Get API for returning all posts based on postId",
        description = "REST API for returning all posts based on postId"
    )
    @RequestMapping(value = "/posts/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public AuditionPost getPostsById(@PathVariable("id")
                                     @Pattern(regexp = AuditionConstants.REGEX_PATTERN, message = AuditionConstants.ERROR_PATTERN) final String postId) {

        if (StringUtils.isBlank(postId)|| "null".equalsIgnoreCase(postId)) {
            throw new SystemException(AuditionConstants.POST_ID_EMPTY, AuditionConstants.POST_ID_EMPTY,
                HttpStatus.BAD_REQUEST.value());
        }
        return auditionService.getPostById(postId);
    }

    @Operation(
        summary = "Get API for returning all comments based on postId",
        description = "REST API for returning all commnets based on postId"
    )
    @GetMapping("/posts/{postId}/comments")
    public AuditionPostWithComments getPostComments(@PathVariable(AuditionConstants.POSTID) final String postId) {

        return auditionService.getPostComments(postId);
    }

    @Operation(
        summary = "Get API for returning all comments based on postId",
        description = "REST API for returning all comments based on postId"
    )
    @GetMapping("/comments")
    public List<Comments> getCommentsByPostId(@RequestParam(AuditionConstants.POSTID) final String postId) {

        return auditionService.getComments(postId);
    }

}
