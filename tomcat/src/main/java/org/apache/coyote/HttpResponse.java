package org.apache.coyote;

import org.apache.http.HttpPath;
import org.apache.http.HttpStatus;
import org.apache.http.body.HttpBody;
import org.apache.http.session.HttpCookie;
import org.apache.http.header.HttpResponseHeaders;
import org.apache.http.header.Location;
import org.apache.http.header.SetCookie;

/**
 * https://developer.mozilla.org/en-US/docs/Web/HTTP/Messages#http_responses
 */
public class HttpResponse {
    private final static String DELIMITER = "\r\n";

    private final HttpResponseStatusLine statusLine;
    private final HttpResponseHeaders headers;
    private final HttpBody body;

    HttpResponse(HttpResponseStatusLine statusLine, HttpResponseHeaders headers, HttpBody body) {
        this.statusLine = statusLine;
        this.headers = headers;
        this.body = body;
    }

    public HttpResponse(HttpBody body) {
        this(new HttpResponseStatusLine(HttpStatus.OK), body.addContentHeader(new HttpResponseHeaders()), body);
    }

    public HttpResponse(HttpStatus status, HttpResponseHeaders headers) {
        this(new HttpResponseStatusLine(status), headers, null);
    }

    public HttpResponse(final HttpPath path) {
        this(HttpStatus.Found, new HttpResponseHeaders().add(new Location(path)));
    }

    public HttpResponse cookie(HttpCookie cookie) {
        return new HttpResponse(statusLine, headers.add(new SetCookie().addCookie(cookie)), body);
    }

    @Override
    public String toString() {
        if (body != null) {
            return String.join(DELIMITER, statusLine.toString(), headers.toString(), "", body.toString());
        }
        return statusLine + DELIMITER + headers;
    }

}
