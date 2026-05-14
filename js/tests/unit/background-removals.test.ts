import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ValidationError, type HttpClient } from '@runapi.ai/core';
import { BackgroundRemovals } from '../../src/resources/background-removals';
import type { ImageTaskResponse, TaskCreateResponse } from '../../src/types';

describe('BackgroundRemovals', () => {
  const mockHttp: HttpClient = { request: vi.fn() };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('POSTs create requests to the background removal endpoint', async () => {
    const mockResponse: TaskCreateResponse = { id: 'task-456' };
    vi.mocked(mockHttp.request).mockResolvedValueOnce(mockResponse);

    const removals = new BackgroundRemovals(mockHttp);
    const result = await removals.create({
      model: 'recraft-remove-background',
      image_url: 'https://example.com/input.webp',
    });

    expect(mockHttp.request).toHaveBeenCalledWith('POST', '/api/v1/recraft/background_removals', {
      body: {
        model: 'recraft-remove-background',
        image_url: 'https://example.com/input.webp',
      },
    });
    expect(result).toEqual(mockResponse);
  });

  it('validates required create params', async () => {
    const removals = new BackgroundRemovals(mockHttp);

    await expect(removals.create({ image_url: 'https://example.com/input.webp' } as any)).rejects.toThrow(ValidationError);
    await expect(removals.create({ model: 'recraft-remove-background' } as any)).rejects.toThrow(ValidationError);
    expect(mockHttp.request).not.toHaveBeenCalled();
  });

  it('GETs task status by id', async () => {
    const mockResponse: ImageTaskResponse = { id: 'task-456', status: 'completed', images: [{ url: 'https://file.runapi.ai/bg.png' }] };
    vi.mocked(mockHttp.request).mockResolvedValueOnce(mockResponse);

    const removals = new BackgroundRemovals(mockHttp);
    const result = await removals.get('task-456');

    expect(mockHttp.request).toHaveBeenCalledWith('GET', '/api/v1/recraft/background_removals/task-456', {});
    expect(result).toEqual(mockResponse);
  });
});
