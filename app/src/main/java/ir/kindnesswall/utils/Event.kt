package ir.kindnesswall.utils

class Event<T>(private val content: T) {
    var hasBeenHandled = false
        private set

    val contentIfNotHandled: T?
        get() =
            if (hasBeenHandled) null
            else {
                hasBeenHandled = true
                content
            }

    fun peekContent(): T? = content

    fun ifNotHandled(eventHandler: (T) -> Unit) {
        if (!hasBeenHandled) {
            hasBeenHandled = true
            eventHandler.invoke(content)
        }
    }
}