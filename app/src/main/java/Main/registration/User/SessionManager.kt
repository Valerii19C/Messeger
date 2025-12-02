package Main.registration.User

import Main.registration.Data.Users

/**
 * Простой синглтон для хранения данных о текущей сессии.
 */
object SessionManager {
    // Хранит информацию о пользователе, который сейчас вошел в систему.
    var currentUser: Users? = null
}
