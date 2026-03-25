package com.asdf.minilog.graphql.response;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
public class FollowResponse {
    @NonNull
    Long followerId;

    @NonNull
    private Long followeeId;
}
