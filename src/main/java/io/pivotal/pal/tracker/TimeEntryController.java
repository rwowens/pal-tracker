package io.pivotal.pal.tracker;

import io.pivotal.pal.tracker.TimeEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {
    private TimeEntryRepository repo;

    @Autowired
    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.repo = timeEntryRepository;
    }

    protected TimeEntryRepository getRepo() {
        return this.repo;
    }

    @PostMapping
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry entry) {
        return new ResponseEntity<TimeEntry>(getRepo().create(entry), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable  long id) {
        TimeEntry entry = getRepo().find(Long.valueOf(id));
        return new ResponseEntity<TimeEntry>(entry, entry != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable long id, @RequestBody TimeEntry entry) {
        entry = getRepo().update(id, entry);
        return new ResponseEntity<TimeEntry>(entry, entry != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable long id) {
        getRepo().delete(Long.valueOf(id));
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        return new ResponseEntity<List<TimeEntry>>(getRepo().list(), HttpStatus.OK);
    }
}

