fun main(){
    val code = PlayfairCipher("ILYA", "ABCDEFGHIJKLMNOPQRSTUVWXY")
    println(code.encryptionProcess(code.keyTable))
    println(code.fillKeyTable().toString())
    val keyTable = code.fillKeyTable()
    keyTable.forEach {
        it.forEach {
            print(" $it ")
        }
        println()
    }
    val name = code.encryptionProcess(code.keyTable)
    val code1 = PlayfairCipher(name, "ABCDEFGHIJKLMNOPQRSTUVWXY")
    println(code1.encryptionProcess(code1.keyTable))
    println(code.formatInput().toString())


    val gammaWork = Gamma("привет")
    val res = gammaWork.encryptionProcess()
    println(res)

    println(gammaWork.decryptProcess(res))
}

