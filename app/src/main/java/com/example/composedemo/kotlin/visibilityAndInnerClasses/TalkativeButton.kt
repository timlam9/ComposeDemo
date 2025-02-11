package com.example.composedemo.kotlin.visibilityAndInnerClasses

internal open class TalkativeButton {

    private fun yell() = println("Hey!")


    protected fun whisper() = println("Let's talk")
}

private class YellingButton : TalkativeButton() {

    fun callWhisper() = super.whisper()


    // Cannot call yell
//    fun callYell() = yell()
}

// Cannot call whisper
//private class Test {
//
//    init {
//        val talkativeButton = TalkativeButton()
//        talkativeButton.whisper()
//    }
//}
//
//// Visibility error
//fun TalkativeButton.giveSpeech() {
//    yell()
//    whisper()
//}


// 9. You can use protected to make a method accessible only to subclasses
