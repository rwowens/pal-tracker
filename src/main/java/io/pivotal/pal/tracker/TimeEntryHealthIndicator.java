package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class TimeEntryHealthIndicator implements HealthIndicator {
    private final TimeEntryRepository timeEntryRepository;

    @Autowired
    public TimeEntryHealthIndicator(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @Override
    public Health health() {
        int size = timeEntryRepository.list().size();
        return size >= 5 ? Health.down().build() : Health.up().build();
    }
}
