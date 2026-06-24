package ai.runapi.recraft.types;

import com.fasterxml.jackson.annotation.JsonCreator;

/** Model slug for remove background operations. */
public final class RemoveBackgroundModel extends RecraftValue {
  /** recraft-remove-background model slug. */
  public static final RemoveBackgroundModel RECRAFT_REMOVE_BACKGROUND = new RemoveBackgroundModel("recraft-remove-background");

  /** Creates a model value from a literal model slug. */
  @JsonCreator
  public RemoveBackgroundModel(String value) {
    super(value);
  }
}
