package com.codewithmosh.store.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class UserDto {

    private long id;

    @JsonProperty("family")
    private String email;

    private String name;

    @JsonFormat(pattern = "yyyy-mm-dd HH:MM:ss")
    private LocalDateTime createdAt;


}
