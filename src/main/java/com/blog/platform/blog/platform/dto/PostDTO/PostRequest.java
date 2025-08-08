package com.blog.platform.blog.platform.dto.PostDTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PostRequest {

    private String title;
    private String content;

}
