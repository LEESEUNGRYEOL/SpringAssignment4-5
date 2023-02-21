package com.example.blogservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class BlogRequestDto {
    private String title;
    private String content;

}
