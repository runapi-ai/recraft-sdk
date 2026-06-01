import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ValidationError, type HttpClient } from '@runapi.ai/core';
import { UpscaleImage } from '../../src/resources/upscale-image';
import type { ImageTaskResponse, TaskCreateResponse } from '../../src/types';

describe('UpscaleImage', () => {
  const mockHttp: HttpClient = { request: vi.fn() };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('POSTs create requests to the upscale endpoint', async () => {
    const mockResponse: TaskCreateResponse = { id: 'task-123' };
    vi.mocked(mockHttp.request).mockResolvedValueOnce(mockResponse);

    const upscaleImage = new UpscaleImage(mockHttp);
    const result = await upscaleImage.create({
      model: 'recraft-crisp-upscale',
      source_image_url: 'https://cdn.runapi.ai/public/samples/input.png',
      callback_url: 'https://example.com/callback',
    });

    expect(mockHttp.request).toHaveBeenCalledWith('POST', '/api/v1/recraft/upscale_image', {
      body: {
        model: 'recraft-crisp-upscale',
        source_image_url: 'https://cdn.runapi.ai/public/samples/input.png',
        callback_url: 'https://example.com/callback',
      },
    });
    expect(result).toEqual(mockResponse);
  });

  it('validates required create params', async () => {
    const upscaleImage = new UpscaleImage(mockHttp);

    await expect(upscaleImage.create({ source_image_url: 'https://cdn.runapi.ai/public/samples/input.png' } as any)).rejects.toThrow(ValidationError);
    await expect(upscaleImage.create({ model: 'recraft-crisp-upscale' } as any)).rejects.toThrow(ValidationError);
    expect(mockHttp.request).not.toHaveBeenCalled();
  });

  it('GETs task status by id', async () => {
    const mockResponse: ImageTaskResponse = { id: 'task-123', status: 'completed', images: [{ url: 'https://file.runapi.ai/out.png' }] };
    vi.mocked(mockHttp.request).mockResolvedValueOnce(mockResponse);

    const upscaleImage = new UpscaleImage(mockHttp);
    const result = await upscaleImage.get('task-123');

    expect(mockHttp.request).toHaveBeenCalledWith('GET', '/api/v1/recraft/upscale_image/task-123', {});
    expect(result).toEqual(mockResponse);
  });
});
