package com.audition.constants;

public final class AuditionConstants {

    private AuditionConstants() {}

    public static final String POSTS_API_URL = "https://jsonplaceholder.typicode.com/posts/";
    public static final String COMMENTS_API_URL = "https://jsonplaceholder.typicode.com/comments";
    public static final String POST = "postId";
    public static final String COMMENTS = "comments";
    public static final String RESOURCE_NOT_FOUND = "Resource Not Found";
    public static final String POST_NOT_FOUND = "Post Not Found";
    public static final String POST_ID_NOT_FOUND = "The post with ID was not found";
    public static final String POST_ID_EMPTY = "Post ID must not be empty";
    public static final String JAEGER_URL = "http://localhost:14250";
    public static final String INJECT_RESPONSE_HEADERS = "inject-response-headers";
    public static final String TRACEID = "X-B3-TraceId";
    public static final String SPANID = "X-B3-SpanId";

}
