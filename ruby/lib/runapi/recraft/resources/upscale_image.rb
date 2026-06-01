# frozen_string_literal: true

module RunApi
  module Recraft
    module Resources
      class UpscaleImage
        include RunApi::Core::ResourceHelpers

        ENDPOINT = "/api/v1/recraft/upscale_image"

        RESPONSE_CLASS = Types::ImageTaskResponse
        COMPLETED_RESPONSE_CLASS = Types::CompletedImageTaskResponse

        def initialize(http)
          @http = http
        end

        def run(**params)
          task = create(**params)
          poll_until_complete { get(task.id) }
        end

        def create(**params)
          params = compact_params(params)
          validate_params!(params)
          request(:post, ENDPOINT, body: params)
        end

        def get(id)
          request(:get, "#{ENDPOINT}/#{id}")
        end

        private

        def validate_params!(params)
          raise Core::ValidationError, "model is required" unless param(params, :model)
          raise Core::ValidationError, "source_image_url is required" unless param(params, :source_image_url)

          model = param(params, :model)
          unless Types::UPSCALE_IMAGE_MODELS.include?(model)
            raise Core::ValidationError, "Invalid model: #{model}. Must be one of: #{Types::UPSCALE_IMAGE_MODELS.join(", ")}"
          end
        end
      end
    end
  end
end
