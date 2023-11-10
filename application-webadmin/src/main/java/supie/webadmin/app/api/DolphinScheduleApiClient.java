package supie.webadmin.app.api;

import com.dtflys.forest.annotation.Address;
import com.dtflys.forest.annotation.Post;

/**
 * 数据接口-任务调度-任务调度接口。
 */
@Address(host = "${baseUrl}", port = "{port}")
public interface DolphinScheduleApiClient {

    @Post("/dolphinscheduler/login")
    String login(String username, String password);
    
}
