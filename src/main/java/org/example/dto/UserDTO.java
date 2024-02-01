package org.example.dto;

import lombok.*;

import java.io.InputStream;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserDTO {
    private String userId;
    private String username;
    private byte[] image;
}
