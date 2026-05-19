import { createHttpClient, type ClientOptions } from '@runapi.ai/core';
import { UpscaleImage } from './resources/upscale-image';
import { RemoveBackground } from './resources/remove-background';

export class RecraftClient {
  public readonly upscaleImage: UpscaleImage;
  public readonly removeBackground: RemoveBackground;

  constructor(options: ClientOptions = {}) {
    const http = createHttpClient(options);
    this.upscaleImage = new UpscaleImage(http);
    this.removeBackground = new RemoveBackground(http);
  }
}
