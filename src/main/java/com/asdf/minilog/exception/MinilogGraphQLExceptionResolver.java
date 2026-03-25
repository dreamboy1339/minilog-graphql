package com.asdf.minilog.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

@Component
public class MinilogGraphQLExceptionResolver extends DataFetcherExceptionResolverAdapter {
  private static final Logger logger =
      LoggerFactory.getLogger(MinilogGraphQLExceptionResolver.class);

  @Override
  protected @Nullable GraphQLError resolveToSingleError(
      Throwable exception, DataFetchingEnvironment env) {
    logger.error(
        "Error occurred while fetching data at {}: {}",
        env.getExecutionStepInfo().getPath(),
        exception.getMessage(),
        exception);

    GraphQLError error;
    var handlerParameterPath = env.getExecutionStepInfo().getPath();

    if (exception instanceof ArticleNotFoundException
        || exception instanceof UserNotFoundException) {
      error =
          GraphqlErrorBuilder.newError()
              .message(exception.getMessage())
              .errorType(ErrorType.NOT_FOUND)
              .path(handlerParameterPath)
              .build();
    } else if (exception instanceof NotAuthorizedException) {
      error =
          GraphqlErrorBuilder.newError()
              .message(exception.getMessage())
              .errorType(ErrorType.UNAUTHORIZED)
              .path(handlerParameterPath)
              .build();
    } else if (exception instanceof IllegalArgumentException) {
      error =
          GraphqlErrorBuilder.newError()
              .message(exception.getMessage())
              .errorType(ErrorType.BAD_REQUEST)
              .path(handlerParameterPath)
              .build();
    } else {
      error =
          GraphqlErrorBuilder.newError()
              .message("Internal Server Error")
              .errorType(ErrorType.INTERNAL_ERROR)
              .path(handlerParameterPath)
              .build();
    }

    return error;
  }
}
