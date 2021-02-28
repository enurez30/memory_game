package com.sera.memorygame

data class MessageEvent(
    var reciever: String = "",
    var key: String = "",
    var message: String = "",
    var options: HashMap<String, Any> = HashMap()
)
