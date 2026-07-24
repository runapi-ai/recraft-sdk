# frozen_string_literal: true

module RunApi
  module Recraft
    module Resources
      # Background removal resource.
      # Produces a transparent cutout by removing the image background.
      class RemoveBackground
        include RunApi::Core::ResourceHelpers

        ENDPOINT = "/api/v1/recraft/remove_background"

        RESPONSE_CLASS = Types::ImageTaskResponse
        COMPLETED_RESPONSE_CLASS = Types::CompletedImageTaskResponse

        def initialize(http)
          @http = http
        end

        def run(options: nil, **params)
          task = create(options: options, **params)
          poll_until_complete { get(task.id, options: options) }
        end

        def create(options: nil, **params)
          params = compact_params(params)
          validate_contract!(CONTRACT["remove-background"], params)
          request(:post, ENDPOINT, body: params, options: options)
        end

        def get(id, options: nil)
          request(:get, "#{ENDPOINT}/#{id}", options: options)
        end
      end
    end
  end
end
