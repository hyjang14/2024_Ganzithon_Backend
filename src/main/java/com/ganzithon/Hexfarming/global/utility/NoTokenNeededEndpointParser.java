package com.ganzithon.Hexfarming.global.utility;

import com.ganzithon.Hexfarming.global.GlobalConstants;
import org.springframework.util.AntPathMatcher;

import java.util.Arrays;

public class NoTokenNeededEndpointParser {
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    public static boolean parsePath(String path) {
        return Arrays.stream(GlobalConstants.noTokenNeededAPIs)
                .anyMatch(endpoint -> pathMatcher.match(endpoint, path));
    }
}
