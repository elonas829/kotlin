// !API_VERSION: 1.3
// !JVM_DEFAULT_MODE: enable
// JVM_TARGET: 1.8
// WITH_RUNTIME
// FULL_JDK

interface Test {
    @JvmDefault
    fun test() {
    }
}

interface Test2 : Test {

}

interface Test3 : Test2 {

}

fun box(): String {
    try {
        Test3::class.java.getDeclaredMethod("test")
    }
    catch (e: NoSuchMethodException) {
        return "OK"
    }
    return "fail"
}
