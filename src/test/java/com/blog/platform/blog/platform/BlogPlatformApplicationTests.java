package com.blog.platform.blog.platform;

import com.blog.platform.blog.platform.dto.CommentResponse;
import com.blog.platform.blog.platform.entity.Comment;
import com.blog.platform.blog.platform.entity.User;
import com.blog.platform.blog.platform.mapper.CommentMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BlogPlatformApplicationTests {

//	@Test
//	void testMapping()
//	{
//		Comment comment = new Comment();
//		comment.setContent("Nice post!");
//		comment.setUser(new User("john", "john.jpg"));
//
//		CommentResponse dto = CommentMapper.toResponse(comment);
//
//		assertEquals("Nice post!", dto.getContent());
//		assertEquals("john", dto.getUsername());
//	}

}
