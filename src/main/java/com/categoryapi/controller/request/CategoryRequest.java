package com.categoryapi.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Schema(description = "상위 카테고리 등록/삭제")
public class CategoryRequest {

    @NotBlank
    @Schema(description = "상위카테고리", example = "상의", required = true)
    private String cate;
}
