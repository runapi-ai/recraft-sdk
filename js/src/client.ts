import { BaseClient, type ClientOptions } from '@runapi.ai/core';
import { UpscaleImage } from './resources/upscale-image';
import { RemoveBackground } from './resources/remove-background';

/**
 * Recraft image processing client for AI-powered upscaling and background removal.
 *
 * @example
 * ```typescript
 * import { RecraftClient } from '@runapi.ai/recraft';
 * const client = new RecraftClient({ apiKey: 'sk-...' });
 *
 * // Upscale an image
 * const upscaled = await client.upscaleImage.run({
 *   model: 'recraft-crisp-upscale',
 *   source_image_url: 'https://cdn.runapi.ai/public/samples/image.jpg',
 * });
 *
 * // Remove background to produce a transparent PNG
 * const cutout = await client.removeBackground.run({
 *   model: 'recraft-remove-background',
 *   source_image_url: 'https://cdn.runapi.ai/public/samples/image.jpg',
 * });
 * ```
 */
export class RecraftClient extends BaseClient {
  /** Increases image resolution while preserving detail and sharpness. */
  public readonly upscaleImage: UpscaleImage;
  /** Isolates the foreground subject and removes the background, producing a transparent PNG. */
  public readonly removeBackground: RemoveBackground;

  constructor(options: ClientOptions = {}) {
    super(options);
    this.upscaleImage = new UpscaleImage(this.http);
    this.removeBackground = new RemoveBackground(this.http);
  }
}
