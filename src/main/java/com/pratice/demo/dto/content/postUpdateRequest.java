package com.pratice.demo.dto.content;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class postUpdateRequest {
    private String userId;

    @NotBlank(message = "제목을 입력해 주세요")
    @Size(min = 1, max = 15, message = "제목은 1 ~ 15자 사이여야 합니다.")
    private String title;

    @Size(max = 1000, message = "본문은 최대 1000자까지 입력할 수 있습니다.")
    private String content;
}
