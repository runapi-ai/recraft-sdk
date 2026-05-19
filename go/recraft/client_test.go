package recraft

import (
	"context"
	"encoding/json"
	"testing"

	"github.com/runapi-ai/core-sdk/go/core"
)

type stubHTTPClient struct {
	method string
	path   string
	body   any
}

func (s *stubHTTPClient) Request(_ context.Context, method, path string, opts *core.HTTPRequestOptions) (json.RawMessage, error) {
	s.method = method
	s.path = path
	if opts != nil {
		s.body = opts.Body
	}
	return json.RawMessage(`{"id":"task_123","status":"processing"}`), nil
}

func TestUpscaleImageCreate(t *testing.T) {
	stub := &stubHTTPClient{}
	client := NewClientWithHTTP(stub)
	_, err := client.UpscaleImage.Create(context.Background(), UpscaleImageParams{
		Model:    ModelUpscale,
		ImageURL: "https://example.com/input.png",
	})
	if err != nil {
		t.Fatal(err)
	}
	if stub.method != "POST" || stub.path != "/api/v1/recraft/upscale_image" {
		t.Fatalf("unexpected request: %s %s", stub.method, stub.path)
	}
	body := stub.body.(map[string]any)
	if body["model"] != "recraft-crisp-upscale" {
		t.Fatalf("unexpected model: %v", body["model"])
	}
	if body["image_url"] != "https://example.com/input.png" {
		t.Fatalf("unexpected image_url: %v", body["image_url"])
	}
}

func TestUpscaleImageCreateValidatesRequiredParams(t *testing.T) {
	client := NewClientWithHTTP(&stubHTTPClient{})

	if _, err := client.UpscaleImage.Create(context.Background(), UpscaleImageParams{ImageURL: "https://example.com/input.png"}); !core.IsValidation(err) {
		t.Fatalf("expected validation error for missing model, got %v", err)
	}
	if _, err := client.UpscaleImage.Create(context.Background(), UpscaleImageParams{Model: ModelUpscale}); !core.IsValidation(err) {
		t.Fatalf("expected validation error for missing image_url, got %v", err)
	}
}

func TestRemoveBackgroundCreate(t *testing.T) {
	stub := &stubHTTPClient{}
	client := NewClientWithHTTP(stub)
	_, err := client.RemoveBackground.Create(context.Background(), RemoveBackgroundParams{
		Model:    ModelBackgroundRemoval,
		ImageURL: "https://example.com/input.webp",
	})
	if err != nil {
		t.Fatal(err)
	}
	if stub.method != "POST" || stub.path != "/api/v1/recraft/remove_background" {
		t.Fatalf("unexpected request: %s %s", stub.method, stub.path)
	}
}

func TestRemoveBackgroundCreateValidatesRequiredParams(t *testing.T) {
	client := NewClientWithHTTP(&stubHTTPClient{})

	if _, err := client.RemoveBackground.Create(context.Background(), RemoveBackgroundParams{ImageURL: "https://example.com/input.webp"}); !core.IsValidation(err) {
		t.Fatalf("expected validation error for missing model, got %v", err)
	}
	if _, err := client.RemoveBackground.Create(context.Background(), RemoveBackgroundParams{Model: ModelBackgroundRemoval}); !core.IsValidation(err) {
		t.Fatalf("expected validation error for missing image_url, got %v", err)
	}
}

func TestUpscaleImageGet(t *testing.T) {
	stub := &stubHTTPClient{}
	client := NewClientWithHTTP(stub)
	_, err := client.UpscaleImage.Get(context.Background(), "task_upscale_123")
	if err != nil {
		t.Fatal(err)
	}
	if stub.method != "GET" || stub.path != "/api/v1/recraft/upscale_image/task_upscale_123" {
		t.Fatalf("unexpected request: %s %s", stub.method, stub.path)
	}
}
