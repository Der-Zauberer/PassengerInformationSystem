package eu.derzauberer.pis.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

public class Command {
	
	private final String name;
	private String description;
	private String usage;
	private final Map<String, String> flags = new HashMap<>();
	private int minArguments;
	private Consumer<String[]> action;
	private final Map<String, Command> commands;
	
	private static final ArrayList<Consumer<String>> outputObserver = new ArrayList<>();
	
	public Command(String name) {
		this.name = name;
		this.commands = new HashMap<>();
		this.minArguments = 0;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getUsage() {
		return usage;
	}
	
	public void addFlag(String flag, String description) {
		flags.put(flag, description);
	}
	
	public Map<String, String> getFlags() {
		return Collections.unmodifiableMap(flags);
	}
	
	public void setUsage(String usage) {
		this.usage = usage;
	}
	
	public int getMinArguments() {
		return minArguments;
	}
	
	public void setMinArguments(int minArguments) {
		this.minArguments = minArguments;
	}
	
	public Consumer<String[]> getAction() {
		return action;
	}
	
	public void setAction(Consumer<String[]> action) {
		this.action = action;
	}
	
	public void registerSubCommand(Command command) {
		commands.put(command.getName(), command);
	}
	
	public void executeCommand(String label, String args[]) {
		if (args.length > 0) {
			final Command command = commands.get(args[0]);
			if (command != null) {
				command.executeCommand(args[0], Arrays.copyOfRange(args, 1, args.length));
				return;
			} else if (Arrays.asList(args).contains("-h")) {
				final StringBuilder string = new StringBuilder("Command " + getName() + ": ");
				if (description != null) string.append(description);
				if (usage != null) string.append("\n" + usage);
				for (Command entries : commands.values()) {
					string.append("\n" + entries.getName() + (entries.description != null ? "\t\t" + entries.getDescription() : ""));
				}
				for (Entry<String, String> entries : flags.entrySet()) {
					string.append("\n" + entries.getKey() + (entries.getValue() != null ? "\t\t" + entries.getValue() : ""));
				}
				consoleOut(string.toString());
				return;
			} else if (action == null) {
				consoleOut("Error: Command option " + args[0] + " does not exist!");
				return;
			}
		}
		if (args.length < minArguments) {
			consoleOut("Error: Not enough arguments for command " + label + "!");
			return;
		}
		action.accept(args);
	}
	
	public void executeCommand(String input) {
		if (input.isBlank()) return;
		final ArrayList<String> strings = new ArrayList<>();
		final StringBuilder builder = new StringBuilder();
		char lastCharacter = ' ';
		boolean enclosed = false;
		for (char character : input.toCharArray()) {
			if (character == ' ' && !enclosed) {
				if (builder.length() != 0) {
					strings.add(builder.toString().replace("\\\"", "\""));
					builder.setLength(0);
				}
			} else if (character == '"' && lastCharacter != '\\') {
				if (!enclosed) {
					enclosed = true;
				} else {
					strings.add(builder.toString().replace("\\\"", "\""));
					builder.setLength(0);
					enclosed = false;
				}
			} else {
				builder.append(character);
			}
			lastCharacter = character;
		}
		if (builder.length() != 0) strings.add(builder.toString().replace("\\\"", "\""));
		final String[] args = new String[strings.size() - 1];
		for (int i = 1; i < args.length; i++) {
			args[i] = strings.get(i);
		}
		executeCommand(strings.get(0), args);
	}
	
	public static ArrayList<Consumer<String>> getOutputObserver() {
		return outputObserver;
	}
	
	public static void consoleOut(String message) {
		outputObserver.forEach(observer -> observer.accept(message));
		System.out.println(message);
	}

}
