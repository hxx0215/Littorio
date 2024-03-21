package org.iris

import fs2.io.file.Files
import cats.MonadThrow
import org.http4s.dsl.Http4sDsl
import org.http4s.HttpApp
import org.http4s.HttpRoutes
import org.http4s.StaticFile
import fs2.Stream
import fs2.Pipe
import org.http4s.websocket.WebSocketFrame
import org.http4s.server.websocket.WebSocketBuilder2

class Routes[F[_]: Files: MonadThrow] extends Http4sDsl[F] {
  def service(wsb: WebSocketBuilder2[F]): HttpApp[F] = 
    HttpRoutes.of[F]{
      case request @ GET -> Root / "chat.html" => 
        StaticFile.fromPath(
          fs2.io.file.Path(getClass().getClassLoader().getResource("chat.html").getFile()),
          Some(request)
        ).getOrElseF(NotFound())
      case GET -> Root / "ws" => 
        val send: Stream[F, WebSocketFrame] = ???
        val receive: Pipe[F, WebSocketFrame, Unit] = ???
        wsb.build(send, receive)
    }.orNotFound

  
}