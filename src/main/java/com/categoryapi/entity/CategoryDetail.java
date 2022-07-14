package com.categoryapi.entity;

import com.categoryapi.common.entiry.BaseTimeEntity;
import com.categoryapi.service.CategoryService;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class CategoryDetail extends BaseTimeEntity implements Serializable {

    @GeneratedValue
    @Id
    private Long categoryDetailNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryNo")
    private Category category;

    private String categoryDetailName;

    private int deleteFlg;

    @Builder
    public CategoryDetail(Long categoryDetailNo, String categoryDetailName, int deleteFlg, Category category) {
        this.categoryDetailNo = categoryDetailNo;
        this.categoryDetailName = categoryDetailName;
        this.deleteFlg = deleteFlg;
        this.category = category;
    }
}
