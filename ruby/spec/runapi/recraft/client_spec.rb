# frozen_string_literal: true

require "open3"
require "rbconfig"
require "spec_helper"

RSpec.describe RunApi::Recraft::Client do
  before do
    allow(ConnectionPool).to receive(:new).and_return(instance_double(ConnectionPool))
  end

  after { RunApi.api_key = nil }

  it "accepts api_key as parameter" do
    client = described_class.new(api_key: "param-key")
    expect(client).to be_a(described_class)
  end

  it "exposes both resources" do
    client = described_class.new(api_key: "test-key")
    expect(client.upscale_image).to be_a(RunApi::Recraft::Resources::UpscaleImage)
    expect(client.remove_background).to be_a(RunApi::Recraft::Resources::RemoveBackground)
  end

  it "loads without relying on another SDK to load core first" do
    ruby_libs = %w[runapi-recraft runapi-core].map do |gem_name|
      File.expand_path("../../../gems/#{gem_name}/lib", __dir__)
    end
    output, status = Open3.capture2e(
      {"RUBYLIB" => ruby_libs.join(File::PATH_SEPARATOR)},
      RbConfig.ruby,
      "-e",
      "require 'runapi/recraft'; puts RunApi::Recraft::Client.name"
    )

    expect(status).to be_success, output
    expect(output).to include("RunApi::Recraft::Client")
  end
end
