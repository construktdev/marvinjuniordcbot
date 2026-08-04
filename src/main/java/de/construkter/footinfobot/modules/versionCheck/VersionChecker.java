package de.construkter.footinfobot.modules.versionCheck;

import de.construkter.footinfobot.Main;

public class VersionChecker {

    private static final String remoteVersion = VersionFetcher.getRecentVersion();

    public static boolean isUpToDate() {
        String localVersion = Main.VERSION;
        return localVersion.equals(remoteVersion);
    }
}
