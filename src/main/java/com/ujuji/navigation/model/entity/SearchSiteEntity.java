package com.ujuji.navigation.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("search_sites")
@AllArgsConstructor
@NoArgsConstructor
public class SearchSiteEntity extends BaseEntity {

    private Integer userId;
    private String name;//
    private String notice;//
    private String searchUrl;//
    private Integer siteOrder;

}
