package com.puppetlabs.http.client.impl.metrics;

import io.dropwizard.metrics5.Metric;
import io.dropwizard.metrics5.MetricName;
import com.puppetlabs.http.client.metrics.UrlAndMethodClientTimer;

public class UrlAndMethodClientTimerFilter extends UrlClientTimerFilter {
    private final String method;

    public UrlAndMethodClientTimerFilter(String url, String method) {
        super(url);
        this.method = method;
    }

    @Override
    public boolean matches(MetricName name, Metric metric) {
        if (metric.getClass().equals(UrlAndMethodClientTimer.class)) {
            UrlAndMethodClientTimer timer = (UrlAndMethodClientTimer) metric;
            return timer.getMethod().equals(this.method) &&
                    timer.getUrl().equals(this.getUrl());
        }
        return false;
    }
}
