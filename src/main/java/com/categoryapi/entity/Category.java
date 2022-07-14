package com.categoryapi.entity;

import com.categoryapi.common.entiry.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Category extends BaseTimeEntity implements Serializable {

    @GeneratedValue
    @Id
    private Long categoryNo;

    private String categoryName;

    private int deleteFlg;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    private List<CategoryDetail> categoryDetailList = new ArrayList<>();

    @Builder
    public Category(Long categoryNo, String categoryName, int deleteFlg, List<CategoryDetail> categoryDetailList) {
        this.categoryNo = categoryNo;
        this.categoryName = categoryName;
        this.deleteFlg = deleteFlg;
        this.categoryDetailList = categoryDetailList;
    }
}
