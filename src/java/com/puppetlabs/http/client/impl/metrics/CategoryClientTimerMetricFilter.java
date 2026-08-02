package com.puppetlabs.http.client.impl.metrics;

import io.dropwizard.metrics5.Metric;
import io.dropwizard.metrics5.MetricFilter;
import io.dropwizard.metrics5.MetricName;
import com.puppetlabs.http.client.metrics.ClientTimer;
import com.puppetlabs.http.client.metrics.Metrics;

public class CategoryClientTimerMetricFilter implements MetricFilter {
    private final Metrics.MetricCategory category;

    public CategoryClientTimerMetricFilter(Metrics.MetricCategory category) {
        this.category = category;
    }

    @Override
    public boolean matches(MetricName name, Metric metric) {
        return metric instanceof ClientTimer &&
                ((ClientTimer) metric).isCategory(category);
    }
}
