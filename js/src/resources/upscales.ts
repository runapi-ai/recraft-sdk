import type { HttpClient, PollingOptions, RequestOptions } from '@runapi.ai/core';
import { compactParams, ValidationError } from '@runapi.ai/core';
import { pollUntilComplete } from '@runapi.ai/core/internal';
import type { CompletedImageTaskResponse, ImageTaskResponse, TaskCreateResponse, UpscaleParams } from '../types';

const ENDPOINT = '/api/v1/recraft/upscales';

export class Upscales {
  constructor(private readonly http: HttpClient) {}

  async run(params: UpscaleParams, options?: RequestOptions & PollingOptions): Promise<CompletedImageTaskResponse> {
    const { id } = await this.create(params, options);
    const response = await pollUntilComplete<ImageTaskResponse>(() => this.get(id, options), {
      maxWaitMs: options?.maxWaitMs,
      pollIntervalMs: options?.pollIntervalMs,
    });
    return response as CompletedImageTaskResponse;
  }

  async create(params: UpscaleParams, options?: RequestOptions): Promise<TaskCreateResponse> {
    const body = compactParams(params);
    if (!body.model) throw new ValidationError('model is required');
    if (!body.image_url) throw new ValidationError('image_url is required');

    return this.http.request<TaskCreateResponse>('POST', ENDPOINT, {
      body,
      ...options,
    });
  }

  async get(id: string, options?: RequestOptions): Promise<ImageTaskResponse> {
    return this.http.request<ImageTaskResponse>('GET', `${ENDPOINT}/${id}`, {
      ...options,
    });
  }
}
