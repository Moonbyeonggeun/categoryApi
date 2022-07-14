package com.categoryapi.service;

import com.categoryapi.common.CodeUtill;
import com.categoryapi.controller.request.*;
import com.categoryapi.controller.response.CategoryReponse;
import com.categoryapi.controller.response.SearchCategoryResponse;
import com.categoryapi.entity.Category;
import com.categoryapi.entity.CategoryDetail;
import com.categoryapi.repository.CategoryDetailRepository;
import com.categoryapi.repository.CategoryRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryDetailRepository categoryDetailRepository;

    /**
     * 데이터 등록 확인
     */
    @Order(1)
    @Test
    void categoryInsertTest() {

        Category category = Category.builder()
                .categoryName("상의")
                .deleteFlg(CodeUtill.DELETE_FLAG_ON)
                .build();
        Category categorySaveResult = categoryRepository.save(category);

        assertEquals("상의", categorySaveResult.getCategoryName());
        assertEquals(CodeUtill.DELETE_FLAG_ON, categorySaveResult.getDeleteFlg());

        CategoryDetail categoryDetail = CategoryDetail.builder()
                .category(categorySaveResult)
                .categoryDetailName("티셔츠")
                .deleteFlg(CodeUtill.DELETE_FLAG_ON)
                .build();
        CategoryDetail categoryDetailSaveResult = categoryDetailRepository.save(categoryDetail);

        assertEquals("티셔츠", categoryDetailSaveResult.getCategoryDetailName());
        assertEquals(CodeUtill.DELETE_FLAG_ON, categoryDetailSaveResult.getDeleteFlg());

    }

    /**
     * 상위 카테고리 등록
     * 정상 등록 확인
     */
    @Order(2)
    @Test
    void categoryInsertTest001() {

        CategoryRequest request = new CategoryRequest();

        request.setCate("하의");
        CategoryReponse result = categoryService.insertCategory(request);

        assertEquals(CodeUtill.RESULT_SUCCESS, result.getResultCode());
    }

    /**
     * 상위 카테고리 등록
     * 중복 에러 확인
     */
    @Order(3)
    @Test
    void categoryInsertTest002() {

        CategoryRequest request = new CategoryRequest();

        request.setCate("하의");
        CategoryReponse result = categoryService.insertCategory(request);

        assertEquals(CodeUtill.RESULT_OVERLAP, result.getResultCode());

    }

    /**
     * 하위 카테고리 등록
     * 정상 등록 확인
     */
    @Order(4)
    @Test
    void categoryDetailInsetTest001() {

        CategoryDetailRequest request = new CategoryDetailRequest();

        request.setCategory("상의");
        request.setCategoryDetail("반팔");

        CategoryReponse result = categoryService.insertCategoryDetail(request);

        assertEquals(CodeUtill.RESULT_SUCCESS, result.getResultCode());

    }

    /**
     * 하위 카테고리 등록
     * 중복 에러 확인
     */
    @Order(5)
    @Test
    void categoryDetailInsetTest002() {

        CategoryDetailRequest request = new CategoryDetailRequest();

        request.setCategory("상의");
        request.setCategoryDetail("티셔츠");

        CategoryReponse result = categoryService.insertCategoryDetail(request);

        assertEquals(CodeUtill.RESULT_OVERLAP, result.getResultCode());

    }

    /**
     * 하위 카테고리 등록
     * 상위 카테고리가 존재하지 않아 등록 실패
     */
    @Order(6)
    @Test
    void categoryDetailInsetTest003() {

        CategoryDetailRequest request = new CategoryDetailRequest();

        request.setCategory("외투");
        request.setCategoryDetail("점퍼");

        CategoryReponse result = categoryService.insertCategoryDetail(request);

        assertEquals(CodeUtill.RESULT_FAIL, result.getResultCode());

    }

    /**
     * 카테고리 수정
     * 정상 수정 확인
     */
    @Order(7)
    @Test
    void categoryUpdateTest001() {

        UpdateCategoryRequest request = new UpdateCategoryRequest();

        request.setCate("하의");
        request.setUpdateCate("의류");

        CategoryReponse result = categoryService.updateCategory(request);

        assertEquals(CodeUtill.RESULT_SUCCESS, result.getResultCode());
    }

    /**
     * 카테고리 수정
     * 수정 대상 없음 에러 확인
     */
    @Order(8)
    @Test
    void categoryUpdateTest002() {
        UpdateCategoryRequest request = new UpdateCategoryRequest();

        request.setCate("아우터");
        request.setUpdateCate("외투");

        CategoryReponse result = categoryService.updateCategory(request);

        assertEquals(CodeUtill.RESULT_FAIL, result.getResultCode());
    }

    /**
     * 카테고리 수정
     * 수정 대상 중복 에러
     */
    @Order(9)
    @Test
    void categoryUpdateTest003() {

        UpdateCategoryRequest request = new UpdateCategoryRequest();

        request.setCate("의류");
        request.setUpdateCate("상의");

        CategoryReponse result = categoryService.updateCategory(request);

        assertEquals(CodeUtill.RESULT_OVERLAP, result.getResultCode());
    }

    /**
     * 하위 카테고리 수정
     * 정상 수정 확인
     */
    @Order(10)
    @Test
    void categoryDetailUpdateTest001() {
        UpdateCategoryDetailRequest request = new UpdateCategoryDetailRequest();

        request.setCate("상의");
        request.setCategoryDetail("티셔츠");
        request.setUpdateCategoryDetail("맨투맨");

        CategoryReponse result = categoryService.updateCategoryDetail(request);

        assertEquals(CodeUtill.RESULT_SUCCESS, result.getResultCode());
    }

    /**
     * 하위 카테고리 수정
     * 중복 에러 확인
     */
    @Order(11)
    @Test
    void categoryDetailUpdateTest002() {
        UpdateCategoryDetailRequest request = new UpdateCategoryDetailRequest();

        request.setCate("상의");
        request.setCategoryDetail("반팔");
        request.setUpdateCategoryDetail("맨투맨");

        CategoryReponse result = categoryService.updateCategoryDetail(request);

        assertEquals(CodeUtill.RESULT_OVERLAP, result.getResultCode());
    }

    /**
     * 하위 카테고리 수정
     * 상위 카테고리가 존재하지 않음
     */
    @Order(12)
    @Test
    void categoryDetailUpdateTest003() {
        UpdateCategoryDetailRequest request = new UpdateCategoryDetailRequest();

        request.setCate("아우터");
        request.setCategoryDetail("코트");
        request.setUpdateCategoryDetail("자켓");

        CategoryReponse result = categoryService.updateCategoryDetail(request);

        assertEquals(CodeUtill.RESULT_FAIL, result.getResultCode());
    }

    /**
     * 카테고리 삭제
     * 정상 삭제 확인
     */
    @Order(13)
    @Test
    void categoryDeleteTest001() {
        CategoryRequest request = new CategoryRequest();

        request.setCate("의류");

        CategoryReponse result = categoryService.deleteCategory(request);

        assertEquals(CodeUtill.RESULT_SUCCESS, result.getResultCode());
    }

    /**
     * 카테고리 삭제
     * 삭제 대상이 없어 에러
     */
    @Order(14)
    @Test
    void categoryDeleteTest002() {
        CategoryRequest request = new CategoryRequest();

        request.setCate("아우터");

        CategoryReponse result = categoryService.deleteCategory(request);

        assertEquals(CodeUtill.RESULT_FAIL, result.getResultCode());
    }

    /**
     * 하위 카테고리 삭제
     * 정상 삭제 확인
     */
    @Order(15)
    @Test
    void categoryDetailDeleteTest001() {
        CategoryDetailRequest request = new CategoryDetailRequest();

        request.setCategory("상의");
        request.setCategoryDetail("맨투맨");

        CategoryReponse result = categoryService.deleteCategoryDetail(request);

        assertEquals(CodeUtill.RESULT_SUCCESS, result.getResultCode());
    }

    /**
     * 하위 카테고리 삭제
     * 삭제 대상이 없어 에러
     */
    @Order(16)
    @Test
    void categoryDetailDeleteTest002() {
        CategoryDetailRequest request = new CategoryDetailRequest();

        request.setCategory("상의");
        request.setCategoryDetail("셔츠");

        CategoryReponse result = categoryService.deleteCategoryDetail(request);

        assertEquals(CodeUtill.RESULT_FAIL, result.getResultCode());
    }

    /**
     * 하위 카테고리 삭제
     * 상위 카테고리가 없어 에러
     */
    @Order(17)
    @Test
    void categoryDetailDeleteTest003() {
        CategoryDetailRequest request = new CategoryDetailRequest();

        request.setCategory("아우터");
        request.setCategoryDetail("셔츠");

        CategoryReponse result = categoryService.deleteCategoryDetail(request);

        assertEquals(CodeUtill.RESULT_FAIL, result.getResultCode());
    }

    /**
     * 카테고리 검색
     * 전체 검색
     */
    @Order(18)
    @Test
    void categorySearchTest001() {
        SearchCategoryRequest request = new SearchCategoryRequest();

        SearchCategoryResponse result = categoryService.searchCategory(request);

        assertEquals(CodeUtill.RESULT_SUCCESS, result.getResultCode());

    }

    /**
     * 카테고리 검색
     * 카테고리 지정 검색
     */
    @Order(19)
    @Test
    void categorySearchTest002() {
        SearchCategoryRequest request = new SearchCategoryRequest();
        request.setCategoryName("상의");

        SearchCategoryResponse result = categoryService.searchCategory(request);

        assertEquals(CodeUtill.RESULT_SUCCESS, result.getResultCode());
        assertEquals("상의", result.getCategoryListResponseList().get(0).getCategoryName());
    }
}