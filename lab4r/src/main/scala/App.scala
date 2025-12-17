
import cats.effect.*
import sttp.client4.asynchttpclient.cats.AsyncHttpClientCatsBackend

object App extends ResourceApp.Forever {
  
  def run(args: List[String]): Resource[IO, Unit] = for
    config <- Resource.eval(IO(loadBotConfig))
    botClientBackend <- AsyncHttpClientCatsBackend.resource[IO]()
    bot <- CITelegramBot
      .create[IO](config, botClientBackend)
      .toResource
    fiber <- Resource.make(bot.run().start)(_ => IO.blocking(bot.shutdown()))
  yield ExitCode.Success
}
