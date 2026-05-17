/**
 * Represents a pro subscription plan with team member slots and unlimited
 * prompts.
 * Manages available slots and enforces context limits for prompt execution.
 *
 * @author Sirjan Dulal
 * @version 1.0
 */
public class ProPlan extends AIModel {
    private int availableSlots;
    private final int maxSlots;

    /**
     * Constructs a pro plan with the given slot count and model configuration.
     *
     * @param modelName      the model name displayed to users
     * @param price          the price per 1L tokens
     * @param parameterCount the parameter count in billions
     * @param contextWindow  the maximum allowed context window size
     * @param availableSlots the initial team slots available
     * @throws IllegalArgumentException if any base parameters are invalid
     */
    public ProPlan(String modelName, double price, int parameterCount,
            int contextWindow, int availableSlots) {
        super(modelName, price, parameterCount, contextWindow);
        this.availableSlots = availableSlots;
        this.maxSlots = availableSlots;
    }

    /**
     * Returns the number of available team slots.
     *
     * @return the available team slot count
     */
    public int getAvailableSlots() {
        return availableSlots;
    }

    /**
     * Adds a team member if slots are available.
     *
     * @param teamMemberName the name of the team member to add
     * @return a message indicating success or an error
     */
    public String addTeamMember(String teamMemberName) {
        if (teamMemberName == null || teamMemberName.trim().isEmpty()) {
            return "Error: Team member name cannot be empty";
        }

        if (availableSlots <= 0) {
            return "Error: No available team slots";
        }

        availableSlots--;
        return "Team member '" + teamMemberName + "' added successfully. Slots remaining: " + availableSlots;
    }

    /**
     * Removes a team member and frees a slot when possible.
     *
     * @param teamMemberName the name of the team member to remove
     * @return a message indicating success or an error
     */
    public String removeTeamMember(String teamMemberName) {
        if (teamMemberName == null || teamMemberName.trim().isEmpty()) {
            return "Error: Team member name cannot be empty";
        }

        if (availableSlots >= maxSlots) {
            return "Error: All team slots are already available";
        }

        availableSlots++;
        return "Team member '" + teamMemberName + "' removed successfully. Slots available: " + availableSlots;
    }

    @Override
    /**
     * Executes a prompt request with unlimited quota enforcement.
     *
     * @param promptText   the user prompt text
     * @param outputTokens the requested output token count
     * @return a formatted result string describing the execution outcome
     */
    public String enterPrompt(String promptText, int outputTokens) {
        // Validate total tokens against the context window before responding.
        boolean isValid = calculateTokenUsage(promptText, outputTokens);

        if (!isValid) {
            return "Error: Context limit exceeded. Request too large for this model.";
        }

        int inputTokens = promptText.length() / 4;
        int systemTokens = 50;
        int totalTokens = inputTokens + outputTokens + systemTokens;

        return " Prompt executed successfully! (ProPlan - Unlimited) " +
                " Prompt: " + promptText + "\n" +
                "Total tokens used: " + totalTokens +
                " (Input: " + inputTokens + ", Output: " + outputTokens + ", System: " + systemTokens + ")\n" +
                "Note: ProPlan has unlimited monthly prompts";
    }

    @Override
    /**
     * Returns a formatted string describing the pro plan details.
     *
     * @return a display string for the plan
     */
    public String display() {
        return "Model Name: " + getModelName() +
                ", Price: NPR " + getPrice() + " per 1L tokens" +
                ", Parameters: " + getParameterCount() + "B" +
                ", Context Window: " + getContextWindow() + " tokens" +
                ", Available Team Slots: " + availableSlots;
    }
}