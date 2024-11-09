package com.kosher.iskosher.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupervisorModel {
    private String name;
    private String phone;
    private String training;
}
