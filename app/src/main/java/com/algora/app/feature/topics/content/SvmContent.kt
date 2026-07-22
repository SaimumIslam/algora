package com.algora.app.feature.topics.content

import com.algora.app.core.data.model.ApplicationCard
import com.algora.app.core.data.model.CodeBlock
import com.algora.app.core.data.model.CrossLink
import com.algora.app.core.data.model.FormulaEntry
import com.algora.app.core.data.model.NotationEntry
import com.algora.app.core.data.model.SimulationType
import com.algora.app.core.data.model.StepCard
import com.algora.app.core.data.model.TopicContent

internal val svmContent = TopicContent(
    topicId = "svm",
    whatIsIt = listOf(
        "A support vector machine (SVM) classifies by finding the hyperplane that separates classes with the widest possible margin — the biggest gap to the nearest points of each class.",
        "The kernel trick lets it draw non-linear boundaries by implicitly mapping data into a higher-dimensional space where a linear separator exists.",
    ),
    steps = listOf(
        StepCard(1, "Maximize the Margin", "Find the separating hyperplane farthest from the closest points of both classes.", 0xFF818CF8),
        StepCard(2, "Support Vectors", "Only the borderline points (the support vectors) determine the boundary; the rest are irrelevant.", 0xFF60A5FA),
        StepCard(3, "Soft Margin", "Allow a few misclassifications, controlled by C, to handle overlapping classes.", 0xFF10B981),
        StepCard(4, "Kernel Trick", "Replace dot products with a kernel (RBF, polynomial) to bend the boundary non-linearly.", 0xFFF59E0B),
    ),
    formulas = listOf(
        FormulaEntry("Decision", "f(x) = sign(w·x + b)", "Side of the hyperplane."),
        FormulaEntry("Margin objective", "min ½‖w‖² s.t. yᵢ(w·xᵢ+b) ≥ 1", "Widest margin, correct side."),
        FormulaEntry("RBF kernel", "K(x, x′) = exp(−γ‖x−x′‖²)", "Similarity in an implicit high-dim space."),
    ),
    notationKey = listOf(
        NotationEntry("w, b", "hyperplane weights and offset"),
        NotationEntry("C", "soft-margin penalty for misclassifications"),
        NotationEntry("γ", "RBF kernel width"),
    ),
    codeBlocks = listOf(
        CodeBlock(
            title = "SVM with an RBF kernel (scikit-learn)",
            accentColor = 0xFF6366F1,
            code = """
                from sklearn.svm import SVC

                clf = SVC(kernel="rbf", C=1.0, gamma="scale")
                clf.fit(X_train, y_train)

                preds = clf.predict(X_test)
                print(clf.support_vectors_.shape)   # the decisive borderline points
            """.trimIndent(),
        ),
    ),
    simulation = SimulationType.ClassifierPlayground,
    applications = listOf(
        ApplicationCard("image", 0xFF818CF8, "Image Classification", "Strong on small-to-medium, high-dimensional datasets before deep nets took over."),
        ApplicationCard("flask", 0xFF60A5FA, "Bioinformatics", "Gene-expression and protein classification, where features vastly outnumber samples."),
        ApplicationCard("search", 0xFF10B981, "Text Categorization", "Sparse high-dimensional bag-of-words features suit linear SVMs well."),
    ),
    takeaways = listOf(
        "SVMs maximize the margin between classes; only support vectors shape the boundary.",
        "The soft-margin C trades training accuracy against generalization.",
        "The kernel trick yields non-linear boundaries without explicit high-dim features.",
        "They excel in high dimensions but scale poorly to very large datasets.",
    ),
    crossLinks = listOf(
        CrossLink("logistic_regression", "Logistic Regression"),
        CrossLink("perceptron", "Perceptron"),
    ),
)
