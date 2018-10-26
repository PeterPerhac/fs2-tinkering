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

}

