package ir.kindnesswall

class Asghar private constructor() {
    @get:Synchronized
    var instance: Asghar? = null
        get() {
            if (field == null) {
                field = this
            }
            return field
        }

    companion object {
        var instance: Asghar? = null
    }
}