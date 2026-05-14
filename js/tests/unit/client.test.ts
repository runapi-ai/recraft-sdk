import { describe, expect, it } from 'vitest';
import { AuthenticationError } from '@runapi.ai/core';
import { RecraftClient } from '../../src';

describe('RecraftClient', () => {
  it('initializes both resources with an API key', () => {
    const client = new RecraftClient({ apiKey: 'test-key' });
    expect(client.upscales).toBeDefined();
    expect(client.backgroundRemovals).toBeDefined();
  });

  it('throws if no API key is provided', () => {
    // @ts-expect-error: testing missing API key
    expect(() => new RecraftClient({})).toThrow(AuthenticationError);
  });
});
