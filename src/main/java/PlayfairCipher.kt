import java.util.ArrayList

class PlayfairCipher(
// User inputted text and key taken from the constructor
    private val plainText: String, private val key: String
) {
    // Boolean values for which rule we need to use
    private var columnRule = false
    private var rowRule = false
    private var thirdRule = false

    // Two-letter chunk version of plaintext input
    private val formattedInput: Array<CharArray>

    // 5x5 key table to use for encryption
    var keyTable: Array<CharArray>


    // Constructor that takes a plaintext input and key from the user
    init {
        formattedInput = formatInput()
        keyTable = fillKeyTable()
    }

    // Format input into two letter chunks
    // If uneven letters, 'Z' is added to last chunk
    fun formatInput(): Array<CharArray> {
        val tempArray = ArrayList<CharArray>()
        run {
            var i = 0
            while (i < plainText.length) {
                val tempChar = CharArray(2)
                var j = 0
                while (j < tempChar.size) {
                    while (!Character.isLetter(plainText[i + j])) i++
                    tempChar[j] = plainText.toUpperCase()[i + j]
                    if (i == plainText.length - 1) tempChar[++j] = 'Z'
                    j++
                }
                tempArray.add(tempChar)
                i += 2
            }
        }
        val formattedInput = Array(tempArray.size) { CharArray(2) }
        for (i in tempArray.indices) formattedInput[i] = tempArray[i]
        return formattedInput
    }

    // TODO duplicateCheck methods -> change parameters because we're using class variables
    // Takes the key and fills the 5x5 key table
    fun fillKeyTable(): Array<CharArray> {
        // 5x5 key table
        keyTable = Array(5) { CharArray(5) }
        var i = 0
        tableloop@ while (i < key.length) {
            for (row in 0..4) {
                for (column in 0..4) {
                    while (Character.isWhitespace(key[i])) i++
                    while (duplicateCheck(keyTable, i)) i++
                    keyTable[row][column] = key.toUpperCase()[i++]
                    if (i >= key.length) break@tableloop
                }
            }
        }

        // Putting missing letters of ABC into key table following the text letters
        // Q is omitted (table is only 25 letters)
        // r(ow) and c(olumn) variable set to the next empty index
        var r = 0
        var c = 0
        findemptyloop@ while (r < 5) {
            while (c < 5) {
                if (keyTable[r][c] == '0') break@findemptyloop
                c++
            }
            c = 0
            r++
        }

        // If text that was put into key table ends in the middle of a row, fill it separately
        var ch = 'A'
        if (c != 0) {
            while (c < 5) {
                while (duplicateCheck(keyTable, ch)) ch++
                if (ch != 'Q') keyTable[r][c] = ch++
                c++
            }
            r++
        }

        // Rest of the key table is filled with ABC
        while (r < 5) {
            for (column in 0..4) {
                if (ch == 'Q') ch++
                while (duplicateCheck(keyTable, ch)) ch++
                keyTable[r][column] = ch++
            }
            r++
        }
        return keyTable
    }

    // Checks if any letters that are about to be added to the key table are not already there to avoid duplicates
    fun duplicateCheck(keyTable: Array<CharArray>, itr: Int): Boolean {
        for (row in 0..4) {
            for (column in 0..4) {
                if (keyTable[row][column] == key.toUpperCase()[itr]) {
                    return true
                }
            }
        }
        return false
    }

    // Function overload for when char is used as iterator
    fun duplicateCheck(keyTable: Array<CharArray>, ch: Char): Boolean {
        for (row in 0..4) {
            for (column in 0..4) {
                if (keyTable[row][column] == ch) {
                    return true
                }
            }
        }
        return false
    }

    // Actual encryption process
    // If both letters are in the same column, take the letter below each one
    // If both letters are in the same row, take the letter to the right of each one
    // If neither of the preceding two rules are true, form a rectangle of two letters and take the letters on the horizontal opposite corner of the rectangle
    fun encryptionProcess(keyTable: Array<CharArray>): String {
        // Finding correct rule and finding index in key table
        val firstIndex = IntArray(2)
        val secondIndex = IntArray(2)
        val encrypted = CharArray(formattedInput.size * 2)
        var i = 0
        var j = 0
        while (i < formattedInput.size) {
            for (r in 0..4) {
                for (c in 0..4) {
                    if (formattedInput[i][0] == keyTable[r][c]) {
                        firstIndex[0] = r
                        firstIndex[1] = c
                    }
                    if (formattedInput[i][1] == keyTable[r][c]) {
                        secondIndex[0] = r
                        secondIndex[1] = c
                    }
                }
            }

            // Getting correct encrypted letter from key table
            findRule(keyTable, formattedInput[i])
            if (columnRule) {
                if (firstIndex[0] == 4) firstIndex[0] = -1
                encrypted[j++] = keyTable[firstIndex[0] + 1][firstIndex[1]]
                if (secondIndex[0] == 4) secondIndex[0] = -1
                encrypted[j] = keyTable[secondIndex[0] + 1][secondIndex[1]]
            }
            if (rowRule) {
                if (firstIndex[1] == 4) firstIndex[1] = -1
                encrypted[j++] = keyTable[firstIndex[0]][firstIndex[1] + 1]
                if (secondIndex[1] == 4) secondIndex[1] = -1
                encrypted[j] = keyTable[secondIndex[0]][secondIndex[1] + 1]
            }
            if (thirdRule) {
                encrypted[j++] = keyTable[firstIndex[0]][secondIndex[1]]
                encrypted[j] = keyTable[secondIndex[0]][firstIndex[1]]
            }
            i++
            j++
        }
        return String(encrypted)
    }

    // Method iterates through input to check which rule we need to use on each chunk
    fun findRule(keyTable: Array<CharArray>, chunk: CharArray) {
        // Resetting all boolean values to false
        columnRule = false
        rowRule = false
        thirdRule = false

        // Storing indexes of letters of chunks in key table to later compare positions and decide rule
        val firstIndex = IntArray(2)
        val secondIndex = IntArray(2)
        for (r in 0..4) {
            for (c in 0..4) {
                if (chunk[0] == keyTable[r][c]) {
                    firstIndex[0] = r
                    firstIndex[1] = c
                }
                if (chunk[1] == keyTable[r][c]) {
                    secondIndex[0] = r
                    secondIndex[1] = c
                }
            }
        }
        if (firstIndex[1] == secondIndex[1]) columnRule = true else if (firstIndex[0] == secondIndex[0]) rowRule =
            true else thirdRule = true
    }
}