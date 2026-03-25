package com.asdf.minilog.graphql.response;

import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class ArticleResponse {
  @NonNull private Long articleId;

  @NonNull private String content;

  @NonNull private Long authorId;

  @NonNull private String authorName;

  @NonNull private OffsetDateTime createdAt;
}
