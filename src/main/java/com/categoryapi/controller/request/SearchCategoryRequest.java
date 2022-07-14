package com.categoryapi.controller.request;

import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Schema(description = "카테고리 조회")
public class SearchCategoryRequest {

    @Schema(description = "상위카테고리", example = "상의")
    private String categoryName;

}
