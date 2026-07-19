# frozen_string_literal: true

require "spec_helper"

RSpec.describe RunApi::Recraft::Resources::UpscaleImage do
  let(:http) { instance_double(RunApi::Core::HttpClient) }
  let(:upscale_image) { described_class.new(http) }
  let(:endpoint) { "/api/v1/recraft/upscale_image" }

  it "POSTs to the correct endpoint with params" do
    params = {model: "recraft-crisp-upscale", source_image_url: "https://cdn.runapi.ai/public/samples/input.png"}
    expect(http).to receive(:request).with(:post, endpoint, body: params).and_return("id" => "task-1")

    result = upscale_image.create(**params)
    expect(result).to be_a(RunApi::Recraft::Types::ImageTaskResponse)
    expect(result.id).to eq("task-1")
  end

  it "GETs the correct endpoint" do
    expect(http).to receive(:request).with(:get, "#{endpoint}/task-1").and_return(
      "id" => "task-1",
      "status" => "completed",
      "images" => [{"url" => "https://file.runapi.ai/out.png"}]
    )

    result = upscale_image.get("task-1")
    expect(result.images.first.url).to eq("https://file.runapi.ai/out.png")
  end
end
