package me.manuelp.jevsto.dataTypes;

import me.manuelp.jevsto.NotNull;
import org.threeten.bp.LocalDateTime;
import rx.functions.Func1;

import java.util.UUID;

public class Event {
  private final UUID id;
  private final LocalDateTime timestamp;
  private final EventType type;
  private final EventData data;

  private Event(UUID id, LocalDateTime timestamp, EventType type, EventData data) {
    NotNull.check(id, timestamp, type, data);
    this.id = id;
    this.timestamp = timestamp;
    this.type = type;
    this.data = data;
  }

  public static Event event(LocalDateTime timestamp, EventType type, EventData data) {
    return new Event(UUID.randomUUID(), timestamp, type, data);
  }

  public static Event event(UUID id, LocalDateTime timestamp, EventType type, EventData data) {
    return new Event(id, timestamp, type, data);
  }

  public UUID getId() {
    return id;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public EventType getType() {
    return type;
  }

  public EventData getData() {
    return data;
  }

  public static Func1<Event, Boolean> hasBeenCreatedAtOrAfter(final LocalDateTime t) {
    return new Func1<Event, Boolean>() {
      @Override
      public Boolean call(Event e) {
        return e.getTimestamp().isEqual(t) || e.getTimestamp().isAfter(t);
      }
    };
  }

  public static Func1<Event, Boolean> hasBeenCreatedFrom(final LocalDateTime t) {
    return new Func1<Event, Boolean>() {
      @Override
      public Boolean call(Event event) {
        return hasBeenCreatedAtOrAfter(t).call(event);
      }
    };
  }

  public static Func1<Event, Boolean> hasId(final UUID id) {
    return new Func1<Event, Boolean>() {
      @Override
      public Boolean call(Event event) {
        return id.equals(event.getId());
      }
    };
  }

  /**
   * Returns a predicate that checks if an {@link Event} is of a certain type.
   *
   * @param t {@link EventType} to check for
   * @return Predicate on {@link Event}s
   */
  public static Func1<Event, Boolean> isOfType(final EventType t) {
    return new Func1<Event, Boolean>() {
      @Override
      public Boolean call(Event e) {
        return e.getType().equals(t);
      }
    };
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Event event = (Event) o;

    if (!id.equals(event.id)) return false;
    if (!timestamp.equals(event.timestamp)) return false;
    if (!type.equals(event.type)) return false;
    return data.equals(event.data);

  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + timestamp.hashCode();
    result = 31 * result + type.hashCode();
    result = 31 * result + data.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "Event{" +
           "id=" + id +
           ", timestamp=" + timestamp +
           ", type=" + type +
           ", data=" + data +
           '}';
  }
}
