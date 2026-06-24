    package ai.runapi.recraft;

    import static org.junit.jupiter.api.Assertions.assertEquals;
    import static org.junit.jupiter.api.Assertions.assertNotNull;
    import static org.junit.jupiter.api.Assumptions.assumeTrue;

        import ai.runapi.core.errors.TaskFailedException;
    import ai.runapi.core.RequestOptions;
    import ai.runapi.core.json.Json;
    import ai.runapi.recraft.types.CompletedRemoveBackgroundResponse;
    import ai.runapi.recraft.types.CompletedRemoveBackgroundResponse;
import ai.runapi.recraft.types.RemoveBackgroundModel;
import ai.runapi.recraft.types.RemoveBackgroundParams;
import ai.runapi.recraft.types.RemoveBackgroundResponse;
    import com.fasterxml.jackson.databind.node.ObjectNode;
    import java.nio.charset.StandardCharsets;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.time.Duration;
    import org.junit.jupiter.api.Test;

    class RecraftLiveApiSmokeTest {
      @Test
      void primaryResourceRunAgainstLiveRunApi() throws Exception {
        assumeTrue("true".equals(System.getenv("RUNAPI_JAVA_LIVE_RECRAFT_SMOKE")));

        String baseUrl = requireEnv("RUNAPI_BASE_URL");
        String apiKey = requireEnv("RUNAPI_API_KEY");
        String callbackUrl = callbackUrl("recraft");
        Path outputPath = Paths.get(System.getenv().getOrDefault("RUNAPI_JAVA_LIVE_RECRAFT_OUTPUT", "build/live-recraft-smoke-result.json"));
        Files.createDirectories(outputPath.getParent());
        try (RecraftClient client = RecraftClient.builder().apiKey(apiKey).baseUrl(baseUrl).build()) {
          ObjectNode result = Json.mapper().createObjectNode();
          result.put("action", "recraft/remove-background");
          result.put("result_field", "images");
          result.put("callback_url", callbackUrl);
          try {
      CompletedRemoveBackgroundResponse response =
          client.removeBackground().run(
              RemoveBackgroundParams.builder()
                  .model(RemoveBackgroundModel.RECRAFT_REMOVE_BACKGROUND)
                  .sourceImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .callbackUrl(callbackUrl)
                  .build(),
              RequestOptions.builder()
                  .pollingInterval(Duration.ofSeconds(10))
                  .pollingMaxWait(Duration.ofMinutes(15))
                  .maxRetries(0)
                  .build());

          assertEquals("completed", response.getStatus().value());
            assertNotNull(response.getImages());
            result.put("id", response.getId());
            result.put("status", response.getStatus().value());
            Files.write(outputPath, Json.mapper().writerWithDefaultPrettyPrinter().writeValueAsString(result).getBytes(StandardCharsets.UTF_8));
          } catch (TaskFailedException failure) {
            result.put("status", "failed");
            result.put("exception", failure.getClass().getSimpleName());
            result.put("message", failure.getMessage());
            Object taskResponse = failure.getTaskResponse();
            if (taskResponse instanceof RemoveBackgroundResponse) {
              result.put("id", ((RemoveBackgroundResponse) taskResponse).getId());
              result.put("status", ((RemoveBackgroundResponse) taskResponse).getStatus().value());
              result.put("error", ((RemoveBackgroundResponse) taskResponse).getError());
            }
            Files.write(outputPath, Json.mapper().writerWithDefaultPrettyPrinter().writeValueAsString(result).getBytes(StandardCharsets.UTF_8));
            throw failure;
          } catch (RuntimeException failure) {
            result.put("status", "error");
            result.put("exception", failure.getClass().getSimpleName());
            result.put("message", failure.getMessage());
            Files.write(outputPath, Json.mapper().writerWithDefaultPrettyPrinter().writeValueAsString(result).getBytes(StandardCharsets.UTF_8));
            throw failure;
          }
        }
      }

      private static String callbackUrl(String modelSlug) {
        String base = requireEnv("RUNAPI_CALLBACK_URL");
        String normalized = base.endsWith("/") ? base.substring(0, base.length() - 1) : base;
        return normalized + "/java-live-smoke/" + modelSlug + "/" + System.currentTimeMillis();
      }

      private static String requireEnv(String name) {
        String value = System.getenv(name);
        if (value == null || value.trim().isEmpty()) {
          throw new IllegalStateException(name + " is required");
        }
        return value;
      }
    }
