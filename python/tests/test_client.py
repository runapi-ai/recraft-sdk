import pytest

from runapi.core import config
from runapi.core.errors import AuthenticationError, ValidationError
from runapi.recraft import RecraftClient
from runapi.recraft.resources.remove_background import RemoveBackground
from runapi.recraft.resources.upscale_image import UpscaleImage
from runapi.recraft.types import CompletedImageTaskResponse, ImageTaskResponse


class FakeHttp:
    def __init__(self, *responses):
        self._responses = list(responses)
        self.calls = []

    def request(self, method, path, body=None, options=None):
        self.calls.append((method, path, body))
        if self._responses:
            return self._responses.pop(0)
        return {"id": "task_1", "status": "pending"}


@pytest.fixture(autouse=True)
def reset_config(monkeypatch):
    monkeypatch.delenv("RUNAPI_API_KEY", raising=False)
    monkeypatch.setattr(config, "api_key", None)
    yield


# --- auth -----------------------------------------------------------------


def test_accepts_api_key_parameter():
    assert isinstance(RecraftClient(api_key="k", http_client=FakeHttp()), RecraftClient)


def test_falls_back_to_global(monkeypatch):
    monkeypatch.setattr(config, "api_key", "global-key")
    assert isinstance(RecraftClient(http_client=FakeHttp()), RecraftClient)


def test_falls_back_to_env(monkeypatch):
    monkeypatch.setenv("RUNAPI_API_KEY", "env-key")
    assert isinstance(RecraftClient(http_client=FakeHttp()), RecraftClient)


def test_raises_without_api_key():
    with pytest.raises(AuthenticationError, match="API key is required"):
        RecraftClient()


# --- injection / accessors ------------------------------------------------


def test_uses_injected_http_client():
    fake = FakeHttp()
    client = RecraftClient(api_key="k", http_client=fake)
    assert client.upscale_image._http is fake
    assert client.remove_background._http is fake


def test_exposes_resource_accessors():
    client = RecraftClient(api_key="k", http_client=FakeHttp())
    assert isinstance(client.upscale_image, UpscaleImage)
    assert isinstance(client.remove_background, RemoveBackground)


# --- request shapes -------------------------------------------------------


def test_upscale_create_posts_compacted_body():
    fake = FakeHttp({"id": "t1", "status": "pending"})
    client = RecraftClient(api_key="k", http_client=fake)
    result = client.upscale_image.create(
        model="recraft-crisp-upscale",
        source_image_url="https://x/a.png",
        callback_url=None,
    )
    assert fake.calls == [
        (
            "post",
            "/api/v1/recraft/upscale_image",
            {"model": "recraft-crisp-upscale", "source_image_url": "https://x/a.png"},
        ),
    ]
    assert isinstance(result, ImageTaskResponse)


def test_upscale_get_fetches_by_id():
    fake = FakeHttp({"id": "t1", "status": "processing"})
    client = RecraftClient(api_key="k", http_client=fake)
    client.upscale_image.get("t1")
    assert fake.calls == [("get", "/api/v1/recraft/upscale_image/t1", None)]


def test_remove_background_create_posts_compacted_body():
    fake = FakeHttp({"id": "t1", "status": "pending"})
    client = RecraftClient(api_key="k", http_client=fake)
    client.remove_background.create(
        model="recraft-remove-background",
        source_image_url="https://x/a.png",
    )
    assert fake.calls == [
        (
            "post",
            "/api/v1/recraft/remove_background",
            {"model": "recraft-remove-background", "source_image_url": "https://x/a.png"},
        ),
    ]


def test_remove_background_get_fetches_by_id():
    fake = FakeHttp({"id": "t1", "status": "processing"})
    client = RecraftClient(api_key="k", http_client=fake)
    client.remove_background.get("t1")
    assert fake.calls == [("get", "/api/v1/recraft/remove_background/t1", None)]


def test_run_narrows_completed_type():
    fake = FakeHttp(
        {"id": "t1", "status": "pending"},
        {"id": "t1", "status": "completed", "images": [{"url": "https://x/y.png"}]},
    )
    client = RecraftClient(api_key="k", http_client=fake)
    result = client.upscale_image.run(
        model="recraft-crisp-upscale", source_image_url="https://x/a.png"
    )
    assert isinstance(result, CompletedImageTaskResponse)
    assert result.images[0].url == "https://x/y.png"


# --- validation -----------------------------------------------------------


def test_upscale_requires_model():
    client = RecraftClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="model must be one of: recraft-crisp-upscale"):
        client.upscale_image.create(source_image_url="https://x/a.png")


def test_upscale_requires_source_image_url():
    client = RecraftClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="source_image_url is required"):
        client.upscale_image.create(model="recraft-crisp-upscale")


def test_upscale_rejects_unknown_model():
    client = RecraftClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="model must be one of: recraft-crisp-upscale"):
        client.upscale_image.create(model="nope", source_image_url="https://x/a.png")


def test_remove_background_rejects_unknown_model():
    client = RecraftClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="model must be one of: recraft-remove-background"):
        client.remove_background.create(model="nope", source_image_url="https://x/a.png")
