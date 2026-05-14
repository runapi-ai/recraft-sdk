# frozen_string_literal: true

module RunApi
  module Recraft
    module Types
      UPSCALE_MODELS = %w[recraft-crisp-upscale].freeze
      BACKGROUND_REMOVAL_MODELS = %w[recraft-remove-background].freeze

      class Image < RunApi::Core::BaseModel
        optional :url, String
      end

      class ImageTaskResponse < RunApi::Core::TaskResponse
        required :id, String
        optional :status, String, enum: -> { RunApi::Core::TaskResponse::Status::ALL }
        optional :images, [ -> { Image } ]
        optional :error, String
      end

      class CompletedImageTaskResponse < ImageTaskResponse
        required :images, [ -> { Image } ]
      end
    end
  end
end
