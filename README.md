# AI Subscription Manager (College Project)

A small Java Swing application that models AI subscription plans and lets users manage them through a GUI. This is a college project focused on object-oriented design, validation, and basic file export/load workflows rather than a production-ready system.

## Overview

The app simulates two types of AI model subscription plans:

- **Personal Plan**: has a monthly prompt quota and supports buying extra prompts.
- **Pro Plan**: has team member slots and unlimited prompts.

Users can add plans, execute prompts with token validation, change plan types, and export plan data to text files.

## Features

- Add Personal and Pro plans with validation
- Execute prompts and calculate token usage
- Track remaining prompt quota for Personal plans
- Add/remove team members for Pro plans
- Change plan types (Personal <-> Pro)
- Export all plans to a timestamped text file
- Load and view exported files inside the GUI

## Project Structure

- `src/AIModel.java`: abstract base class with shared model data and token validation
- `src/PersonalPlan.java`: personal plan logic and quota tracking
- `src/ProPlan.java`: pro plan logic and team slot tracking
- `src/SubscriptionGUI.java`: Swing GUI and all user interactions
- `src/exports/`: export directory for generated plan files

## How It Works

- **Token validation** uses a simple formula: input tokens are estimated as `prompt length / 4`, with a fixed 50 system tokens added.
- **Personal plans** decrement the monthly quota on every successful prompt.
- **Pro plans** allow unlimited prompts and manage a limited number of team slots.

## How To Run

### Option A: BlueJ (recommended for this coursework)

1. Open the project in BlueJ.
2. Compile all classes.
3. Run `SubscriptionGUI`.

### Option B: Command line (JDK required)

From the project root:

```bash
javac -d out src/*.java
java -cp out SubscriptionGUI
```

## Usage Notes

- Use the **Index** field to select a plan for prompt execution or plan type checks.
- Exported files are stored under `src/exports/` and listed in the load dialog.
- The GUI shows messages in both dialogs and the output log area.

## Limitations

- Token counting is a simple approximation for learning purposes.
- No persistence between runs unless you export and manually view files.
- No database or networking; everything runs locally in memory.

## Author

- Sirjan Dulal

## License

This project is for educational use. No explicit license is provided.
