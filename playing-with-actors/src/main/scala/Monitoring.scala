package com.packt.akka

import akka.actor.{ ActorRef, ActorSystem, Props, Actor, Terminated }


class Ares(athena: ActorRef) extends Actor {

  override def preStart() = {
    context.watch(athena)
  }

  override def postStop() = {
    println("Ares postStop...")
  }

  def receive = {
    // you have to destructure this, otherwise it will not match
    case Terminated(a) => 
      context.stop(self)
    case _ => println("something else happened to athena...")
  }
}

class Athena extends Actor {

  def receive = {
    case msg => 
      println(s"Athena received ${msg}")
      context.stop(self)
  }

}

object Monitoring extends App {

 // Create the 'monitoring' actor system
 val system = ActorSystem("monitoring")

 val athena = system.actorOf(Props[Athena], "athena")

 val ares = system.actorOf(Props(classOf[Ares], athena), "ares")

 athena ! "Hi"

 system.terminate()


}
