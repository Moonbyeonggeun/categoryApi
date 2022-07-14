package com.categoryapi.controller;

import com.categoryapi.controller.request.*;
import com.categoryapi.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

//@ExtendWith(SpringExtension.class)
//@WebMvcTest(CategoryController.class)
@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {

    @Autowired
    private MockMvc mvc;

    private ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private CategoryService categoryService;

    private String baseurl = "/api/category/";

    /**
     * 정상 등록 확인
     * @throws Exception
     */
    @Test
    void insertCategoryTest001() throws Exception {


        String body = mapper.writeValueAsString(
                CategoryRequest.builder().cate("운동복").build());

        mvc.perform(post(baseurl + "insertCategory")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * 파라미터 체크
     * 공백인 경우
     * @throws Exception
     */
    @Test
    void insertCategoryTest002() throws Exception {

        String body = mapper.writeValueAsString(
                CategoryRequest.builder().cate("").build());

        mvc.perform(post(baseurl + "insertCategory")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * 파라미터 체크
     * null인경우
     * @throws Exception
     */
    @Test
    void insertCategoryTest003() throws Exception {

        String body = mapper.writeValueAsString(
                CategoryRequest.builder()
                        .build());

        mvc.perform(post(baseurl + "insertCategory")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * 정상 등록 확인
     * @throws Exception
     */
    @Test
    void insertCategoryDetailTest001() throws Exception {

        String body = mapper.writeValueAsString(
                CategoryDetailRequest.builder()
                        .category("운동복")
                        .categoryDetail("후드")
                        .build());

        mvc.perform(post(baseurl + "insertCategoryDetail")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * 파라미터 체크
     * 공백인 경우
     * @throws Exception
     */
    @Test
    void insertCategoryDetailTest002() throws Exception {

        String body = mapper.writeValueAsString(
                CategoryDetailRequest.builder()
                        .category("")
                        .categoryDetail("")
                        .build());

        mvc.perform(post(baseurl + "insertCategoryDetail")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * 파라미터 체크
     * null인경우
     * @throws Exception
     */
    @Test
    void insertCategoryDetailTest003() throws Exception {

        String body = mapper.writeValueAsString(
                CategoryDetailRequest.builder()
                        .build());

        mvc.perform(post(baseurl + "insertCategoryDetail")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * 정상 등록 확인
     * @throws Exception
     */
    @Test
    void updateCategoryTest001() throws Exception {

        String body = mapper.writeValueAsString(
                UpdateCategoryRequest.builder()
                        .cate("운동복")
                        .updateCate("트레이닝복")
                        .build());

        mvc.perform(post(baseurl + "updateCategory")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * 파라미터 체크
     * 공백인 경우
     * @throws Exception
     */
    @Test
    void updateCategoryTest002() throws Exception {

        String body = mapper.writeValueAsString(
                UpdateCategoryRequest.builder()
                        .cate("")
                        .updateCate("")
                        .build());

        mvc.perform(post(baseurl + "insertCategoryDetail")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * 파라미터 체크
     * null인경우
     * @throws Exception
     */
    @Test
    void updateCategoryTest003() throws Exception {

        String body = mapper.writeValueAsString(
                UpdateCategoryRequest.builder()
                        .build());

        mvc.perform(post(baseurl + "insertCategoryDetail")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * 정상 등록 확인
     * @throws Exception
     */
    @Test
    void updateCategoryDetailTest001() throws Exception {

        String body = mapper.writeValueAsString(
                UpdateCategoryDetailRequest.builder()
                        .cate("트레이닝복")
                        .categoryDetail("바람막이")
                        .updateCategoryDetail("후드")
                        .build());

        mvc.perform(post(baseurl + "updateCategoryDetail")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * 파라미터 체크
     * 공백인 경우
     * @throws Exception
     */
    @Test
    void updateCategoryDetailTest002() throws Exception {

        String body = mapper.writeValueAsString(
                UpdateCategoryDetailRequest.builder()
                        .cate("")
                        .categoryDetail("")
                        .updateCategoryDetail("")
                        .build());

        mvc.perform(post(baseurl + "updateCategoryDetail")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * 파라미터 체크
     * null인경우
     * @throws Exception
     */
    @Test
    void updateCategoryDetailTest003() throws Exception {

        String body = mapper.writeValueAsString(
                UpdateCategoryDetailRequest.builder()
                        .build());

        mvc.perform(post(baseurl + "updateCategoryDetail")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * 정상 등록 확인
     * @throws Exception
     */
    @Test
    void deleteCategoryTest001() throws Exception {

        String body = mapper.writeValueAsString(
                CategoryRequest.builder()
                        .cate("트레이닝복")
                        .build());

        mvc.perform(post(baseurl + "deleteCategory")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * 파라미터 체크
     * 공백인 경우
     * @throws Exception
     */
    @Test
    void deleteCategoryTest002() throws Exception {

        String body = mapper.writeValueAsString(
                CategoryRequest.builder()
                        .cate("")
                        .build());

        mvc.perform(post(baseurl + "deleteCategory")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * 파라미터 체크
     * null인경우
     * @throws Exception
     */
    @Test
    void deleteCategoryTest003() throws Exception {

        String body = mapper.writeValueAsString(
                CategoryRequest.builder()
                        .build());

        mvc.perform(post(baseurl + "deleteCategory")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * 정상 등록 확인
     * @throws Exception
     */
    @Test
    void deleteCategoryDetailTest001() throws Exception {

        String body = mapper.writeValueAsString(
                CategoryDetailRequest.builder()
                        .category("운동복")
                        .categoryDetail("후드")
                        .build());

        mvc.perform(post(baseurl + "deleteCategoryDetail")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * 파라미터 체크
     * 공백인 경우
     * @throws Exception
     */
    @Test
    void deleteCategoryDetailTest002() throws Exception {

        String body = mapper.writeValueAsString(
                CategoryDetailRequest.builder()
                        .category("")
                        .categoryDetail("")
                        .build());

        mvc.perform(post(baseurl + "deleteCategoryDetail")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * 파라미터 체크
     * null인경우
     * @throws Exception
     */
    @Test
    void deleteCategoryDetailTest003() throws Exception {

        String body = mapper.writeValueAsString(
                CategoryDetailRequest.builder()
                        .build());

        mvc.perform(post(baseurl + "deleteCategoryDetail")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * 정상 등록 확인
     * @throws Exception
     */
    @Test
    void searchCategoryTest001() throws Exception {

        String body = mapper.writeValueAsString(
                SearchCategoryRequest.builder()
                        .categoryName("트레이닝복")
                        .build());

        mvc.perform(post(baseurl + "searchCategory")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * 파라미터 체크
     * 공백인 경우
     * @throws Exception
     */
    @Test
    void searchCategoryTest002() throws Exception {

        String body = mapper.writeValueAsString(
                SearchCategoryRequest.builder()
                        .build());

        mvc.perform(post(baseurl + "searchCategory")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}