package com.andy.vertx_learning.p1_basics;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Consumer;


/**
 *
 * <p>This class demonstrates the use of an event loop for processing asynchronous events.
 * An event loop is a popular threading model for handling events, such as I/O events and timers,
 * in an asynchronous manner without blocking a thread.
 *
 * <p>In this example, two threads dispatch events to an event loop, and the event loop processes
 * these events based on registered event handlers.
 * <ul>
 *   <li>{@link #main(String[])} method starts the event loop and demonstrates event dispatching from multiple threads.</li>
 *   <li>{@link Event} is a static inner class representing an event with a key and associated data.</li>
 *   <li>{@link #on(String, Consumer)} registers event handlers for specific event keys.</li>
 *   <li>{@link #dispatch(Event)} adds an event to the event queue.</li>
 *   <li>{@link #run()} method runs the event loop, processing events and invoking registered handlers.</li>
 *   <li>{@link #stop()} method interrupts the event loop thread to stop its execution.</li>
 *   <li>{@link #delay(long)} method is a utility for adding delays in milliseconds.</li>
 * </ul>
 *
 * <p>The event loop continues to run until all events have been processed and the thread is interrupted.
 * Event handlers are executed in response to specific event keys, and unhandled events are reported to the error stream.
 *
 */

public final class EventLoop {

  public static void main(String[] args) {
    EventLoop eventLoop = new EventLoop();

    new Thread(() -> {
      for (int n = 0; n < 6; n++) {
        delay(1000);
        eventLoop.dispatch(new EventLoop.Event("tick", n));
      }
      eventLoop.dispatch(new EventLoop.Event("stop", null));
    }).start();

    new Thread(() -> {
      delay(2500);
      eventLoop.dispatch(new EventLoop.Event("hello", "beautiful world"));
      delay(800);
      eventLoop.dispatch(new EventLoop.Event("hello", "beautiful universe"));
    }).start();

    eventLoop.dispatch(new EventLoop.Event("hello", "world!"));
    eventLoop.dispatch(new EventLoop.Event("foo", "bar"));

    eventLoop
            .on("hello", s -> System.out.println("hello " + s))
            .on("tick", n -> System.out.println("tick #" + n))
            .on("stop", v -> eventLoop.stop())
            .run();

    System.out.println("Bye!");
  }
  public static final class Event {
    private final String key;
    private final Object data;

    public Event(String key, Object data) {
      this.key = key;
      this.data = data;
    }
  }
  private final ConcurrentLinkedDeque<Event> events = new ConcurrentLinkedDeque<>();
  private final ConcurrentHashMap<String, Consumer<Object>> handlers = new ConcurrentHashMap<>();

  public EventLoop on(String key, Consumer<Object> handler) {
    handlers.put(key, handler);
    return this;
  }

  public void dispatch(Event event) {
    events.add(event);
  }

  public void run() {
    while (!(events.isEmpty() && Thread.interrupted())) {
      if (!events.isEmpty()) {
        Event event = events.pop();
        if (handlers.containsKey(event.key)) {
          handlers.get(event.key).accept(event.data);
        } else {
          System.err.println("No handler for key " + event.key);
        }
      }
    }
  }

  public void stop() {
    Thread.currentThread().interrupt();
  }

  private static void delay(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}