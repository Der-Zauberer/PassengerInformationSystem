package eu.derzauberer.pis.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Command {
	
	private final String name;
	private final Map<String, Command> commands;
	private Consumer<String[]> action;
	
	public Command(String name) {
		this.name = name;
		this.commands = new HashMap<>();
	}
	
	public String getName() {
		return name;
	}
	
	public void registerSubCommand(Command command) {
		commands.put(command.getName(), command);
	}
	
	public Consumer<String[]> getAction() {
		return action;
	}
	
	public void setAction(Consumer<String[]> action) {
		this.action = action;
	}
	
	public void executeCommand(String label, String args[]) {
		final Command command = commands.get(label);
		if (command != null && args.length != 0) {
			command.executeCommand(args[0], Arrays.copyOfRange(args, 1, args.length));
		} else {
			action.accept(args);
		}
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

}
