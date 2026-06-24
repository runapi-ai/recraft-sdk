package ai.runapi.recraft.types;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Parameters for remove background operations. */
public final class RemoveBackgroundParams {
  private final String model;
  private final String sourceImageUrl;
  private final String callbackUrl;

  private RemoveBackgroundParams(Builder builder) {
    this.model = builder.model;
    this.sourceImageUrl = RecraftParamUtils.requireNonBlank(builder.sourceImageUrl, "sourceImageUrl");
    this.callbackUrl = builder.callbackUrl;
  }

  /** Creates a new RemoveBackgroundParams builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** Returns the RunAPI action key for this request. */
  public String action() {
    return "recraft/remove-background";
  }

  /** Converts these parameters to the JSON request body shape. */
  public Map<String, Object> toMap() {
    Map<String, Object> raw = new LinkedHashMap<String, Object>();
    raw.put("model", RecraftParamUtils.wireValue(model));
    raw.put("source_image_url", RecraftParamUtils.wireValue(sourceImageUrl));
    raw.put("callback_url", RecraftParamUtils.wireValue(callbackUrl));
    return RecraftParamUtils.compact(raw);
  }



  /** Builder for {@link RemoveBackgroundParams}. */
  public static final class Builder {
    private String model;
    private String sourceImageUrl;
    private String callbackUrl;

    private Builder() {}

    /** Sets the model slug using a typed model value. */
    public Builder model(RemoveBackgroundModel value) {
      this.model = java.util.Objects.requireNonNull(value, "model").value();
      return this;
    }

    /** Sets the model slug using a string value. */
    public Builder model(String value) {
      this.model = RecraftParamUtils.requireNonBlankTrim(value, "model");
      return this;
    }


    /** Sets the source image URL. */
    public Builder sourceImageUrl(String value) {
      this.sourceImageUrl = RecraftParamUtils.requireNonBlank(value, "sourceImageUrl");
      return this;
    }

    /** Sets the webhook URL for task completion notifications. */
    public Builder callbackUrl(String value) {
      this.callbackUrl = RecraftParamUtils.requireNonBlank(value, "callbackUrl");
      return this;
    }

    /** Builds immutable remove background parameters. */
    public RemoveBackgroundParams build() {
      return new RemoveBackgroundParams(this);
    }
  }
}
