package org.example

import ai.koog.prompt.dsl.prompt
import ai.koog.prompt.executor.clients.LLMClient
import ai.koog.prompt.executor.clients.google.GoogleLLMClient
import ai.koog.prompt.executor.clients.google.GoogleModels

import ai.koog.prompt.llm.LLModel
import ai.koog.prompt.params.LLMParams

suspend fun main() {

    val client : LLMClient = GoogleLLMClient(apiKey = "AIzaSyCY_dOBIdobrEtW3wxmyyGhjriP52BwomU")
    val model: LLModel = GoogleModels.Gemini2_5Flash

    val userMessage = readln()
    val prompt = prompt(
        id = "translation-request",
        params = LLMParams(temperature = 0.7)
    ){
        user(content = "Translate this sentence to Tagalog/Filipino: $userMessage")
    }

    val response = client.execute(prompt,model)

    with(response.first()){
        println("Translation: $content")
        println("Meta-info: $metaInfo")
    }

}

