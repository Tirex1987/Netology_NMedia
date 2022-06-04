package ru.netology.nmedia.utils

import com.google.android.material.button.MaterialButton

internal fun MaterialButton.setTextAmountFormat(amount : Int) {
    this.text = getAmountFormat(amount)
}

private fun getAmountFormat(amount: Int): String = when {
    amount >= 1_000_000 -> "%.1f".format((amount / 100_000) / 10.0) + "M"
    amount >= 10_000 -> "${amount / 1_000}K"
    amount >= 1_000 -> "%.1f".format((amount / 100) / 10.0) + "K"
    else -> "$amount"
}