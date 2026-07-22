package com.algora.app.feature.topics.content

import com.algora.app.core.data.model.ApplicationCard
import com.algora.app.core.data.model.CodeBlock
import com.algora.app.core.data.model.CrossLink
import com.algora.app.core.data.model.FormulaEntry
import com.algora.app.core.data.model.NotationEntry
import com.algora.app.core.data.model.SimulationType
import com.algora.app.core.data.model.StepCard
import com.algora.app.core.data.model.TopicContent

internal val logisticRegressionContent = TopicContent(
    topicId = "logistic_regression",
    whatIsIt = listOf(
        "Logistic regression predicts the probability that an input belongs to a class by passing a linear score through the sigmoid function, squashing it into the 0–1 range.",
        "Despite the name it's a classifier: it draws a linear decision boundary and outputs calibrated probabilities rather than a raw line.",
    ),
    steps = listOf(
        StepCard(1, "Linear Score", "Compute z = w·x + b, a weighted sum of the features.", 0xFF818CF8),
        StepCard(2, "Sigmoid Squash", "Map z to a probability with σ(z) = 1 / (1 + e^(−z)).", 0xFF60A5FA),
        StepCard(3, "Cross-Entropy Loss", "Penalize confident wrong predictions with log loss, not squared error.", 0xFF10B981),
        StepCard(4, "Gradient Descent", "Nudge the weights downhill on the loss until the boundary settles.", 0xFFF59E0B),
    ),
    formulas = listOf(
        FormulaEntry("Hypothesis", "ŷ = σ(w·x + b)", "Probability of the positive class."),
        FormulaEntry("Sigmoid", "σ(z) = 1 / (1 + e^(−z))", "Maps any real number to (0, 1)."),
        FormulaEntry("Log loss", "J = −Σ[y·ln ŷ + (1−y)·ln(1−ŷ)]", "Cross-entropy between labels and predictions."),
    ),
    notationKey = listOf(
        NotationEntry("w, b", "weight vector and bias"),
        NotationEntry("σ", "the sigmoid (logistic) function"),
        NotationEntry("ŷ", "predicted probability of class 1"),
    ),
    codeBlocks = listOf(
        CodeBlock(
            title = "Logistic regression (scikit-learn)",
            accentColor = 0xFF6366F1,
            code = """
                from sklearn.linear_model import LogisticRegression

                clf = LogisticRegression()
                clf.fit(X_train, y_train)

                probs = clf.predict_proba(X_test)[:, 1]   # P(class = 1)
                preds = clf.predict(X_test)               # thresholded at 0.5
            """.trimIndent(),
        ),
    ),
    simulation = SimulationType.NotYetAvailable,
    applications = listOf(
        ApplicationCard("finance", 0xFF818CF8, "Credit & Risk Scoring", "Estimating default probability from applicant features is a textbook logistic-regression task."),
        ApplicationCard("flask", 0xFF60A5FA, "Medical Diagnosis", "Predicting disease presence from measurements, with interpretable odds ratios."),
        ApplicationCard("search", 0xFF10B981, "Spam & Click Prediction", "Binary yes/no scoring at scale — spam filters and click-through models."),
    ),
    takeaways = listOf(
        "Logistic regression is linear classification via the sigmoid, outputting probabilities.",
        "It optimizes cross-entropy (log loss), not mean squared error.",
        "Its weights are interpretable as log-odds contributions of each feature.",
        "The decision boundary is linear; use kernels or trees when classes aren't linearly separable.",
    ),
    crossLinks = listOf(
        CrossLink("linear_regression", "Linear Regression"),
        CrossLink("svm", "Support Vector Machines"),
    ),
)
