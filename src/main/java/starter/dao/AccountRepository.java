package starter.dao;

import org.springframework.stereotype.Repository;
import starter.base.dao.BaseRepository;
import starter.entity.Account;

/**
 * 账户
 *
 * @author zhyf
 */
@Repository
public interface AccountRepository extends BaseRepository<Account> {

    long countByAccountName(String accountName);

    Account findByAccountName(String accountName);

}