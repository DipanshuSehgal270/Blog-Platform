package com.blog.platform.blog.platform.mapper;

import com.blog.platform.blog.platform.dto.CommentDTO.CommentResponse;
import com.blog.platform.blog.platform.entity.Comment;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "profileImageUrl", source = "user.profileImageUrl")
//    @Mapping(target = "isReply", expression = "java(comment.getParent()!=null)")
    @Mapping(target = "likes", ignore = true)
    @Mapping(target = "dislikes", ignore = true)
    CommentResponse toResponse(Comment comment);

    @InheritInverseConfiguration
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "post", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
//    @Mapping(target = "isReply", ignore = true)
    Comment toEntity(CommentResponse response);
}