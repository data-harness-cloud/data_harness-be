package supie.webadmin.app.controller;

import io.swagger.annotations.ApiOperation;
import supie.common.log.annotation.OperationLog;
import supie.common.log.model.constant.SysOperationLogType;
import com.github.pagehelper.page.PageMethod;
import supie.webadmin.app.vo.*;
import supie.webadmin.app.dto.*;
import supie.webadmin.app.model.*;
import supie.webadmin.app.service.*;
import supie.webadmin.upms.vo.*;
import supie.webadmin.upms.dto.*;
import supie.webadmin.upms.model.*;
import supie.webadmin.upms.service.*;
import supie.common.core.object.*;
import supie.common.core.util.*;
import supie.common.core.constant.*;
import supie.common.core.annotation.MyRequestBody;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据项目—项目管理主表操作控制器类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Api(tags = "数据项目—项目管理主表管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/app/projectMain")
public class ProjectMainController {

    @Autowired
    private ProjectMainService projectMainService;
    @Autowired
    private SchedulingTasksService schedulingTasksService;
    @Autowired
    private ProjectDatasourceService projectDatasourceService;
    @Autowired
    private RemoteHostService remoteHostService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SeatunnelConfigService seatunnelConfigService;
    @Autowired
    private DevLiteflowRulerService devLiteflowRulerService;

    /**
     * 新增数据项目—项目管理主表数据。
     *
     * @param projectMainDto 新增对象。
     * @return 应答结果对象，包含新增对象主键Id。
     */
    @ApiOperationSupport(ignoreParameters = {
            "projectMainDto.id",
            "projectMainDto.searchString",
            "projectMainDto.updateTimeStart",
            "projectMainDto.updateTimeEnd",
            "projectMainDto.createTimeStart",
            "projectMainDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.ADD)
    @PostMapping("/add")
    public ResponseResult<Long> add(@MyRequestBody ProjectMainDto projectMainDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(projectMainDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        ProjectMain projectMain = MyModelUtil.copyTo(projectMainDto, ProjectMain.class);
        // 验证关联Id的数据合法性
        CallResult callResult = projectMainService.verifyRelatedData(projectMain, null);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        projectMain = projectMainService.saveNew(projectMain);
        return ResponseResult.success(projectMain.getId());
    }

    /**
     * 更新数据项目—项目管理主表数据。
     *
     * @param projectMainDto 更新对象。
     * @return 应答结果对象。
     */
    @ApiOperationSupport(ignoreParameters = {
            "projectMainDto.searchString",
            "projectMainDto.updateTimeStart",
            "projectMainDto.updateTimeEnd",
            "projectMainDto.createTimeStart",
            "projectMainDto.createTimeEnd"})
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> update(@MyRequestBody ProjectMainDto projectMainDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(projectMainDto, true);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        ProjectMain projectMain = MyModelUtil.copyTo(projectMainDto, ProjectMain.class);
        ProjectMain originalProjectMain = projectMainService.getById(projectMain.getId());
        if (originalProjectMain == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [数据] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        // 验证关联Id的数据合法性
        CallResult callResult = projectMainService.verifyRelatedData(projectMain, originalProjectMain);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        if (!projectMainService.update(projectMain, originalProjectMain)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 删除数据项目—项目管理主表数据。
     *
     * @param id 删除对象主键Id。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.DELETE)
    @PostMapping("/delete")
    public ResponseResult<Void> delete(@MyRequestBody Long id) {
        if (MyCommonUtil.existBlankArgument(id)) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        return this.doDelete(id);
    }

    /**
     * 列出符合过滤条件的数据项目—项目管理主表列表。
     *
     * @param projectMainDtoFilter 过滤对象。
     * @param remoteHostDtoFilter 一对多从表过滤对象。
     * @param seatunnelConfigDtoFilter 一对多从表过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/list")
    public ResponseResult<MyPageData<ProjectMainVo>> list(
            @MyRequestBody ProjectMainDto projectMainDtoFilter,
            @MyRequestBody RemoteHostDto remoteHostDtoFilter,
            @MyRequestBody SeatunnelConfigDto seatunnelConfigDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        ProjectMain projectMainFilter = MyModelUtil.copyTo(projectMainDtoFilter, ProjectMain.class);
        RemoteHost remoteHostFilter = MyModelUtil.copyTo(remoteHostDtoFilter, RemoteHost.class);
        SeatunnelConfig seatunnelConfigFilter = MyModelUtil.copyTo(seatunnelConfigDtoFilter, SeatunnelConfig.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, ProjectMain.class);
        List<ProjectMain> projectMainList =
                projectMainService.getProjectMainListWithRelation(projectMainFilter, remoteHostFilter, seatunnelConfigFilter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(projectMainList, ProjectMain.INSTANCE));
    }

    /**
     * 分组列出符合过滤条件的数据项目—项目管理主表列表。
     *
     * @param projectMainDtoFilter 过滤对象。
     * @param groupParam 分组参数。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping("/listWithGroup")
    public ResponseResult<MyPageData<ProjectMainVo>> listWithGroup(
            @MyRequestBody ProjectMainDto projectMainDtoFilter,
            @MyRequestBody(required = true) MyGroupParam groupParam,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        String orderBy = MyOrderParam.buildOrderBy(orderParam, ProjectMain.class, false);
        groupParam = MyGroupParam.buildGroupBy(groupParam, ProjectMain.class);
        if (groupParam == null) {
            return ResponseResult.error(
                    ErrorCodeEnum.INVALID_ARGUMENT_FORMAT, "数据参数错误，分组参数不能为空！");
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        ProjectMain filter = MyModelUtil.copyTo(projectMainDtoFilter, ProjectMain.class);
        MyGroupCriteria criteria = groupParam.getGroupCriteria();
        List<ProjectMain> resultList = projectMainService.getGroupedProjectMainListWithRelation(
                filter, criteria.getGroupSelect(), criteria.getGroupBy(), orderBy);
        // 分页连同对象数据转换copy工作，下面的方法一并完成。
        return ResponseResult.success(MyPageUtil.makeResponseData(resultList, ProjectMain.INSTANCE));
    }

    /**
     * 查看指定数据项目—项目管理主表对象详情。
     *
     * @param id 指定对象主键Id。
     * @return 应答结果对象，包含对象详情。
     */
    @GetMapping("/view")
    public ResponseResult<ProjectMainVo> view(@RequestParam Long id) {
        ProjectMain projectMain = projectMainService.getByIdWithRelation(id, MyRelationParam.full());
        if (projectMain == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        ProjectMainVo projectMainVo = ProjectMain.INSTANCE.fromModel(projectMain);
        return ResponseResult.success(projectMainVo);
    }

    /**
     * 列出不与指定数据项目—项目管理主表存在多对多关系的 [用户管理] 列表数据。通常用于查看添加新 [用户管理] 对象的候选列表。
     *
     * @param id 主表关联字段。
     * @param sysUserDtoFilter [用户管理] 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，返回符合条件的数据列表。
     */
    @PostMapping("/listNotInProjectMember")
    public ResponseResult<MyPageData<SysUserVo>> listNotInProjectMember(
            @MyRequestBody Long id,
            @MyRequestBody SysUserDto sysUserDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (MyCommonUtil.isNotBlankOrNull(id) && !projectMainService.existId(id)) {
            return ResponseResult.error(ErrorCodeEnum.INVALID_RELATED_RECORD_ID);
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        SysUser filter = MyModelUtil.copyTo(sysUserDtoFilter, SysUser.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, SysUser.class);
        List<SysUser> sysUserList =
                sysUserService.getNotInSysUserListByMemberProjectId(id, filter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(sysUserList, SysUser.INSTANCE));
    }

    /**
     * 列出与指定数据项目—项目管理主表存在多对多关系的 [用户管理] 列表数据。
     *
     * @param id 主表关联字段。
     * @param sysUserDtoFilter [用户管理] 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，返回符合条件的数据列表。
     */
    @PostMapping("/listProjectMember")
    public ResponseResult<MyPageData<SysUserVo>> listProjectMember(
            @MyRequestBody(required = true) Long id,
            @MyRequestBody SysUserDto sysUserDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (!projectMainService.existId(id)) {
            return ResponseResult.error(ErrorCodeEnum.INVALID_RELATED_RECORD_ID);
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        SysUser filter = MyModelUtil.copyTo(sysUserDtoFilter, SysUser.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, SysUser.class);
        List<SysUser> sysUserList =
                sysUserService.getSysUserListByMemberProjectId(id, filter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(sysUserList, SysUser.INSTANCE));
    }

    /**
     * 批量添加数据项目—项目管理主表和 [用户管理] 对象的多对多关联关系数据。
     *
     * @param memberProjectId 主表主键Id。
     * @param projectMemberDtoList 关联对象列表。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.ADD_M2M)
    @PostMapping("/addProjectMember")
    public ResponseResult<Void> addProjectMember(
            @MyRequestBody Long memberProjectId,
            @MyRequestBody List<ProjectMemberDto> projectMemberDtoList) {
        if (MyCommonUtil.existBlankArgument(memberProjectId, projectMemberDtoList)) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        Set<Long> memberUserIdSet =
                projectMemberDtoList.stream().map(ProjectMemberDto::getMemberUserId).collect(Collectors.toSet());
        if (!projectMainService.existId(memberProjectId)
                || !sysUserService.existUniqueKeyList("userId", memberUserIdSet)) {
            return ResponseResult.error(ErrorCodeEnum.INVALID_RELATED_RECORD_ID);
        }
        List<ProjectMember> projectMemberList =
                MyModelUtil.copyCollectionTo(projectMemberDtoList, ProjectMember.class);
        projectMainService.addProjectMemberList(projectMemberList, memberProjectId);
        return ResponseResult.success();
    }

    /**
     * 更新指定数据项目—项目管理主表和指定 [用户管理] 的多对多关联数据。
     *
     * @param projectMemberDto 对多对中间表对象。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/updateProjectMember")
    public ResponseResult<Void> updateProjectMember(
            @MyRequestBody ProjectMemberDto projectMemberDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(projectMemberDto);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        ProjectMember projectMember = MyModelUtil.copyTo(projectMemberDto, ProjectMember.class);
        if (!projectMainService.updateProjectMember(projectMember)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 显示数据项目—项目管理主表和指定 [用户管理] 的多对多关联详情数据。
     *
     * @param memberProjectId 主表主键Id。
     * @param memberUserId 从表主键Id。
     * @return 应答结果对象，包括中间表详情。
     */
    @GetMapping("/viewProjectMember")
    public ResponseResult<ProjectMemberVo> viewProjectMember(
            @RequestParam Long memberProjectId, @RequestParam Long memberUserId) {
        ProjectMember projectMember = projectMainService.getProjectMember(memberProjectId, memberUserId);
        if (projectMember == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        ProjectMemberVo projectMemberVo = MyModelUtil.copyTo(projectMember, ProjectMemberVo.class);
        return ResponseResult.success(projectMemberVo);
    }

    /**
     * 移除指定数据项目—项目管理主表和指定 [用户管理] 的多对多关联关系。
     *
     * @param memberProjectId 主表主键Id。
     * @param memberUserId 从表主键Id。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.DELETE_M2M)
    @PostMapping("/deleteProjectMember")
    public ResponseResult<Void> deleteProjectMember(
            @MyRequestBody Long memberProjectId, @MyRequestBody Long memberUserId) {
        if (MyCommonUtil.existBlankArgument(memberProjectId, memberUserId)) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        if (!projectMainService.removeProjectMember(memberProjectId, memberUserId)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 列出不与指定数据项目—项目管理主表存在多对多关系的 [数据项目-数据源表] 列表数据。通常用于查看添加新 [数据项目-数据源表] 对象的候选列表。
     *
     * @param id 主表关联字段。
     * @param projectDatasourceDtoFilter [数据项目-数据源表] 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，返回符合条件的数据列表。
     */
    @PostMapping("/listNotInProjectDatasourceRelation")
    public ResponseResult<MyPageData<ProjectDatasourceVo>> listNotInProjectDatasourceRelation(
            @MyRequestBody Long id,
            @MyRequestBody ProjectDatasourceDto projectDatasourceDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (MyCommonUtil.isNotBlankOrNull(id) && !projectMainService.existId(id)) {
            return ResponseResult.error(ErrorCodeEnum.INVALID_RELATED_RECORD_ID);
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        ProjectDatasource filter = MyModelUtil.copyTo(projectDatasourceDtoFilter, ProjectDatasource.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, ProjectDatasource.class);
        List<ProjectDatasource> projectDatasourceList =
                projectDatasourceService.getNotInProjectDatasourceListByProjectId(id, filter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(projectDatasourceList, ProjectDatasource.INSTANCE));
    }

    /**
     * 列出与指定数据项目—项目管理主表存在多对多关系的 [数据项目-数据源表] 列表数据。
     *
     * @param id 主表关联字段。
     * @param projectDatasourceDtoFilter [数据项目-数据源表] 过滤对象。
     * @param orderParam 排序参数。
     * @param pageParam 分页参数。
     * @return 应答结果对象，返回符合条件的数据列表。
     */
    @PostMapping("/listProjectDatasourceRelation")
    public ResponseResult<MyPageData<ProjectDatasourceVo>> listProjectDatasourceRelation(
            @MyRequestBody(required = true) Long id,
            @MyRequestBody ProjectDatasourceDto projectDatasourceDtoFilter,
            @MyRequestBody MyOrderParam orderParam,
            @MyRequestBody MyPageParam pageParam) {
        if (!projectMainService.existId(id)) {
            return ResponseResult.error(ErrorCodeEnum.INVALID_RELATED_RECORD_ID);
        }
        if (pageParam != null) {
            PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        }
        ProjectDatasource filter = MyModelUtil.copyTo(projectDatasourceDtoFilter, ProjectDatasource.class);
        String orderBy = MyOrderParam.buildOrderBy(orderParam, ProjectDatasource.class);
        List<ProjectDatasource> projectDatasourceList =
                projectDatasourceService.getProjectDatasourceListByProjectId(id, filter, orderBy);
        return ResponseResult.success(MyPageUtil.makeResponseData(projectDatasourceList, ProjectDatasource.INSTANCE));
    }

    /**
     * 批量添加数据项目—项目管理主表和 [数据项目-数据源表] 对象的多对多关联关系数据。
     *
     * @param projectId 主表主键Id。
     * @param projectDatasourceRelationDtoList 关联对象列表。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.ADD_M2M)
    @PostMapping("/addProjectDatasourceRelation")
    public ResponseResult<Void> addProjectDatasourceRelation(
            @MyRequestBody Long projectId,
            @MyRequestBody List<ProjectDatasourceRelationDto> projectDatasourceRelationDtoList) {
        if (MyCommonUtil.existBlankArgument(projectId, projectDatasourceRelationDtoList)) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        Set<Long> datasourceIdSet =
                projectDatasourceRelationDtoList.stream().map(ProjectDatasourceRelationDto::getDatasourceId).collect(Collectors.toSet());
        if (!projectMainService.existId(projectId)
                || !projectDatasourceService.existUniqueKeyList("id", datasourceIdSet)) {
            return ResponseResult.error(ErrorCodeEnum.INVALID_RELATED_RECORD_ID);
        }
        List<ProjectDatasourceRelation> projectDatasourceRelationList =
                MyModelUtil.copyCollectionTo(projectDatasourceRelationDtoList, ProjectDatasourceRelation.class);
        projectMainService.addProjectDatasourceRelationList(projectDatasourceRelationList, projectId);
        return ResponseResult.success();
    }

    /**
     * 更新指定数据项目—项目管理主表和指定 [数据项目-数据源表] 的多对多关联数据。
     *
     * @param projectDatasourceRelationDto 对多对中间表对象。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.UPDATE)
    @PostMapping("/updateProjectDatasourceRelation")
    public ResponseResult<Void> updateProjectDatasourceRelation(
            @MyRequestBody ProjectDatasourceRelationDto projectDatasourceRelationDto) {
        String errorMessage = MyCommonUtil.getModelValidationError(projectDatasourceRelationDto);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        ProjectDatasourceRelation projectDatasourceRelation = MyModelUtil.copyTo(projectDatasourceRelationDto, ProjectDatasourceRelation.class);
        if (!projectMainService.updateProjectDatasourceRelation(projectDatasourceRelation)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    /**
     * 显示数据项目—项目管理主表和指定 [数据项目-数据源表] 的多对多关联详情数据。
     *
     * @param projectId 主表主键Id。
     * @param datasourceId 从表主键Id。
     * @return 应答结果对象，包括中间表详情。
     */
    @GetMapping("/viewProjectDatasourceRelation")
    public ResponseResult<ProjectDatasourceRelationVo> viewProjectDatasourceRelation(
            @RequestParam Long projectId, @RequestParam Long datasourceId) {
        ProjectDatasourceRelation projectDatasourceRelation = projectMainService.getProjectDatasourceRelation(projectId, datasourceId);
        if (projectDatasourceRelation == null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        ProjectDatasourceRelationVo projectDatasourceRelationVo = MyModelUtil.copyTo(projectDatasourceRelation, ProjectDatasourceRelationVo.class);
        return ResponseResult.success(projectDatasourceRelationVo);
    }

    /**
     * 移除指定数据项目—项目管理主表和指定 [数据项目-数据源表] 的多对多关联关系。
     *
     * @param projectId 主表主键Id。
     * @param datasourceId 从表主键Id。
     * @return 应答结果对象。
     */
    @OperationLog(type = SysOperationLogType.DELETE_M2M)
    @PostMapping("/deleteProjectDatasourceRelation")
    public ResponseResult<Void> deleteProjectDatasourceRelation(
            @MyRequestBody Long projectId, @MyRequestBody Long datasourceId) {
        if (MyCommonUtil.existBlankArgument(projectId, datasourceId)) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        if (!projectMainService.removeProjectDatasourceRelation(projectId, datasourceId)) {
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.success();
    }

    private ResponseResult<Void> doDelete(Long id) {
        String errorMessage;
        // 验证关联Id的数据合法性
        ProjectMain originalProjectMain = projectMainService.getById(id);
        if (originalProjectMain == null) {
            // NOTE: 修改下面方括号中的话述
            errorMessage = "数据验证失败，当前 [对象] 并不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        // NOTE: 如果该对象的删除前数据一致性验证和实际需求有偏差，可以根据需求调整验证字段，甚至也可以直接删除下面的验证。
        // 删除前，先主动验证是否存在关联的从表数据。
        CallResult callResult = projectMainService.verifyRelatedDataBeforeDelete(originalProjectMain);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        if (!projectMainService.remove(id)) {
            errorMessage = "数据操作失败，删除的对象不存在，请刷新后重试！";
            return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
        }
        return ResponseResult.success();
    }

    /**
     * 新增项目
     */
    @ApiOperation("新增项目")
    @PostMapping("/addProject")
    public ResponseResult<Long> addProject(
            @MyRequestBody ProjectMainDto projectMainDto, @MyRequestBody List<Long> memberUserIdList) {
        String errorMessage = MyCommonUtil.getModelValidationError(projectMainDto, false);
        if (errorMessage != null) {
            return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
        }
        ProjectMain projectMain = MyModelUtil.copyTo(projectMainDto, ProjectMain.class);
        // 验证关联Id的数据合法性
        CallResult callResult = projectMainService.verifyRelatedData(projectMain, null);
        if (!callResult.isSuccess()) {
            return ResponseResult.errorFrom(callResult);
        }
        try {
            return ResponseResult.success(projectMainService.addProject(projectMain, memberUserIdList));
        } catch (Exception e) {
            return ResponseResult.error("500", e.getMessage());
        }
    }

}
