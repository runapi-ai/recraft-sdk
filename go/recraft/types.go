package recraft

// UpscaleImageModel selects the upscaling engine. See [ModelUpscale].
type UpscaleImageModel string

// RemoveBackgroundModel selects the background removal engine. See [ModelBackgroundRemoval].
type RemoveBackgroundModel string

const (
	// ModelUpscale is the crisp upscaling model that enhances resolution while preserving detail.
	ModelUpscale UpscaleImageModel = "recraft-crisp-upscale"
	// ModelBackgroundRemoval isolates the foreground subject and outputs a transparent PNG.
	ModelBackgroundRemoval RemoveBackgroundModel = "recraft-remove-background"
)

// Image holds a URL to a processed image.
type Image struct {
	URL string `json:"url"`
}

// AsyncTaskResponse carries the task ID, lifecycle status, and error for all Recraft async operations.
type AsyncTaskResponse struct {
	ID     string `json:"id"`
	Status string `json:"status"`
	Error  string `json:"error,omitempty"`
}

func (r AsyncTaskResponse) GetID() string     { return r.ID }
func (r AsyncTaskResponse) GetStatus() string { return r.Status }
func (r AsyncTaskResponse) GetError() string  { return r.Error }

// ImageTaskResponse is the result of an upscale or background removal task.
type ImageTaskResponse struct {
	AsyncTaskResponse
	Images []Image `json:"images,omitempty"`
}

// UpscaleImageParams configures image upscaling. Both Model and ImageURL are required.
type UpscaleImageParams struct {
	Model       UpscaleImageModel `json:"model" help:"required; model slug"`
	ImageURL    string            `json:"source_image_url" help:"required; source image URL"`
	CallbackURL string            `json:"callback_url,omitempty" help:"optional; webhook URL"`
}

// RemoveBackgroundParams configures background removal. Both Model and ImageURL are required.
type RemoveBackgroundParams struct {
	Model       RemoveBackgroundModel `json:"model" help:"required; model slug"`
	ImageURL    string                `json:"source_image_url" help:"required; source image URL"`
	CallbackURL string                `json:"callback_url,omitempty" help:"optional; webhook URL"`
}
