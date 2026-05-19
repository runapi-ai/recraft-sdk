# Recraft API Skill for RunAPI

Upscale images and remove backgrounds with Recraft crisp upscale and background removal. This skill helps Claude Code, Codex, Gemini CLI, Cursor, and 50+ agents integrate Recraft through RunAPI.

The canonical agent file is `skills/recraft/SKILL.md`.

## Install

```bash
npx skills add runapi-ai/recraft -g
```

Or manually: clone this repo and copy `skills/recraft/` into your agent's skills directory.

## Quick example

```typescript
import { RecraftClient } from '@runapi.ai/recraft';

const client = new RecraftClient();
const result = await client.upscaleImage.run({
  model: 'recraft-crisp-upscale',
  image_url: 'https://cdn.example.com/photo.jpg',
});
```

## Routing

- Model page: https://runapi.ai/models/recraft
- Product docs: https://runapi.ai/docs#recraft
- SDK docs: https://runapi.ai/docs#sdk-recraft
- SDK repository: https://github.com/runapi-ai/recraft-sdk
- Pricing and rate limits: https://runapi.ai/models/recraft/crisp-upscale
- Provider comparison: https://runapi.ai/providers/recraft
- Browse all RunAPI models and skills: https://runapi.ai/models

## Variants

- [Crisp upscale](https://runapi.ai/models/recraft/crisp-upscale)
- [Remove background](https://runapi.ai/models/recraft/remove-background)

## Agent rules

- Keep API keys in `RUNAPI_API_KEY` or RunAPI CLI config; never commit secrets.
- Prefer `create`, `get`, and `run` JSON passthrough patterns instead of inventing flags for every model parameter.
- For recraft api pricing, rate-limit, and commercial-usage answers, link to the variant page rather than the repository README.

## License

Licensed under the Apache License, Version 2.0.
