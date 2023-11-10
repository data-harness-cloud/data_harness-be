package supie.webadmin.app.service.impl;

import supie.webadmin.app.service.AreaCodeService;
import supie.webadmin.app.dao.AreaCodeMapper;
import supie.webadmin.app.model.AreaCode;
import supie.common.core.cache.MapTreeDictionaryCache;
import supie.common.core.base.service.BaseDictService;
import supie.common.core.base.dao.BaseDaoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 行政区划的Service类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Service("areaCodeService")
public class AreaCodeServiceImpl extends BaseDictService<AreaCode, Long> implements AreaCodeService {

    @Autowired
    private AreaCodeMapper areaCodeMapper;

    public AreaCodeServiceImpl() {
        super();
        this.dictionaryCache = MapTreeDictionaryCache.create(AreaCode::getAreaId, AreaCode::getParentId);
    }

    @PostConstruct
    public void init() {
        this.reloadCachedData(true);
    }

    @Override
    protected BaseDaoMapper<AreaCode> mapper() {
        return areaCodeMapper;
    }
}
