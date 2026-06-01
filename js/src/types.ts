import type { AsyncTaskStatus } from '@runapi.ai/core';

export type RecraftUpscaleImageModel = 'recraft-crisp-upscale';
export type RecraftRemoveBackgroundModel = 'recraft-remove-background';

export interface UpscaleImageParams {
  model: RecraftUpscaleImageModel;
  source_image_url: string;
  callback_url?: string;
}

export interface RemoveBackgroundParams {
  model: RecraftRemoveBackgroundModel;
  source_image_url: string;
  callback_url?: string;
}

export interface TaskCreateResponse {
  id: string;
}

export interface Image {
  url: string;
}

export interface ImageTaskResponse {
  id: string;
  status: AsyncTaskStatus;
  images?: Image[];
  error?: string;
  [key: string]: unknown;
}

export type CompletedImageTaskResponse = ImageTaskResponse & {
  status: 'completed';
  images: Image[];
};
