package starter.base.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import starter.base.constants.ResponseCode;
import starter.base.exception.BusinessException;

/**
 * @author zhyf
 */
@NoRepositoryBean
public interface BaseRepository<T> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {

    /**
     * 获取并校验单个实体
     *
     * @param id 实体id
     */
    default T getAndCheck(Long id) {
        if (id == null) {
            throw new BusinessException(ResponseCode.SYSTEM_DATA_NOT_FOUND, "数据不存在：" + id);
        }

        T one = findOne(id);
        if (one == null) {
            throw new BusinessException(ResponseCode.SYSTEM_DATA_NOT_FOUND, "数据不存在：" + id);
        }
        return one;
    }

}