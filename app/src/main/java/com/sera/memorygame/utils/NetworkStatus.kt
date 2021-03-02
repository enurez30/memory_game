package com.sera.memorygame.utils

enum class NetworkStatus(val status: Int) {
    NONE(-1),
    START(0),
    ERROR(1),
    NO_INTERNET_CONNECTION(2),
    FINISH(3)
}