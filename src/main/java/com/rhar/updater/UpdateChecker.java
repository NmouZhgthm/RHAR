package com.rhar.updater;

import org.kohsuke.github.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.fusesource.jansi.Ansi;

import java.io.IOException;
import java.util.List;

public class UpdateChecker {
    private static final Logger logger = LoggerFactory.getLogger(UpdateChecker.class);
    private final String currentVersion;
    private final String githubRepo;

    public UpdateChecker(String currentVersion, String githubRepo) {
        this.currentVersion = currentVersion;
        this.githubRepo = githubRepo;
    }

    public void checkForUpdates() {
        System.out.println(Ansi.ansi().fgYellow().a("正在获取更新...").reset());
        
        try {
            GitHub github = GitHub.connectAnonymously();
            GHRepository repository = github.getRepository(githubRepo);
            List<GHRelease> releases = repository.listReleases().toList();
            
            if (releases.isEmpty()) {
                System.out.println(Ansi.ansi().fgGreen().a("已是最新版本").reset());
                return;
            }
            
            String latestVersion = releases.get(0).getTagName();
            if (isNewer(latestVersion, currentVersion)) {
                System.out.print(Ansi.ansi().a("发现版本 [" + latestVersion + "] 是否更新(y/n)? "));
                byte[] input = new byte[1024];
                System.in.read(input);
                String response = new String(input).trim().toLowerCase();
                
                if (response.equals("y")) {
                    performUpdate(releases.get(0));
                }
            } else {
                System.out.println(Ansi.ansi().fgGreen().a("已是最新版本").reset());
            }
            
        } catch (IOException e) {
            logger.error("检查更新时发生错误：", e);
            System.out.println(Ansi.ansi().fgRed().a("检查更新失败：" + e.getMessage()).reset());
        }
    }

    private void performUpdate(GHRelease release) {
        System.out.println("开始更新...");
        // TODO: 实现更新逻辑
    }
    
    private boolean isNewer(String latestVersion, String currentVersion) {
        String[] latest = latestVersion.replace("v", "").split("\\.");
        String[] current = currentVersion.split("\\.");
        
        for (int i = 0; i < Math.min(latest.length, current.length); i++) {
            int l = Integer.parseInt(latest[i]);
            int c = Integer.parseInt(current[i]);
            if (l > c) return true;
            if (l < c) return false;
        }
        
        return latest.length > current.length;
    }
} 