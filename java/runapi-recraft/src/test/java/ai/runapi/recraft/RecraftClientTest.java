package ai.runapi.recraft;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ai.runapi.core.RequestOptions;
import ai.runapi.core.errors.ValidationException;
import ai.runapi.core.http.HttpRequest;
import ai.runapi.core.http.HttpResponse;
import ai.runapi.core.http.HttpTransport;
import ai.runapi.core.http.JsonRequestBody;
import ai.runapi.core.json.Json;
import ai.runapi.recraft.types.CompletedRemoveBackgroundResponse;
import ai.runapi.recraft.types.RemoveBackgroundResponse;
import ai.runapi.recraft.types.CompletedRemoveBackgroundResponse;
import ai.runapi.recraft.types.CompletedUpscaleImageResponse;
import ai.runapi.recraft.types.RemoveBackgroundModel;
import ai.runapi.recraft.types.RemoveBackgroundParams;
import ai.runapi.recraft.types.RemoveBackgroundResponse;
import ai.runapi.recraft.types.UpscaleImageModel;
import ai.runapi.recraft.types.UpscaleImageParams;
import ai.runapi.recraft.types.UpscaleImageResponse;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.ByteArrayOutputStream;
import java.time.Duration;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class RecraftClientTest {
  @Test
  void builderCreatesClientAndUniversalResources() {
    RecraftClient client = RecraftClient.builder().apiKey("sk-test").build();

    assertNotNull(client.removeBackground());
    assertNotNull(client.files());
    assertNotNull(client.account());
  }

  @Test
  void openValueClassesSerializeAsScalarStrings() throws Exception {
    String json = Json.mapper().writeValueAsString(new RemoveBackgroundModel("recraft-remove-background"));

    assertEquals("\"recraft-remove-background\"", json);
    assertEquals(new RemoveBackgroundModel("recraft-remove-background"), Json.mapper().readValue(json, RemoveBackgroundModel.class));
  }

  @Test
  void createSendsExpectedRequestShape() throws Exception {
    CapturingTransport transport = new CapturingTransport("{\"id\":\"task_123\",\"status\":\"processing\"}");
    RecraftClient client = RecraftClient.builder().apiKey("sk-test").transport(transport).build();

    client.removeBackground().create(
        RemoveBackgroundParams.builder()
            .model(RemoveBackgroundModel.RECRAFT_REMOVE_BACKGROUND)
            .sourceImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
            .build()
    );

    assertEquals("POST", transport.request.getMethod().name());
    assertEquals("/api/v1/recraft/remove_background", transport.request.getPath());
    JsonNode body = bodyJson(transport.request);
    assertNotNull(body);
  }

  @Test
  void getDecodesTaskResponseAndExtraFields() {
    CapturingTransport transport = new CapturingTransport("{\"id\":\"task_456\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}],\"custom\":\"kept\"}");
    RecraftClient client = RecraftClient.builder().apiKey("sk-test").transport(transport).build();

    RemoveBackgroundResponse response = client.removeBackground().get("task_456");

    assertEquals("GET", transport.request.getMethod().name());
    assertEquals("/api/v1/recraft/remove_background/task_456", transport.request.getPath());
    assertEquals("completed", response.getStatus().value());
    assertNotNull(response.getImages());
    assertEquals("kept", response.extraFields().get("custom").asText());
  }

  @Test
  void runPollsUntilCompletedAndKeepsExtraFields() {
    SequenceTransport transport = new SequenceTransport(
        "{\"id\":\"task_789\",\"status\":\"processing\"}",
        "{\"id\":\"task_789\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}],\"custom\":\"kept\"}");
    RecraftClient client = RecraftClient.builder().apiKey("sk-test").transport(transport).build();

    CompletedRemoveBackgroundResponse response = client.removeBackground().run(
        RemoveBackgroundParams.builder()
            .model(RemoveBackgroundModel.RECRAFT_REMOVE_BACKGROUND)
            .sourceImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
            .build(),
        RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build());

    assertEquals("completed", response.getStatus().value());
    assertNotNull(response.getImages());
    assertEquals("kept", response.extraFields().get("custom").asText());
    assertEquals(2, transport.calls);
  }

  @Test
  void runRejectsCompletedResponseMissingResultField() {
    SequenceTransport transport = new SequenceTransport(
        "{\"id\":\"task_missing\",\"status\":\"processing\"}",
        "{\"id\":\"task_missing\",\"status\":\"completed\"}");
    RecraftClient client = RecraftClient.builder().apiKey("sk-test").transport(transport).build();

    assertThrows(
        ValidationException.class,
        () -> client.removeBackground().run(
                RemoveBackgroundParams.builder()
                    .model(RemoveBackgroundModel.RECRAFT_REMOVE_BACKGROUND)
                    .sourceImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                    .build(),
            RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build()));
  }

    @Test
    void coversRemovebackgroundResourceMethods() {
      CapturingTransport createTransport = new CapturingTransport("{\"id\":\"task_remove_background\",\"status\":\"processing\"}");
      RecraftClient createClient = RecraftClient.builder().apiKey("sk-test").transport(createTransport).build();
      assertNotNull(createClient.removeBackground().create(
              RemoveBackgroundParams.builder()
                  .model(RemoveBackgroundModel.RECRAFT_REMOVE_BACKGROUND)
                  .sourceImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .build()
      ));

      CapturingTransport createWithOptionsTransport = new CapturingTransport("{\"id\":\"task_remove_background_options\",\"status\":\"processing\"}");
      RecraftClient createWithOptionsClient = RecraftClient.builder().apiKey("sk-test").transport(createWithOptionsTransport).build();
      assertNotNull(createWithOptionsClient.removeBackground().create(
              RemoveBackgroundParams.builder()
                  .model(RemoveBackgroundModel.RECRAFT_REMOVE_BACKGROUND)
                  .sourceImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .build(),
          RequestOptions.none()));

      CapturingTransport getTransport = new CapturingTransport("{\"id\":\"task_remove_background\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      RecraftClient getClient = RecraftClient.builder().apiKey("sk-test").transport(getTransport).build();
      assertNotNull(getClient.removeBackground().get("task_remove_background"));

      CapturingTransport getWithOptionsTransport = new CapturingTransport("{\"id\":\"task_remove_background_options\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      RecraftClient getWithOptionsClient = RecraftClient.builder().apiKey("sk-test").transport(getWithOptionsTransport).build();
      assertNotNull(getWithOptionsClient.removeBackground().get("task_remove_background_options", RequestOptions.none()));

      SequenceTransport runTransport = new SequenceTransport(
          "{\"id\":\"task_remove_background_run\",\"status\":\"processing\"}",
          "{\"id\":\"task_remove_background_run\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      RecraftClient runClient = RecraftClient.builder().apiKey("sk-test").transport(runTransport).build();
      CompletedRemoveBackgroundResponse runResponse = runClient.removeBackground().run(
              RemoveBackgroundParams.builder()
                  .model(RemoveBackgroundModel.RECRAFT_REMOVE_BACKGROUND)
                  .sourceImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build());
      assertNotNull(runResponse);

      SequenceTransport runWithOptionsTransport = new SequenceTransport(
          "{\"id\":\"task_remove_background_run_options\",\"status\":\"processing\"}",
          "{\"id\":\"task_remove_background_run_options\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      RecraftClient runWithOptionsClient = RecraftClient.builder().apiKey("sk-test").transport(runWithOptionsTransport).build();
      assertNotNull(runWithOptionsClient.removeBackground().run(
              RemoveBackgroundParams.builder()
                  .model(RemoveBackgroundModel.RECRAFT_REMOVE_BACKGROUND)
                  .sourceImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build()));
    }

    @Test
    void coversUpscaleimageResourceMethods() {
      CapturingTransport createTransport = new CapturingTransport("{\"id\":\"task_upscale_image\",\"status\":\"processing\"}");
      RecraftClient createClient = RecraftClient.builder().apiKey("sk-test").transport(createTransport).build();
      assertNotNull(createClient.upscaleImage().create(
              UpscaleImageParams.builder()
                  .model(UpscaleImageModel.RECRAFT_CRISP_UPSCALE)
                  .sourceImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .build()
      ));

      CapturingTransport createWithOptionsTransport = new CapturingTransport("{\"id\":\"task_upscale_image_options\",\"status\":\"processing\"}");
      RecraftClient createWithOptionsClient = RecraftClient.builder().apiKey("sk-test").transport(createWithOptionsTransport).build();
      assertNotNull(createWithOptionsClient.upscaleImage().create(
              UpscaleImageParams.builder()
                  .model(UpscaleImageModel.RECRAFT_CRISP_UPSCALE)
                  .sourceImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .build(),
          RequestOptions.none()));

      CapturingTransport getTransport = new CapturingTransport("{\"id\":\"task_upscale_image\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      RecraftClient getClient = RecraftClient.builder().apiKey("sk-test").transport(getTransport).build();
      assertNotNull(getClient.upscaleImage().get("task_upscale_image"));

      CapturingTransport getWithOptionsTransport = new CapturingTransport("{\"id\":\"task_upscale_image_options\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      RecraftClient getWithOptionsClient = RecraftClient.builder().apiKey("sk-test").transport(getWithOptionsTransport).build();
      assertNotNull(getWithOptionsClient.upscaleImage().get("task_upscale_image_options", RequestOptions.none()));

      SequenceTransport runTransport = new SequenceTransport(
          "{\"id\":\"task_upscale_image_run\",\"status\":\"processing\"}",
          "{\"id\":\"task_upscale_image_run\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      RecraftClient runClient = RecraftClient.builder().apiKey("sk-test").transport(runTransport).build();
      CompletedUpscaleImageResponse runResponse = runClient.upscaleImage().run(
              UpscaleImageParams.builder()
                  .model(UpscaleImageModel.RECRAFT_CRISP_UPSCALE)
                  .sourceImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build());
      assertNotNull(runResponse);

      SequenceTransport runWithOptionsTransport = new SequenceTransport(
          "{\"id\":\"task_upscale_image_run_options\",\"status\":\"processing\"}",
          "{\"id\":\"task_upscale_image_run_options\",\"status\":\"completed\",\"images\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      RecraftClient runWithOptionsClient = RecraftClient.builder().apiKey("sk-test").transport(runWithOptionsTransport).build();
      assertNotNull(runWithOptionsClient.upscaleImage().run(
              UpscaleImageParams.builder()
                  .model(UpscaleImageModel.RECRAFT_CRISP_UPSCALE)
                  .sourceImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build()));
    }

  private static JsonNode bodyJson(HttpRequest request) throws Exception {
    JsonRequestBody body = (JsonRequestBody) request.getBody();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    body.writeTo(out);
    return Json.mapper().readTree(out.toByteArray());
  }

  private static final class CapturingTransport implements HttpTransport {
    private final String body;
    private HttpRequest request;

    private CapturingTransport(String body) {
      this.body = body;
    }

    public HttpResponse send(HttpRequest request) {
      this.request = request;
      return new HttpResponse(200, body, Collections.<String, java.util.List<String>>emptyMap());
    }

    public void close() {}
  }

  private static final class SequenceTransport implements HttpTransport {
    private final String[] responses;
    private int calls;

    private SequenceTransport(String... responses) {
      this.responses = responses;
    }

    public HttpResponse send(HttpRequest request) {
      String response = responses[Math.min(calls, responses.length - 1)];
      calls++;
      return new HttpResponse(200, response, Collections.<String, java.util.List<String>>emptyMap());
    }

    public void close() {}
  }
}
