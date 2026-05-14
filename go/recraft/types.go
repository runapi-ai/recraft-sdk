package recraft

type UpscaleModel string
type BackgroundRemovalModel string

const (
	ModelUpscale           UpscaleModel           = "recraft-crisp-upscale"
	ModelBackgroundRemoval BackgroundRemovalModel = "recraft-remove-background"
)

type Image struct {
	URL string `json:"url"`
}

type AsyncTaskResponse struct {
	ID     string `json:"id"`
	Status string `json:"status"`
	Error  string `json:"error,omitempty"`
}

func (r AsyncTaskResponse) GetID() string     { return r.ID }
func (r AsyncTaskResponse) GetStatus() string { return r.Status }
func (r AsyncTaskResponse) GetError() string  { return r.Error }

type ImageTaskResponse struct {
	AsyncTaskResponse
	Images []Image `json:"images,omitempty"`
}

type UpscaleParams struct {
	Model       UpscaleModel `json:"model" help:"required; must be recraft-crisp-upscale"`
	ImageURL    string       `json:"image_url" help:"required; source image URL"`
	CallbackURL string       `json:"callback_url,omitempty" help:"optional; webhook URL"`
}

type BackgroundRemovalParams struct {
	Model       BackgroundRemovalModel `json:"model" help:"required; must be recraft-remove-background"`
	ImageURL    string                 `json:"image_url" help:"required; source image URL"`
	CallbackURL string                 `json:"callback_url,omitempty" help:"optional; webhook URL"`
}
