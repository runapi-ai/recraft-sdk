package recraft

import (
	"context"

	"github.com/runapi-ai/core-sdk/go/core"
	"github.com/runapi-ai/core-sdk/go/option"
)

const (
	upscalesPath           = "/api/v1/recraft/upscales"
	backgroundRemovalsPath = "/api/v1/recraft/background_removals"
)

type Client struct {
	Upscales           *Upscales
	BackgroundRemovals *BackgroundRemovals
}

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

func NewClientWithHTTP(httpClient core.HTTPClient) *Client {
	return &Client{
		Upscales:           &Upscales{http: httpClient},
		BackgroundRemovals: &BackgroundRemovals{http: httpClient},
	}
}

type Upscales struct{ http core.HTTPClient }

func (r *Upscales) Create(ctx context.Context, params UpscaleParams, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	body := core.CompactParams(params)
	if body["model"] == nil {
		return nil, core.NewError(core.ErrValidation, "model is required", 422, "", nil, nil)
	}
	if body["image_url"] == nil {
		return nil, core.NewError(core.ErrValidation, "image_url is required", 422, "", nil, nil)
	}
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.PostJSON[core.TaskCreateResponse](ctx, r.http, upscalesPath, body, requestOptions)
}
func (r *Upscales) Get(ctx context.Context, id string, opts ...option.RequestOption) (*ImageTaskResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.GetJSON[ImageTaskResponse](ctx, r.http, core.ResourcePath(upscalesPath, id), requestOptions)
}
func (r *Upscales) Run(ctx context.Context, params UpscaleParams, opts ...option.RequestOption) (*ImageTaskResponse, error) {
	_, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.RunAsync(ctx, func(ctx context.Context) (*core.TaskCreateResponse, error) { return r.Create(ctx, params, opts...) }, func(ctx context.Context, id string) (*ImageTaskResponse, error) { return r.Get(ctx, id, opts...) }, pollingOptions)
}

type BackgroundRemovals struct{ http core.HTTPClient }

func (r *BackgroundRemovals) Create(ctx context.Context, params BackgroundRemovalParams, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	body := core.CompactParams(params)
	if body["model"] == nil {
		return nil, core.NewError(core.ErrValidation, "model is required", 422, "", nil, nil)
	}
	if body["image_url"] == nil {
		return nil, core.NewError(core.ErrValidation, "image_url is required", 422, "", nil, nil)
	}
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.PostJSON[core.TaskCreateResponse](ctx, r.http, backgroundRemovalsPath, body, requestOptions)
}
func (r *BackgroundRemovals) Get(ctx context.Context, id string, opts ...option.RequestOption) (*ImageTaskResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.GetJSON[ImageTaskResponse](ctx, r.http, core.ResourcePath(backgroundRemovalsPath, id), requestOptions)
}
func (r *BackgroundRemovals) Run(ctx context.Context, params BackgroundRemovalParams, opts ...option.RequestOption) (*ImageTaskResponse, error) {
	_, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.RunAsync(ctx, func(ctx context.Context) (*core.TaskCreateResponse, error) { return r.Create(ctx, params, opts...) }, func(ctx context.Context, id string) (*ImageTaskResponse, error) { return r.Get(ctx, id, opts...) }, pollingOptions)
}
