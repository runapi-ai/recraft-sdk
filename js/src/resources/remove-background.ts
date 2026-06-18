import type { HttpClient, PollingOptions, RequestOptions } from '@runapi.ai/core';
import { compactParams, ValidationError } from '@runapi.ai/core';
import { pollUntilComplete } from '@runapi.ai/core/internal';
import type { RemoveBackgroundParams, CompletedImageTaskResponse, ImageTaskResponse, TaskCreateResponse } from '../types';

const ENDPOINT = '/api/v1/recraft/remove_background';

/**
 * Isolates the foreground subject and removes the background, producing a transparent PNG.
 * Uses the `recraft-remove-background` model.
 */
export class RemoveBackground {
  constructor(private readonly http: HttpClient) {}

  /**
   * Remove the image background to produce a transparent cutout and wait until complete.
   * @param params Background-removal parameters.
   * @param options Per-request and polling overrides.
   * @returns The completed task with image results.
   */
  async run(params: RemoveBackgroundParams, options?: RequestOptions & PollingOptions): Promise<CompletedImageTaskResponse> {
    const { id } = await this.create(params, options);
    const response = await pollUntilComplete<ImageTaskResponse>(() => this.get(id, options), {
      maxWaitMs: options?.maxWaitMs,
      pollIntervalMs: options?.pollIntervalMs,
    });
    return response as CompletedImageTaskResponse;
  }

  /**
   * Remove the image background to produce a transparent cutout; returns immediately with a task id.
   * @param params Background-removal parameters.
   * @param options Per-request overrides.
   * @returns The task creation result with id.
   */
  async create(params: RemoveBackgroundParams, options?: RequestOptions): Promise<TaskCreateResponse> {
    const body = compactParams(params);
    if (!body.model) throw new ValidationError('model is required');
    if (!body.source_image_url) throw new ValidationError('source_image_url is required');

    return this.http.request<TaskCreateResponse>('POST', ENDPOINT, {
      body,
      ...options,
    });
  }

  /**
   * Fetch the current status of a background-removal task.
   * @param id The task id.
   * @param options Per-request overrides.
   * @returns The current background-removal task status.
   */
  async get(id: string, options?: RequestOptions): Promise<ImageTaskResponse> {
    return this.http.request<ImageTaskResponse>('GET', `${ENDPOINT}/${id}`, {
      ...options,
    });
  }
}
