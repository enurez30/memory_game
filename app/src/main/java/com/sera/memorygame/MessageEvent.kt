package com.sera.memorygame

data class MessageEvent(
    var reciever: String = "",
    var key: String = "",
    var message: String = "",
    var network_status: Int = -1,
    var options: HashMap<String, Any> = HashMap()
)
