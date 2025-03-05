package com.kosher.iskosher.types;

import lombok.Data;

import java.util.List;

@Data
public class OsrmResponseWrapper {
    private List<OsrmRoute> routes;
}