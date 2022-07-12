package com.myblog.myblog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Type {

    private Long id;
    private String name; // 分类名字
    private List<Blog> blogs = new ArrayList<>();

}