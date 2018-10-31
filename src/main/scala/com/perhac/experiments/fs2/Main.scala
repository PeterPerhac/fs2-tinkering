package com.perhac.experiments.fs2

import cats.effect.IO
import fs2.Stream

object Main extends App {
  println("Look ma! I stream functionally!")
  Stream.emit(1).repeat.take(10).toList.foreach(println)

  val effectfulStream = Stream.eval(IO {
    println("BEING RUN!!")
    1 + 1
  })
  effectfulStream.compile.toList.unsafeRunSync().foreach(println)
  val err = Stream.raiseError[IO](new Exception("oh noes!"))
  val err2 = Stream(1,2,3) ++ (throw new Exception("!@#$"))
  val err3 = Stream.eval(IO(throw new Exception("error in effect!!!")))
  val count = new java.util.concurrent.atomic.AtomicLong(0)
  val acquire = IO { println("incremented: " + count.incrementAndGet); () }
  val release = IO { println("decremented: " + count.decrementAndGet); () }
  Stream.bracket(acquire)(_ => release).flatMap(_ => Stream(1,2,3) ++ err).compile.drain.unsafeRunSync()

  Stream(1,0).repeat.take(6).toList
//  List[Int] = List(1, 0, 1, 0, 1, 0)
  Stream(1,2,3).drain.toList
//  List[fs2.INothing] = List()
  Stream.eval_(IO(println("!!"))).compile.toVector.unsafeRunSync()
//  Vector[fs2.INothing] = Vector()
  (Stream(1,2) ++ (throw new Exception("nooo!!!"))).attempt.toList
//  List[Either[Throwable,Int]] = List(Right(1), Right(2), Left(java.lang.Exception: nooo!!!))

}

