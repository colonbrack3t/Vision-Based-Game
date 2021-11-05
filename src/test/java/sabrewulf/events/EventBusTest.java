// package sabrewulf.events;

// import static org.junit.Assert.assertEquals;

// import org.junit.Before;
// import org.junit.Test;

// public class EventBusTest {
// EventBus eventBus;
// TestListener subscribedListener, listenerTwo;

// @Before
// public void setup_tests() {
// eventBus = new EventBus();
// subscribedListener = new TestListener();
// listenerTwo = new TestListener();
// eventBus.subscribe(EventOne.class, subscribedListener);
// }

// @Test
// public void subscriber_is_notified_when_event_occurs() {
// eventBus.trigger(new EventOne());
// assertEquals(1, subscribedListener.callCounter());
// }

// @Test
// public void listener_can_only_subsribe_once() {
// eventBus.subscribe(EventOne.class, subscribedListener);
// eventBus.trigger(new EventOne());

// // although subscribe is called twice the listener is only called once
// assertEquals(1, subscribedListener.callCounter());
// }

// @Test
// public void listener_can_end_subscription() {
// eventBus.unsubscribe(EventOne.class, subscribedListener);
// eventBus.trigger(new EventOne());
// assertEquals(0, subscribedListener.callCounter()); // is now a unsubscribed
// listener
// }

// @Test
// public void multiple_subscriber_can_be_notified() {
// eventBus.trigger(new EventOne());
// eventBus.subscribe(EventOne.class, listenerTwo);
// eventBus.trigger(new EventOne());

// // First listener was notified twice
// assertEquals(2, subscribedListener.callCounter());
// // Second listener was notified once, since it missed the first trigger of
// the event
// assertEquals(1, listenerTwo.callCounter());
// }

// @Test
// public void can_trigger_event_with_no_subscribers() {
// eventBus.trigger(new EventTwo());
// // none of them is subscribed to EventTwo
// assertEquals(0, subscribedListener.callCounter());
// assertEquals(0, listenerTwo.callCounter());
// }

// @Test
// public void listener_can_subscribe_to_multiple_channels() {
// eventBus.subscribe(EventOne.class, listenerTwo);
// eventBus.subscribe(EventTwo.class, listenerTwo);

// // will call channel one
// eventBus.trigger(new EventOne());
// // will call channel two
// eventBus.trigger(new EventTwo());

// assertEquals(2, listenerTwo.callCounter());
// }

// @Test
// public void listener_are_only_notified_when_subscribed_to_the_right_channel()
// {
// eventBus.subscribe(EventTwo.class, listenerTwo);
// eventBus.trigger(new EventOne());
// assertEquals(1, subscribedListener.callCounter());
// assertEquals(0, listenerTwo.callCounter());

// eventBus.trigger(new EventTwo());
// assertEquals(1, subscribedListener.callCounter());
// assertEquals(1, listenerTwo.callCounter());

// eventBus.subscribe(EventTwo.class, subscribedListener);
// eventBus.trigger(new EventTwo());
// assertEquals(2, subscribedListener.callCounter());
// assertEquals(2, listenerTwo.callCounter());
// }
// }

// class TestListener implements EventListener {
// private int callCounter = 0;

// @Override
// public void notify(Event event) {
// callCounter++;
// }

// public int callCounter() {
// return callCounter;
// }
// }

// /*
// * Don't ask my why they need to override the toString method, they just have
// to
// */

// // class EventOne implements Event {

// // @Override
// // public String toString() {
// // return "EventOne []";
// // }
// // }

// // class EventTwo implements Event {
// // @Override
// // public String toString() {
// // return "EventOne []";
// // }
// // }
