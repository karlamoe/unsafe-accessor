package moe.karla.test;

import com.google.auto.service.AutoService;
import org.junit.platform.launcher.LauncherDiscoveryListener;

@AutoService(LauncherDiscoveryListener.class)
public class CustomDiscoverListener implements LauncherDiscoveryListener {
}
