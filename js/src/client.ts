import { createHttpClient, type ClientOptions } from '@runapi.ai/core';
import { Upscales } from './resources/upscales';
import { BackgroundRemovals } from './resources/background-removals';

export class RecraftClient {
  public readonly upscales: Upscales;
  public readonly backgroundRemovals: BackgroundRemovals;

  constructor(options: ClientOptions = {}) {
    const http = createHttpClient(options);
    this.upscales = new Upscales(http);
    this.backgroundRemovals = new BackgroundRemovals(http);
  }
}
