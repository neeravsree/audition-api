package com.audition.service;

import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostWithComments;
import com.audition.model.Comments;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditionService {

    @Autowired
    private AuditionIntegrationClient auditionIntegrationClient;


    public List<AuditionPost> getPosts() {

        return auditionIntegrationClient.getPosts();
    }

    public AuditionPost getPostById(final String postId) {

        return auditionIntegrationClient.getPostById(postId);
    }

    public AuditionPostWithComments getPostComments(final String postId) {

        return auditionIntegrationClient.getPostComments(postId);
    }

    public List<Comments> getComments(final String id) {
        return auditionIntegrationClient.getComments(id);
    }

}
