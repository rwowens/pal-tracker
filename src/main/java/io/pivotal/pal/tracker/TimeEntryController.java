package io.pivotal.pal.tracker;

import io.pivotal.pal.tracker.TimeEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {
    private TimeEntryRepository repo;
    private final CounterService counter;
    private final GaugeService gauge;

    @Autowired
    public TimeEntryController(TimeEntryRepository timeEntryRepository,
                               CounterService counter,
                               GaugeService gauge) {
        this.repo = timeEntryRepository;
        this.counter = counter;
        this.gauge = gauge;
    }

    protected TimeEntryRepository getRepo() {
        return this.repo;
    }

    @PostMapping
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry entry) {
        counter.increment("TimeEntry.created");
        gauge.submit("timeEntries.count", getRepo().list().size());
        return new ResponseEntity<TimeEntry>(getRepo().create(entry), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable  long id) {
        TimeEntry entry = getRepo().find(Long.valueOf(id));
        counter.increment("TimeEntry.read");
        return new ResponseEntity<TimeEntry>(entry, entry != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable long id, @RequestBody TimeEntry entry) {
        entry = getRepo().update(id, entry);
        counter.increment("TimeEntry.updated");
        return new ResponseEntity<TimeEntry>(entry, entry != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable long id) {
        getRepo().delete(Long.valueOf(id));
        counter.increment("TimeEntry.deleted");
        gauge.submit("timeEntries.count", getRepo().list().size());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        counter.increment("TimeEntry.listed");
        return new ResponseEntity<List<TimeEntry>>(getRepo().list(), HttpStatus.OK);
    }
}

