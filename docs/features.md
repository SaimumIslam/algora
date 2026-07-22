# Algora — Features & Content Specification

Extracted from reference app demo (`docs/demo/`) — a mobile app titled "Data Structure and Algorithms" with an "AI Simulation" module. This doc captures the topic taxonomy and the feature set to build for our own app.

---

## 1. App Structure

Two major modes, each with its own bottom navigation:

**DSA Mode** — bottom nav: `Interview Prep (?)` · `Data Structures` · `Home` · `Algorithms` · `Analysis`

**AI Mode** — bottom nav: `Machine Learning (robot)` · `Deep Learning (chip)` · `Home` · `NLP (globe)` · `Reinforcement Learning (controller)`

Home screen is a dashboard linking into: Data Structures, Algorithms, Interview Prep, Analysis (DSA side) and ML, DL, NLP, RL (AI side).

---

## 2. Topic Taxonomy (content to build)

### 2.1 Data Structures
- **Core**: Array, String, Singly Linked List, Doubly Linked List, Stack, Queue, Hash Table/Hash Map
- **Non-Linear**: Tree, Binary Search Tree (BST), Heap (Min/Max), Trie (Prefix Tree), Graph
- **Advanced**: Segment Tree, Fenwick Tree (BIT), Disjoint Set (Union-Find), AVL/Red-Black Tree, B-Tree/B+ Tree, Suffix Tree/Suffix Array, Skip List
- **Specialized/Real-World**: Bloom Filter, LRU Cache, K-D Tree/Quad Tree/Octree, Graph Variants
- **Abstract Data Types**: List, Set, Map, Priority Queue

### 2.2 Algorithms
1. **Sorting** (implied — Merge Sort, Quick Sort, etc.)
2. **Searching**: Linear Search, Binary Search, Jump Search, Interpolation Search, Exponential Search
3. **Recursion & Backtracking**: Factorial, Fibonacci, N-Queens, Sudoku Solver, Subset Sum, Permutation Generation
4. **Divide and Conquer**: Merge Sort, Quick Sort, Binary Search, Closest Pair of Points, Strassen's Matrix Multiplication, Karatsuba's Algorithm
5. **Greedy Algorithms**: Fractional Knapsack, Huffman Coding, Kruskal's MST, Prim's MST, Dijkstra's Shortest Path, Job Sequencing with Deadlines
6. **Dynamic Programming**: Longest Common Subsequence (LCS), Knapsack, etc.
7. **Graph Algorithms**: BFS, DFS, shortest path family (Dijkstra, Bellman-Ford, Floyd-Warshall), MST (Kruskal's, Prim's), Union-Find, Tarjan's/Kosaraju's (SCC)
8. **Pathfinding**: A*, D* Algorithm
9–11. **Miscellaneous & Advanced Techniques**: Top-K (Heap/Quickselect), Sliding Window, Two Pointer, Prefix Sum/Difference Array, Kadane's Algorithm, Reservoir Sampling, Monte Carlo/Randomized Algorithms, Mo's Algorithm

### 2.3 Algorithm Analysis
- **Fundamentals**: Operation Counter, Cost Profiler, Parameterized Input Generator
- **Growth Visualization**: Complexity Playground, Logarithmic & Exponential Simulators, Input Size Scaling Simulator
- **Case Analysis**: Best vs Worst, Input-Dependent Demo, Adaptive Algorithm Behavior, Recursion Tree Analyzer
- **Empirical Analysis**: Benchmark Dashboard, Real vs Predicted Chart, Hardware Effects (Cache & CPU)
- **Advanced**: Amortized Analysis, Dynamic Array Resizing, Parallel Algorithm Scaling, Probabilistic Analysis, Heuristic Algorithms Runtime
- **Exploration & Games**: Complexity Map, Complexity Proof Explorer, Sandbox

### 2.4 Machine Learning
- Linear Regression (and presumably Logistic Regression, Decision Trees, SVM, k-NN, Naive Bayes, clustering, etc. as siblings)
- The Perceptron (first artificial neuron)

### 2.5 Deep Learning
Implied from perceptron entry point — neural network fundamentals (backprop, activation functions, CNNs, etc.)

### 2.6 NLP
- Stemming (Porter Stemmer)
- Implied siblings: Tokenization, Lemmatization, Bag-of-Words/TF-IDF, Word Embeddings, RNN/LSTM, Attention, Transformers, LLMs

### 2.7 Reinforcement Learning (fully mapped — most detailed category)
- **Foundations**: MDP, Q-Learning (off-policy)
- **Deep Q-Networks (DQN Family)**: DQN, Experience Replay, Target Networks, Double DQN, Dueling DQN, Prioritized Experience Replay, Noisy Nets, Distributional RL (C51), Rainbow DQN
- **Policy Gradient Methods**: REINFORCE, Actor-Critic, A2C, A3C, GAE, TRPO, PPO
- **Continuous Control (Robotics)**: DPG, DDPG, TD3, SAC, Maximum Entropy RL
- **Model-Based & Planning**: Dyna-Q, MCTS, AlphaGo, AlphaZero, MuZero, World Models, Dreamer (V1-V3), MBPO
- **Exploration Strategies**: Epsilon-Greedy, UCB, Thompson Sampling, Boltzmann Exploration, Intrinsic Motivation, Curiosity-Driven (ICM), Random Network Distillation
- **Multi-Agent Systems (MARL)**: Minimax, Independent Q-Learning, VDN, QMIX, MADDPG, Self-Play
- **Advanced Frontiers**: Imitation Learning, IRL, GAIL, Offline RL, CQL, Decision Transformer, Meta-RL (MAML), RLHF
- **Famous Benchmarks**: Grid World, CartPole, Mountain Car, Atari 2600, MuJoCo, StarCraft II, Dota 2

---

## 3. Core Feature Set

### 3.1 Topic Browser
- Category list screens grouped into labeled sections (icon + title), each with a colored icon badge
- Row items show a radio/checkbox (progress marker), title, and either a chevron (unlocked/free) or a lock icon (premium)
- Freemium gating: a subset of topics per category are free; the rest require unlock/subscription
- Per-category search bar (e.g., "Search RL topics...")
- Numbered category sections for large taxonomies (e.g., "11. Miscellaneous & Advanced")

### 3.2 Topic Detail Page (the core learning unit — every topic follows this template)
1. **Hero card** — icon, topic name, tagline, "What is X?" plain-language explanation with a relatable analogy
2. **"How Does It Work?"** — numbered, color-coded step cards (3–5 steps) breaking the concept into a sequence
3. **"The Mathematics"** — formatted equations/formulas with a notation key glossary (e.g., `y = mx + c`, `J = (1/n)Σ(y-ŷ)²`)
4. **"Technical Deep Dive"** — collapsible/scrollable sections with syntax-highlighted code blocks (implementation, optimized variant, complexity notes)
5. **Interactive Simulation / Lab** — the differentiating feature, varies by topic type:
   - Slider-driven parameter exploration (e.g., drag `m`/`c` and watch a live regression line redraw)
   - Canvas-based data structure visualizers with operation buttons (Insert Head/Tail/At, Delete, Search, Reset) and live pointer/node diagrams
   - Graph simulators with add-node/link-edge controls and animated BFS/DFS traversal (state legend: Idle/Current/Visited/Queue/Active)
   - Recursion tree / call-stack visualizers with playback transport (reset, step-back, play/pause, step-forward, speed slider)
   - DP tabulation grid that animates cell-by-cell fill + traceback highlighting (Comparing/Match/Mismatch/Traceback/Answer legend)
   - Classifier "gate solver" (AND/OR/XOR) with weight/bias sliders and pass/fail detection
   - Generate-new-dataset / randomize controls to re-run a simulation against fresh input
6. **"Real-World Applications"** — 3–5 cards (icon + short description) grounding the concept in industry use cases
7. **"Key Takeaways"** — closing summary box (green, checkmarked bullet list) recapping the essential facts

### 3.3 Algorithm Analysis Tools
- Operation counter tied to live code execution
- Cost/runtime profiler
- Parameterized input generator (vary n, distribution, etc.)
- Growth-curve plotting (log/linear/exponential overlays)
- Best-case vs worst-case side-by-side comparison
- Benchmark dashboard with real vs predicted performance charts
- Hardware-aware notes (cache effects, CPU behavior)
- Amortized analysis walkthroughs (e.g., dynamic array resizing)
- Sandbox mode for free-form experimentation

### 3.4 Progress & Monetization
- Per-topic completion checkbox/radio tracked in list views
- Per-category progress bar (e.g., "Algorithm Analysis — 0%")
- Lock icons on premium content — freemium upsell model
- Consistent visual language: color-coded category icons, badge-style step numbers

---

## 4. Recommended Additions (gaps vs. reference app worth building in ours)

- **Spaced-repetition review mode** — resurface completed topics on a schedule to fight forgetting
- **Quiz / flashcard mode per topic** — auto-generated from the "Key Takeaways" so users self-test
- **Code playground** — let users edit the shown implementation and run it against custom input (not just watch a canned simulation)
- **Interview Prep module** — timed mock questions, company-tagged problem sets, pattern tagging (sliding window, two pointer, etc.) that cross-links back to the relevant Algorithms topic pages
- **Bookmarks / "Continue where you left off"**
- **Difficulty tagging & prerequisite graph** — show "learn X before Y" links between topics (e.g., BFS/DFS before Dijkstra)
- **Cross-linking between DSA and AI sections** — e.g., link Graph traversal to MCTS, or Gradient Descent (ML) to backprop (DL)
- **Dark mode**
- **Offline caching of topic content** for use without connectivity
- **Shareable progress / streaks** for engagement retention
- **Multi-language code snippets** (toggle Python/Java/JS/C++ in the Technical Deep Dive)
