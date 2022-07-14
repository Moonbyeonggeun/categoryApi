package com.categoryapi.repository;

import com.categoryapi.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryName(String categoryName);

    Optional<Category> findByCategoryNameAndDeleteFlg(String categoryName, int deleteFlg);

    List<Category> findByDeleteFlg(int deleteFlg);

}
