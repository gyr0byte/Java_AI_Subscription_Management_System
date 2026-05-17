# AI Subscription Manager (College Project)

A Java Swing application that models AI subscription plans and lets users manage them through a GUI. This is a college project focused on object-oriented design, validation, and basic file export/load workflows rather than a production-ready system.

## Quick Facts

- Built with Java and Swing
- In-memory plan management with export to text files
- Simple token estimation for prompt validation
- Designed for coursework and OOP practice

## Overview

The app simulates two types of AI model subscription plans and provides a GUI to add, manage, and test them.

### Plan Types

| Plan          | Prompt Usage  | Extras            |
| ------------- | ------------- | ----------------- |
| Personal Plan | Monthly quota | Buy extra prompts |
| Pro Plan      | Unlimited     | Team member slots |

## Key Features

- Add Personal and Pro plans with validation
- Execute prompts and calculate token usage
- Track remaining prompt quota for Personal plans
- Add/remove team members for Pro plans
- Change plan types (Personal <-> Pro)
- Export all plans to a timestamped text file
- Load and view exported files inside the GUI

## How It Works

- Token validation uses a simple estimate: input tokens are calculated as `prompt length / 4`, plus a fixed 50 system tokens.
- Personal plans decrement the monthly quota on each successful prompt.
- Pro plans allow unlimited prompts and manage a limited number of team slots.

## Project Structure

- `src/AIModel.java`: abstract base class with shared model data and token validation
- `src/PersonalPlan.java`: personal plan logic and quota tracking
- `src/ProPlan.java`: pro plan logic and team slot tracking
- `src/SubscriptionGUI.java`: Swing GUI and all user interactions
- `src/exports/`: export directory for generated plan files

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

## Usage Walkthrough

1. Enter model details (name, price, parameters, context window).
2. Add either a Personal plan (prompt quota) or a Pro plan (team slots).
3. Use the Index field to select a plan for prompt execution or plan type checks.
4. Export plans to text files stored under `src/exports/` and load them from the GUI.

## Limitations

- Token counting is a simple approximation for learning purposes.
- No persistence between runs unless you export and manually view files.
- No database or networking; everything runs locally in memory.

## License

This project is for educational use. No explicit license is provided.
