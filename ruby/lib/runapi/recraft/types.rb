# frozen_string_literal: true

module RunApi
  module Recraft
    # Type definitions and constants for the Recraft image processing API.
    module Types
      # Crisp upscaling model that enhances resolution while preserving detail.
      UPSCALE_IMAGE_MODELS = %w[recraft-crisp-upscale].freeze
      # Background removal model that isolates the foreground subject.
      REMOVE_BACKGROUND_MODELS = %w[recraft-remove-background].freeze

      # URL to a processed image (upscaled or background-removed).
      class Image < RunApi::Core::BaseModel
        optional :url, String
      end

      # Async image task result shared by upscale and background removal operations.
      class ImageTaskResponse < RunApi::Core::TaskResponse
        required :id, String
        optional :status, String, enum: -> { RunApi::Core::TaskResponse::Status::ALL }
        optional :images, [-> { Image }]
        optional :error, String
      end

      # Narrowed response returned by +run+ once polling confirms completion.
      # Images are guaranteed present.
      class CompletedImageTaskResponse < ImageTaskResponse
        required :images, [-> { Image }]
      end
    end
  end
end
