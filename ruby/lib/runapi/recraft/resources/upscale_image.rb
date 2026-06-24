# frozen_string_literal: true

module RunApi
  module Recraft
    module Resources
      # AI-powered image upscaling resource.
      # Enhance image resolution while preserving detail.
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
          validate_contract!(CONTRACT["upscale-image"], params)
          request(:post, ENDPOINT, body: params)
        end

        def get(id)
          request(:get, "#{ENDPOINT}/#{id}")
        end
      end
    end
  end
end
