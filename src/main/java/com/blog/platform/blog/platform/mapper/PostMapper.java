package com.blog.platform.blog.platform.mapper;

import com.blog.platform.blog.platform.dto.AuthorInfoDto;
import com.blog.platform.blog.platform.dto.PostDTO.PostRequest;
import com.blog.platform.blog.platform.dto.PostDTO.PostResponse;
import com.blog.platform.blog.platform.entity.Post;
import com.blog.platform.blog.platform.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "author.username", source = "user.username")
    @Mapping(target = "author.profileImageUrl", source = "user.profileImageUrl")
    @Mapping(target = "attachmentUrls", ignore = true)
    @Mapping(target = "likeCount", ignore = true)
    @Mapping(target = "dislikeCount", ignore = true)
    PostResponse toResponse(Post post);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "attachments", ignore = true)
    @Mapping(target = "likeCount", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "reactions", ignore = true)
    Post toEntity(PostRequest request);

    AuthorInfoDto toAuthorInfoDto(User user);
}
