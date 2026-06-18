import type { AsyncTaskStatus } from '@runapi.ai/core';

/** Crisp upscaling model that enhances resolution while preserving detail. */
export type RecraftUpscaleImageModel = 'recraft-crisp-upscale';
/** Background removal model that isolates the foreground subject and outputs a transparent PNG. */
export type RecraftRemoveBackgroundModel = 'recraft-remove-background';

/** Parameters for image upscaling. Both `model` and `source_image_url` are required. */
export interface UpscaleImageParams {
  model: RecraftUpscaleImageModel;
  /** Public URL of the source image to upscale. */
  source_image_url: string;
  /** HTTPS callback URL for task completion notification. */
  callback_url?: string;
}

/** Parameters for background removal. Both `model` and `source_image_url` are required. */
export interface RemoveBackgroundParams {
  model: RecraftRemoveBackgroundModel;
  /** Public URL of the source image whose background will be removed. */
  source_image_url: string;
  /** HTTPS callback URL for task completion notification. */
  callback_url?: string;
}

/** Acknowledgement returned by `create()` before the task starts processing. */
export interface TaskCreateResponse {
  id: string;
}

/** URL to a processed image (upscaled or background-removed). */
export interface Image {
  url: string;
}

/** Async image task result shared by upscale and background removal operations. */
export interface ImageTaskResponse {
  id: string;
  status: AsyncTaskStatus;
  /** Processed image files; populated once the task completes. */
  images?: Image[];
  error?: string;
  [key: string]: unknown;
}

/** Narrowed response returned by `run()` once polling confirms completion. Images are guaranteed present. */
export type CompletedImageTaskResponse = ImageTaskResponse & {
  status: 'completed';
  images: Image[];
};
