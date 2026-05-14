import type { AsyncTaskStatus } from '@runapi.ai/core';

export type RecraftUpscaleModel = 'recraft-crisp-upscale';
export type RecraftBackgroundRemovalModel = 'recraft-remove-background';

export interface UpscaleParams {
  model: RecraftUpscaleModel;
  image_url: string;
  callback_url?: string;
}

export interface BackgroundRemovalParams {
  model: RecraftBackgroundRemovalModel;
  image_url: string;
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
