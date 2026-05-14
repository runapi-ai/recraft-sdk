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

func TestUpscalesCreate(t *testing.T) {
	stub := &stubHTTPClient{}
	client := NewClientWithHTTP(stub)
	_, err := client.Upscales.Create(context.Background(), UpscaleParams{
		Model:    ModelUpscale,
		ImageURL: "https://example.com/input.png",
	})
	if err != nil {
		t.Fatal(err)
	}
	if stub.method != "POST" || stub.path != "/api/v1/recraft/upscales" {
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

func TestUpscalesCreateValidatesRequiredParams(t *testing.T) {
	client := NewClientWithHTTP(&stubHTTPClient{})

	if _, err := client.Upscales.Create(context.Background(), UpscaleParams{ImageURL: "https://example.com/input.png"}); !core.IsValidation(err) {
		t.Fatalf("expected validation error for missing model, got %v", err)
	}
	if _, err := client.Upscales.Create(context.Background(), UpscaleParams{Model: ModelUpscale}); !core.IsValidation(err) {
		t.Fatalf("expected validation error for missing image_url, got %v", err)
	}
}

func TestBackgroundRemovalsCreate(t *testing.T) {
	stub := &stubHTTPClient{}
	client := NewClientWithHTTP(stub)
	_, err := client.BackgroundRemovals.Create(context.Background(), BackgroundRemovalParams{
		Model:    ModelBackgroundRemoval,
		ImageURL: "https://example.com/input.webp",
	})
	if err != nil {
		t.Fatal(err)
	}
	if stub.method != "POST" || stub.path != "/api/v1/recraft/background_removals" {
		t.Fatalf("unexpected request: %s %s", stub.method, stub.path)
	}
}

func TestBackgroundRemovalsCreateValidatesRequiredParams(t *testing.T) {
	client := NewClientWithHTTP(&stubHTTPClient{})

	if _, err := client.BackgroundRemovals.Create(context.Background(), BackgroundRemovalParams{ImageURL: "https://example.com/input.webp"}); !core.IsValidation(err) {
		t.Fatalf("expected validation error for missing model, got %v", err)
	}
	if _, err := client.BackgroundRemovals.Create(context.Background(), BackgroundRemovalParams{Model: ModelBackgroundRemoval}); !core.IsValidation(err) {
		t.Fatalf("expected validation error for missing image_url, got %v", err)
	}
}

func TestUpscalesGet(t *testing.T) {
	stub := &stubHTTPClient{}
	client := NewClientWithHTTP(stub)
	_, err := client.Upscales.Get(context.Background(), "task_upscale_123")
	if err != nil {
		t.Fatal(err)
	}
	if stub.method != "GET" || stub.path != "/api/v1/recraft/upscales/task_upscale_123" {
		t.Fatalf("unexpected request: %s %s", stub.method, stub.path)
	}
}
