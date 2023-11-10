package supie.webadmin.app.service.databasemanagement.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import supie.common.core.validator.UpdateGroup;

import javax.validation.constraints.NotNull;

@ApiModel("databaseManagement对象")
@Data
public class DatabaseManagement {


     @ApiModelProperty(value = "数据库类型", required = true)
     @NotNull(message = "数据验证失败，数据库类型不能为空！", groups = {UpdateGroup.class})
     String databaseType;

     @ApiModelProperty(value = "ip", required = true)
     @NotNull(message = "数据验证失败，ip不能为空！", groups = {UpdateGroup.class})
     String ip;

     @ApiModelProperty(value = "端口", required = true)
     @NotNull(message = "数据验证失败，端口不能为空！", groups = {UpdateGroup.class})
     String port;

     @ApiModelProperty(value = "数据名(服务名-架构名)", required = true)
     @NotNull(message = "数据验证失败，数据名(服务名-架构名)不能为空！", groups = {UpdateGroup.class})
     String databaseName;

     @ApiModelProperty(value = "用户名", required = true)
     @NotNull(message = "数据验证失败，用户名不能为空！", groups = {UpdateGroup.class})
     String username;

     @ApiModelProperty(value = "密码", required = true)
     @NotNull(message = "数据验证失败，密码不能为空！", groups = {UpdateGroup.class})
     String password;


     @ApiModelProperty(value = "表名", required = true)
     String tableName;
}
