package com.categoryapi.repository;

import com.categoryapi.entity.Category;
import com.categoryapi.entity.CategoryDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryDetailRepository extends JpaRepository<CategoryDetail, Long> {

    Optional<CategoryDetail> findByCategoryAndCategoryDetailName(Category category, String categoryDetailName);

    Optional<CategoryDetail> findByCategoryAndCategoryDetailNameAndDeleteFlg(Category category, String categoryDetailName, int deleteFlg);

}
