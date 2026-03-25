package com.asdf.minilog.graphql;

import com.asdf.minilog.dto.ArticleResponseDto;
import com.asdf.minilog.dto.FollowResponseDto;
import com.asdf.minilog.dto.UserRequestDto;
import com.asdf.minilog.dto.UserResponseDto;
import com.asdf.minilog.graphql.input.CreateArticleInput;
import com.asdf.minilog.graphql.input.CreateUserInput;
import com.asdf.minilog.graphql.input.UpdateArticleInput;
import com.asdf.minilog.graphql.input.UpdateUserInput;
import com.asdf.minilog.graphql.response.ArticleResponse;
import com.asdf.minilog.graphql.response.FollowResponse;
import com.asdf.minilog.graphql.response.UserResponse;
import com.asdf.minilog.security.MinilogUserDetails;
import com.asdf.minilog.service.ArticleService;
import com.asdf.minilog.service.FollowService;
import com.asdf.minilog.service.UserService;
import com.asdf.minilog.util.DtoGraphqlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
public class GraphQLMutationController {
    private final ArticleService articleService;
    private final FollowService followService;
    private final UserService userService;

    @Autowired
    public GraphQLMutationController(ArticleService articleService, FollowService followService, UserService userService) {
        this.articleService = articleService;
        this.followService = followService;
        this.userService = userService;
    }

    private MinilogUserDetails getCurrentUser() {
        return (MinilogUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @MutationMapping
    public ArticleResponse createArticle(@Argument CreateArticleInput input) {
        MinilogUserDetails userDetails = getCurrentUser();
        ArticleResponseDto articleResponse = articleService.createArticle(input.getContent(), userDetails.getId());
        return DtoGraphqlMapper.toGraphql(articleResponse);
    }

    @MutationMapping
    public ArticleResponse updateArticle(@Argument UpdateArticleInput input) {
        MinilogUserDetails userDetails = getCurrentUser();
        ArticleResponseDto articleResponse = articleService.updateArticle(userDetails.getId(), input.getArticleId(), input.getContent());
        return DtoGraphqlMapper.toGraphql(articleResponse);
    }

    @MutationMapping
    public Boolean deleteArticle(@Argument Long articleId) {
        MinilogUserDetails userDetails = getCurrentUser();
        articleService.deleteArticle(userDetails.getId(), articleId);
        return true;
    }

    @MutationMapping
    public FollowResponse follow(@Argument Long followeeId) {
        MinilogUserDetails userDetails = getCurrentUser();
        FollowResponseDto followResponseDto = followService.follow(userDetails.getId(), followeeId);
        return DtoGraphqlMapper.toGraphql(followResponseDto);
    }

    @MutationMapping
    public Boolean unfollow(@Argument Long followeeId) {
        MinilogUserDetails userDetails = getCurrentUser();
        followService.unfollow(userDetails.getId(), followeeId);
        return true;
    }

    @MutationMapping
    public UserResponse createUser(@Argument CreateUserInput input) {
        UserRequestDto request = UserRequestDto.builder()
            .username(input.getUsername())
            .password(input.getPassword())
            .build();
        UserResponseDto createdUser = userService.createUser(request);
        return DtoGraphqlMapper.toGraphql(createdUser);
    }

    @MutationMapping
    public UserResponse updateUser(@Argument UpdateUserInput input) {
        MinilogUserDetails userDetails = getCurrentUser();
        UserRequestDto request = UserRequestDto.builder()
            .username(input.getUsername())
            .password(input.getPassword())
            .build();
        UserResponseDto updatedUser = userService.updateUser(userDetails, input.getUserId(), request);
        return DtoGraphqlMapper.toGraphql(updatedUser);
    }

    @MutationMapping
    public Boolean deleteUser(@Argument Long userId) {
        userService.deleteUser(userId);
        return true;
    }
}
