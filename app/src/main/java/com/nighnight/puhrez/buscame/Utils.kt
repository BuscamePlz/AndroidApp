package com.nighnight.puhrez.buscame

import java.util.*

fun varyDouble(f: Double, rand: Random, variance: Double): Double {
    val max = f + variance
    val min = f - variance
    return min + rand.nextDouble() * (max - min)
}