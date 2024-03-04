package com.example;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

import java.util.Objects;

// #greeter
public class Greeter extends AbstractBehavior<Greeter.Greet> {

  //public static record Greet(String whom, ActorRef<Greeted> replyTo) {}
  //public static record Greeted(String whom, ActorRef<Greet> from) {}

  //重写Greet
  public static final class Greet{
    private final String whom;
    private final ActorRef<Greeted> replyTo;

    public Greet(String whom, ActorRef<Greeted> replyTo){
      this.whom = whom;
      this.replyTo = replyTo;
    }

    public String whom(){
      return this.whom;
    }

    public ActorRef<Greeted> replyTo(){
      return this.replyTo;
    }

    public String toString(){
      return String.format("Point[x=%s, y=%s]", this.whom, this.replyTo);
    }

    public boolean equals(Object o){
      return true;
    }

    public int hashCode(){
      return 110;
    }
  }

  //重写Greeted
  public static final class Greeted{
    private final String whom;
    private final ActorRef<Greet> from;

    public Greeted(String whom, ActorRef<Greet> from){
      this.whom = whom;
      this.from = from;
    }

    public String whom(){
      return this.whom;
    }

    public ActorRef<Greet> from(){
      return this.from;
    }

    public String toString(){
      return String.format("Point[x=%s, y=%s]", this.whom, this.from);
    }

    public boolean equals(Object o){
      return false;
    }

    public int hashCode(){
      return 120;
    }
  }


  public static Behavior<Greet> create() {
      return Behaviors.setup(Greeter::new);
  }

  private Greeter(ActorContext<Greet> context) {
    super(context);
  }

  @Override
  public Receive<Greet> createReceive() {
    return newReceiveBuilder().onMessage(Greet.class, this::onGreet).build();
  }

  private Behavior<Greet> onGreet(Greet command) {
    getContext().getLog().info("Hello {}!", command.whom());
    //#greeter-send-message
    command.replyTo().tell(new Greeted(command.whom(), getContext().getSelf()));
    //#greeter-send-message
    return this;
  }
}
// #greeter