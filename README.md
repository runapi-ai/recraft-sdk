<p align="center">
  <a href="https://runapi.ai"><img src="https://runapi.ai/icon.svg" height="56" alt="RunAPI"></a>
</p>

<h3 align="center">
  <a href="https://github.com/runapi-ai/recraft-sdk">Recraft API SDK for RunAPI</a>
</h3>

<p align="center">
  Recraft API SDKs for JavaScript, Ruby, and Go on RunAPI.
</p>

<div align="center">

[![npm](https://img.shields.io/npm/v/@runapi.ai/recraft)](https://www.npmjs.com/package/@runapi.ai/recraft)
[![RubyGems](https://img.shields.io/gem/v/runapi-recraft)](https://rubygems.org/gems/runapi-recraft)
[![Go Reference](https://pkg.go.dev/badge/github.com/runapi-ai/recraft-sdk/go.svg)](https://pkg.go.dev/github.com/runapi-ai/recraft-sdk/go)
[![License](https://img.shields.io/github/license/runapi-ai/recraft-sdk)](https://github.com/runapi-ai/recraft-sdk/blob/main/LICENSE)

</div>
<br/>

The recraft api SDK packages JavaScript, Ruby, and Go clients for Recraft on RunAPI. Use this recraft api SDK for text-to-image, image editing, upscales, and creative production workflows that need typed installs, JSON request bodies, task polling, and consistent RunAPI errors across services.

Recraft belongs to the Recraft catalog on RunAPI. The public model page is https://runapi.ai/models/recraft; variant pages below carry pricing, rate-limit, and commercial-usage details. The public `recraft-sdk` repository groups the JavaScript, Ruby, and Go packages for this model.

## Install

```bash
npm install @runapi.ai/recraft
gem install runapi-recraft
go get github.com/runapi-ai/recraft-sdk/go@latest
```

## What you can build

- Build product imagery, creative automation, design previews, and agent image workflows with the recraft api SDK.
- Keep one model-specific repository while installing only the language package your app needs.
- Use `create` for submit-only jobs, `get` for status lookup, and `run` for submit-and-poll scripts.
- Handle authentication, validation, rate limits, insufficient credits, task failures, and polling timeouts through RunAPI SDK errors.

The JavaScript client exposes upscales, background removals resources, and the Ruby and Go packages mirror the same RunAPI task lifecycle.

## JavaScript quick start

```typescript
import { RecraftClient } from '@runapi.ai/recraft';

const client = new RecraftClient();

const task = await client.upscales.create({
  // Pass the Recraft request body documented at https://runapi.ai/docs#recraft.
});

const status = await client.upscales.get(task.id);
```

For short scripts, use `run` with the same JSON body to create the task and wait for completion. For web request handlers, prefer `create` plus webhook or later `get` polling so the server does not hold a worker open.

## Repository layout

- `js/` publishes `@runapi.ai/recraft`.
- `ruby/` publishes `runapi-recraft` when RubyGems publishing resumes.
- `go/` publishes `github.com/runapi-ai/recraft-sdk/go` and depends on `github.com/runapi-ai/core-sdk/go`.

## Public links

- Model page: https://runapi.ai/models/recraft
- SDK docs: https://runapi.ai/docs#sdk-recraft
- Product docs: https://runapi.ai/docs#recraft
- SDK repository: https://github.com/runapi-ai/recraft-sdk
- Skill repository: https://github.com/runapi-ai/recraft
- Provider comparison: https://runapi.ai/providers/recraft
- Full catalog: https://runapi.ai/models

## Pricing and variants

Use the most specific recraft api variant page for pricing, rate limits, and commercial usage:
- [Crisp upscale](https://runapi.ai/models/recraft/crisp-upscale)
- [Remove background](https://runapi.ai/models/recraft/remove-background)

Default pricing link for the recraft api SDK: https://runapi.ai/models/recraft/crisp-upscale

## Generated file storage

RunAPI-generated file URLs are temporary. Download and store generated images, videos, audio, or other files in your own durable storage within 7 days; do not treat returned URLs as long-term assets.

## FAQ

### Which package should I install for recraft api work?

Install the model package for your language: `@runapi.ai/recraft`, `runapi-recraft`, or `github.com/runapi-ai/recraft-sdk/go`. Install core SDK packages only when you are building shared SDK infrastructure.

### Where should public links point?

Primary recraft api links point to https://runapi.ai/models/recraft. Pricing and usage-policy links point to variant pages such as https://runapi.ai/models/recraft/crisp-upscale. Provider comparisons point to https://runapi.ai/providers/recraft, and broad browsing points to https://runapi.ai/models.

## License

Licensed under the Apache License, Version 2.0.
