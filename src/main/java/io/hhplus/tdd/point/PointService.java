package io.hhplus.tdd.point;
import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointService {
    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;

    public PointService(UserPointTable userPointTable, PointHistoryTable pointHistoryTable) {
        this.userPointTable = userPointTable;
        this.pointHistoryTable = pointHistoryTable;
    }

    public UserPoint getPoint(long userId) {
        return userPointTable.selectById(userId);
    }

    public List<PointHistory> getPointHistories(long userId) {
        return pointHistoryTable.selectAllByUserId(userId);
    }

    public UserPoint chargePoint(long userId, long amount) {
        UserPoint userPoint = userPointTable.selectById(userId);
        long updatedPoint = userPoint.point() + amount;
        if(updatedPoint >= 100000) {
            throw new RuntimeException();
        }
        userPointTable.insertOrUpdate(userId, updatedPoint);
        pointHistoryTable.insert(userId, amount, TransactionType.CHARGE, System.currentTimeMillis());
        return userPointTable.selectById(userId);
    }

    public UserPoint usePoint(long userId, long amount) {
        UserPoint userPoint = userPointTable.selectById(userId);
        if (userPoint.point() < amount) {
            throw new IllegalArgumentException("Insufficient points");
        }
        long updatedPoint = userPoint.point() - amount;
        UserPoint result = userPointTable.insertOrUpdate(userId, updatedPoint);
        pointHistoryTable.insert(userId, amount, TransactionType.USE, System.currentTimeMillis());
        return result;
    }
}
