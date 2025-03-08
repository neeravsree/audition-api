package com.audition.model;

import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditionPostWithComments extends AuditionPost {

    private List<Comments> comments;

}
