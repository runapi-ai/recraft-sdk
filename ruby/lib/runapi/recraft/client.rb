# frozen_string_literal: true

module RunApi
  module Recraft
    # Recraft image post-processing API client.
    #
    # Provides AI-powered image upscaling and background removal.
    #
    # @example
    #   client = RunApi::Recraft::Client.new(api_key: "your-api-key")
    #
    #   upscaled = client.upscale_image.run(
    #     model: "recraft-crisp-upscale",
    #     source_image_url: "https://cdn.runapi.ai/public/samples/image.jpg"
    #   )
    #
    #   cutout = client.remove_background.run(
    #     model: "recraft-remove-background",
    #     source_image_url: "https://cdn.runapi.ai/public/samples/image.jpg"
    #   )
    class Client < RunApi::Core::Client
      # @return [Resources::UpscaleImage] AI-powered image upscaling operations.
      attr_reader :upscale_image
      # @return [Resources::RemoveBackground] Background removal operations.
      attr_reader :remove_background

      def initialize(api_key: nil, **options)
        super
        @upscale_image = Resources::UpscaleImage.new(http)
        @remove_background = Resources::RemoveBackground.new(http)
      end
    end
  end
end
