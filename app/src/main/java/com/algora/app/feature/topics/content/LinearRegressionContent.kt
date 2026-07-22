package com.algora.app.feature.topics.content

import com.algora.app.core.data.model.ApplicationCard
import com.algora.app.core.data.model.CodeBlock
import com.algora.app.core.data.model.CodeVariant
import com.algora.app.core.data.model.CrossLink
import com.algora.app.core.data.model.FormulaEntry
import com.algora.app.core.data.model.NotationEntry
import com.algora.app.core.data.model.SimulationType
import com.algora.app.core.data.model.StepCard
import com.algora.app.core.data.model.TopicContent

// Content ported verbatim from docs/design/Algora.dc.html's `regression` topic (one of the mock's
// three fully-specified content references).
internal val linearRegressionContent = TopicContent(
    topicId = "linear_regression",
    whatIsIt = listOf(
        "Linear regression finds the straight line that best describes the relationship between an input x and an output y.",
        "It answers “given x, what is the most likely y?” by minimizing the gap between the line and every data point.",
    ),
    steps = listOf(
        StepCard(1, "The Model", "Assume y ≈ m·x + c. Learning means finding the best slope m and intercept c.", 0xFF818CF8),
        StepCard(2, "The Cost", "Measure error with Mean Squared Error — the average squared distance from points to the line.", 0xFF60A5FA),
        StepCard(3, "Gradient Descent", "Nudge m and c downhill on the cost surface until the error stops shrinking.", 0xFF10B981),
    ),
    formulas = listOf(
        FormulaEntry("Hypothesis", "y = m·x + c", "A line defined by slope m and intercept c."),
        FormulaEntry("Cost (MSE)", "J = (1/n)Σ(y−ŷ)²", "Average squared error over all n points."),
        FormulaEntry("Gradient Step", "θ := θ − α ∂J/∂θ", "α is the learning rate."),
    ),
    notationKey = listOf(
        NotationEntry("m", "slope of the fitted line"),
        NotationEntry("c", "y-intercept of the fitted line"),
        NotationEntry("ŷ", "the model's prediction m·x + c"),
        NotationEntry("α", "learning rate — the gradient-descent step size"),
    ),
    codeBlocks = listOf(
        CodeBlock(
            title = "Closed-form & Gradient Descent (Python)",
            accentColor = 0xFF6366F1,
            code = """
                import numpy as np

                # Closed-form (Normal Equation)
                m, c = np.polyfit(x, y, 1)

                # Gradient descent
                m, c, lr = 0.0, 0.0, 0.01
                for _ in range(1000):
                    yhat = m*x + c
                    err  = yhat - y
                    m -= lr * (2/n) * (err*x).sum()
                    c -= lr * (2/n) * err.sum()
            """.trimIndent(),
            variants = listOf(
                CodeVariant(
                    "Python",
                    """
                        m, c, lr = 0.0, 0.0, 0.01
                        for _ in range(1000):
                            err = (m*x + c) - y
                            m -= lr * (2/n) * (err*x).sum()
                            c -= lr * (2/n) * err.sum()
                    """.trimIndent(),
                ),
                CodeVariant(
                    "Java",
                    """
                        double m = 0, c = 0, lr = 0.01;
                        for (int step = 0; step < 1000; step++) {
                            double dm = 0, dc = 0;
                            for (int i = 0; i < n; i++) {
                                double err = (m*x[i] + c) - y[i];
                                dm += err * x[i];
                                dc += err;
                            }
                            m -= lr * (2.0/n) * dm;
                            c -= lr * (2.0/n) * dc;
                        }
                    """.trimIndent(),
                ),
                CodeVariant(
                    "JavaScript",
                    """
                        let m = 0, c = 0, lr = 0.01;
                        for (let step = 0; step < 1000; step++) {
                            let dm = 0, dc = 0;
                            for (let i = 0; i < n; i++) {
                                const err = (m*x[i] + c) - y[i];
                                dm += err * x[i];
                                dc += err;
                            }
                            m -= lr * (2/n) * dm;
                            c -= lr * (2/n) * dc;
                        }
                    """.trimIndent(),
                ),
            ),
        ),
    ),
    simulation = SimulationType.RegressionExplorer,
    applications = listOf(
        ApplicationCard("finance", 0xFF10B981, "Price Forecasting", "Predicting house prices from size, location and age."),
        ApplicationCard("trend", 0xFF3B82F6, "Trend Analysis", "Estimating sales growth or user retention over time."),
        ApplicationCard("chip", 0xFFA78BFA, "Risk Scoring", "Baseline models for credit and insurance decisions."),
    ),
    takeaways = listOf(
        "Simple, fast and highly interpretable.",
        "Assumes a roughly linear relationship.",
        "MSE punishes large errors and outliers heavily.",
        "The foundation for logistic regression and neural nets.",
    ),
    crossLinks = listOf(
        CrossLink("perceptron", "The Perceptron"),
    ),
)
