"""Recraft client."""

from __future__ import annotations

from typing import Any, Optional

from runapi.core import ClientOptions, HttpClient, resolve_api_key

from .resources.remove_background import RemoveBackground
from .resources.upscale_image import UpscaleImage


class RecraftClient:
    """Recraft image upscale and background-removal client.

    Example::

        client = RecraftClient(api_key="sk-...")
        result = client.upscale_image.run(
            model="recraft-crisp-upscale",
            source_image_url="https://cdn.runapi.ai/public/samples/image.jpg",
        )
    """

    def __init__(self, api_key: Optional[str] = None, **options: Any) -> None:
        resolved_api_key = resolve_api_key(api_key)
        client_options = ClientOptions(api_key=resolved_api_key, **options)
        http = client_options.http_client or HttpClient(client_options)
        self.upscale_image = UpscaleImage(http)
        self.remove_background = RemoveBackground(http)
