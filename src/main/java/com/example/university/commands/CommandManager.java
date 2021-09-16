package com.example.university.commands;

import com.example.university.commands.faculty.*;
import com.example.university.commands.profile.*;
import com.example.university.commands.subject.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that manages all commands.
 */
public final class CommandManager {

    private static final Logger LOG = LogManager.getLogger(CommandManager.class);
    private static final Map<String, Command> COMMANDS = new HashMap<>();

    static {
        // common commands
        COMMANDS.put("noCommand", new NoCommand());
        COMMANDS.put("login", new Login());
        COMMANDS.put("logout", new Logout());
        COMMANDS.put("viewProfile", new ViewProfile());
        COMMANDS.put("editProfile", new EditProfile());
        COMMANDS.put("viewFaculty", new ViewFaculty());
        COMMANDS.put("viewAllFaculties", new ViewAllFaculties());
        COMMANDS.put("setSessionLanguage", new SetSessionLanguage());
        // user commands
        COMMANDS.put("userRegistration", new UserRegistration());
        COMMANDS.put("applyFaculty", new ApplyFacultyView());
        // admin commands
        COMMANDS.put("adminRegistration", new AdminRegistration());
        COMMANDS.put("editFaculty", new EditFaculty());
        COMMANDS.put("addFaculty", new AddFaculty());
        COMMANDS.put("deleteFaculty", new DeleteFaculty());
        COMMANDS.put("addSubject", new AddSubject());
        COMMANDS.put("editSubject", new EditSubject());
        COMMANDS.put("viewAllSubjects", new ViewAllSubjects());
        COMMANDS.put("viewSubject", new ViewSubject());
        COMMANDS.put("viewApplicant", new ViewApplicant());
        COMMANDS.put("createReport", new CreateFacultyReport());
        COMMANDS.put("deleteSubject", new DeleteSubject());

        LOG.debug("Command container was successfully initialized");
        LOG.trace("Total number of commands equals to {}", COMMANDS.size());
    }

    /**
     * Returns command object which execution will give path to the resource.
     *
     * @param commandName Name of the command.
     * @return Command object if container contains such command, otherwise
     * specific noCommand object will be returned.
     */
    public static Command get(String commandName) {
        if (commandName == null || !COMMANDS.containsKey(commandName)) {
            LOG.trace("Command not found with name = {}", commandName);
            return COMMANDS.get("noCommand");
        }
        return COMMANDS.get(commandName);
    }

    public static Map<String, Command> getAllCommands() {
        return COMMANDS;
    }

    private CommandManager() {
    }
}