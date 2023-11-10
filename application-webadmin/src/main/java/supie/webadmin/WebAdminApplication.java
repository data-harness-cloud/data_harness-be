package supie.webadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 应用服务启动类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@EnableAsync
@SpringBootApplication
@ComponentScan("supie")
public class WebAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebAdminApplication.class, args);
	}
}
