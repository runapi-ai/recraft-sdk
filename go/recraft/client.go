package recraft

import (
	"context"

	"github.com/runapi-ai/core-sdk/go/core"
	"github.com/runapi-ai/core-sdk/go/option"
)

const (
	upscaleImagePath     = "/api/v1/recraft/upscale_image"
	removeBackgroundPath = "/api/v1/recraft/remove_background"
)

type Client struct {
	UpscaleImage     *UpscaleImage
	RemoveBackground *RemoveBackground
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
		UpscaleImage:     &UpscaleImage{http: httpClient},
		RemoveBackground: &RemoveBackground{http: httpClient},
	}
}

type UpscaleImage struct{ http core.HTTPClient }

func (r *UpscaleImage) Create(ctx context.Context, params UpscaleImageParams, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	body := core.CompactParams(params)
	if body["model"] == nil {
		return nil, core.NewError(core.ErrValidation, "model is required", 422, "", nil, nil)
	}
	if body["image_url"] == nil {
		return nil, core.NewError(core.ErrValidation, "image_url is required", 422, "", nil, nil)
	}
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.PostJSON[core.TaskCreateResponse](ctx, r.http, upscaleImagePath, body, requestOptions)
}
func (r *UpscaleImage) Get(ctx context.Context, id string, opts ...option.RequestOption) (*ImageTaskResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.GetJSON[ImageTaskResponse](ctx, r.http, core.ResourcePath(upscaleImagePath, id), requestOptions)
}
func (r *UpscaleImage) Run(ctx context.Context, params UpscaleImageParams, opts ...option.RequestOption) (*ImageTaskResponse, error) {
	_, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.RunAsync(ctx, func(ctx context.Context) (*core.TaskCreateResponse, error) { return r.Create(ctx, params, opts...) }, func(ctx context.Context, id string) (*ImageTaskResponse, error) { return r.Get(ctx, id, opts...) }, pollingOptions)
}

type RemoveBackground struct{ http core.HTTPClient }

func (r *RemoveBackground) Create(ctx context.Context, params RemoveBackgroundParams, opts ...option.RequestOption) (*core.TaskCreateResponse, error) {
	body := core.CompactParams(params)
	if body["model"] == nil {
		return nil, core.NewError(core.ErrValidation, "model is required", 422, "", nil, nil)
	}
	if body["image_url"] == nil {
		return nil, core.NewError(core.ErrValidation, "image_url is required", 422, "", nil, nil)
	}
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.PostJSON[core.TaskCreateResponse](ctx, r.http, removeBackgroundPath, body, requestOptions)
}
func (r *RemoveBackground) Get(ctx context.Context, id string, opts ...option.RequestOption) (*ImageTaskResponse, error) {
	requestOptions, _ := option.ResolveRequestOptions(opts...)
	return core.GetJSON[ImageTaskResponse](ctx, r.http, core.ResourcePath(removeBackgroundPath, id), requestOptions)
}
func (r *RemoveBackground) Run(ctx context.Context, params RemoveBackgroundParams, opts ...option.RequestOption) (*ImageTaskResponse, error) {
	_, pollingOptions := option.ResolveRequestOptions(opts...)
	return core.RunAsync(ctx, func(ctx context.Context) (*core.TaskCreateResponse, error) { return r.Create(ctx, params, opts...) }, func(ctx context.Context, id string) (*ImageTaskResponse, error) { return r.Get(ctx, id, opts...) }, pollingOptions)
}
