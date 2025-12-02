package Main.registration.bots

// Объект-синглтон для управления всеми ботами. Доступен из любой части программы.
object ChatBotSystem {

    // Ответ по умолчанию, если бот не знает фразы
    private const val DEFAULT_RESPONSE = "Я не знаю, что на это ответить."

    // --- Создаем 4 ботов ---
    private val botViktor = Bot(
        name = "Виктор",
        responses = mapOf(
            "привет" to "Здравствуй, человек!",
            "как дела" to "Все отлично, спасибо, что спросил!",
            "что делаешь" to "Общаюсь с тобой.",
            "пока" to "До скорой встречи!"
        )
    )

    private val botAnna = Bot(
        name = "Анна",
        responses = mapOf(
            "привет" to "Привет! Рада тебя видеть.",
            "как дела" to "Лучше всех! А у тебя?",
            "что делаешь" to "Мечтаю об электроовцах.",
            "пока" to "Пока-пока!"
        )
    )

    private val botMax = Bot(
        name = "Макс",
        responses = mapOf(
            "привет" to "Йоу!",
            "как дела" to "Норм. Ты как?",
            "что делаешь" to "Смотрю в цифровое ничто.",
            "пока" to "Бывай."
        )
    )

    private val botEva = Bot(
        name = "Ева",
        responses = mapOf(
            "привет" to "Добро пожаловать.",
            "как дела" to "Все системы в норме.",
            "что делаешь" to "Анализирую данные.",
            "пока" to "До связи."
        )
    )

    // Список всех ботов, которых можно использовать
    val allBots = listOf(botViktor, botAnna, botMax, botEva)

    /**
     * Основная функция для получения ответа от бота.
     * @param bot Экземпляр бота, который должен ответить.
     * @param userMessage Сообщение от пользователя.
     * @return Ответ бота или фраза по умолчанию.
     */
    fun getResponse(bot: Bot, userMessage: String): String {
        // Приводим фразу пользователя к нижнему регистру и убираем пробелы по краям
        val cleanMessage = userMessage.trim().lowercase()
        // Ищем ответ в словаре бота. Если не находим (результат null), возвращаем DEFAULT_RESPONSE
        return bot.responses[cleanMessage] ?: DEFAULT_RESPONSE
    }
}
