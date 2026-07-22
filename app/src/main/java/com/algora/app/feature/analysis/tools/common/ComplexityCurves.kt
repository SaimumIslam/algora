package com.algora.app.feature.analysis.tools.common

import androidx.compose.ui.graphics.Color
import kotlin.math.ln

// A labeled, colored function of n — the shared unit consumed by the growth/complexity tools and
// MiniLineChart. Colors follow the app's simulation palette (blue/green/amber/violet/red).
class Curve(val label: String, val color: Color, val fn: (Double) -> Double)

// The five complexity classes shown throughout the app.
val complexityCurves = listOf(
    Curve("O(1)", Color(0xFF3B82F6)) { 1.0 },
    Curve("O(log n)", Color(0xFF10B981)) { n -> ln(n + 1) },
    Curve("O(n)", Color(0xFFF59E0B)) { n -> n },
    Curve("O(n log n)", Color(0xFF8B5CF6)) { n -> n * ln(n + 1) },
    Curve("O(n²)", Color(0xFFEF4444)) { n -> n * n },
)
