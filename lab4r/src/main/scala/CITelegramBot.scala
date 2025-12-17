import cats.effect.MonadCancelThrow
import cats.effect.kernel.Sync
import cats.syntax.functor.*
import com.bot4s.telegram.api.declarative.Commands
import com.bot4s.telegram.cats.{Polling, TelegramBot}
import eu.timepit.refined.auto.*
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import sttp.client4.Backend

class CITelegramBot[F[_] : Sync] private(
  botAppConfig: BotAppConfig,
  backend: Backend[F],
  logger: Logger[F]
)(using MCT: MonadCancelThrow[F]) extends
  TelegramBot[F](botAppConfig.telegramApiConfig.token, backend),
  Commands[F],
  Polling[F] {

  onCommand("start") { implicit msg =>
    for {
      _ <- reply("Бот запущен. Команда /start обработана.")
    } yield ()
  }

}

object CITelegramBot {
  def create[F[_] : Sync]
  (
    botAppConfig: BotAppConfig,
    backend: Backend[F]
  )(
    using
    MCT: MonadCancelThrow[F]
  ): F[CITelegramBot[F]] =
    Slf4jLogger
      .fromName[F]("CITelegramBot")
      .map(CITelegramBot(botAppConfig, backend, _))
}