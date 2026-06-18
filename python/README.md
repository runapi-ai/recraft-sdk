# Recraft Python SDK for RunAPI

The Recraft Python SDK is the language-specific package for Recraft on RunAPI. Use this recraft package for image upscale and background-removal flows when your application needs JSON request bodies, task status lookup, and consistent RunAPI errors in Python.

This recraft README is the Python package guide inside the public `recraft-sdk` repository. For the repository overview, start at `../README.md`; for model details, use https://runapi.ai/models/recraft; for API reference, use https://runapi.ai/docs#recraft; for SDK docs, use https://runapi.ai/docs#sdk-recraft.

## Install

```bash
pip install runapi-recraft
```

## Quick start

```python
from runapi.recraft import RecraftClient

client = RecraftClient()  # reads RUNAPI_API_KEY, or pass api_key="sk-..."

task = client.upscale_image.create(
    model="recraft-crisp-upscale",
    source_image_url="https://example.com/source.jpg",
)
status = client.upscale_image.get(task.id)

removed = client.remove_background.create(
    model="recraft-remove-background",
    source_image_url="https://example.com/source.jpg",
)
```

Use `create` when you want to submit a task and return quickly, `get` when you need the latest task state, and `run` when a script should create and poll until completion:

```python
result = client.upscale_image.run(
    model="recraft-crisp-upscale",
    source_image_url="https://example.com/source.jpg",
)
print(result.images[0].url)
```

In web request handlers, prefer `create` plus webhook or later `get` polling so a worker is not held open.

RunAPI-generated file URLs are temporary. Download and store generated images, videos, audio, or other files in your own durable storage within 7 days; do not treat returned URLs as long-term assets.

## Language notes

Pass parameters as keyword arguments and catch the `runapi.recraft` error classes when building image jobs, workers, or scripts. The available resources are `upscale_image` and `remove_background`. Keep `RUNAPI_API_KEY` in the environment or your secret manager; never commit API keys or callback secrets.

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
