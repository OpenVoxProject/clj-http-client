package com.puppetlabs.http.client.impl.metrics;

import io.dropwizard.metrics5.Metric;
import io.dropwizard.metrics5.MetricFilter;
import io.dropwizard.metrics5.MetricName;
import com.puppetlabs.http.client.metrics.UrlClientTimer;

public class UrlClientTimerFilter implements MetricFilter {
    private final String url;

    public UrlClientTimerFilter(String url) {
        this.url = url;
    }

    protected String getUrl() {
        return url;
    }

    @Override
    public boolean matches(MetricName name, Metric metric) {
        return metric.getClass().equals(UrlClientTimer.class) &&
                ((UrlClientTimer) metric).
                        getUrl().equals(url);
    }
}
