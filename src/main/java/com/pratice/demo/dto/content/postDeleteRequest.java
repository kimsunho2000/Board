package com.pratice.demo.dto.content;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class postDeleteRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long contentId;
    private String userId;
}
