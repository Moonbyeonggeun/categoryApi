package com.categoryapi.controller.response;

import lombok.Data;

import java.util.List;

@Data
public class CategoryListResponse {

    private Long categoryNo;

    private String categoryName;

    private List<CategoryDetailResponse> categoryDetailResponseList;

}
