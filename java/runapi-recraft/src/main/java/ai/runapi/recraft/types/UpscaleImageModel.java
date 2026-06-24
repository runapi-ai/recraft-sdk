package ai.runapi.recraft.types;

import com.fasterxml.jackson.annotation.JsonCreator;

/** Model slug for upscale image operations. */
public final class UpscaleImageModel extends RecraftValue {
  /** recraft-crisp-upscale model slug. */
  public static final UpscaleImageModel RECRAFT_CRISP_UPSCALE = new UpscaleImageModel("recraft-crisp-upscale");

  /** Creates a model value from a literal model slug. */
  @JsonCreator
  public UpscaleImageModel(String value) {
    super(value);
  }
}
