package fit.hutech.LePhuocToan_3296.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.net.URI;

@Component
public class BrowserLauncher {

    @Value("${app.browser.auto-launch:false}")
    private boolean autoLaunch;

    @Value("${app.browser.url:http://localhost:8080}")
    private String url;

    @EventListener(ApplicationReadyEvent.class)
    public void launchBrowser() {
        if (!autoLaunch) {
            return;
        }

        try {
            // Kiểm tra xem Desktop có được hỗ trợ không
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                System.out.println("Dang mo trinh duyet tai: " + url);
                Desktop.getDesktop().browse(new URI(url));
                System.out.println("Trinh duyet da duoc mo thanh cong!");
            } else {
                System.out.println("Desktop khong ho tro mo trinh duyet tu dong");
                openBrowserManually();
            }
        } catch (Exception e) {
            System.err.println("Khong the mo trinh duyet tu dong: " + e.getMessage());
            System.out.println("Vui long mo trinh duyet thu cong tai: " + url);
        }
    }

    private void openBrowserManually() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            Runtime runtime = Runtime.getRuntime();

            if (os.contains("win")) {
                // Windows
                runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else if (os.contains("mac")) {
                // MacOS
                runtime.exec("open " + url);
            } else if (os.contains("nix") || os.contains("nux")) {
                // Linux/Unix
                runtime.exec("xdg-open " + url);
            }
            System.out.println("Trinh duyet da duoc mo bang lenh he thong!");
        } catch (Exception e) {
            System.err.println("Khong the mo trinh duyet: " + e.getMessage());
            System.out.println("Vui long mo trinh duyet thu cong tai: " + url);
        }
    }
}
