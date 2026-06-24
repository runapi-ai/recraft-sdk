// Package recraft provides the Recraft image processing API client for upscaling and background removal.
//
//	client, err := recraft.NewClient(option.WithAPIKey("sk-your-api-key"))
//	result, err := client.UpscaleImage.Run(ctx, recraft.UpscaleImageParams{
//	    Model: recraft.ModelUpscale, ImageURL: "https://example.com/photo.jpg",
//	})
package recraft

import (
	"context"

	"github.com/runapi-ai/core-sdk/go/base"
	"github.com/runapi-ai/core-sdk/go/core"
	"github.com/runapi-ai/core-sdk/go/option"
)

const (
	upscaleImagePath     = "/api/v1/recraft/upscale_image"
	removeBackgroundPath = "/api/v1/recraft/remove_background"
)

// Client provides image upscaling and background removal powered by Recraft.
type Client struct {
	base.Base
	UpscaleImage     *UpscaleImage
	RemoveBackground *RemoveBackground
}

// NewClient creates a Recraft client with the given options.
func NewClient(opts ...option.ClientOption) (*Client, error) {
	resolved, err := option.ResolveClientOptions(opts...)
	if err != nil {
		return nil, err
	}
	httpClient, err := core.NewHTTPClient(resolved)
	if err != nil {
		return nil, err
	}
	return NewClientWithHTTP(httpClient), nil
}

// NewClientWithHTTP creates a Recraft client with a pre-configured HTTP transport.
func NewClientWithHTTP(httpClient core.HTTPClient) *Client {
	return &Client{
		Base:             base.New(httpClient),
		UpscaleImage:     &UpscaleImage{http: httpClient},
		RemoveBackground: &RemoveBackground{http: httpClient},
	}
}

// UpscaleImage increases image resolution while preserving detail and sharpness.
// Uses [ModelUpscale] ("recraft-crisp-upscale").
type UpscaleImage struct{ http core.HTTPClient }

// Create submits an upscale-image task and returns immediately with a task id.
func (r *UpscaleImage) Create(ctx context.Context, params UpscaleImageParams, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	body := core.CompactParams(params)
	if err := core.ValidateParams(contractSchema["upscale-image"], body); err != nil {
		return nil, err
	}
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.PostJSON[core.TaskCreateResponse](ctx, r.http, upscaleImagePath, body, requestOptions)
}

// Get fetches the current status of an upscale-image task by id.
func (r *UpscaleImage) Get(ctx context.Context, id string, opts ...option.RequestOption) (*ImageTaskResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.GetJSON[ImageTaskResponse](ctx, r.http, core.ResourcePath(upscaleImagePath, id), requestOptions)
}

// Run submits an upscale-image task and polls until it completes.
func (r *UpscaleImage) Run(ctx context.Context, params UpscaleImageParams, opts ...option.RequestOption) (*ImageTaskResponse, error) {
	_, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.RunAsync(ctx, func(ctx context.Context) (*core.TaskCreateResponse, error) { return r.Create(ctx, params, opts...) }, func(ctx context.Context, id string) (*ImageTaskResponse, error) { return r.Get(ctx, id, opts...) }, pollingOptions)
}

// RemoveBackground isolates the foreground subject and removes the background, producing a transparent PNG.
// Uses [ModelBackgroundRemoval] ("recraft-remove-background").
type RemoveBackground struct{ http core.HTTPClient }

// Create submits a remove-background task and returns immediately with a task id.
func (r *RemoveBackground) Create(ctx context.Context, params RemoveBackgroundParams, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	body := core.CompactParams(params)
	if err := core.ValidateParams(contractSchema["remove-background"], body); err != nil {
		return nil, err
	}
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.PostJSON[core.TaskCreateResponse](ctx, r.http, removeBackgroundPath, body, requestOptions)
}

// Get fetches the current status of a remove-background task by id.
func (r *RemoveBackground) Get(ctx context.Context, id string, opts ...option.RequestOption) (*ImageTaskResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.GetJSON[ImageTaskResponse](ctx, r.http, core.ResourcePath(removeBackgroundPath, id), requestOptions)
}

// Run submits a remove-background task and polls until it completes.
func (r *RemoveBackground) Run(ctx context.Context, params RemoveBackgroundParams, opts ...option.RequestOption) (*ImageTaskResponse, error) {
	_, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.RunAsync(ctx, func(ctx context.Context) (*core.TaskCreateResponse, error) { return r.Create(ctx, params, opts...) }, func(ctx context.Context, id string) (*ImageTaskResponse, error) { return r.Get(ctx, id, opts...) }, pollingOptions)
}
