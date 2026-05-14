import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ValidationError, type HttpClient } from '@runapi.ai/core';
import { Upscales } from '../../src/resources/upscales';
import type { ImageTaskResponse, TaskCreateResponse } from '../../src/types';

describe('Upscales', () => {
  const mockHttp: HttpClient = { request: vi.fn() };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('POSTs create requests to the upscale endpoint', async () => {
    const mockResponse: TaskCreateResponse = { id: 'task-123' };
    vi.mocked(mockHttp.request).mockResolvedValueOnce(mockResponse);

    const upscales = new Upscales(mockHttp);
    const result = await upscales.create({
      model: 'recraft-crisp-upscale',
      image_url: 'https://example.com/input.png',
      callback_url: 'https://example.com/callback',
    });

    expect(mockHttp.request).toHaveBeenCalledWith('POST', '/api/v1/recraft/upscales', {
      body: {
        model: 'recraft-crisp-upscale',
        image_url: 'https://example.com/input.png',
        callback_url: 'https://example.com/callback',
      },
    });
    expect(result).toEqual(mockResponse);
  });

  it('validates required create params', async () => {
    const upscales = new Upscales(mockHttp);

    await expect(upscales.create({ image_url: 'https://example.com/input.png' } as any)).rejects.toThrow(ValidationError);
    await expect(upscales.create({ model: 'recraft-crisp-upscale' } as any)).rejects.toThrow(ValidationError);
    expect(mockHttp.request).not.toHaveBeenCalled();
  });

  it('GETs task status by id', async () => {
    const mockResponse: ImageTaskResponse = { id: 'task-123', status: 'completed', images: [{ url: 'https://file.runapi.ai/out.png' }] };
    vi.mocked(mockHttp.request).mockResolvedValueOnce(mockResponse);

    const upscales = new Upscales(mockHttp);
    const result = await upscales.get('task-123');

    expect(mockHttp.request).toHaveBeenCalledWith('GET', '/api/v1/recraft/upscales/task-123', {});
    expect(result).toEqual(mockResponse);
  });
});
