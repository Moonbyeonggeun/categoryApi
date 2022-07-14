package com.categoryapi.controller.request;

import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Schema(description = "상위 카테고리 수정")
public class UpdateCategoryRequest {

    @NotBlank
    @Schema(description = "수정 전 카테고리", example = "상의", required = true)
    private String cate;

    @NotBlank
    @Schema(description = "수정 후 카테고리", example = "아우터", required = true)
    private String updateCate;

}
