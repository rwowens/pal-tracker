package io.pivotal.pal.tracker;

import java.time.LocalDate;

public class TimeEntry {
    private long id;
    private long projectId;
    private long userId;
    private LocalDate date;
    private int hours;

    public TimeEntry() {

    }

    public TimeEntry(long id, long projectId, long userId, LocalDate date, int hours) {
        this(projectId, userId, date, hours);
        this.id = id;
    }

    public TimeEntry(long projectId, long userId, LocalDate date, int hours) {
        this.projectId = projectId;
        this.userId = userId;
        this.date = date;
        this.hours = hours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimeEntry timeEntry = (TimeEntry) o;

        if (getId() != timeEntry.getId()) return false;
        if (getProjectId() != timeEntry.getProjectId()) return false;
        if (getUserId() != timeEntry.getUserId()) return false;
        if (getHours() != timeEntry.getHours()) return false;
        return getDate() != null ? getDate().equals(timeEntry.getDate()) : timeEntry.getDate() == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (int) (getProjectId() ^ (getProjectId() >>> 32));
        result = 31 * result + (int) (getUserId() ^ (getUserId() >>> 32));
        result = 31 * result + (getDate() != null ? getDate().hashCode() : 0);
        result = 31 * result + getHours();
        return result;
    }

    public long getId() {
        return id;
    }

    public long getProjectId() {
        return projectId;
    }

    public long getUserId() {
        return userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getHours() {
        return hours;
    }
}
