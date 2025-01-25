
package com.kosher.iskosher.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnsplashResponse {
    private Urls urls;

    @Getter
    @Setter
    public static class Urls {
        private String regular;

    }
}


