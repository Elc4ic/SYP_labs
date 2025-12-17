import eu.timepit.refined.types.string.NonEmptyString

final case class BotAppConfig(
  telegramApiConfig: TelegramApiConfig,
)

final case class TelegramApiConfig(
  token: NonEmptyString
)

def loadBotConfig: BotAppConfig = BotAppConfig(
  telegramApiConfig = TelegramApiConfig(token =
    NonEmptyString.unsafeFrom(sys.env.getOrElse("BOT_API_TOKEN", throw Exception("no token")))
  ),
)
