# Recraft Go SDK for RunAPI

The Recraft Go SDK is the language-specific package for Recraft on RunAPI. Use this package for image generation, image editing, and creative production workflows when your application needs request bodies, task status lookup, and consistent RunAPI errors in Go.

This README is the Go package guide inside the public `recraft-sdk` repository. For the repository overview, start at `../README.md`; for model details, use https://runapi.ai/models/recraft; for API reference, use https://runapi.ai/docs#recraft; for SDK docs, use https://runapi.ai/docs#sdk-recraft.

## Install

```bash
go get github.com/runapi-ai/recraft-sdk/go@latest
```

## Quick start

```go
import (
  "context"

  "github.com/runapi-ai/recraft-sdk/go/recraft"
)

client, err := recraft.NewClient()
task, err := client.UpscaleImage.Create(context.Background(), recraft.UpscaleImageParams{
  // Pass the Recraft JSON request body from https://runapi.ai/docs#recraft.
})
status, err := client.UpscaleImage.Get(context.Background(), task.ID)
```

Use `create` when you want to submit a task and return quickly, `get` when you need the latest task state, and `run` when a script should create and poll until completion. In web request handlers, prefer `create` plus webhook or later `get` polling so a worker is not held open.

RunAPI-generated file URLs are temporary. Download and store generated images, videos, audio, or other files in your own durable storage within 7 days; do not treat returned URLs as long-term assets.

## Language notes

Use the public Go module with `github.com/runapi-ai/core-sdk/go` options when building image services, CLIs, or workers. The available resources are `UpscaleImage` and `RemoveBackground`. Keep `RUNAPI_API_KEY` in the environment or your secret manager; never commit API keys or callback secrets.

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
