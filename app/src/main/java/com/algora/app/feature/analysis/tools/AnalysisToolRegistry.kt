package com.algora.app.feature.analysis.tools

import androidx.compose.runtime.Composable

// Analysis "tools" (features.md §3.3) are live dashboards, not prose topics — they bypass
// TopicContent/TopicDetailScreen's 7-section template entirely and render their own layout.
// Topics not listed here fall through to the standard "coming soon" placeholder.
object AnalysisToolRegistry {
    private val tools: Map<String, @Composable () -> Unit> = mapOf(
        "operation_counter" to { OperationCounterTool() },
        "growth_curve_chart" to { GrowthCurveChartTool() },
    )

    fun get(topicId: String): (@Composable () -> Unit)? = tools[topicId]
}
