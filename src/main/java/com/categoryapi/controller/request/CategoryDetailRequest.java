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
@Schema(description = "하위 카테고리 등록/삭제")
public class CategoryDetailRequest {

    @NotBlank
    @Schema(description = "상위카테고리", example = "상의", required = true)
    private String category;

    @NotBlank
    @Schema(description = "하위카테고리", example = "티셔츠", required = true)
    private String categoryDetail;

}
