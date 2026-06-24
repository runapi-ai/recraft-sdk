package ai.runapi.recraft.resources;

import ai.runapi.core.ClientOptions;
import ai.runapi.core.RequestOptions;
import ai.runapi.core.http.HttpTransport;
import ai.runapi.core.polling.TaskCreateResponse;
import ai.runapi.recraft.types.CompletedRemoveBackgroundResponse;
import ai.runapi.recraft.types.RemoveBackgroundParams;
import ai.runapi.recraft.types.RemoveBackgroundResponse;

/** Remove Background operations. */
public final class RemoveBackgroundResource extends RecraftResource {
  /** API endpoint path for remove background operations. */
  public static final String ENDPOINT = "/api/v1/recraft/remove_background";

  /** Creates a resource bound to the supplied transport and client options. */
  public RemoveBackgroundResource(HttpTransport transport, ClientOptions options) {
    super(transport, options, ENDPOINT);
  }

  /** Creates a remove background task. */
  public TaskCreateResponse create(RemoveBackgroundParams params) {
    return create(params, RequestOptions.none());
  }

  /** Creates a remove background task with per-request options. */
  public TaskCreateResponse create(RemoveBackgroundParams params, RequestOptions options) {
    return createTask(params.action(), params.toMap(), options);
  }

  /** Retrieves a remove background task by ID. */
  public RemoveBackgroundResponse get(String id) {
    return get(id, RequestOptions.none());
  }

  /** Retrieves a remove background task by ID with per-request options. */
  public RemoveBackgroundResponse get(String id, RequestOptions options) {
    return getTask(id, options, RemoveBackgroundResponse.class);
  }

  /** Creates a remove background task and polls until it completes. */
  public CompletedRemoveBackgroundResponse run(RemoveBackgroundParams params) {
    return run(params, RequestOptions.none());
  }

  /** Creates a remove background task with per-request options and polls until it completes. */
  public CompletedRemoveBackgroundResponse run(RemoveBackgroundParams params, RequestOptions options) {
    return runTask(params.action(), params.toMap(), options, RemoveBackgroundResponse.class, CompletedRemoveBackgroundResponse.class);
  }
}
