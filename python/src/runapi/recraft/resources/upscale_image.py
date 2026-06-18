"""Recraft upscale-image resource."""

from __future__ import annotations

from typing import Any, Dict

from runapi.core import Resource, ValidationError

from ..types import (
    UPSCALE_IMAGE_MODELS,
    CompletedImageTaskResponse,
    ImageTaskResponse,
)


class UpscaleImage(Resource):
    """Upscale images with Recraft models."""

    ENDPOINT = "/api/v1/recraft/upscale_image"

    RESPONSE_CLASS = ImageTaskResponse
    COMPLETED_RESPONSE_CLASS = CompletedImageTaskResponse

    def run(self, **params: Any) -> Any:
        """Run an image upscale task and poll until it completes.

        Args:
            **params: image upscale parameters (model, ...).

        Returns:
            The completed (narrowed) image upscale response.
        """
        task = self.create(**params)
        return self._poll_until_complete(lambda: self.get(task.id))

    def create(self, **params: Any) -> Any:
        """Create an image upscale task and return immediately with an id.

        Args:
            **params: image upscale parameters (model, ...).

        Returns:
            The task creation result with an id.
        """
        compacted = self._compact_params(params)
        self._validate_params(compacted)
        return self._request("post", self.ENDPOINT, body=compacted)

    def get(self, id: str) -> Any:
        """Fetch the current status of an image upscale task.

        Args:
            id: The task id returned by ``create``.

        Returns:
            The current task status.
        """
        return self._request("get", f"{self.ENDPOINT}/{id}")

    def _validate_params(self, params: Dict[str, Any]) -> None:
        if not params.get("model"):
            raise ValidationError("model is required")
        if not params.get("source_image_url"):
            raise ValidationError("source_image_url is required")

        model = params.get("model")
        if model not in UPSCALE_IMAGE_MODELS:
            raise ValidationError(f"Invalid model: {model}. Must be one of: {', '.join(UPSCALE_IMAGE_MODELS)}")
