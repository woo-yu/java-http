package org.apache.coyote.http11;

import camp.nextstep.exception.UncheckedServletException;
import org.apache.coyote.HttpRequest;
import org.apache.coyote.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Collections;

public class Http11Processor implements Runnable, Processor {

    private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

    private final Socket connection;
    private final Http11ProcessorHandlers handlers = new Http11ProcessorHandlers();

    public Http11Processor(final Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        log.info("connect host: {}, port: {}", connection.getInetAddress(), connection.getPort());
        process(connection);
    }

    @Override
    public void process(final Socket connection) {
        try (final var inputReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             final var outputStream = connection.getOutputStream()) {
            final var request = new HttpRequest(Collections.singletonList(inputReader.readLine()));
            final var response = handlers.handle(request);

            outputStream.write(response.toOutput());
            outputStream.flush();
        } catch (IOException | UncheckedServletException e) {
            log.error(e.getMessage(), e);
        }
    }
}
