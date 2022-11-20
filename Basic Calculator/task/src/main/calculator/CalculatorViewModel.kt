package org.hyperskill.calculator

import android.view.View
import android.widget.Button
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {
    private var firstValue: Double = 0.0
    private var trackedOperation: String = ""

    // this is for safety so we can change result and hint itself only in this inner class,
    // but outside we could only get that
    private val result = MutableLiveData<String>()
    val getResult: LiveData<String>
        get() = result

    // postValue is running on the background thread, changes are visible only when main thread is acti  ve
    private val hint = MutableLiveData<String>().apply { postValue("0") }
    val getHint: LiveData<String>
        get() = hint


    /**
     * method to handle numeric buttons from 1-9
     * @param buttonValue the name of the button
     */
    fun handleNumeric(buttonValue: String) {
        /* logic: if in result we have a zero then we need to assign with a new value
                  if we don't have a zero then add to an existing result
         */

        // elvis operator to protect from null result.value != null ? result.value : ""
        // ?: "" instead of null
        result.value =
            if ((result.value ?: "") == "0") buttonValue else (result.value ?: "") + buttonValue
    }

    // handle operations
    /**
     * method to handle operational buttons like +, -, =, etc
     * @param operation the name of the operation
     */
    fun handleOperational(operation: String) {
        /* logic: We need to handle different operations so, we need to use conditions
                  When operational button is clicked handle operation
         */

        // protect second value from null
        // if pressed equals right away then set to 0.0
        val secondValue = (result.value ?: "0").toDoubleOrNull() ?: 0.0

        // by default firstvalue is not a first clicked - need to fix it

        // if operation havent been there yet -> therefore its a first value
        // now secondValue can be set to first value at first operation
        // we need to set our tracking operation to default at first
        // click "+" tacked is "=" therefore just assign and keep "+" -> at "=" then it's "+" add -> then next operation, tracked is "=" again
        when (trackedOperation) {
            "+" -> firstValue += secondValue
            "-" -> firstValue -= secondValue
            "*" -> firstValue *= secondValue
            "/" -> firstValue /= secondValue
            else -> firstValue = secondValue
        }

        val checkedValue = { (if (firstValue % 1.0 == 0.0) firstValue.toInt() else firstValue).toString() }

        // when equal set result in text, when its operation set hint to result
        if (operation == "=") {
            result.value = checkedValue()
            firstValue = 0.0
        } else {
            hint.value = checkedValue()
            result.value = ""
            trackedOperation = operation
        }

    }

    // listener for zero
    fun handleZero(zero: String) {
        result.value = if ((result.value ?: "") !in "-0") (result.value ?: "") + zero else zero
    }


    // creates an instructions for "-" and call the function for handling operational buttons
    fun handleSubtraction(subtraction: String) {
        if ((result.value ?: "").isEmpty()) result.value = subtraction else handleOperational(
            subtraction
        )
    }

    fun handleDot(dot: String) {
        if ((result.value ?: "") in "-") result.value = "0$dot"
    }

    // method for clearing
    fun clear() {
        firstValue = 0.0
        result.value = ""
        hint.value = "0"
        trackedOperation = ""
    }
}
