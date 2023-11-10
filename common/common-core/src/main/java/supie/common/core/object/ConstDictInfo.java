package supie.common.core.object;

import lombok.Data;

import java.util.List;

/**
 * 常量字典的数据结构。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
public class ConstDictInfo {

    private List<ConstDictData> dictData;

    @Data
    public static class ConstDictData {
        private String type;
        private Object id;
        private String name;
    }
}
