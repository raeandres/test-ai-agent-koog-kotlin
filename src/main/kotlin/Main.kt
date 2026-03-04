package org.example

import ai.koog.prompt.dsl.prompt
import ai.koog.prompt.executor.clients.LLMClient
import ai.koog.prompt.executor.clients.google.GoogleLLMClient
import ai.koog.prompt.executor.clients.google.GoogleModels

import ai.koog.prompt.llm.LLModel
import ai.koog.prompt.params.LLMParams

suspend fun main() {

    val client : LLMClient = GoogleLLMClient(apiKey = System.getenv("GOOGLE_API_KEY"))
    val model: LLModel = GoogleModels.Gemini2_5Flash

    val prompt = prompt(
        id = "translation-request",
        params = LLMParams(temperature = 0.7)
    ){
        system("""
       You're a banking assistant. You can send money by writing the following JSON:
       {
           "name": "send_money",
           "params": {
               "recipient": <recipient_name>,
               "amount": <amount_in_euros>,
               "purpose": <purpose_of_the_transaction>
           }
       }
   """.trimIndent())
        user(content = "Send 20 euros to Daniel for dinner at the restaurant")
    }

    val response = client.execute(prompt,model)

    with(response.first()){
        println("Translation: $content")
        println("Meta-info: $metaInfo")
    }

}

