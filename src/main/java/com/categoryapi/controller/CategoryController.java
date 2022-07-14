package com.categoryapi.controller;


import com.categoryapi.controller.request.*;
import com.categoryapi.controller.response.CategoryReponse;
import com.categoryapi.controller.response.SearchCategoryResponse;
import com.categoryapi.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category/")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "상위 카테고리 등록", description = "상위 카테고리 등록")
    @PostMapping("insertCategory")
    public CategoryReponse insertCategory(@Valid @RequestBody CategoryRequest categoryRequest) {

        return categoryService.insertCategory(categoryRequest);
    }

    @Operation(summary = "하위 카테고리 등록", description = "하위 카테고리 등록")
    @PostMapping("insertCategoryDetail")
    public CategoryReponse insertCategoryDetail(@Valid @RequestBody CategoryDetailRequest categoryDetailRequest) {

        return categoryService.insertCategoryDetail(categoryDetailRequest);
    }

    @Operation(summary = "상위 카테고리 수정", description = "상위 카테고리 수정")
    @PostMapping("updateCategory")
    public CategoryReponse updateCategory(@Valid @RequestBody UpdateCategoryRequest request) {

        return categoryService.updateCategory(request);
    }

    @Operation(summary = "하위 카테고리 수정", description = "하위 카테고리 수정")
    @PostMapping("updateCategoryDetail")
    public CategoryReponse updateCategoryDetail(@Valid @RequestBody UpdateCategoryDetailRequest request) {

        return categoryService.updateCategoryDetail(request);
    }

    @Operation(summary = "상위 카테고리 삭제", description = "하위 카테고리 삭제")
    @PostMapping("deleteCategory")
    public CategoryReponse delCategoryDetail(@Valid @RequestBody CategoryRequest categoryRequest) {

        return categoryService.deleteCategory(categoryRequest);
    }

    @Operation(summary = "하위 카테고리 삭제", description = "하위 카테고리 삭제")
    @PostMapping("deleteCategoryDetail")
    public CategoryReponse delCategoryDetail(@Valid @RequestBody CategoryDetailRequest categoryDetailRequest) {

        return categoryService.deleteCategoryDetail(categoryDetailRequest);
    }

    @Operation(summary = "카테고리 검색", description = "카테고리 검색")
    @PostMapping("searchCategory")
    public SearchCategoryResponse delCategoryDetail(@RequestBody SearchCategoryRequest request) {

        return categoryService.searchCategory(request);
    }

}
