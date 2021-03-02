package com.sera.memorygame.utils

enum class NetworkStatus(val status: Int) {
    NONE(-1),
    START(0),
    DOWNLOAD(1),
    ERROR(2),
    NO_INTERNET_CONNECTION(3),
    FINISH(4)
}