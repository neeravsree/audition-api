package com.audition.model;

import lombok.Data;

@Data
public class Comments {
     private int postId;
     private int id;
     private String name;
     private String email;
     private String body;
}
