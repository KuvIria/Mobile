import java.io.File
import java.io.IOException

// Функция для шифрования и расшифровки текста (русский и английский алфавиты)
fun caesarCipher(text: String, shift: Int, encrypt: Boolean = true): String {
    val engAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val rusAlphabet = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ"

    val lowerEngAlphabet = engAlphabet.lowercase()
    val lowerRusAlphabet = rusAlphabet.lowercase()

    val shiftDirection = if (encrypt) shift else -shift

    return text.map { char ->
        when {
            char in engAlphabet -> shiftChar(char, engAlphabet, shiftDirection)
            char in lowerEngAlphabet -> shiftChar(char, lowerEngAlphabet, shiftDirection)
            char in rusAlphabet -> shiftChar(char, rusAlphabet, shiftDirection)
            char in lowerRusAlphabet -> shiftChar(char, lowerRusAlphabet, shiftDirection)
            else -> char // Не меняем знаки и пробелы
        }
    }.joinToString("")
}

// Сдвиг символа в алфавите
fun shiftChar(char: Char, alphabet: String, shift: Int): Char {
    val newIndex = (alphabet.indexOf(char) + shift + alphabet.length) % alphabet.length
    return alphabet[newIndex]
}

// Чтение файла
fun readFile(filePath: String): String? {
    return try {
        File(filePath).readText(Charsets.UTF_8)
    } catch (e: IOException) {
        println("❌ Ошибка при чтении файла: ${e.message}")
        null
    }
}

// Запись в файл
fun writeFile(filePath: String, content: String) {
    try {
        File(filePath).writeText(content, Charsets.UTF_8)
    } catch (e: IOException) {
        println("❌ Ошибка при записи в файл: ${e.message}")
    }
}

// Валидация ключа
fun isValidKey(key: Int): Boolean = key >= 0

fun main() {
    while (true) {
        println("\n🔹 Выберите режим работы:")
        println(" 1️⃣ - Шифрование")
        println(" 2️⃣ - Расшифровка")
        println(" 3️⃣ - Brute force")
        println(" 4️⃣ - Выход")

        when (readLine()) {
            "1" -> {
                println("📄 Введите путь к исходному файлу (или !выход для отмены):")
                val inputFile = readLine()
                if (inputFile == "!выход") continue

                println("📂 Введите путь для сохранения зашифрованного файла:")
                val outputFile = readLine()
                if (outputFile == "!выход") continue

                println("🔑 Введите ключ (сдвиг):")
                val shift = readLine()?.toIntOrNull()
                if (shift == null || !isValidKey(shift)) {
                    println("❌ Ошибка: неверный ключ.")
                    continue
                }

                val inputText = readFile(inputFile!!)
                if (inputText != null) {
                    val encryptedText = caesarCipher(inputText, shift, encrypt = true)
                    writeFile(outputFile!!, encryptedText)
                    println("✅ Текст успешно зашифрован и сохранен.")
                }
            }
            "2" -> {
                println("📄 Введите путь к зашифрованному файлу (или !выход для отмены):")
                val inputFile = readLine()
                if (inputFile == "!выход") continue

                println("📂 Введите путь для сохранения расшифрованного файла:")
                val outputFile = readLine()
                if (outputFile == "!выход") continue

                println("🔑 Введите ключ (сдвиг):")
                val shift = readLine()?.toIntOrNull()
                if (shift == null || !isValidKey(shift)) {
                    println("❌ Ошибка: неверный ключ.")
                    continue
                }

                val inputText = readFile(inputFile!!)
                if (inputText != null) {
                    val decryptedText = caesarCipher(inputText, shift, encrypt = false)
                    writeFile(outputFile!!, decryptedText)
                    println("✅ Текст успешно расшифрован и сохранен.")
                }
            }
            "3" -> {
                println("📄 Введите путь к зашифрованному файлу для brute force (или !выход для отмены):")
                val inputFile = readLine()
                if (inputFile == "!выход") continue

                val inputText = readFile(inputFile!!)
                if (inputText != null) {
                    println("🔎 Попытки расшифровки с разными ключами...")
                    for (shift in 0 until 33) { // 33 символа в русском алфавите
                        val decryptedText = caesarCipher(inputText, shift, encrypt = false)
                        println("🔑 Ключ: $shift - $decryptedText")
                    }
                }
            }
            "4" -> {
                println("👋 Программа завершена.")
                return
            }
            else -> println("⚠️ Неверный ввод. Попробуйте снова.")
        }
    }
}
