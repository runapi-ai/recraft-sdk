package ai.runapi.recraft;

import ai.runapi.core.BaseClient;
import ai.runapi.core.ClientOptions;
import ai.runapi.core.http.HttpTransport;
import java.net.URI;
import ai.runapi.recraft.resources.RemoveBackgroundResource;
import ai.runapi.recraft.resources.UpscaleImageResource;

/** Recraft model-family Java SDK client. */
public final class RecraftClient extends BaseClient {
  private final RemoveBackgroundResource removeBackground;
  private final UpscaleImageResource upscaleImage;

  private RecraftClient(ClientOptions options) {
    super(options);
    this.removeBackground = new RemoveBackgroundResource(transport(), options());
    this.upscaleImage = new UpscaleImageResource(transport(), options());
  }

  /** Creates a new RecraftClient builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** Remove Background operations. */
  public RemoveBackgroundResource removeBackground() {
    return removeBackground;
  }

  /** Upscale Image operations. */
  public UpscaleImageResource upscaleImage() {
    return upscaleImage;
  }

  /** Builder for {@link RecraftClient}. */
  public static final class Builder extends BaseClient.Builder<Builder> {
    private Builder() {}

    /** Sets the API key. If omitted, the SDK reads {@code RUNAPI_API_KEY}. */
    @Override
    public Builder apiKey(String value) {
      return super.apiKey(value);
    }

    /** Sets the RunAPI base URL. If omitted, the SDK reads {@code RUNAPI_BASE_URL}. */
    @Override
    public Builder baseUrl(String value) {
      return super.baseUrl(value);
    }

    /** Sets the RunAPI base URL from a URI. */
    @Override
    public Builder baseUrl(URI value) {
      return super.baseUrl(value);
    }

    /** Sets a custom HTTP transport. User-provided transports are not closed by SDK clients. */
    @Override
    public Builder transport(HttpTransport value) {
      return super.transport(value);
    }

    /** Builds an immutable RecraftClient. */
    @Override
    public RecraftClient build() {
      return new RecraftClient(options.build());
    }
  }
}
