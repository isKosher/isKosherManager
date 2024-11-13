
package com.kosher.iskosher.dto.response;
public class UnsplashResponse {
    private Urls urls;

    public Urls getUrls() {
        return urls;
    }

    public void setUrls(Urls urls) {
        this.urls = urls;
    }

    public static class Urls {
        private String regular;

        public String getRegular() {
            return regular;
        }

        public void setRegular(String regular) {
            this.regular = regular;
        }
    }
}


