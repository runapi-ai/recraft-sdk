# frozen_string_literal: true

require "spec_helper"

RSpec.describe RunApi::Recraft::Resources::RemoveBackground do
  let(:http) { instance_double(RunApi::Core::HttpClient) }
  let(:remove_background) { described_class.new(http) }
  let(:endpoint) { "/api/v1/recraft/remove_background" }

  it "POSTs to the correct endpoint with params" do
    params = {model: "recraft-remove-background", source_image_url: "https://cdn.runapi.ai/public/samples/input.webp"}
    expect(http).to receive(:request).with(:post, endpoint, body: params).and_return("id" => "task-2")

    result = remove_background.create(**params)
    expect(result).to be_a(RunApi::Recraft::Types::ImageTaskResponse)
    expect(result.id).to eq("task-2")
  end

  it "GETs the correct endpoint" do
    expect(http).to receive(:request).with(:get, "#{endpoint}/task-2").and_return(
      "id" => "task-2",
      "status" => "completed",
      "images" => [{"url" => "https://file.runapi.ai/bg.png"}]
    )

    result = remove_background.get("task-2")
    expect(result.images.first.url).to eq("https://file.runapi.ai/bg.png")
  end
end
