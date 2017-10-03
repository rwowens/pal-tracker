package io.pivotal.pal.tracker;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {
    private Map<Long, TimeEntry> repo;
    private long nextId = 0;

    public InMemoryTimeEntryRepository() {
        this.repo = new HashMap<Long, TimeEntry>();
    }

    public TimeEntry create(TimeEntry entry) {
        TimeEntry entryWithId = new TimeEntry(getNextId(), entry.getProjectId(), entry.getUserId(), entry.getDate(), entry.getHours());
        this.repo.put(entryWithId.getId(), entryWithId);
        return entryWithId;
    }

    @Override
    public TimeEntry find(Long id) {
        return this.repo.get(id);
    }

    @Override
    public List<TimeEntry> list() {
        List<TimeEntry> timeEntries = Arrays.asList(this.repo.values().toArray(new TimeEntry[this.repo.size()]));
//        timeEntries.sort((o1, o2) -> Long.valueOf(((TimeEntry)o1).getId()).compareTo(Long.valueOf(((TimeEntry)o2).getId())));
        return timeEntries;
    }

    @Override
    public TimeEntry update(Long id, TimeEntry entry) {
        TimeEntry entryWithId = null;
        if (this.repo.get(id) != null) {
            entryWithId = new TimeEntry(id, entry.getProjectId(), entry.getUserId(), entry.getDate(), entry.getHours());
            this.repo.put(id, entryWithId);
        }
        return entryWithId;
    }

    @Override
    public void delete(Long id) {
        this.repo.remove(id);
    }

    protected Long getNextId() {
        return Long.valueOf(++nextId);
    }
}
