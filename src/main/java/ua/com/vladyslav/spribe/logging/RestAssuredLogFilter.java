package ua.com.vladyslav.spribe.logging;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.internal.NoParameterValue;

import java.util.stream.Collectors;

public class RestAssuredLogFilter implements Filter {

    private static final Log LOG = Log.get(RestAssuredLogFilter.class);

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {

        try {
            logRequest(requestSpec);
            long start = System.currentTimeMillis();

            Response response = ctx.next(requestSpec, responseSpec);

            long elapsed = System.currentTimeMillis() - start;
            logResponse(response, elapsed);

            return response;
        } catch (Exception e) {
            LOG.error("Error during request/response logging: {}", e.getMessage());
            throw e;
        }
    }

    private void logRequest(FilterableRequestSpecification requestSpec) {
        String headers = requestSpec.getHeaders().asList().isEmpty()
                ? "<none>"
                : requestSpec.getHeaders().asList().stream()
                .map(h -> h.getName() + "=" + h.getValue())
                .collect(Collectors.joining(", "));

        String params = requestSpec.getQueryParams().isEmpty()
                ? "<none>"
                : requestSpec.getQueryParams().entrySet().stream()
                .map(e -> {
                    Object value = e.getValue();
                    if (value == null || value instanceof NoParameterValue) {
                        return e.getKey() + "=<no-value>";
                    }
                    return e.getKey() + "=" + value;
                })
                .collect(Collectors.joining(", "));

        Object rawBody = requestSpec.getBody();
        String body = rawBody != null ? String.valueOf(rawBody) : "<empty>";

        LOG.info("------------------------------------------------------------");
        LOG.info("---> REQUEST [{}] {}", requestSpec.getMethod(), requestSpec.getURI());
        LOG.debug("Headers: {}", headers);
        LOG.debug("Query params: {}", params);
        if (!"<empty>".equals(body)) {
            LOG.debug("Body: {}", body);
        }
    }

    private void logResponse(Response response, long elapsed) {
        String headers = response.getHeaders().asList().isEmpty()
                ? "<none>"
                : response.getHeaders().asList().stream()
                .map(h -> h.getName() + "=" + h.getValue())
                .collect(Collectors.joining(", "));

        String body;
        try {
            body = response.getBody() != null ? response.getBody().asPrettyString() : "<empty>";
        } catch (Exception ex) {
            body = "<unreadable body: " + ex.getMessage() + ">";
        }

        LOG.info("<--- RESPONSE [{}] {} ({} ms)", response.statusCode(), response.statusLine(), elapsed);
        LOG.debug("Headers: {}", headers);
        LOG.debug("Body:\n{}", body);
        LOG.info("------------------------------------------------------------");
    }
}