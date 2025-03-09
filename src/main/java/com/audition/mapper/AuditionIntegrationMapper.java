package com.audition.mapper;

import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostWithComments;
import com.audition.model.Comments;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class AuditionIntegrationMapper {

    public AuditionPostWithComments mapper(AuditionPost post, List<Comments> comments){
        AuditionPostWithComments postWithComments = new AuditionPostWithComments();
        Optional.ofNullable(post).ifPresent(p -> {
            postWithComments.setUserId(Optional.of(p.getId()).orElse(null));
            postWithComments.setId(p.getId());
            postWithComments.setTitle(Optional.ofNullable(p.getTitle()).orElse(""));
            postWithComments.setBody(Optional.ofNullable(p.getBody()).orElse(""));
        });
        postWithComments.setComments(Optional.ofNullable(comments).orElse(List.of()));

        return postWithComments;
    }
}
