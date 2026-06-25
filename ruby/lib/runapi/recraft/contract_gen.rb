# frozen_string_literal: true

module RunApi
  module Recraft
    CONTRACT = {
      "remove-background" => {
        "models" => ["recraft-remove-background"],
        "fields_by_model" => {
          "recraft-remove-background" => {
            "source_image_url" => {
              "required" => true
            }
          }
        }
      },
      "upscale-image" => {
        "models" => ["recraft-crisp-upscale"],
        "fields_by_model" => {
          "recraft-crisp-upscale" => {
            "source_image_url" => {
              "required" => true
            }
          }
        }
      }
    }.freeze
  end
end
