package com.example.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var tvDisplay: TextView
    private var currentNumber: String = ""
    private var operator: String = ""
    private var firstNumber: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvDisplay = findViewById(R.id.tvDisplay)

        val buttons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
            R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide,
            R.id.btnClear, R.id.btnEquals, R.id.btnDecimal, R.id.btnPlusMinus,
            R.id.btnPercentage
        )

        buttons.forEach { id ->
            findViewById<Button>(id).setOnClickListener { onButtonClick(it as Button) }
        }
    }

    private fun onButtonClick(button: Button) {
        val buttonText = button.text.toString()

        when (buttonText) {
            "C" -> clear()
            "=" -> calculate()
            "+", "-", "×", "÷" -> setOperator(buttonText)
            "±" -> toggleSign()
            "%" -> percentage()
            "." -> appendDecimal()
            else -> appendNumber(buttonText)
        }
    }

    private fun clear() {
        currentNumber = ""
        operator = ""
        firstNumber = null
        tvDisplay.text = "0"
    }

    private fun setOperator(op: String) {
        if (currentNumber.isNotEmpty()) {
            firstNumber = currentNumber.toDoubleOrNull()
            currentNumber = ""
            operator = op
        }
    }

    private fun toggleSign() {
        if (currentNumber.isNotEmpty()) {
            currentNumber = if (currentNumber.startsWith("-")) {
                currentNumber.substring(1)
            } else {
                "-$currentNumber"
            }
            tvDisplay.text = currentNumber
        }
    }

    private fun percentage() {
        if (currentNumber.isNotEmpty()) {
            currentNumber = (currentNumber.toDouble() / 100).toString()
            tvDisplay.text = currentNumber
        }
    }

    private fun appendDecimal() {
        if (!currentNumber.contains(".")) {
            currentNumber += "."
            tvDisplay.text = currentNumber
        }
    }

    private fun appendNumber(number: String) {
        currentNumber += number
        tvDisplay.text = currentNumber
    }

    private fun calculate() {
        val secondNumber = currentNumber.toDoubleOrNull()
        if (firstNumber != null && secondNumber != null) {
            val result = when (operator) {
                "+" -> firstNumber!! + secondNumber
                "-" -> firstNumber!! - secondNumber
                "×" -> firstNumber!! * secondNumber
                "÷" -> {
                    if (secondNumber != 0.0) {
                        firstNumber!! / secondNumber
                    } else {
                        "Cannot divide by zero"
                    }
                }
                else -> "Unknown operator"
            }
            tvDisplay.text = result.toString()
        }
    }
}
