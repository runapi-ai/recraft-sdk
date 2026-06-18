# Recraft API Ruby SDK for RunAPI

The recraft api Ruby SDK is the language-specific package for Recraft on RunAPI. Use this recraft api package for text-to-image, image editing, and creative production flows when your application needs JSON request bodies, task status lookup, and consistent RunAPI errors in Ruby.

This recraft api README is the Ruby package guide inside the public `recraft-sdk` repository. For the repository overview, start at `../README.md`; for model details, use https://runapi.ai/models/recraft; for API reference, use https://runapi.ai/docs#recraft; for SDK docs, use https://runapi.ai/docs#sdk-recraft.

## Install

```bash
gem install runapi-recraft
```

## Quick start

```ruby
require "runapi-recraft"

client = RunApi::Recraft::Client.new
task = client.upscales.create(
  # Pass the Recraft JSON request body from https://runapi.ai/docs#recraft.
)
status = client.upscales.get(task.id)
```

Use `create` when you want to submit a task and return quickly, `get` when you need the latest task state, and `run` when a script should create and poll until completion. In web request handlers, prefer `create` plus webhook or later `get` polling so a worker is not held open.

RunAPI-generated file URLs are temporary. Download and store generated images, videos, audio, or other files in your own durable storage within 7 days; do not treat returned URLs as long-term assets.

## Language notes

Use Ruby keyword arguments and the `RunApi::Recraft` error classes when building image jobs, Rails workers, or scripts. The available resources include upscales, and background removals. Keep `RUNAPI_API_KEY` in the environment or your secret manager; never commit API keys or callback secrets.

## Links

- Model page: https://runapi.ai/models/recraft
- SDK docs: https://runapi.ai/docs#sdk-recraft
- Product docs: https://runapi.ai/docs#recraft
- Pricing and rate limits: https://runapi.ai/models/recraft/crisp-upscale
- Provider comparison: https://runapi.ai/providers/recraft
- Full catalog: https://runapi.ai/models
- Repository: https://github.com/runapi-ai/recraft-sdk

## License

Licensed under the Apache License, Version 2.0.
