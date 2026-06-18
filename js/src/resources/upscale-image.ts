import type { HttpClient, PollingOptions, RequestOptions } from '@runapi.ai/core';
import { compactParams, ValidationError } from '@runapi.ai/core';
import { pollUntilComplete } from '@runapi.ai/core/internal';
import type { CompletedImageTaskResponse, ImageTaskResponse, TaskCreateResponse, UpscaleImageParams } from '../types';

const ENDPOINT = '/api/v1/recraft/upscale_image';

/**
 * Increases image resolution while preserving detail and sharpness.
 * Uses the `recraft-crisp-upscale` model.
 */
export class UpscaleImage {
  constructor(private readonly http: HttpClient) {}

  /**
   * Increase image resolution while preserving detail and wait until complete.
   * @param params Upscale parameters.
   * @param options Per-request and polling overrides.
   * @returns The completed task with image results.
   */
  async run(params: UpscaleImageParams, options?: RequestOptions & PollingOptions): Promise<CompletedImageTaskResponse> {
    const { id } = await this.create(params, options);
    const response = await pollUntilComplete<ImageTaskResponse>(() => this.get(id, options), {
      maxWaitMs: options?.maxWaitMs,
      pollIntervalMs: options?.pollIntervalMs,
    });
    return response as CompletedImageTaskResponse;
  }

  /**
   * Increase image resolution while preserving detail; returns immediately with a task id.
   * @param params Upscale parameters.
   * @param options Per-request overrides.
   * @returns The task creation result with id.
   */
  async create(params: UpscaleImageParams, options?: RequestOptions): Promise<TaskCreateResponse> {
    const body = compactParams(params);
    if (!body.model) throw new ValidationError('model is required');
    if (!body.source_image_url) throw new ValidationError('source_image_url is required');

    return this.http.request<TaskCreateResponse>('POST', ENDPOINT, {
      body,
      ...options,
    });
  }

  /**
   * Fetch the current status of an upscale task.
   * @param id The task id.
   * @param options Per-request overrides.
   * @returns The current upscale task status.
   */
  async get(id: string, options?: RequestOptions): Promise<ImageTaskResponse> {
    return this.http.request<ImageTaskResponse>('GET', `${ENDPOINT}/${id}`, {
      ...options,
    });
  }
}
