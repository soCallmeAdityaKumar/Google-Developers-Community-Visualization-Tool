package com.example.googledeveloperscommunityvisualisationtool.TextToSpeech

import android.content.Context
import android.speech.tts.TextToSpeech

class TextToSpeechClass(val context:Context) {
    var ttsCode:Int =0
    lateinit var textToSpeech:TextToSpeech
    fun initialise():Int{
        textToSpeech=TextToSpeech(context){
            ttsCode=it
        }
        return ttsCode
    }
    fun speakText(text:String){
        textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null, null)
    }
    fun speechStop(){
        textToSpeech.stop()
        textToSpeech.shutdown()
    }
}