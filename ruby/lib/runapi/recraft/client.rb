# frozen_string_literal: true

module RunApi
  module Recraft
    class Client
      attr_reader :upscales, :background_removals

      def initialize(api_key: nil, **options)
        @api_key = Core::Auth.resolve_api_key(api_key)

        client_options = Core::ClientOptions.new(api_key: @api_key, **options)
        http = client_options.http_client || Core::HttpClient.new(client_options)
        @upscales = Resources::Upscales.new(http)
        @background_removals = Resources::BackgroundRemovals.new(http)
      end
    end
  end
end
