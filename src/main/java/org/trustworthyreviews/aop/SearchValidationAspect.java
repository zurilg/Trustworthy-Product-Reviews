package org.trustworthyreviews.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.trustworthyreviews.exception.IllegalSearchException;

@Component
@Aspect
public class SearchValidationAspect {
    // Validate search parameter before executing home method when search parameter is present
    @Before("execution(* org.trustworthyreviews.FrontController.home(..)) && args(search, ..)")
    public void validateSearch(String search) {
        /* Our search validation logic will validate the following:
               1.) Search string length is between 1 and 100 characters
               2.) Search string does not contain more than 15 words
         */
        if (search != null) {
            String trimmedSearch = search.trim();
            if (trimmedSearch.length() > 100) {
                throw new IllegalSearchException("Search cannot exceed 100 characters.");
            }
            String[] words = trimmedSearch.split("\\s+");
            if (words.length > 15) {
                throw new IllegalSearchException("Search cannot contain more than 15 words.");
            }
        }

    }
}
