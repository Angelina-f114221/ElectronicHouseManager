package org.example.dto;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
// перфектно за изброяване на живущи в сграда
public class IdNameDto {
    private long id;
    private String name;
}