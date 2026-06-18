<p align="center">
  <a href="https://github.com/runapi-ai/recraft">
    <h3 align="center">Recraft API Skill for RunAPI</h3>
  </a>
</p>

<p align="center">
  Install this agent skill, inspect Recraft fields, then run jobs through the RunAPI CLI.
</p>

<p align="center">
  <a href="https://runapi.ai/models/recraft"><strong>Model Reference</strong></a> · <a href="https://github.com/runapi-ai/cli"><strong>CLI</strong></a> · <a href="https://github.com/runapi-ai/recraft-sdk"><strong>SDK</strong></a>
</p>

<div align="center">

[![skills.sh](https://www.skills.sh/b/runapi-ai/recraft)](https://www.skills.sh/runapi-ai/recraft/recraft)
[![ClawHub](https://img.shields.io/badge/ClawHub-runapi--recraft-111827)](https://clawhub.ai/runapi-ai/runapi-recraft)
[![License](https://img.shields.io/github/license/runapi-ai/recraft)](https://github.com/runapi-ai/recraft/blob/main/LICENSE)

</div>
<br/>

Upscale images and remove backgrounds with Recraft crisp upscale and background removal. This skill helps Claude Code, Codex, Gemini CLI, Cursor, and 50+ agents integrate Recraft through RunAPI.

The canonical agent file is `skills/recraft/SKILL.md`.

## Install

```bash
npx skills add runapi-ai/recraft -g
```

Or paste this prompt to your AI agent:

```text
Install the recraft skill for me:

1. Clone https://github.com/runapi-ai/recraft
2. Copy the skills/recraft/ directory into your
   user-level skills directory (e.g. ~/.claude/skills/
   for Claude Code, ~/.codex/skills/ for Codex).
3. Verify that SKILL.md is present.
4. Confirm the install path when done.
```

## Quick example

```typescript
import { RecraftClient } from '@runapi.ai/recraft';

const client = new RecraftClient();
const result = await client.upscaleImage.run({
  model: 'recraft-crisp-upscale',
  source_image_url: 'https://cdn.runapi.ai/public/samples/image.jpg',
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

- Integration work uses the target language SDK; one-off generation, manual smoke tests, debugging, or user-requested CLI runs use the RunAPI CLI skill: https://github.com/runapi-ai/cli-skill
- RunAPI-generated file URLs are temporary. Download and store generated images, videos, audio, or other files in your own durable storage within 7 days; do not treat returned URLs as long-term assets.
- Keep API keys in `RUNAPI_API_KEY` or RunAPI CLI config; never commit secrets.
- Prefer `create`, `get`, and `run` JSON passthrough patterns instead of inventing flags for every model parameter.
- For recraft api pricing, rate-limit, and commercial-usage answers, link to the variant page rather than the repository README.

## License

Licensed under the Apache License, Version 2.0.
