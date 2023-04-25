package eu.derzauberer.pis.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Command {
	
	private final String name;
	private String path;
	private String description;
	private final Map<String, String> branches = new HashMap<>();
	private int minArguments;
	private Consumer<String[]> action;
	private final Map<String, Command> commands;
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(Command.class);
	
	public Command(String name, String path) {
		this.name = name;
		this.path = path;
		this.commands = new HashMap<>();
		this.minArguments = 0;
		addBranch("-h", "Display help for every command branch");
	}
	
	public void executeCommand(String label, String args[]) {
		if (args.length > 0) {
			final Command command = commands.get(args[0]);
			if (command != null) {
				command.executeCommand(args[0], Arrays.copyOfRange(args, 1, args.length));
				return;
			} else if (Arrays.asList(args).contains("-h")) {
				printCommandHelp();
				return;
			} else if (action == null) {
				LOGGER.error("Command option {} does not exist!", args[0]);
				return;
			}
		}
		if (args.length < minArguments) {
			LOGGER.error("Not enough arguments for command {}!", label);
			return;
		}
		action.accept(args);
	}
	
	public void printCommandHelp() {
		final StringBuilder string = new StringBuilder("Help for: " + path);
		
		final List<Command> sortedCommands = commands.values()
				.stream()
				.sorted((command1, command2) -> command1.getName().compareToIgnoreCase(command2.getName()))
				.toList();
		final List<Entry<String, String>> sortedBranches = branches.entrySet()
				.stream()
				.sorted((entry1, entry2) -> entry1.getKey().compareToIgnoreCase(entry2.getKey()))
				.toList();
		for (Command entries : sortedCommands) {
			string.append("\n\n" + path + " " + entries.getName() + (entries.description != null ? "\n" + entries.getDescription() : ""));
		}
		for (Entry<String, String> entries : sortedBranches) {
			string.append("\n\n" + path + " " + entries.getKey() + (entries.getValue() != null ? "\n" + entries.getValue() : ""));
		}
		System.out.println(string.toString());
	}
	
	public void printList(Collection<String> list) {
		final List<String> sortedNames = list.stream().sorted().toList();
		final StringBuilder string = new StringBuilder();
		for (String name : sortedNames) {
			string.append(name + "\n");
		}
		if (string.length() != 0) string.deleteCharAt(string.length() - 1);
		System.out.println(string);
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
	
	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void addBranch(String flag, String description) {
		branches.put(flag, description);
	}
	
	public Map<String, String> getBranches() {
		return Collections.unmodifiableMap(branches);
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
	
}
