package com.kosher.iskosher.common.interfaces;

import java.util.UUID;

public interface NamedEntity {
    UUID getId();
    String getName();
    void setName(String name);
}