import { describe, expect, it } from 'vitest';
import { RecraftClient } from '../../src';

describe('RecraftClient', () => {
  it('initializes both resources with an API key', () => {
    const client = new RecraftClient({ apiKey: 'test-key' });
    expect(client.upscaleImage).toBeDefined();
    expect(client.removeBackground).toBeDefined();
  });

});
