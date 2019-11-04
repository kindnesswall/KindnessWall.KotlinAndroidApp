package com.farshidabz.kindnesswall.utils

import java.io.Serializable


/**
 * Created by Farshid Abazari since 25/10/19
 *
 * Usage: bottom navigation fragments stack
 *
 * How to call: call push to push fragment to the stack of navigation history and call popPrevious
 * to show last fragment
 */

class BottomTabHistory : Serializable {

    private val stack: ArrayList<Int> = ArrayList()

    private val isEmpty: Boolean
        get() = stack.size == 0

    val size: Int
        get() = stack.size

    fun push(entry: Int,removeExist:Boolean = true) {
        if(removeExist)
            stack.remove(entry)
        stack.add(entry)
    }

    fun popPrevious(): Int {
        var entry = -1

        if (!isEmpty) {
            entry = stack[stack.size - 2]
            stack.removeAt(stack.size - 2)
        }
        return entry
    }

    fun hasNext():Boolean
    {
        return stack.size!=0
    }

    fun clear() {
        stack.clear()
    }
}
