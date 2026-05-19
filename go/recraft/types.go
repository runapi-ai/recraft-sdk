package recraft

type UpscaleImageModel string
type RemoveBackgroundModel string

const (
	ModelUpscale           UpscaleImageModel     = "recraft-crisp-upscale"
	ModelBackgroundRemoval RemoveBackgroundModel = "recraft-remove-background"
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

type UpscaleImageParams struct {
	Model       UpscaleImageModel `json:"model" help:"required; must be recraft-crisp-upscale"`
	ImageURL    string            `json:"image_url" help:"required; source image URL"`
	CallbackURL string            `json:"callback_url,omitempty" help:"optional; webhook URL"`
}

type RemoveBackgroundParams struct {
	Model       RemoveBackgroundModel `json:"model" help:"required; must be recraft-remove-background"`
	ImageURL    string                `json:"image_url" help:"required; source image URL"`
	CallbackURL string                `json:"callback_url,omitempty" help:"optional; webhook URL"`
}
