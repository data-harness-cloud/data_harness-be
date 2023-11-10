package supie.webadmin.app.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.common.core.base.service.BaseService;
import supie.common.core.constant.GlobalDeletedFlag;
import supie.common.core.object.CallResult;
import supie.common.core.object.MyRelationParam;
import supie.common.core.util.MyModelUtil;
import supie.common.sequence.wrapper.IdGeneratorWrapper;
import supie.webadmin.app.dao.SeatunnelConfigMapper;
import supie.webadmin.app.dto.SeatunnelEnvDto;
import supie.webadmin.app.dto.SeatunnelSinkDto;
import supie.webadmin.app.dto.SeatunnelSourceDto;
import supie.webadmin.app.model.ProjectMain;
import supie.webadmin.app.model.RemoteHost;
import supie.webadmin.app.model.SeatunnelConfig;
import supie.webadmin.app.service.ProjectMainService;
import supie.webadmin.app.service.RemoteHostService;
import supie.webadmin.app.service.SeatunnelConfigService;
import supie.webadmin.upms.service.SysDeptService;
import supie.webadmin.upms.service.SysUserService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 项目中心-数据传输引擎配置数据操作服务类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
@Service("seatunnelConfigService")
public class SeatunnelConfigServiceImpl extends BaseService<SeatunnelConfig, Long> implements SeatunnelConfigService {

    @Autowired
    private SeatunnelConfigMapper seatunnelConfigMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private ProjectMainService projectMainService;
    @Autowired
    private RemoteHostService remoteHostService;
    @Autowired
    private IdGeneratorWrapper idGenerator;

    /**
     * 返回当前Service的主表Mapper对象。
     *
     * @return 主表Mapper对象。
     */
    @Override
    protected BaseDaoMapper<SeatunnelConfig> mapper() {
        return seatunnelConfigMapper;
    }

    /**
     * 保存新增对象。
     *
     * @param seatunnelConfig 新增对象。
     * @return 返回新增对象。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SeatunnelConfig saveNew(SeatunnelConfig seatunnelConfig) {
        seatunnelConfigMapper.insert(this.buildDefaultValue(seatunnelConfig));
        return seatunnelConfig;
    }

    /**
     * 利用数据库的insertList语法，批量插入对象列表。
     *
     * @param seatunnelConfigList 新增对象列表。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveNewBatch(List<SeatunnelConfig> seatunnelConfigList) {
        if (CollUtil.isNotEmpty(seatunnelConfigList)) {
            seatunnelConfigList.forEach(this::buildDefaultValue);
            seatunnelConfigMapper.insertList(seatunnelConfigList);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SeatunnelConfig saveNewWithRelation(SeatunnelConfig seatunnelConfig, JSONObject relationData) {
        this.saveNew(seatunnelConfig);
        this.saveOrUpdateOneToOneRelationData(seatunnelConfig, relationData);
        return seatunnelConfig;
    }

    /**
     * 更新数据对象。
     *
     * @param seatunnelConfig         更新的对象。
     * @param originalSeatunnelConfig 原有数据对象。
     * @return 成功返回true，否则false。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(SeatunnelConfig seatunnelConfig, SeatunnelConfig originalSeatunnelConfig) {
        MyModelUtil.fillCommonsForUpdate(seatunnelConfig, originalSeatunnelConfig);
        // 这里重点提示，在执行主表数据更新之前，如果有哪些字段不支持修改操作，请用原有数据对象字段替换当前数据字段。
        UpdateWrapper<SeatunnelConfig> uw = this.createUpdateQueryForNullValue(seatunnelConfig, seatunnelConfig.getId());
        return seatunnelConfigMapper.update(seatunnelConfig, uw) == 1;
    }

    private void saveOrUpdateOneToOneRelationData(SeatunnelConfig seatunnelConfig, JSONObject relationData) {
        // 对于一对一新增或更新，如果主键值为空就新增，否则就更新，同时更新updateTime和updateUserId。
        RemoteHost remoteHost = relationData.getObject("remoteHost", RemoteHost.class);
        if (remoteHost != null) {
            remoteHost.setId(seatunnelConfig.getRemoteHostId());
            remoteHostService.saveNewOrUpdate(remoteHost,
                    remoteHostService::saveNew, remoteHostService::update);
        }
    }

    /**
     * 删除指定数据。
     *
     * @param id 主键Id。
     * @return 成功返回true，否则false。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean remove(Long id) {
        return seatunnelConfigMapper.deleteById(id) == 1;
    }

    /**
     * 当前服务的支持表为从表，根据主表的关联Id，删除一对多的从表数据。
     *
     * @param projectId 从表关联字段。
     * @return 删除数量。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int removeByProjectId(Long projectId) {
        SeatunnelConfig deletedObject = new SeatunnelConfig();
        deletedObject.setProjectId(projectId);
        return seatunnelConfigMapper.delete(new QueryWrapper<>(deletedObject));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateBatchByProjectId(Long projectId, List<SeatunnelConfig> dataList) {
        this.updateBatchOneToManyRelation("projectId", projectId,
                null, null, dataList, this::saveNewBatch);
    }

    /**
     * 获取单表查询结果。由于没有关联数据查询，因此在仅仅获取单表数据的场景下，效率更高。
     * 如果需要同时获取关联数据，请移步(getSeatunnelConfigListWithRelation)方法。
     *
     * @param filter  过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<SeatunnelConfig> getSeatunnelConfigList(SeatunnelConfig filter, String orderBy) {
        return seatunnelConfigMapper.getSeatunnelConfigList(filter, orderBy);
    }

    /**
     * 获取主表的查询结果，以及主表关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     * 该查询会涉及到一对一从表的关联过滤，或一对多从表的嵌套关联过滤，因此性能不如单表过滤。
     * 如果仅仅需要获取主表数据，请移步(getSeatunnelConfigList)，以便获取更好的查询性能。
     *
     * @param filter 主表过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<SeatunnelConfig> getSeatunnelConfigListWithRelation(SeatunnelConfig filter, String orderBy) {
        List<SeatunnelConfig> resultList = seatunnelConfigMapper.getSeatunnelConfigList(filter, orderBy);
        // 在缺省生成的代码中，如果查询结果resultList不是Page对象，说明没有分页，那么就很可能是数据导出接口调用了当前方法。
        // 为了避免一次性的大量数据关联，规避因此而造成的系统运行性能冲击，这里手动进行了分批次读取，开发者可按需修改该值。
        int batchSize = resultList instanceof Page ? 0 : 1000;
        this.buildRelationForDataList(resultList, MyRelationParam.normal(), batchSize);
        return resultList;
    }

    /**
     * 根据最新对象和原有对象的数据对比，判断关联的字典数据和多对一主表数据是否都是合法数据。
     *
     * @param seatunnelConfig 最新数据对象。
     * @param originalSeatunnelConfig 原有数据对象。
     * @return 数据全部正确返回true，否则false。
     */
    @Override
    public CallResult verifyRelatedData(SeatunnelConfig seatunnelConfig, SeatunnelConfig originalSeatunnelConfig) {
        String errorMessageFormat = "数据验证失败，关联的%s并不存在，请刷新后重试！";
        //这里是基于字典的验证。
        if (this.needToVerify(seatunnelConfig, originalSeatunnelConfig, SeatunnelConfig::getCreateUserId)
                && !sysUserService.existId(seatunnelConfig.getCreateUserId())) {
            return CallResult.error(String.format(errorMessageFormat, "创建者ID"));
        }
        //这里是基于字典的验证。
        if (this.needToVerify(seatunnelConfig, originalSeatunnelConfig, SeatunnelConfig::getDataUserId)
                && !sysUserService.existId(seatunnelConfig.getDataUserId())) {
            return CallResult.error(String.format(errorMessageFormat, "数据所属人ID"));
        }
        //这里是基于字典的验证。
        if (this.needToVerify(seatunnelConfig, originalSeatunnelConfig, SeatunnelConfig::getDataDeptId)
                && !sysDeptService.existId(seatunnelConfig.getDataDeptId())) {
            return CallResult.error(String.format(errorMessageFormat, "数据所属部门ID"));
        }
        //这里是一对多的验证
        if (this.needToVerify(seatunnelConfig, originalSeatunnelConfig, SeatunnelConfig::getProjectId)
                && !projectMainService.existId(seatunnelConfig.getProjectId())) {
            return CallResult.error(String.format(errorMessageFormat, "关联项目id"));
        }
        return CallResult.ok();
    }

    private SeatunnelConfig buildDefaultValue(SeatunnelConfig seatunnelConfig) {
        if (seatunnelConfig.getId() == null) {
            seatunnelConfig.setId(idGenerator.nextLongId());
        }
        MyModelUtil.fillCommonsForInsert(seatunnelConfig);
        seatunnelConfig.setIsDelete(GlobalDeletedFlag.NORMAL);
        MyModelUtil.setDefaultValue(seatunnelConfig, "localhostUri", "");
        MyModelUtil.setDefaultValue(seatunnelConfig, "submitJobUrl", "");
        return seatunnelConfig;
    }

//    @Override
    public String assemblyProfile(SeatunnelEnvDto seatunnelEnvDto, SeatunnelSourceDto seatunnelSourceDto, SeatunnelSinkDto seatunnelSinkDto,String folderPath) throws IOException {
        // 修改后的生成路径
        String modifiedFolderPath = folderPath;
        // 获取脚本文件
        folderPath = folderPath+"v2.batch.config.template";
        // 读取文件内容
        List<String> lines = FileUtil.readLines(folderPath, "UTF-8");
        // 处理Env
        SeatunnelEnvDto seatunnelEnv = new SeatunnelEnvDto();
        Field[] fields = seatunnelEnv.getClass().getDeclaredFields();
        // 获取类的字段
        List<String> seatunnelEnvNameList = getFieldName(fields);
        // 将参数转化为List
        ArrayList<String> strings = new ArrayList<>();
        strings.add(seatunnelEnvDto.getJobMode());
        strings.add(seatunnelEnvDto.getExecutionParallelism());
        // 修改文件内容
        modifyContent(lines,seatunnelEnvNameList,strings);
        // 处理Source
        SeatunnelSourceDto seatunnelSource = new SeatunnelSourceDto();
        Field[] seatunnelSourcefields = seatunnelSource.getClass().getDeclaredFields();
        // 获取类的字段
        List<String> seatunnelSourceDtoNameList = getFieldName(seatunnelSourcefields);
        // 将参数转化为List
        ArrayList<String> seatunnelSourcefieldsstringList = new ArrayList<>();
        seatunnelSourcefieldsstringList.add(String.valueOf(seatunnelSourceDto.getSeatunnelSourceParallelism()));
        seatunnelSourcefieldsstringList.add(seatunnelSourceDto.getPartitionColumn());
        seatunnelSourcefieldsstringList.add(seatunnelSourceDto.getPartitionNum());
        seatunnelSourcefieldsstringList.add(seatunnelSourceDto.getResultTableName());
        seatunnelSourcefieldsstringList.add(seatunnelSourceDto.getSeatunnelSourceQuery());
        seatunnelSourcefieldsstringList.add(seatunnelSourceDto.getSeatunnelSourceDatabase());
        seatunnelSourcefieldsstringList.add(seatunnelSourceDto.getSeatunnelSourceUser());
        seatunnelSourcefieldsstringList.add(seatunnelSourceDto.getSeatunnelSourcePassword());
        seatunnelSourcefieldsstringList.add(seatunnelSourceDto.getSeatunnelSourceDriver());
        seatunnelSourcefieldsstringList.add(seatunnelSourceDto.getSeatunnelSourceUrl());
        // 修改文件内容
        modifyContent(lines,seatunnelSourceDtoNameList,seatunnelSourcefieldsstringList);
        // 处理Sink
        SeatunnelSinkDto seatunnelSink = new SeatunnelSinkDto();
        Field[] seatunnelSinkFields = seatunnelSink.getClass().getDeclaredFields();
        // 获取类的字段
        List<String> seatunnelSinkNameList = getFieldName(seatunnelSinkFields);
        // 将参数转化为List
        ArrayList<String> seatunnelSinkStringList = new ArrayList<>();
        seatunnelSinkStringList.add(seatunnelSinkDto.getSourceTableName());
        seatunnelSinkStringList.add(seatunnelSinkDto.getSeatunnelSinkTable());
        seatunnelSinkStringList.add(seatunnelSinkDto.getSeatunnelSinkDatabase());
        seatunnelSinkStringList.add(seatunnelSinkDto.getSeatunnelSinkUser());
        seatunnelSinkStringList.add(seatunnelSinkDto.getSeatunnelSinkPassword());
        seatunnelSinkStringList.add(seatunnelSinkDto.getSeatunnelSinkDriver());
        seatunnelSinkStringList.add(seatunnelSinkDto.getSeatunnelSinkUrl());
        // 修改文件内容
        modifyContent(lines,seatunnelSinkNameList,seatunnelSinkStringList);
        // 生成新文件名
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = dateFormat.format(new Date());
        String modifiedFileName = "v2.batch.config-" + timestamp + ".template";
        String modifiedFilePath = modifiedFolderPath + modifiedFileName;
        // 将修改后的内容写入新文件
        FileWriter writer = new FileWriter(new File(modifiedFilePath));
        for (String line : lines) {
            writer.write(line + StrUtil.LF);
        }
        writer.close();
        return "成功";
    }

    private static void modifyContent(List<String> lines, List<String> parameterList, List<String> modifiedParameterList) {
        List<Integer> modifiedLines = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            // 在这里进行需要的内容修改操作
            for (int j = 0; j < modifiedParameterList.size(); j++) {
                if (line.contains(parameterList.get(j))) {
                    lines.set(i, line.replace(parameterList.get(j), modifiedParameterList.get(j)));
                    modifiedLines.add(i + 1); // 记录修改的行数（从1开始）
                }
            }
        }
    }

    private  List<String> getFieldName( Field[] fields){
     List<String> strings = new ArrayList<>();
        // 遍历字段并打印字段名
        for (Field field : fields) {
            String fieldName = field.getName();
            strings.add(fieldName);
        }
        return  strings;
    }
}
