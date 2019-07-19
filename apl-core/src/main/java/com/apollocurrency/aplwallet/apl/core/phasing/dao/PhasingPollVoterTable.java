/*
 *  Copyright © 2018-2019 Apollo Foundation
 */

package com.apollocurrency.aplwallet.apl.core.phasing.dao;

import com.apollocurrency.aplwallet.apl.core.app.Transaction;
import com.apollocurrency.aplwallet.apl.core.db.DbKey;
import com.apollocurrency.aplwallet.apl.core.db.DbUtils;
import com.apollocurrency.aplwallet.apl.core.db.LongKey;
import com.apollocurrency.aplwallet.apl.core.db.LongKeyFactory;
import com.apollocurrency.aplwallet.apl.core.db.derived.ValuesDbTable;
import com.apollocurrency.aplwallet.apl.core.phasing.mapper.PhasingPollVoterMapper;
import com.apollocurrency.aplwallet.apl.core.phasing.model.PhasingPollVoter;
import com.apollocurrency.aplwallet.apl.core.utils.BlockchainUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PhasingPollVoterTable extends ValuesDbTable<PhasingPollVoter> {
    private static final String TABLE_NAME = "phasing_poll_voter";
    private static final LongKeyFactory<PhasingPollVoter> KEY_FACTORY = new LongKeyFactory<PhasingPollVoter>("transaction_id") {
        @Override
        public DbKey newKey(PhasingPollVoter poll) {
            if (poll.getDbKey() == null) {
                poll.setDbKey(new LongKey(poll.getPollId()));
            }
            return poll.getDbKey();
        }
    };
    private static final PhasingPollVoterMapper MAPPER = new PhasingPollVoterMapper(KEY_FACTORY);


    @Inject
    public PhasingPollVoterTable() {
        super(TABLE_NAME, false, KEY_FACTORY, false);
    }

    public List<PhasingPollVoter> get(long pollId) {
        return get(KEY_FACTORY.newKey(pollId));
    }

    @Override
    public PhasingPollVoter load(Connection con, ResultSet rs, DbKey dbKey) throws SQLException {
        return MAPPER.map(rs, null);
    }

    @Override
    public void save(Connection con, PhasingPollVoter voter) throws SQLException {
        try (PreparedStatement pstmt = con.prepareStatement("INSERT INTO phasing_poll_voter (transaction_id, "
                + "voter_id, height) VALUES (?, ?, ?)")) {
            int i = 0;
            pstmt.setLong(++i, voter.getPollId());
            pstmt.setLong(++i, voter.getVoterId());
            pstmt.setInt(++i, voter.getHeight());
            pstmt.executeUpdate();
        }
    }

    public List<Transaction> getVoterPhasedTransactions(long voterId, int from, int to, int height) throws SQLException {
        Connection con = null;
        try {
            con = getDatabaseManager().getDataSource().getConnection();
            PreparedStatement pstmt = con.prepareStatement("SELECT transaction.* "
                    + "FROM transaction, phasing_poll_voter, phasing_poll "
                    + "LEFT JOIN phasing_poll_result ON phasing_poll.id = phasing_poll_result.id "
                    + "WHERE transaction.id = phasing_poll.id AND "
                    + "phasing_poll.finish_height > ? AND "
                    + "phasing_poll.id = phasing_poll_voter.transaction_id "
                    + "AND phasing_poll_voter.voter_id = ? "
                    + "AND phasing_poll_result.id IS NULL "
                    + "ORDER BY transaction.height DESC, transaction.transaction_index DESC "
                    + DbUtils.limitsClause(from, to));
            int i = 0;
            pstmt.setInt(++i, height);
            pstmt.setLong(++i, voterId);
            DbUtils.setLimits(++i, pstmt, from, to);

            return BlockchainUtil.loadTransactions(pstmt);
        } catch (SQLException e) {
            DbUtils.close(con);
            throw e;
        }
    }
}
