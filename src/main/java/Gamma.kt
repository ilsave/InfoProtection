/*
Задание №2.1
Реализовать алгоритм шифрования данных «Шифрование методом гаммирования»
По модулю 2
 */

class Gamma(private val text: String) {

    private val validChars = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя ,."

    private var gammaWord: String = ""

    init {
        isInputValid(text)
        generateGammaWord()
    }

    fun isInputValid(strInput: String): Boolean {
        return strInput.toList().filter { isCharValid(it) }.size == strInput.length
    }

    private fun isCharValid(ch: Char): Boolean {
        return validChars.firstOrNull { it == ch } != null
    }

    private fun generateGammaWord() {
        for(i in text.indices){
            gammaWord += validChars[(0..validChars.length).random()]
        }
    }


    fun decryptProcess(result: List<String>): List<String> {
        val totalResult = mutableListOf<String>()
        for (i in result.indices) {

            val textIndex = validChars.indexOf(result[i])
            val gammaIndex = validChars.indexOf(gammaWord[i])

            val sum = textIndex - gammaIndex + validChars.length
            val modOperation = sum % validChars.length
            val modValue = if (modOperation == 0) validChars.length else modOperation

            totalResult.add(validChars[modValue].toString())
        }
        return totalResult
    }

    fun encryptionProcess(): List<String> {
        val result = mutableListOf<String>()

        for (i in text.indices) {

            val textIndex = validChars.indexOf(text[i])
            val gammaIndex = validChars.indexOf(gammaWord[i])

            val sum = textIndex + gammaIndex
            val modOperation = sum % validChars.length
            val modValue = if (modOperation == 0) validChars.length else modOperation

            result.add(validChars[modValue].toString())
        }
        return result
    }
}