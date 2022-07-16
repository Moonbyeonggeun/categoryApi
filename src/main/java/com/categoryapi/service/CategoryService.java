package com.categoryapi.service;

import com.categoryapi.common.CodeUtill;
import com.categoryapi.common.RedisDaoPattern;
import com.categoryapi.controller.request.*;
import com.categoryapi.controller.response.CategoryDetailResponse;
import com.categoryapi.controller.response.CategoryListResponse;
import com.categoryapi.controller.response.CategoryReponse;
import com.categoryapi.controller.response.SearchCategoryResponse;
import com.categoryapi.entity.Category;
import com.categoryapi.entity.CategoryDetail;
import com.categoryapi.repository.CategoryDetailRepository;
import com.categoryapi.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryDetailRepository categoryDetailRepository;

    @Autowired
    private RedisDaoPattern redisDaoPattern = new RedisDaoPattern();

    /**
     * 상위 카테고리 등록
     * @param request
     * @return
     */
    @Transactional
    public CategoryReponse insertCategory(CategoryRequest request) {
        String result;
        CategoryReponse reponse = new CategoryReponse();
        try {

            log.info("start insert Category  param : " + request.getCate());

            // 존재 체크
            Category category = categoryDataChk(request.getCate());

            Category upsertCate;

            // 카테고리 등록
            // 카테고리 첫 등록인 경우
            if (category == null) {

                // 카테고리 등록
                upsertCate = Category.builder()
                        .categoryName(request.getCate())
                        .deleteFlg(CodeUtill.DELETE_FLAG_ON)
                        .build();
            } else {

                // 삭제된 카테고리가 존재하는 경우
                // 삭제플래그 해제
                if (category.getDeleteFlg() == 1) {

                    upsertCate = Category.builder()
                            .categoryNo(category.getCategoryNo())
                            .categoryName(category.getCategoryName())
                            .deleteFlg(CodeUtill.DELETE_FLAG_ON)
                            .build();
                } else {

                    // 카테고리 중복 코드 리턴(300)
                    result = CodeUtill.RESULT_OVERLAP;
                    reponse.setResultCode(result);
                    return reponse;
                }
            }

            // 상위 카테고리 upsert
            categoryRepository.save(upsertCate);

            // redis 값 초기화
            redisDaoPattern.delRedisKey(CodeUtill.SEARCH_KEY + CodeUtill.SEARCH_KEY_ALL);

            // 등록 성공 코드 리턴(200)
            result = CodeUtill.RESULT_SUCCESS;

        } catch(Exception e) {

            log.error(e.getMessage());

            // 등록 실패 코드 리턴(400)
            result = CodeUtill.RESULT_ERROR;
        }

        log.info("end insert Category");
        reponse.setResultCode(result);
        return reponse;
    }

    /**
     * 상위 카테고리 수정
     * @param request
     * @return
     */
    @Transactional
    public CategoryReponse updateCategory(UpdateCategoryRequest request) {
        String result;
        CategoryReponse reponse = new CategoryReponse();

        try {

            log.info("start update Category  param : " + request.getCate() + "   " + request.getUpdateCate());

            // 존재 체크
            Category category = categoryDataChk(request.getCate(), CodeUtill.DELETE_FLAG_ON);

            // 수정 대상 카테고리가 존재하지 않거나, 삭제되어 있는 경우
            if (category == null) {

                // 카테고리 등록, 수정 실패 코드 리턴(500)
                result = CodeUtill.RESULT_FAIL;
                reponse.setResultCode(result);
                return reponse;
            }

            // 중복 체크
            Category categoryOverlapChk = categoryDataChk(request.getUpdateCate());

            // 수정할 카테고리가 이미 존재 하는 경우
            if (categoryOverlapChk != null && categoryOverlapChk.getDeleteFlg() == CodeUtill.DELETE_FLAG_ON) {
                // 카테고리 중복 코드 리턴(300)
                result = CodeUtill.RESULT_OVERLAP;
                reponse.setResultCode(result);
                return reponse;

                // 수정할 카테고리가 삭제되어 있는 경우
            } else if (categoryOverlapChk != null && categoryOverlapChk.getDeleteFlg() == CodeUtill.DELETE_FLAG_OFF) {

                // 수정 대상 삭제 플래그 1
                Category updateCate = Category.builder()
                        .categoryNo(categoryOverlapChk.getCategoryNo())
                        .categoryName(request.getCate())
                        .deleteFlg(CodeUtill.DELETE_FLAG_OFF)
                        .build();

                // 상위 카테고리 upsert
                categoryRepository.save(updateCate);

                // 수정 할 대상 삭제 플래그 0
                updateCate = Category.builder()
                        .categoryNo(category.getCategoryNo())
                        .categoryName(request.getUpdateCate())
                        .deleteFlg(CodeUtill.DELETE_FLAG_ON)
                        .build();

                // 상위 카테고리 upsert
                categoryRepository.save(updateCate);

                // 카테고리 수정
            } else {

                // 카테고리 수정
                Category updateCate = Category.builder()
                        .categoryNo(category.getCategoryNo())
                        .categoryName(request.getUpdateCate())
                        .deleteFlg(CodeUtill.DELETE_FLAG_ON)
                        .build();

                // 상위 카테고리 upsert
                categoryRepository.save(updateCate);
            }

            // redis 값 초기화
            redisDaoPattern.delRedisKey(CodeUtill.SEARCH_KEY + CodeUtill.SEARCH_KEY_ALL);

            // 수정 전 카테고리 초기화
            redisDaoPattern.delRedisKey(CodeUtill.SEARCH_KEY + setUnicode(request.getCate()));

            // 등록 성공 코드 리턴(200)
            result = CodeUtill.RESULT_SUCCESS;

        } catch(Exception e) {
            log.error(e.getMessage());

            // 등록 실패 코드 리턴(400)
            result = CodeUtill.RESULT_ERROR;
        }

        log.info("end update Category");
        reponse.setResultCode(result);
        return reponse;
    }

    /**
     * 상위 카테고리 삭제
     * @param request
     * @return
     */
    @Transactional
    public CategoryReponse deleteCategory(CategoryRequest request) {
        String result;
        CategoryReponse reponse = new CategoryReponse();

        try {

            log.info("start delete Category   param : " + request.getCate());

            // 존재 체크
            Category category = categoryDataChk(request.getCate(), CodeUtill.DELETE_FLAG_ON);

            // 카테고리 삭제
            if (category != null) {

                // 하위 카테고리 삭제
                if (!category.getCategoryDetailList().isEmpty()) {
                    // 삭제되지 않은 하위 카테고리 조회
                    List<CategoryDetail> categoryDetailList = category.getCategoryDetailList().stream().filter(a -> a.getDeleteFlg() == CodeUtill.DELETE_FLAG_ON).collect(Collectors.toList());

                    for (CategoryDetail detail : categoryDetailList) {

                        // 상위 카테고리에 해당하는 하위 카테고리 삭제
                        CategoryDetail delCategoryDetail = CategoryDetail.builder()
                                .categoryDetailNo(detail.getCategoryDetailNo())
                                .categoryDetailName(detail.getCategoryDetailName())
                                .category(category)
                                .deleteFlg(CodeUtill.DELETE_FLAG_OFF)
                                .build();

                        categoryDetailRepository.save(delCategoryDetail);
                    }
                }

                // 상위 카테고리 삭제
                Category delCategory = Category.builder()
                        .categoryNo(category.getCategoryNo())
                        .categoryName(category.getCategoryName())
                        .deleteFlg(CodeUtill.DELETE_FLAG_OFF)
                        .build();

                categoryRepository.save(delCategory);

                // redis 값 초기화
                redisDaoPattern.delRedisKey(CodeUtill.SEARCH_KEY + CodeUtill.SEARCH_KEY_ALL);

                // 삭제된 카테고리 초기화
                redisDaoPattern.delRedisKey(CodeUtill.SEARCH_KEY + setUnicode(request.getCate()));

                // 등록 성공 코드 리턴(200)
                result = CodeUtill.RESULT_SUCCESS;
            } else {

                // 이미 삭제된 데이터
                // 등록, 수정 실패 (잘못된 등록방법)
                result = CodeUtill.RESULT_FAIL;
            }


        } catch(Exception e) {

            log.error(e.getMessage());

            // 실패 코드 리턴(400)
            result = CodeUtill.RESULT_ERROR;
        }

        log.info("end delete Category");
        reponse.setResultCode(result);
        return reponse;
    }

    /**
     * 하위 카테고리 등록
     * @param request
     * @return
     */
    @Transactional
    public CategoryReponse insertCategoryDetail(CategoryDetailRequest request) {
        String result;
        CategoryReponse reponse = new CategoryReponse();

        try {

            log.info("start insert CategoryDetail   param : " + request.getCategory() + "    " + request.getCategoryDetail());

            // 존재 체크
            Category category = categoryDataChk(request.getCategory(), CodeUtill.DELETE_FLAG_ON);

            // 상위 카테고리가 존재 하는 경우
            if (category != null) {

                // 하위 카테고리 조회
                CategoryDetail categoryDetail = category.getCategoryDetailList().stream().filter(a -> a.getCategoryDetailName().equals(request.getCategoryDetail())).findFirst().orElse(null);

                // 하위 카테고리 체크
                if (categoryDetail != null && categoryDetail.getDeleteFlg() == CodeUtill.DELETE_FLAG_ON) {

                    // 하위 카테고리가 존재함
                    // 카테고리 중복 코드 리턴(300)
                    result = CodeUtill.RESULT_OVERLAP;

                } else if (categoryDetail != null && categoryDetail.getDeleteFlg() == CodeUtill.DELETE_FLAG_OFF) {

                    // 하위 카테고리 등록
                    CategoryDetail upsertCategoryDetail = CategoryDetail.builder()
                            .categoryDetailNo(categoryDetail.getCategoryDetailNo())
                            .category(category)
                            .categoryDetailName(request.getCategoryDetail())
                            .deleteFlg(CodeUtill.DELETE_FLAG_ON)
                            .build();

                    categoryDetailRepository.save(upsertCategoryDetail);

                    // 등록 성공 코드 리턴(200)
                    result = CodeUtill.RESULT_SUCCESS;

                } else {

                    // 하위 카테고리 등록
                    CategoryDetail upsertCategoryDetail = CategoryDetail.builder()
                            .category(category)
                            .categoryDetailName(request.getCategoryDetail())
                            .deleteFlg(CodeUtill.DELETE_FLAG_ON)
                            .build();

                    categoryDetailRepository.save(upsertCategoryDetail);

                    // 등록 성공 코드 리턴(200)
                    result = CodeUtill.RESULT_SUCCESS;
                }

            } else {
                // 상위 카테고리가 존재 하지 않음
                result = CodeUtill.RESULT_FAIL;

            }

            // result가 성공 코드인 경우
            if (CodeUtill.RESULT_SUCCESS.equals(result)) {
                // redis 값 초기화
                redisDaoPattern.delRedisKey(CodeUtill.SEARCH_KEY + CodeUtill.SEARCH_KEY_ALL);

                // 수정 전 카테고리 초기화
                redisDaoPattern.delRedisKey(CodeUtill.SEARCH_KEY + setUnicode(request.getCategory()));
            }

        } catch(Exception e) {
            log.error(e.getMessage());

            // 등록 실패 코드 리턴(400)
            result = CodeUtill.RESULT_ERROR;
        }
        log.info("end insert CategoryDetail");
        reponse.setResultCode(result);
        return reponse;
    }

    /**
     * 하위 카테고리 수정
     * @param request
     * @return
     */
    @Transactional
    public CategoryReponse updateCategoryDetail(UpdateCategoryDetailRequest request) {
        String result;
        CategoryReponse reponse = new CategoryReponse();

        try {

            log.info("start update CategoryDetail   param : " + request.getCate() + "    " + request.getCategoryDetail() + "    " + request.getUpdateCategoryDetail());

            // 존재 체크
            Category category = categoryDataChk(request.getCate(), CodeUtill.DELETE_FLAG_ON);

            // 상위 카테고리가 없는 경우
            if (category == null) {

                // 카테고리 등록, 수정 실패 코드 리턴(500)
                result = CodeUtill.RESULT_FAIL;
                reponse.setResultCode(result);
                return reponse;
            }

            // 하위 카테고리 존재 체크
            CategoryDetail categoryDetail = categoryDetailDataChk(category, request.getCategoryDetail(), CodeUtill.DELETE_FLAG_ON);

            // 하위 카테고리가 존재하지 않을때
            if (categoryDetail == null) {

                // 카테고리 등록, 수정 실패 코드 리턴(500)
                result = CodeUtill.RESULT_FAIL;
                reponse.setResultCode(result);
                return reponse;
            }

            // 수정 후 하위 카테고리 존재 체크
            CategoryDetail categoryDetailOverlap = categoryDetailDataChk(category, request.getUpdateCategoryDetail());

            // 수정 할 카테고리가 존재하는 경우
            if (categoryDetailOverlap != null && categoryDetailOverlap.getDeleteFlg() == CodeUtill.DELETE_FLAG_ON) {

                // 카테고리 중복 코드 리턴(300)
                result = CodeUtill.RESULT_OVERLAP;

                // 수정 할 카테고리가 삭제 되어 있는 경우
            } else if (categoryDetailOverlap != null && categoryDetailOverlap.getDeleteFlg() == CodeUtill.DELETE_FLAG_OFF) {

                // 삭제되어 있는 하위 카테고리의 삭제플래그 0으로 변경
                CategoryDetail updateCategoryDetail = CategoryDetail.builder()
                        .category(category)
                        .categoryDetailNo(categoryDetail.getCategoryDetailNo())
                        .categoryDetailName(categoryDetail.getCategoryDetailName())
                        .deleteFlg(CodeUtill.DELETE_FLAG_ON)
                        .build();
                categoryDetailRepository.save(updateCategoryDetail);

                // 기존 하위카테고리는 삭제플래그 1로 변경
                updateCategoryDetail = CategoryDetail.builder()
                        .category(category)
                        .categoryDetailNo(categoryDetailOverlap.getCategoryDetailNo())
                        .categoryDetailName(request.getUpdateCategoryDetail())
                        .deleteFlg(CodeUtill.DELETE_FLAG_OFF)
                        .build();
                categoryDetailRepository.save(updateCategoryDetail);

                result = CodeUtill.RESULT_SUCCESS;

                // 수정 할 카테고리가 없는 경우
            } else {

                CategoryDetail updateCategoryDetail = CategoryDetail.builder()
                        .category(category)
                        .categoryDetailNo(categoryDetail.getCategoryDetailNo())
                        .categoryDetailName(request.getUpdateCategoryDetail())
                        .deleteFlg(CodeUtill.DELETE_FLAG_ON)
                        .build();

                categoryDetailRepository.save(updateCategoryDetail);

                result = CodeUtill.RESULT_SUCCESS;
            }

            // result가 성공 코드인 경우
            if (CodeUtill.RESULT_SUCCESS.equals(result)) {
                // redis 값 초기화
                redisDaoPattern.delRedisKey(CodeUtill.SEARCH_KEY + CodeUtill.SEARCH_KEY_ALL);

                // 수정 전 카테고리 초기화
                redisDaoPattern.delRedisKey(CodeUtill.SEARCH_KEY + setUnicode(request.getCate()));
            }

        } catch(Exception e) {
            log.error(e.getMessage());

            // 등록 실패 코드 리턴(400)
            result = CodeUtill.RESULT_ERROR;
        }

        log.info("end update CategoryDetail");
        reponse.setResultCode(result);
        return reponse;
    }

    /**
     * 하위 카테고리 삭제
     * @param request
     * @return
     */
    @Transactional
    public CategoryReponse deleteCategoryDetail(CategoryDetailRequest request) {

        String result;
        CategoryReponse reponse = new CategoryReponse();

        try {

            log.info("start delete CategoryDetail   param : " + request.getCategory() + "    " + request.getCategoryDetail());

            // 존재 체크
            Category category = categoryDataChk(request.getCategory(), CodeUtill.DELETE_FLAG_ON);

            // 상위 카테고리가 존재 하는 경우
            if (category != null) {

                // 하위 카테고리 조회
                CategoryDetail categoryDetail = category.getCategoryDetailList().stream().filter(a -> a.getCategoryDetailName().equals(request.getCategoryDetail())).findFirst().orElse(null);

                // 하위 카테고리 체크
                if (categoryDetail != null && categoryDetail.getDeleteFlg() == CodeUtill.DELETE_FLAG_ON) {

                    // 하위 카테고리 삭제
                    CategoryDetail upsertCategoryDetail = CategoryDetail.builder()
                            .category(category)
                            .categoryDetailNo(categoryDetail.getCategoryDetailNo())
                            .categoryDetailName(request.getCategoryDetail())
                            .deleteFlg(CodeUtill.DELETE_FLAG_OFF)
                            .build();

                    categoryDetailRepository.save(upsertCategoryDetail);

                    // 등록 성공 코드 리턴(200)
                    result = CodeUtill.RESULT_SUCCESS;

                } else {

                    // 이미 삭제된 데이터
                    // 등록, 수정 실패 (잘못된 등록방법)
                    result = CodeUtill.RESULT_FAIL;

                }

            } else {
                // 상위 카테고리가 존재 하지 않음
                result = CodeUtill.RESULT_FAIL;

            }

            // result가 성공 코드인 경우
            if (CodeUtill.RESULT_SUCCESS.equals(result)) {
                // redis 값 초기화
                redisDaoPattern.delRedisKey(CodeUtill.SEARCH_KEY + CodeUtill.SEARCH_KEY_ALL);

                // 수정 전 카테고리 초기화
                redisDaoPattern.delRedisKey(CodeUtill.SEARCH_KEY + setUnicode(request.getCategory()));
            }

        } catch(Exception e) {

            log.error(e.getMessage());

            // 등록 실패 코드 리턴(400)
            result = CodeUtill.RESULT_ERROR;
        }

        log.info("end delete CategoryDetail");
        reponse.setResultCode(result);
        return reponse;
    }


    /**
     * 카테고리 조회
     * @param request
     * @return
     */
    public SearchCategoryResponse searchCategory(SearchCategoryRequest request) {

        SearchCategoryResponse response = new SearchCategoryResponse();

        String selectKey = CodeUtill.SEARCH_KEY;
        try {

            // response설정
            List<CategoryListResponse> categoryListResponseList = new ArrayList<>();

            if (request == null || request.getCategoryName() == null || "".equals(request.getCategoryName())) {

                log.info("start searchCategory");

                Category category = Category.builder()
                        .deleteFlg(CodeUtill.DELETE_FLAG_ON)
                        .build();

                selectKey += CodeUtill.SEARCH_KEY_ALL;

                SearchCategoryResponse getRedisResult = redisDaoPattern.getRedisData(selectKey, SearchCategoryResponse.class);


                // 레디스에 값이 있을때
                if (getRedisResult != null) {

                    return getRedisResult;

                    // 레디스에 값이 없을때
                } else {

                    // 전체 검색
                    List<Category> result = categoryRepository.findByDeleteFlg(CodeUtill.DELETE_FLAG_ON);

                    if (result.isEmpty()) {
                        // 결과코드
                        // 검색 결과 없음
                        response.setResultCode(CodeUtill.RESULT_EMPTY);

                    } else {

                        for (Category categoryData : result) {

                            // 레스폰스 설정
                            categoryListResponseList.add(this.setSearchData(categoryData));

                            // 결과코드
                            response.setResultCode(CodeUtill.RESULT_SUCCESS);
                        }
                    }
                }

            } else {

                log.info("start searchCategory   param : " + request.getCategoryName());

                selectKey += setUnicode(request.getCategoryName());

                SearchCategoryResponse getRedisResult = redisDaoPattern.getRedisData(selectKey, SearchCategoryResponse.class);


                // 레디스에 값이 있을때
                if (getRedisResult != null) {

                    return getRedisResult;

                    // 레디스에 값이 없을때
                } else {

                    // 상위 카테고리로 검색
                    Category cateresult = categoryDataChk(request.getCategoryName(), CodeUtill.DELETE_FLAG_ON);

                    if (cateresult != null) {
                        // 레스폰스 설정
                        categoryListResponseList.add(this.setSearchData(cateresult));
                        // 결과코드
                        response.setResultCode(CodeUtill.RESULT_SUCCESS);

                    } else {
                        // 결과코드
                        // 검색 결과 없음
                        response.setResultCode(CodeUtill.RESULT_EMPTY);
                    }
                }
            }

            // 레스폰스 설정
            response.setCategoryListResponseList(categoryListResponseList);

            // 레디스 설정
            redisDaoPattern.setRedisData(selectKey, response, 60);

        } catch(Exception e) {

            log.error(e.getMessage());

            // 등록 실패 코드 리턴(400)
            response.setResultCode(CodeUtill.RESULT_ERROR);
        }

        log.info("end delete CategoryDetail");
        return response;
    }

    /**
     * 카테고리 조회
     * @param cate
     * @return
     */
    public Category categoryDataChk(String cate) {

        Category result = null;

        Optional<Category> category =  categoryRepository.findByCategoryName(cate);

        if (category.isPresent()) {
            result = category.get();
        }

        return result;
    }

    /**
     * 카테고리 조회
     * @param cate
     * @param deleteFlg
     * @return
     */
    public Category categoryDataChk(String cate, int deleteFlg) {

        Category result = null;

        Optional<Category> category =  categoryRepository.findByCategoryNameAndDeleteFlg(cate, deleteFlg);

        if (category.isPresent()) {
            result = category.get();
        }

        return result;
    }

    /**
     * 하위 카테고리 조회
     * @param cate
     * @param categoryDetail
     * @return
     */
    public CategoryDetail categoryDetailDataChk(Category cate, String categoryDetail) {

        CategoryDetail result = null;

        Optional<CategoryDetail> category =  categoryDetailRepository.findByCategoryAndCategoryDetailName(cate, categoryDetail);

        if (category.isPresent()) {
            result = category.get();
        }

        return result;
    }

    /**
     * 하위 카테고리 조회
     * @param cate
     * @param categoryDetail
     * @param deleteFlg
     * @return
     */
    public CategoryDetail categoryDetailDataChk(Category cate, String categoryDetail, int deleteFlg) {

        CategoryDetail result = null;

        Optional<CategoryDetail> category =  categoryDetailRepository.findByCategoryAndCategoryDetailNameAndDeleteFlg(cate, categoryDetail, deleteFlg);

        if (category.isPresent()) {
            result = category.get();
        }

        return result;
    }

    /**
     * 카테고리 조회 결과 레스폰스 설정
     * @param categoryData
     * @return
     */
    private CategoryListResponse setSearchData(Category categoryData) {

        CategoryListResponse categoryListResponse = new CategoryListResponse();

        // 상위 카테고리 ID 설정
        categoryListResponse.setCategoryNo(categoryData.getCategoryNo());

        // 상위 카테고리 이름 설정
        categoryListResponse.setCategoryName(categoryData.getCategoryName());

        // 하위카테고리 삭제된 데이터 제거
        List<CategoryDetail> removeList = categoryData.getCategoryDetailList().stream().filter(a -> a.getDeleteFlg() == CodeUtill.DELETE_FLAG_OFF).collect(Collectors.toList());

        if (!removeList.isEmpty()) {
            removeList.stream().forEach(a -> {
                categoryData.getCategoryDetailList().remove(a);
            });
        }

        List<CategoryDetailResponse> categoryDetailResponseList = new ArrayList<>();
        categoryData.getCategoryDetailList().forEach(a -> {

            CategoryDetailResponse categoryDetailResponse = new CategoryDetailResponse();
            // 하위 카테고리 ID설정
            categoryDetailResponse.setCategoryDetailNo(a.getCategoryDetailNo());
            // 하위 카테고리 이름 설정
            categoryDetailResponse.setCategoryDetailName(a.getCategoryDetailName());

            // 레스폰스 설정
            categoryDetailResponseList.add(categoryDetailResponse);
        });

        categoryListResponse.setCategoryDetailResponseList(categoryDetailResponseList);

        return categoryListResponse;
    }

    /**
     * 카테고리명을 unicode로 변경
     * @param category
     * @return
     */
    private StringBuffer setUnicode(String category) {

        StringBuffer unicode = new StringBuffer();

        for (int i = 0; i < category.length(); i++) {
            int cd = category.codePointAt(i);
            if (cd < 128) {
                unicode.append(String.format("%c", cd));
            } else {
                unicode.append(String.format("\\u%04x", cd));
            }
        }
        return unicode;
    }
}
