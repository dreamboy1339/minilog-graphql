package com.asdf.minilog.graphql.response;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class UserResponse {
    @NonNull
    private Long id;
    @NonNull private String username;
}
