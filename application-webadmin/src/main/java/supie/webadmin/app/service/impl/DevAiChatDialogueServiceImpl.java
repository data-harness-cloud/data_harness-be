package supie.webadmin.app.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import supie.webadmin.app.service.*;
import supie.webadmin.app.dao.*;
import supie.webadmin.app.model.*;
import supie.webadmin.upms.service.SysUserService;
import supie.webadmin.upms.service.SysDeptService;
import supie.common.core.base.dao.BaseDaoMapper;
import supie.common.core.constant.GlobalDeletedFlag;
import supie.common.core.object.MyRelationParam;
import supie.common.core.object.CallResult;
import supie.common.core.base.service.BaseService;
import supie.common.core.util.MyModelUtil;
import supie.common.sequence.wrapper.IdGeneratorWrapper;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 数据开发-AI模块-对话记录数据操作服务类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
@Service("devAiChatDialogueService")
public class DevAiChatDialogueServiceImpl extends BaseService<DevAiChatDialogue, Long> implements DevAiChatDialogueService {

    @Autowired
    private DevAiChatDialogueMapper devAiChatDialogueMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private IdGeneratorWrapper idGenerator;

    /**
     * 返回当前Service的主表Mapper对象。
     *
     * @return 主表Mapper对象。
     */
    @Override
    protected BaseDaoMapper<DevAiChatDialogue> mapper() {
        return devAiChatDialogueMapper;
    }

    /**
     * 保存新增对象。
     *
     * @param devAiChatDialogue 新增对象。
     * @return 返回新增对象。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public DevAiChatDialogue saveNew(DevAiChatDialogue devAiChatDialogue) {
        devAiChatDialogueMapper.insert(this.buildDefaultValue(devAiChatDialogue));
        return devAiChatDialogue;
    }

    /**
     * 利用数据库的insertList语法，批量插入对象列表。
     *
     * @param devAiChatDialogueList 新增对象列表。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveNewBatch(List<DevAiChatDialogue> devAiChatDialogueList) {
        if (CollUtil.isNotEmpty(devAiChatDialogueList)) {
            devAiChatDialogueList.forEach(this::buildDefaultValue);
            devAiChatDialogueMapper.insertList(devAiChatDialogueList);
        }
    }

    /**
     * 更新数据对象。
     *
     * @param devAiChatDialogue         更新的对象。
     * @param originalDevAiChatDialogue 原有数据对象。
     * @return 成功返回true，否则false。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(DevAiChatDialogue devAiChatDialogue, DevAiChatDialogue originalDevAiChatDialogue) {
        MyModelUtil.fillCommonsForUpdate(devAiChatDialogue, originalDevAiChatDialogue);
        // 这里重点提示，在执行主表数据更新之前，如果有哪些字段不支持修改操作，请用原有数据对象字段替换当前数据字段。
        UpdateWrapper<DevAiChatDialogue> uw = this.createUpdateQueryForNullValue(devAiChatDialogue, devAiChatDialogue.getId());
        return devAiChatDialogueMapper.update(devAiChatDialogue, uw) == 1;
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
        return devAiChatDialogueMapper.deleteById(id) == 1;
    }

    /**
     * 获取单表查询结果。由于没有关联数据查询，因此在仅仅获取单表数据的场景下，效率更高。
     * 如果需要同时获取关联数据，请移步(getDevAiChatDialogueListWithRelation)方法。
     *
     * @param filter  过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<DevAiChatDialogue> getDevAiChatDialogueList(DevAiChatDialogue filter, String orderBy) {
        return devAiChatDialogueMapper.getDevAiChatDialogueList(filter, orderBy);
    }

    /**
     * 获取主表的查询结果，以及主表关联的字典数据和一对一从表数据，以及一对一从表的字典数据。
     * 该查询会涉及到一对一从表的关联过滤，或一对多从表的嵌套关联过滤，因此性能不如单表过滤。
     * 如果仅仅需要获取主表数据，请移步(getDevAiChatDialogueList)，以便获取更好的查询性能。
     *
     * @param filter 主表过滤对象。
     * @param orderBy 排序参数。
     * @return 查询结果集。
     */
    @Override
    public List<DevAiChatDialogue> getDevAiChatDialogueListWithRelation(DevAiChatDialogue filter, String orderBy) {
        List<DevAiChatDialogue> resultList = devAiChatDialogueMapper.getDevAiChatDialogueList(filter, orderBy);
        // 在缺省生成的代码中，如果查询结果resultList不是Page对象，说明没有分页，那么就很可能是数据导出接口调用了当前方法。
        // 为了避免一次性的大量数据关联，规避因此而造成的系统运行性能冲击，这里手动进行了分批次读取，开发者可按需修改该值。
        int batchSize = resultList instanceof Page ? 0 : 1000;
        this.buildRelationForDataList(resultList, MyRelationParam.normal(), batchSize);
        return resultList;
    }

    /**
     * 根据最新对象和原有对象的数据对比，判断关联的字典数据和多对一主表数据是否都是合法数据。
     *
     * @param devAiChatDialogue 最新数据对象。
     * @param originalDevAiChatDialogue 原有数据对象。
     * @return 数据全部正确返回true，否则false。
     */
    @Override
    public CallResult verifyRelatedData(DevAiChatDialogue devAiChatDialogue, DevAiChatDialogue originalDevAiChatDialogue) {
        String errorMessageFormat = "数据验证失败，关联的%s并不存在，请刷新后重试！";
        //这里是基于字典的验证。
        if (this.needToVerify(devAiChatDialogue, originalDevAiChatDialogue, DevAiChatDialogue::getCreateUserId)
                && !sysUserService.existId(devAiChatDialogue.getCreateUserId())) {
            return CallResult.error(String.format(errorMessageFormat, "创建人"));
        }
        //这里是基于字典的验证。
        if (this.needToVerify(devAiChatDialogue, originalDevAiChatDialogue, DevAiChatDialogue::getDataUserId)
                && !sysUserService.existId(devAiChatDialogue.getDataUserId())) {
            return CallResult.error(String.format(errorMessageFormat, "数据所属人"));
        }
        //这里是基于字典的验证。
        if (this.needToVerify(devAiChatDialogue, originalDevAiChatDialogue, DevAiChatDialogue::getDataDeptId)
                && !sysDeptService.existId(devAiChatDialogue.getDataDeptId())) {
            return CallResult.error(String.format(errorMessageFormat, "数据所属部门"));
        }
        return CallResult.ok();
    }

    private DevAiChatDialogue buildDefaultValue(DevAiChatDialogue devAiChatDialogue) {
        if (devAiChatDialogue.getId() == null) {
            devAiChatDialogue.setId(idGenerator.nextLongId());
        }
        MyModelUtil.fillCommonsForInsert(devAiChatDialogue);
        devAiChatDialogue.setIsDelete(GlobalDeletedFlag.NORMAL);
        MyModelUtil.setDefaultValue(devAiChatDialogue, "dialogueRole", "");
        MyModelUtil.setDefaultValue(devAiChatDialogue, "dialoguePrompt", "");
        MyModelUtil.setDefaultValue(devAiChatDialogue, "dialogueStrId", "");
        return devAiChatDialogue;
    }
}
