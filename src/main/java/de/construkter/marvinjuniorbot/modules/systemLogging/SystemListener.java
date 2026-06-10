package de.construkter.marvinjuniorbot.modules.systemLogging;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class SystemListener implements EventListener {

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        SystemLogManager logManager = new SystemLogManager();

        HashMap<String, String> args = new HashMap<>();
        if (event.getRawData() != null) {
            args.put("Raw", event.getRawData().toString());
        }

        logManager.log(event.getClass().getSimpleName(), "An event of type "
                + event.getClass().getSimpleName() + " was triggered.", args);
    }
}
