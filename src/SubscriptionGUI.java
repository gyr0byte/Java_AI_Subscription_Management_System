
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Provides the Swing-based GUI for managing AI subscription plans and actions.
 * Handles user input, validation, and file export/load operations.
 *
 * @author Sirjan Dulal
 * @version 1.0
 */
public class SubscriptionGUI extends JFrame implements ActionListener {

    ArrayList<AIModel> plans = new ArrayList<>();
    JTextField tfModelName, tfPrice, tfParams, tfContext; // shared for both plans
    JTextField tfPromptQuota; // PersonalPlan only
    JTextField tfSlots; // ProPlan only
    JTextField tfPromptText, tfResponseLen; // for enterPrompt
    JTextField tfTeamMember; // for add/remove team member
    JTextField tfIndex; // for index input

    JLabel lblPlanCount = new JLabel("Plans in system: 0");
    JComboBox<String> cbExportFiles = new JComboBox<>();

    JTextArea taOutput = new JTextArea(10, 40);

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter FILE_NAME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private static final String EXPORT_FILE_PREFIX = "subscription_plans_export_";
    private static final String EXPORT_FILE_SUFFIX = ".txt";

    JButton btnAddPersonal, btnAddPro;
    JButton btnDisplayAll, btnClear;
    JButton btnGivePrompt;
    JButton btnAddMember, btnRemoveMember;
    JButton btnCheckType, btnChangeType;
    JButton btnExportToFile, btnLoadFromFile;

    /**
     * Constructs the GUI frame and initializes all components.
     */
    public SubscriptionGUI() {
        setTitle("AI Subscription Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        buildUI();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Builds and lays out all GUI components.
     */
    private void buildUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        tfModelName = new JTextField(15);
        tfPrice = new JTextField(15);
        tfParams = new JTextField(15);
        tfContext = new JTextField(15);
        tfPromptQuota = new JTextField(15);
        tfSlots = new JTextField(15);
        tfPromptText = new JTextField(20);
        tfResponseLen = new JTextField(10);
        tfTeamMember = new JTextField(20);
        tfIndex = new JTextField(8);

        JPanel modelPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        modelPanel.setBorder(BorderFactory.createTitledBorder("Model Details"));
        modelPanel.add(new JLabel("Model Name:"));
        modelPanel.add(tfModelName);
        modelPanel.add(new JLabel("Price:"));
        modelPanel.add(tfPrice);
        modelPanel.add(new JLabel("Parameters (B):"));
        modelPanel.add(tfParams);
        modelPanel.add(new JLabel("Context Window:"));
        modelPanel.add(tfContext);

        JPanel personalPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        personalPanel.setBorder(BorderFactory.createTitledBorder("Personal Plan"));
        personalPanel.add(new JLabel("Prompt Quota:"));
        personalPanel.add(tfPromptQuota);

        JPanel proPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        proPanel.setBorder(BorderFactory.createTitledBorder("Pro Plan"));
        proPanel.add(new JLabel("Team Slots:"));
        proPanel.add(tfSlots);

        JPanel planPanel = new JPanel(new GridLayout(1, 2, 10, 5));
        planPanel.add(personalPanel);
        planPanel.add(proPanel);

        JPanel promptPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        promptPanel.setBorder(BorderFactory.createTitledBorder("Prompt Panel"));
        promptPanel.add(new JLabel("Index:"));
        promptPanel.add(tfIndex);
        promptPanel.add(new JLabel("Prompt Text:"));
        promptPanel.add(tfPromptText);
        promptPanel.add(new JLabel("Response Length:"));
        promptPanel.add(tfResponseLen);

        JPanel teamPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        teamPanel.setBorder(BorderFactory.createTitledBorder("Team Member Panel"));
        teamPanel.add(new JLabel("Team Member:"));
        teamPanel.add(tfTeamMember);

        btnAddPersonal = new JButton("Add Personal Plan");
        btnAddPro = new JButton("Add Pro Plan");
        btnDisplayAll = new JButton("Display All");
        btnClear = new JButton("Clear");
        btnGivePrompt = new JButton("Give Prompt");
        btnAddMember = new JButton("Add Team Member");
        btnRemoveMember = new JButton("Remove Team Member");
        btnCheckType = new JButton("Check Plan Type");
        btnChangeType = new JButton("Change Plan Type");
        btnExportToFile = new JButton("Export To File");
        btnLoadFromFile = new JButton("Load From File");

        btnAddPersonal.addActionListener(this);
        btnAddPro.addActionListener(this);
        btnDisplayAll.addActionListener(this);
        btnClear.addActionListener(this);
        btnGivePrompt.addActionListener(this);
        btnAddMember.addActionListener(this);
        btnRemoveMember.addActionListener(this);
        btnCheckType.addActionListener(this);
        btnChangeType.addActionListener(this);
        btnExportToFile.addActionListener(this);
        btnLoadFromFile.addActionListener(this);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 3, 8, 5));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
        buttonPanel.add(btnAddPersonal);
        buttonPanel.add(btnAddPro);
        buttonPanel.add(btnDisplayAll);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnGivePrompt);
        buttonPanel.add(btnAddMember);
        buttonPanel.add(btnRemoveMember);
        buttonPanel.add(btnCheckType);
        buttonPanel.add(btnChangeType);
        buttonPanel.add(btnExportToFile);
        buttonPanel.add(btnLoadFromFile);

        taOutput.setEditable(false);
        JScrollPane outputScrollPane = new JScrollPane(taOutput);
        outputScrollPane.setBorder(BorderFactory.createTitledBorder("Output"));

        mainPanel.add(modelPanel);
        mainPanel.add(planPanel);
        mainPanel.add(promptPanel);
        mainPanel.add(teamPanel);
        mainPanel.add(buttonPanel);
        mainPanel.add(lblPlanCount);

        setLayout(new BorderLayout(8, 8));
        add(mainPanel, BorderLayout.CENTER);
        add(outputScrollPane, BorderLayout.SOUTH);

        updatePlanCount();
        updateButtonStates();
        refreshExportFiles();
    }

    /**
     * Returns a formatted timestamp for output entries.
     *
     * @return the timestamp string
     */
    private String timestamp() {
        return "[" + LocalTime.now().format(TIME_FORMATTER) + "] ";
    }

    /**
     * Appends a message to the output area with timestamps on each line.
     *
     * @param message the message to append
     */
    private void appendOutput(String message) {
        for (String line : message.split("\\n")) {
            taOutput.append(timestamp() + line + "\n");
        }
    }

    /**
     * Displays a result message as a dialog and logs successful results.
     *
     * @param result the result message returned from a plan operation
     */
    private void displayResult(String result) {
        if (result == null || result.isBlank()) {
            return;
        }

        if (result.startsWith("Error:")) {
            JOptionPane.showMessageDialog(
                    this,
                    result,
                    "Operation Failed",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(
                this,
                result,
                "Operation Successful",
                JOptionPane.INFORMATION_MESSAGE);
        appendOutput(result);
    }

    /**
     * Updates the plan count label using the current list size.
     */
    private void updatePlanCount() {
        lblPlanCount.setText("Plans in system: " + plans.size());
    }

    /**
     * Enables or disables action buttons based on whether plans exist.
     */
    private void updateButtonStates() {
        boolean hasPlan = !plans.isEmpty();
        btnGivePrompt.setEnabled(hasPlan);
        btnAddMember.setEnabled(hasPlan);
        btnRemoveMember.setEnabled(hasPlan);
        btnCheckType.setEnabled(hasPlan);
        btnChangeType.setEnabled(hasPlan);
        btnDisplayAll.setEnabled(hasPlan);
    }

    /**
     * Resolves the export directory under src/exports, creating it if needed.
     *
     * @return the export directory path
     */
    private File getExportsDirectory() {
        File workingDir = new File(System.getProperty("user.dir"));

        if (workingDir.getName().equalsIgnoreCase("src")) {
            File exportsDir = new File(workingDir, "exports");
            exportsDir.mkdirs();
            return exportsDir;
        }

        File srcDir = new File(workingDir, "src");
        File baseDir = srcDir.exists() ? srcDir : workingDir;
        File exportsDir = new File(baseDir, "exports");
        exportsDir.mkdirs();
        return exportsDir;
    }

    /**
     * Refreshes the list of exported files for the load dialog.
     */
    private void refreshExportFiles() {
        File exportsDir = getExportsDirectory();
        File[] files = exportsDir
                .listFiles((dir, name) -> name.startsWith(EXPORT_FILE_PREFIX) && name.endsWith(EXPORT_FILE_SUFFIX));

        cbExportFiles.removeAllItems();

        if (files == null || files.length == 0) {
            cbExportFiles.addItem("No exports found");
            cbExportFiles.setEnabled(false);
            return;
        }

        Arrays.sort(files, Comparator.comparing(File::getName).reversed());

        for (File file : files) {
            cbExportFiles.addItem(file.getName());
        }

        cbExportFiles.setEnabled(true);
    }

    /**
     * Validates and returns the index entered by the user.
     *
     * @return the valid index or -1 if invalid
     * @throws NumberFormatException if index parsing fails
     */
    private int getDisplayNumber() {
        int index = -1;

        if (plans.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No plans available. Please add a plan first.",
                    "No Plans",
                    JOptionPane.ERROR_MESSAGE);
            return index;
        }

        try {
            int parsedIndex = Integer.parseInt(tfIndex.getText().trim());
            // Ensure the index is within the current list bounds.
            if (parsedIndex >= 0 && parsedIndex < plans.size()) {
                index = parsedIndex;
                return index;
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Error: Index out of range. Valid range is 0 to " + (plans.size() - 1),
                    "Invalid Index",
                    JOptionPane.ERROR_MESSAGE);
            return index;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error: Index must be an integer.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            return index;
        }
    }

    /**
     * Dispatches GUI button actions to the appropriate handlers.
     *
     * @param e the action event from a button click
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAddPersonal) {
            handleAddPersonal();
        } else if (e.getSource() == btnAddPro) {
            handleAddPro();
        } else if (e.getSource() == btnDisplayAll) {
            handleDisplayAll();
        } else if (e.getSource() == btnClear) {
            handleClear();
        } else if (e.getSource() == btnGivePrompt) {
            handleGivePrompt();
        } else if (e.getSource() == btnAddMember) {
            handleAddMember();
        } else if (e.getSource() == btnRemoveMember) {
            handleRemoveMember();
        } else if (e.getSource() == btnCheckType) {
            int index = getDisplayNumber();

            if (index != -1) {
                handleCheckType(index);
            }
        } else if (e.getSource() == btnChangeType) {
            handleChangeType();
        } else if (e.getSource() == btnExportToFile) {
            handleExportToFile();
        } else if (e.getSource() == btnLoadFromFile) {
            handleLoadFromFile();
        }
    }

    /**
     * Adds a personal plan using the current form inputs.
     *
     * @throws NumberFormatException    if numeric parsing fails
     * @throws IllegalArgumentException if model inputs are invalid
     */
    private void handleAddPersonal() {
        try {
            String modelName = tfModelName.getText().trim();
            double price = Double.parseDouble(tfPrice.getText().trim());
            int parameterCount = Integer.parseInt(tfParams.getText().trim());
            int contextWindow = Integer.parseInt(tfContext.getText().trim());
            int prompts = Integer.parseInt(tfPromptQuota.getText().trim());

            if (prompts <= 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Prompt quota must be a positive integer.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            PersonalPlan plan = new PersonalPlan(modelName, price, parameterCount, contextWindow, prompts);
            plans.add(plan);
            appendOutput("Personal plan added successfully at index " + (plans.size() - 1));
            updatePlanCount();
            updateButtonStates();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid numeric input. Please check price, parameters, context, and prompt quota.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Adds a pro plan using the current form inputs.
     *
     * @throws NumberFormatException    if numeric parsing fails
     * @throws IllegalArgumentException if model inputs are invalid
     */
    private void handleAddPro() {
        try {
            String modelName = tfModelName.getText().trim();
            double price = Double.parseDouble(tfPrice.getText().trim());
            int parameterCount = Integer.parseInt(tfParams.getText().trim());
            int contextWindow = Integer.parseInt(tfContext.getText().trim());
            int slots = Integer.parseInt(tfSlots.getText().trim());

            if (slots <= 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Team slots must be a positive integer.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            ProPlan plan = new ProPlan(modelName, price, parameterCount, contextWindow, slots);
            plans.add(plan);
            appendOutput("Pro plan added successfully at index " + (plans.size() - 1));
            updatePlanCount();
            updateButtonStates();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid numeric input. Please check price, parameters, context, and team slots.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Displays all plans with their index and details.
     */
    private void handleDisplayAll() {
        if (plans.isEmpty()) {
            appendOutput("No plans available.");
        } else {
            for (int i = 0; i < plans.size(); i++) {
                appendOutput(i + ": " + plans.get(i).display());
            }
        }
    }

    /**
     * Clears all input fields and output after user confirmation.
     */
    private void handleClear() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Clear all fields and output?",
                "Confirm Clear",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        tfModelName.setText("");
        tfPrice.setText("");
        tfParams.setText("");
        tfContext.setText("");
        tfPromptQuota.setText("");
        tfSlots.setText("");
        tfPromptText.setText("");
        tfResponseLen.setText("");
        tfTeamMember.setText("");
        tfIndex.setText("");
        taOutput.setText("");
    }

    /**
     * Executes a prompt for the selected plan at the specified index.
     *
     * @throws NumberFormatException if response length parsing fails
     */
    private void handleGivePrompt() {
        int index = getDisplayNumber();

        if (index != -1) {
            AIModel model = plans.get(index);

            try {
                String promptText = tfPromptText.getText().trim();

                if (promptText.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Prompt text cannot be empty.",
                            "Input Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int responseLength = Integer.parseInt(tfResponseLen.getText().trim());
                // Delegate prompt execution to the selected plan type.
                String result = model.enterPrompt(promptText, responseLength);
                displayResult(result);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Response length must be an integer.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Adds a team member to a pro plan at the specified index.
     */
    private void handleAddMember() {
        int index = getDisplayNumber();

        if (index != -1) {
            AIModel model = plans.get(index);

            // Only ProPlan supports team collaboration actions.
            if (model instanceof ProPlan proPlan) {
                String teamMemberName = tfTeamMember.getText().trim();

                if (teamMemberName.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Team member name cannot be empty.",
                            "Input Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String result = proPlan.addTeamMember(teamMemberName);
                displayResult(result);
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "This action is only available for Pro Plan.",
                        "Invalid Plan Type",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Removes a team member from a pro plan at the specified index.
     */
    private void handleRemoveMember() {
        int index = getDisplayNumber();

        if (index != -1) {
            AIModel model = plans.get(index);

            // Only ProPlan supports team collaboration actions.
            if (model instanceof ProPlan proPlan) {
                String teamMemberName = tfTeamMember.getText().trim();

                if (teamMemberName.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Team member name cannot be empty.",
                            "Input Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String result = proPlan.removeTeamMember(teamMemberName);
                displayResult(result);
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "This action is only available for Pro Plan.",
                        "Invalid Plan Type",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Reports the plan type stored at the specified index.
     *
     * @param index the index of the plan in the list
     */
    private void handleCheckType(int index) {
        AIModel model = plans.get(index);

        // Use instanceof checks to determine the plan type.
        if (model instanceof PersonalPlan) {
            appendOutput("This is a Personal Plan");
        } else if (model instanceof ProPlan) {
            appendOutput("This is a Pro Plan");
        } else {
            appendOutput("Unknown plan type");
        }
    }

    /**
     * Converts a plan between personal and pro types using provided inputs.
     *
     * @throws NumberFormatException    if quota or slot parsing fails
     * @throws IllegalArgumentException if model inputs are invalid
     */
    private void handleChangeType() {
        int index = getDisplayNumber();

        if (index != -1) {
            AIModel model = plans.get(index);

            try {
                // Convert based on current plan type using instanceof.
                if (model instanceof PersonalPlan) {
                    int slots = Integer.parseInt(tfSlots.getText().trim());

                    if (slots <= 0) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Team slots must be a positive integer to convert to Pro Plan.",
                                "Validation Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    ProPlan convertedPlan = new ProPlan(
                            model.getModelName(),
                            model.getPrice(),
                            model.getParameterCount(),
                            model.getContextWindow(),
                            slots);
                    plans.set(index, convertedPlan);
                    appendOutput("Plan at index " + index + " changed from Personal Plan to Pro Plan.");
                } else if (model instanceof ProPlan) {
                    int prompts = Integer.parseInt(tfPromptQuota.getText().trim());

                    if (prompts <= 0) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Prompt quota must be a positive integer to convert to Personal Plan.",
                                "Validation Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    PersonalPlan convertedPlan = new PersonalPlan(
                            model.getModelName(),
                            model.getPrice(),
                            model.getParameterCount(),
                            model.getContextWindow(),
                            prompts);
                    plans.set(index, convertedPlan);
                    appendOutput("Plan at index " + index + " changed from Pro Plan to Personal Plan.");
                } else {
                    appendOutput("Unknown plan type. Cannot change plan type.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Enter a valid integer in Team Slots or Prompt Quota based on conversion direction.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Exports all plans to a timestamped text file.
     *
     * @throws Exception if file writing fails
     */
    private void handleExportToFile() {
        String fileName = EXPORT_FILE_PREFIX + LocalDateTime.now().format(FILE_NAME_FORMATTER) + EXPORT_FILE_SUFFIX;
        File selectedFile = new File(getExportsDirectory(), fileName);

        // Write export content to the selected file.
        try (PrintWriter writer = new PrintWriter(selectedFile)) {
            writer.println("AI Subscription Plans Export");
            writer.println("Generated at: " + LocalTime.now().format(TIME_FORMATTER));
            writer.println("Total plans: " + plans.size());
            writer.println();

            for (int i = 0; i < plans.size(); i++) {
                AIModel model = plans.get(i);
                writer.println("Index: " + i);
                writer.println("Type: " + model.getClass().getSimpleName());
                writer.println("Model Name: " + model.getModelName());
                writer.println("Price: NPR " + model.getPrice() + " per 1L tokens");
                writer.println("Parameters: " + model.getParameterCount() + "B");
                writer.println("Context Window: " + model.getContextWindow() + " tokens");

                String typeName = model.getClass().getSimpleName();

                if ("PersonalPlan".equals(typeName)) {
                    writer.println("Monthly Quota Remaining: " + ((PersonalPlan) model).getPromptsRemaining());
                } else if ("ProPlan".equals(typeName)) {
                    writer.println("Available Team Slots: " + ((ProPlan) model).getAvailableSlots());
                }

                writer.println();
            }

            appendOutput("Exported plans to file: " + selectedFile.getAbsolutePath());
            refreshExportFiles();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not export file: " + ex.getMessage(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Loads a selected export file and shows its content in a separate window.
     *
     * @throws Exception if file reading fails
     */
    private void handleLoadFromFile() {
        refreshExportFiles();

        if (!cbExportFiles.isEnabled()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No export files available. Please export first.",
                    "File Not Found",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Build a small selection dialog for available exports.
        JPanel selectorPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        selectorPanel.add(new JLabel("Select an export file:"));
        selectorPanel.add(cbExportFiles);

        int choice = JOptionPane.showConfirmDialog(
                this,
                selectorPanel,
                "Load Exported Plans",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (choice != JOptionPane.OK_OPTION) {
            return;
        }

        Object selectedItem = cbExportFiles.getSelectedItem();

        if (selectedItem == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No export file selected.",
                    "File Not Found",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        File selectedFile = new File(getExportsDirectory(), selectedItem.toString());

        if (!selectedFile.exists()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selected export file not found. Please refresh by exporting again.",
                    "File Not Found",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        StringBuilder fileContent = new StringBuilder();

        // Read the selected export file into a viewer window.
        try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
            String line;

            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append(System.lineSeparator());
            }

            JFrame viewerFrame = new JFrame("Loaded File Content");
            JTextArea viewerArea = new JTextArea(fileContent.toString(), 20, 60);
            viewerArea.setEditable(false);
            viewerFrame.add(new JScrollPane(viewerArea));
            viewerFrame.pack();
            viewerFrame.setLocationRelativeTo(this);
            viewerFrame.setVisible(true);

            appendOutput("Loaded file: " + selectedFile.getAbsolutePath());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not load file: " + ex.getMessage(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Launches the application on the Swing event dispatch thread.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SubscriptionGUI::new);
    }
}
