package de.construkter.marvinjuniorbot.modules.activityShift;

import de.construkter.marvinjuniorbot.Main;
import de.construkter.marvinjuniorbot.logging.LogManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;

import java.util.Timer;
import java.util.TimerTask;

public class ActivityShift {
    public static void start() {
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                shift();
            }
        }, 1000 * 30);
    }

    private static void shift() {
        JDA jda = Main.jda;
        if (jda == null) return;

        String[] activities = {"EA SPORTS FC 26", "FORZA SGD", "Ave Dynamo", "Verbandsstrafen abschaffen!", "Forza Dynamo", "Scheiß FCH", "Scheiß FCM", "Scheiß Schacht"};

        int index = (int) Math.floor(Math.random() * activities.length) - 1;

        jda.getPresence().setActivity(Activity.playing(activities[index]));
    }
}
