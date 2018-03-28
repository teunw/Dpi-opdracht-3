package nl.teun

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import java.util.*

class RandomSentenceService {
    fun getSentences(): List<String> {
        val url = "https://randomwordgenerator.com/json/sentences.json"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute().body()!!.string()
        val obj = Gson().fromJson<WordGeneratorResult>(response)
        return obj.data.map { it -> it.sentence }
    }
}

val random = Random()

inline fun <reified T> List<T>.getRandomElement(): T = this[random.nextInt(this.size)]
private inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object : TypeToken<T>() {}.type)

class WordGeneratorResult {
    var data: List<WordGeneratorSentence> = mutableListOf()
}

class WordGeneratorSentence {
    var sentence: String = ""
}