"""Recraft remove-background resource."""

from __future__ import annotations

from typing import Any, Optional

from runapi.core import Resource, RequestOptions

from ..contract_gen import CONTRACT
from ..types import (
    CompletedImageTaskResponse,
    ImageTaskResponse,
)


class RemoveBackground(Resource):
    """Remove image backgrounds with Recraft models."""

    ENDPOINT = "/api/v1/recraft/remove_background"

    RESPONSE_CLASS = ImageTaskResponse
    COMPLETED_RESPONSE_CLASS = CompletedImageTaskResponse

    def run(self, options: Optional[RequestOptions] = None, **params: Any) -> Any:
        """Run a remove-background task and poll until it completes.

        Args:
            **params: remove-background parameters (model, ...).

        Returns:
            The completed (narrowed) remove-background response.
        """
        task = self.create(options=options, **params)
        return self._poll_until_complete(lambda: self.get(task.id, options=options))

    def create(self, options: Optional[RequestOptions] = None, **params: Any) -> Any:
        """Create a remove-background task and return immediately with an id.

        Args:
            **params: remove-background parameters (model, ...).

        Returns:
            The task creation result with an id.
        """
        compacted = self._compact_params(params)
        self._validate_contract(CONTRACT["remove-background"], compacted)
        return self._request("post", self.ENDPOINT, body=compacted, options=options)

    def get(self, id: str, options: Optional[RequestOptions] = None) -> Any:
        """Fetch the current status of a remove-background task.

        Args:
            id: The task id returned by ``create``.

        Returns:
            The current task status.
        """
        return self._request("get", f"{self.ENDPOINT}/{id}", options=options)
