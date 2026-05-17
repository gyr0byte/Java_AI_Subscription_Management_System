/**
 * Represents a personal subscription plan with a monthly prompt quota.
 * Tracks remaining prompts and enforces quota and context limits.
 *
 * @author Sirjan Dulal
 * @version 1.0
 */
public class PersonalPlan extends AIModel {
    private int promptsRemaining;

    /**
     * Constructs a personal plan with the given quota and model configuration.
     *
     * @param modelName        the model name displayed to users
     * @param price            the price per 1L tokens
     * @param parameterCount   the parameter count in billions
     * @param contextWindow    the maximum allowed context window size
     * @param promptsRemaining the initial monthly prompt quota
     * @throws IllegalArgumentException if any base parameters are invalid
     */
    public PersonalPlan(String modelName, double price, int parameterCount,
            int contextWindow, int promptsRemaining) {
        super(modelName, price, parameterCount, contextWindow);
        this.promptsRemaining = promptsRemaining;
    }

    /**
     * Returns the number of prompts remaining in the monthly quota.
     *
     * @return the remaining prompt count
     */
    public int getPromptsRemaining() {
        return promptsRemaining;
    }

    /**
     * Adds additional prompts to the monthly quota.
     *
     * @param amount the number of prompts to add
     * @return a message indicating success or an error
     */
    public String buyPrompts(int amount) {
        if (amount <= 0) {
            return "Error: Please enter a positive value or upgrade to Pro Plan.";
        }
        this.promptsRemaining += amount;
        return "Successfully purchased " + amount + " prompts. New total: " + promptsRemaining;
    }

    @Override
    /**
     * Executes a prompt request and decrements the remaining quota.
     *
     * @param promptText   the user prompt text
     * @param outputTokens the requested output token count
     * @return a formatted result string describing the execution outcome
     */
    public String enterPrompt(String promptText, int outputTokens) {
        if (promptsRemaining <= 0) {
            return "Error: Monthly quota exhausted. Please purchase more prompts.";
        }
        // Validate total tokens against the context window before charging quota.
        boolean isValid = calculateTokenUsage(promptText, outputTokens);

        if (!isValid) {
            return "Error: Context limit exceeded. Request too large for this model.";
        }

        promptsRemaining--;

        int inputTokens = promptText.length() / 4;
        int systemTokens = 50;
        int totalTokens = inputTokens + outputTokens + systemTokens;

        return " Prompt executed successfully! " +
                "Prompt: " + promptText + "\n" +
                "Total tokens used: " + totalTokens +
                " (Input: " + inputTokens + ", Output: " + outputTokens + ", System: " + systemTokens + ")\n" +
                "Monthly prompts remaining: " + promptsRemaining;
    }

    @Override
    /**
     * Returns a formatted string describing the personal plan details.
     *
     * @return a display string for the plan
     */
    public String display() {
        return "Model Name: " + getModelName() +
                ", Price: NPR " + getPrice() + " per 1L tokens" +
                ", Parameters: " + getParameterCount() + "B" +
                ", Context Window: " + getContextWindow() + " tokens" +
                ", Monthly Quota Remaining: " + promptsRemaining;
    }
}