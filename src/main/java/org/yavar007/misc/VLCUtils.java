package org.yavar007.misc;

import java.io.File;

public class VLCUtils {

    public static boolean isVLCInstalled() {
        String vlcPath = getVLCPath();
        return !vlcPath.isEmpty() && new File(vlcPath).exists();
    }

    public static String getVLCPath() {
        // Windows default VLC install path
        String vlcWindowsPath = "C:\\Program Files\\VideoLAN\\VLC";
        // Linux default VLC install path (you can customize this)
        String vlcLinuxPath = "/usr/lib/vlc";
        // macOS VLC install path
        String vlcMacPath = "/Applications/VLC.app/Contents/MacOS";

        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("win")) {
            return vlcWindowsPath;
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("linux")) {
            return vlcLinuxPath;
        } else if (osName.contains("mac")) {
            return vlcMacPath;
        }

        return ""; // VLC path not found
    }
}
