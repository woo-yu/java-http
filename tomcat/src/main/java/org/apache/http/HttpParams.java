package org.apache.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class HttpParams {
    private Map<String, String> params = new HashMap<>();

    HttpParams() {
    }

    HttpParams(Map<String, String> params) {
        this.params = params;
    }

    public HttpParams(final String url) {
        if (!url.contains("?")) {
            return;
        }
        parseQueryString(url.split("\\?")[1]);
    }

    public String get(final String key) {
        return params.get(key);
    }

    private void parseQueryString(final String queryString) {
        final var values = queryString.split("&");
        for (String param : values) {
            var token = param.split("=");
            if (token.length < 2) continue;
            final String key = token[0];
            final String value = token[1];
            params.put(key, value);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpParams that = (HttpParams) o;
        return Objects.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(params);
    }
}
