package com.algora.app.feature.reinforcementlearning

import com.algora.app.core.data.model.Category
import com.algora.app.core.data.model.Difficulty
import com.algora.app.core.data.model.Topic

private fun topic(
    id: String,
    name: String,
    category: Category,
    tagline: String,
    isPremium: Boolean = false,
    iconName: String = category.iconName,
    accentColor: Long = category.accentColor,
    difficulty: Difficulty? = null,
) = Topic(
    id = id,
    name = name,
    categoryId = category.id,
    tagline = tagline,
    description = tagline,
    iconName = iconName,
    accentColor = accentColor,
    isPremium = isPremium,
    difficulty = difficulty,
)

private val foundations = ReinforcementLearningCategories.foundations
private val dqn = ReinforcementLearningCategories.dqn
private val policyGradient = ReinforcementLearningCategories.policyGradient
private val continuousControl = ReinforcementLearningCategories.continuousControl
private val modelBased = ReinforcementLearningCategories.modelBased
private val exploration = ReinforcementLearningCategories.exploration
private val marl = ReinforcementLearningCategories.marl
private val advanced = ReinforcementLearningCategories.advanced
private val benchmarks = ReinforcementLearningCategories.benchmarks

private val foundationsTopics = listOf(
    topic("mdp", "MDP", foundations, "States, actions, rewards and transitions — the RL contract.", difficulty = Difficulty.BEGINNER),
    topic("q_learning", "Q-Learning (off-policy)", foundations, "Learn action values from experience without a model.", difficulty = Difficulty.INTERMEDIATE),
)

private val dqnTopics = listOf(
    topic("dqn", "DQN", dqn, "A neural network approximates the Q-function.", isPremium = true),
    topic("experience_replay", "Experience Replay", dqn, "Reuse past transitions from a buffer to break correlation.", isPremium = true),
    topic("target_networks", "Target Networks", dqn, "A frozen copy stabilizes the bootstrapped target.", isPremium = true),
    topic("double_dqn", "Double DQN", dqn, "Decouple action selection from evaluation to cut overestimation.", isPremium = true),
    topic("dueling_dqn", "Dueling DQN", dqn, "Split value and advantage streams in the network head.", isPremium = true),
    topic("prioritized_replay", "Prioritized Experience Replay", dqn, "Sample high-error transitions more often.", isPremium = true),
    topic("noisy_nets", "Noisy Nets", dqn, "Learnable parameter noise drives exploration.", isPremium = true),
    topic("c51", "Distributional RL (C51)", dqn, "Model the full return distribution, not just its mean.", isPremium = true),
    topic("rainbow_dqn", "Rainbow DQN", dqn, "Combine every DQN improvement into one agent.", isPremium = true),
)

private val policyGradientTopics = listOf(
    topic("reinforce", "REINFORCE", policyGradient, "The vanilla Monte-Carlo policy gradient.", isPremium = true),
    topic("actor_critic", "Actor-Critic", policyGradient, "A critic estimates value to reduce gradient variance.", isPremium = true),
    topic("a2c", "A2C", policyGradient, "Synchronous advantage actor-critic.", isPremium = true),
    topic("a3c", "A3C", policyGradient, "Asynchronous actors update a shared network.", isPremium = true),
    topic("gae", "GAE", policyGradient, "Generalized advantage estimation trades bias for variance.", isPremium = true),
    topic("trpo", "TRPO", policyGradient, "Constrain each policy update to a trust region.", isPremium = true),
    topic("ppo", "PPO", policyGradient, "Clipped surrogate objective — the workhorse of modern RL.", isPremium = true),
)

private val continuousControlTopics = listOf(
    topic("dpg", "DPG", continuousControl, "Deterministic policy gradient for continuous actions.", isPremium = true),
    topic("ddpg", "DDPG", continuousControl, "Deep deterministic actor-critic off-policy control.", isPremium = true),
    topic("td3", "TD3", continuousControl, "Twin critics and delayed updates tame DDPG.", isPremium = true),
    topic("sac", "SAC", continuousControl, "Maximum-entropy off-policy actor-critic.", isPremium = true),
    topic("max_entropy_rl", "Maximum Entropy RL", continuousControl, "Reward high-entropy policies for robust exploration.", isPremium = true),
)

private val modelBasedTopics = listOf(
    topic("dyna_q", "Dyna-Q", modelBased, "Blend real and model-simulated experience.", isPremium = true),
    topic("mcts", "MCTS", modelBased, "Monte Carlo Tree Search plans by sampling rollouts.", isPremium = true),
    topic("alphago", "AlphaGo", modelBased, "Policy/value nets plus MCTS beat a Go champion.", isPremium = true),
    topic("alphazero", "AlphaZero", modelBased, "Self-play from scratch across board games.", isPremium = true),
    topic("muzero", "MuZero", modelBased, "Plan with a learned model of the environment.", isPremium = true),
    topic("world_models", "World Models", modelBased, "Learn a latent dynamics model and dream inside it.", isPremium = true),
    topic("dreamer", "Dreamer (V1–V3)", modelBased, "Learn behaviors purely in a latent imagination.", isPremium = true),
    topic("mbpo", "MBPO", modelBased, "Model-based policy optimization with short rollouts.", isPremium = true),
)

private val explorationTopics = listOf(
    topic("epsilon_greedy", "Epsilon-Greedy", exploration, "Act randomly a fraction ε of the time.", isPremium = true),
    topic("ucb", "UCB", exploration, "Upper confidence bound — optimism under uncertainty.", isPremium = true),
    topic("thompson_sampling", "Thompson Sampling", exploration, "Sample from the posterior to balance explore/exploit.", isPremium = true),
    topic("boltzmann_exploration", "Boltzmann Exploration", exploration, "Softmax over action values sets exploration temperature.", isPremium = true),
    topic("intrinsic_motivation", "Intrinsic Motivation", exploration, "Reward novelty when extrinsic reward is sparse.", isPremium = true),
    topic("icm", "Curiosity-Driven (ICM)", exploration, "Prediction error of a learned model becomes reward.", isPremium = true),
    topic("rnd", "Random Network Distillation", exploration, "Novelty from error against a fixed random network.", isPremium = true),
)

private val marlTopics = listOf(
    topic("minimax", "Minimax", marl, "Optimal play in zero-sum two-player games.", isPremium = true),
    topic("iql", "Independent Q-Learning", marl, "Each agent learns while treating others as environment.", isPremium = true),
    topic("vdn", "VDN", marl, "Value decomposition sums per-agent utilities.", isPremium = true),
    topic("qmix", "QMIX", marl, "Monotonic mixing of per-agent values for cooperation.", isPremium = true),
    topic("maddpg", "MADDPG", marl, "Centralized critics, decentralized actors.", isPremium = true),
    topic("self_play", "Self-Play", marl, "Agents improve by competing against copies of themselves.", isPremium = true),
)

private val advancedTopics = listOf(
    topic("imitation_learning", "Imitation Learning", advanced, "Learn a policy directly from expert demonstrations.", isPremium = true),
    topic("irl", "IRL", advanced, "Recover the reward function behind observed behavior.", isPremium = true),
    topic("gail", "GAIL", advanced, "Adversarial imitation without recovering the reward.", isPremium = true),
    topic("offline_rl", "Offline RL", advanced, "Learn from a fixed dataset with no new interaction.", isPremium = true),
    topic("cql", "CQL", advanced, "Conservative Q-learning for offline stability.", isPremium = true),
    topic("decision_transformer", "Decision Transformer", advanced, "Cast RL as return-conditioned sequence modeling.", isPremium = true),
    topic("meta_rl", "Meta-RL (MAML)", advanced, "Learn to adapt quickly to new tasks.", isPremium = true),
    topic("rlhf", "RLHF", advanced, "Reinforcement learning from human feedback aligns LLMs.", isPremium = true),
)

private val benchmarksTopics = listOf(
    topic("grid_world", "Grid World", benchmarks, "The canonical toy MDP for teaching RL.", isPremium = true),
    topic("cartpole", "CartPole", benchmarks, "Balance a pole — RL's 'hello world'.", isPremium = true),
    topic("mountain_car", "Mountain Car", benchmarks, "Build momentum to escape a valley.", isPremium = true),
    topic("atari", "Atari 2600", benchmarks, "Pixel-input games that launched deep RL.", isPremium = true),
    topic("mujoco", "MuJoCo", benchmarks, "Continuous-control physics locomotion tasks.", isPremium = true),
    topic("starcraft", "StarCraft II", benchmarks, "Long-horizon, partially-observed strategy.", isPremium = true),
    topic("dota2", "Dota 2", benchmarks, "Five-agent cooperation at professional level.", isPremium = true),
)

object ReinforcementLearningTopics {
    val topics: List<Topic> =
        foundationsTopics + dqnTopics + policyGradientTopics + continuousControlTopics +
            modelBasedTopics + explorationTopics + marlTopics + advancedTopics + benchmarksTopics

    fun find(topicId: String): Topic? = topics.find { it.id == topicId }
}
