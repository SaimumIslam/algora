package com.algora.app.feature.interviewprep.behavioral

internal val behavioralBank = BehavioralBank(
    id = "behavioral_question_bank",
    title = "Behavioral Question Bank",
    description = "Common behavioral prompts with what each one is really testing and how to frame a STAR answer.",
    questions = listOf(
        BehavioralQuestion(
            prompt = "Tell me about a time you disagreed with a teammate. How did you resolve it?",
            category = "Conflict",
            assesses = "Whether you can disagree on substance without making it personal, and reach a decision the team commits to.",
            starTip = "Situation: the technical decision at stake. Task: your stake in it. Action: how you surfaced data and heard them out. Result: the decision made and that the relationship held.",
        ),
        BehavioralQuestion(
            prompt = "Describe a project that failed or missed its goal. What did you learn?",
            category = "Failure",
            assesses = "Ownership over blame-shifting, and whether you extract a concrete, applied lesson.",
            starTip = "Pick a real miss you owned. Keep the setup short, be specific about your contribution to the failure, and end on the changed behavior — not a vague 'I learned to communicate more'.",
        ),
        BehavioralQuestion(
            prompt = "Tell me about a time you led without formal authority.",
            category = "Leadership",
            assesses = "Influence, initiative, and whether people follow you because of trust rather than a title.",
            starTip = "Show you saw a gap, rallied people around a plan, and drove it to a result. Quantify the impact and name what others contributed.",
        ),
        BehavioralQuestion(
            prompt = "Give an example of working under a tight deadline with shifting requirements.",
            category = "Ambiguity",
            assesses = "How you prioritize and stay effective when the target moves — scoping, not heroics.",
            starTip = "Emphasize how you cut scope to the essential, communicated trade-offs early, and still shipped something valuable on time.",
        ),
        BehavioralQuestion(
            prompt = "Describe a time you received difficult feedback. What did you do with it?",
            category = "Growth",
            assesses = "Coachability and self-awareness — do you act on feedback or defend against it?",
            starTip = "State the feedback plainly, resist justifying, then show the specific change you made and evidence it stuck.",
        ),
        BehavioralQuestion(
            prompt = "Tell me about your most significant technical contribution and its impact.",
            category = "Impact",
            assesses = "Scope of ownership and whether you measure your work by outcomes, not effort.",
            starTip = "Lead with the business or user result, then explain the technical decision that drove it. Numbers beat adjectives.",
        ),
        BehavioralQuestion(
            prompt = "Describe a time you had to convince stakeholders to change direction.",
            category = "Influence",
            assesses = "Communicating trade-offs to non-experts and building buy-in without authority.",
            starTip = "Frame their concern first, present the evidence in their terms (cost, risk, timeline), and show how you reached alignment.",
        ),
        BehavioralQuestion(
            prompt = "Tell me about a time you had to learn something unfamiliar quickly to deliver.",
            category = "Learning",
            assesses = "Learning velocity and how you de-risk the unknown under time pressure.",
            starTip = "Show your approach to ramping fast — narrowing to what you needed, leaning on the right sources — and that it produced a shipped result.",
        ),
    ),
)
