package com.categoryapi.controller.response;

import lombok.Data;

import java.util.List;

@Data
public class SearchCategoryResponse {

    private List<CategoryListResponse> categoryListResponseList;

    private String resultCode;

}
