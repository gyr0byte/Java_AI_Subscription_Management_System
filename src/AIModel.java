/**
 * Represents a base AI model plan with shared configuration and behavior.
 * Subclasses provide plan-specific prompt handling and display formatting.
 *
 * @author Sirjan Dulal
 * @version 1.0
 */
public abstract class AIModel {
    private String modelName;
    private double price;
    private int parameterCount;
    private int contextWindow;

    /**
     * Constructs an AI model plan with required configuration fields.
     *
     * @param modelName      the model name displayed to users
     * @param price          the price per 1L tokens
     * @param parameterCount the parameter count in billions
     * @param contextWindow  the maximum allowed context window size
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public AIModel(String modelName, double price, int parameterCount, int contextWindow) {
        if (modelName == null || modelName.trim().isEmpty()) {
            throw new IllegalArgumentException("Model name cannot be empty");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        if (parameterCount <= 0) {
            throw new IllegalArgumentException("Parameter count must be positive");
        }
        if (contextWindow <= 0) {
            throw new IllegalArgumentException("Context window must be positive");
        }

        this.modelName = modelName;
        this.price = price;
        this.parameterCount = parameterCount;
        this.contextWindow = contextWindow;
    }

    /**
     * Returns the model name.
     *
     * @return the model name
     */
    public String getModelName() {
        return modelName;
    }

    /**
     * Executes a prompt request and returns a formatted result message.
     * Implementations should validate limits and update plan-specific state.
     *
     * @param promptText   the user prompt text
     * @param outputTokens the requested output token count
     * @return a formatted result string describing the execution outcome
     */
    public abstract String enterPrompt(String promptText, int outputTokens);

    /**
     * Returns the price per 1L tokens.
     *
     * @return the price per 1L tokens
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns the parameter count in billions.
     *
     * @return the parameter count in billions
     */
    public int getParameterCount() {
        return parameterCount;
    }

    /**
     * Returns the context window size.
     *
     * @return the context window size
     */
    public int getContextWindow() {
        return contextWindow;
    }

    /**
     * Calculates whether the total token usage fits within the context window.
     *
     * @param promptText   the user prompt text
     * @param outputTokens the requested output token count
     * @return true if the total token usage fits within the context window
     */
    public boolean calculateTokenUsage(String promptText, int outputTokens) {
        int inputTokens = promptText.length() / 4;
        // Include fixed system tokens when validating total usage.
        int systemTokens = 50;

        int totalTokens = inputTokens + outputTokens + systemTokens;

        return totalTokens <= contextWindow;
    }

    /**
     * Returns a formatted string describing the model plan details.
     *
     * @return a display string for the plan
     */
    public abstract String display();
}