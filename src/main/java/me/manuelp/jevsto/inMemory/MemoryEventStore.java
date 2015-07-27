package me.manuelp.jevsto.inMemory;

import fj.data.List;
import fj.data.Option;
import me.manuelp.jevsto.EventStore;
import me.manuelp.jevsto.dataTypes.Event;
import org.threeten.bp.LocalDateTime;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

import java.util.ArrayList;
import java.util.UUID;

public class MemoryEventStore implements EventStore {
  private final ArrayList<Event> queue;
  private final SerializedSubject<Event, Event> stream;

  public MemoryEventStore() {
    queue = new ArrayList<>();
    stream = new SerializedSubject<>(PublishSubject.<Event>create());
  }

  @Override
  public synchronized void append(final Event e) {
    queue.add(e);
    stream.onNext(e);
  }

  @Override
  public Observable<Event> getEvents() {
    return stream;
  }

  @Override
  public Observable<Event> getAllEvents() {
    return Observable.from(queue).concatWith(stream);
  }

  @Override
  public Observable<Event> getAllEventsFrom(final LocalDateTime from) {
    return getAllEvents().filter(Event.hasBeenCreatedFrom(from));
  }

  @Override
  public synchronized List<Event> getAll() {
    return List.iterableList(queue);
  }

  @Override
  public synchronized List<Event> getFrom(final LocalDateTime t) {
    return getAll().filter(Event.hasBeenCreatedAtOrAfter(t));
  }

  @Override
  public synchronized Option<Event> getById(final UUID id) {
    return getAll().find(Event.hasId(id));
  }

}
