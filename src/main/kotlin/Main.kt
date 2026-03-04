package org.example

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.tool
import ai.koog.agents.features.eventHandler.feature.EventHandler
import ai.koog.agents.features.eventHandler.feature.handleEvents
import ai.koog.prompt.dsl.prompt
import ai.koog.prompt.executor.clients.LLMClient
import ai.koog.prompt.executor.clients.google.GoogleLLMClient
import ai.koog.prompt.executor.clients.google.GoogleModels
import ai.koog.prompt.executor.llms.SingleLLMPromptExecutor
import ai.koog.prompt.executor.llms.all.simpleGoogleAIExecutor
import ai.koog.prompt.executor.llms.all.simpleOllamaAIExecutor

import ai.koog.prompt.llm.LLModel
import ai.koog.prompt.llm.OllamaModels
import ai.koog.prompt.params.LLMParams


@Tool
@LLMDescription("Sends money to specified recipient with given amount and purpose")
fun sendMoney(
    @LLMDescription("Amount of money to send in Pesos")
    amount: Double,
    @LLMDescription("Name of the recipient")
    recipient:String,
    @LLMDescription("Purpose of the Money Transfer")
    purpose:String) : String {
    println("----------------------------------------------")
    println("Sending money to $recipient for $amount pesos with purpose: $purpose")
    println("Please confirm the transaction by typing 'yes'")
    println("----------------------------------------------")
    val confirmation = readln()

    return if (confirmation.lowercase() == "yes") "Money Sent" else "Transaction declined"

}


suspend fun main() {


//    val client : LLMClient = GoogleLLMClient(apiKey = System.getenv("GOOGLE_API_KEY"))
//    val model: LLModel = GoogleModels.Gemini2_5Pro


    val executor = simpleOllamaAIExecutor(baseUrl = "http://localhost:11434/")
    val model : LLModel = OllamaModels.Alibaba.QWEN_2_5_05B

    val toolRegistry = ToolRegistry {
        tool(::sendMoney)
    }
    val agent = AIAgent(
        executor = executor,
        llmModel = model,
        toolRegistry = toolRegistry,
        systemPrompt = "You're a banking assistant. Accompany the user with their request."
    ) {
//        handleEvents {  }
        install(EventHandler) {
             onBeforeLLMCall { ctx ->
                 println("Request to LLM:")
                 println("     # Messages:")
                 ctx.prompt.messages.forEach { println("    - $it") }
                 println("     # Tools:")
                 ctx.tools.forEach { println("    - $it") }
             }
            onAfterLLMCall { ctx ->
                println("Response from LLM:")
                ctx.tools.forEach { println("    - $it") }

            }

        }
    }

    val userMessage = "Send 25 pesos to Daniel for dinner at the restaurant"

    val response = agent.run(userMessage)

    println("Final Result: $response")

}

