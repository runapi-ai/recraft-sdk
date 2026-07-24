"""Recraft client."""

from __future__ import annotations

from typing import Any, Optional

from runapi.core import ProviderClient

from .resources.remove_background import RemoveBackground
from .resources.upscale_image import UpscaleImage


class RecraftClient(ProviderClient):
    """Recraft image upscale and background-removal client.

    Example::

        client = RecraftClient(api_key="sk-...")
        result = client.upscale_image.run(
            model="recraft-crisp-upscale",
            source_image_url="https://cdn.runapi.ai/public/samples/image.jpg",
        )
    """

    def __init__(self, api_key: Optional[str] = None, **options: Any) -> None:
        super().__init__(api_key, **options)
        http = self._http
        self.upscale_image = UpscaleImage(http)
        self.remove_background = RemoveBackground(http)
