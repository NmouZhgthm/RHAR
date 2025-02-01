package com.rhar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import com.rhar.updater.UpdateChecker;

/**
 * RHAR 应用程序主入口
 */
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    private static final String CURRENT_VERSION = "1.0.0"; // 当前版本号
    private static final String GITHUB_REPO = "你的GitHub用户名/仓库名"; // 替换为实际的GitHub仓库

    // ASCII 艺术标语
    private static final String BANNER = 
        "\n" +
        "----------------------------------------\n" +
        "----------------RHAR-------------------\n" +
        "----------------------------------------\n";

    public static void main(String[] args) {
        // 初始化ANSI支持
        AnsiConsole.systemInstall();
        
        try {
            logger.info("RHAR 应用程序启动成功！");
            
            // 清理控制台
            System.out.print("\033[H\033[2J");
            System.out.flush();
            
            // 显示标语
            System.out.println(BANNER);
            
            UpdateChecker updateChecker = new UpdateChecker(CURRENT_VERSION, GITHUB_REPO);
            updateChecker.checkForUpdates();
            
            // 创建简单的终端提示符
            while (true) {
                System.out.print("RHAR> ");
                try {
                    byte[] input = new byte[1024];
                    System.in.read(input);
                    
                    String command = new String(input).trim();
                    if ("exit".equalsIgnoreCase(command)) {
                        System.out.println("感谢使用 RHAR，再见！");
                        break;
                    }
                    
                } catch (Exception e) {
                    logger.error("处理输入时发生错误：", e);
                }
            }
        } finally {
            AnsiConsole.systemUninstall();
        }
    }
} 