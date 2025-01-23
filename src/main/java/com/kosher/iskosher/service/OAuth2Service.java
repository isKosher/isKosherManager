package com.kosher.iskosher.service;

public interface OAuth2Service<T> {

    /**
     * Verifies the given OAuth2 token and returns user details.
     *
     * @param token the OAuth2 token to verify
     * @return the user details as an object of type T
     * @throws IllegalArgumentException if the token is invalid
     */
    T verifyToken(String token);

    /**
     * Converts the user details into a standardized format for your application.
     *
     * @param userDetails the raw user details from the provider
     * @return the standardized user object
     */
    <R> R mapToApplicationUser(T userDetails);

    /**
     * Retrieves the provider's name (e.g., "Google", "Facebook").
     *
     * @return the name of the OAuth2 provider
     */
    String getProviderName();
}
