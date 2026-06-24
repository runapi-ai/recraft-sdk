"""Recraft model lists and response models."""

from __future__ import annotations

from runapi.core import BaseModel, TaskResponse, optional, required


class Image(BaseModel):
    url = optional(str)


class ImageTaskResponse(TaskResponse):
    """Recraft image task status response."""

    id = required(str)
    status = optional(str, enum=lambda: TaskResponse.Status.ALL)
    images = optional([lambda: Image])
    error = optional(str)


class CompletedImageTaskResponse(ImageTaskResponse):
    """Narrowed response from ``run()`` once polling observes completion."""

    images = required([lambda: Image])
