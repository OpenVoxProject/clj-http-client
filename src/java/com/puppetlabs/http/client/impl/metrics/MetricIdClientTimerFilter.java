package com.puppetlabs.http.client.impl.metrics;

import io.dropwizard.metrics5.Metric;
import io.dropwizard.metrics5.MetricFilter;
import io.dropwizard.metrics5.MetricName;
import com.puppetlabs.http.client.metrics.MetricIdClientTimer;

import java.util.List;

public class MetricIdClientTimerFilter implements MetricFilter {
    private final List<String> metricId;

    public MetricIdClientTimerFilter(List<String> metricId) {
        this.metricId = metricId;
    }

    @Override
    public boolean matches(MetricName name, Metric metric) {
        return metric.getClass().equals(MetricIdClientTimer.class) &&
                ((MetricIdClientTimer) metric).
                        getMetricId().equals(metricId);
    }
}
