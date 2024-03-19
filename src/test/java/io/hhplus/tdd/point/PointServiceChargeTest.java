package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// 단위테스트에서는 @SpringBootTest 지양
public class PointServiceChargeTest {
    UserPointTable userPointTable;
    PointHistoryTable pointHistoryTable;

    PointService pointService;

    PointServiceChargeTest() {
        this.userPointTable = new UserPointTable();
        this.pointHistoryTable = new PointHistoryTable();

        this.pointService = new PointService(userPointTable, pointHistoryTable);
    }

    // charge 함수를 짤건데.
    // 요구사항 분석
    // - 유저의 돈을 조회
    // - 유저 돈이랑 입력값이랑 더함
    // - 유저 돈 저장 후 반환

    // 실패 TC 가 있는가 ?
    // - 유저의 돈이 10만원이 넘을 수 없다. 99999원 까지 밖에 못버는 슬픈 사람.
    // 1. 더한 유저의 돈이 10만원이 넘으면, 실패.
    @Test
    void success_() {
        // 셋업
        Long userId1 = 1L;
        Long userId2 = 2L;
        // 실행
        UserPoint result1 = pointService.chargePoint(userId1, 5000L);
        UserPoint result2 = pointService.chargePoint(userId2, 99999L);
        // 검증
        assertThat(result1.id()).isEqualTo(userId1);
        assertThat(result1.point()).isEqualTo(5000);
        assertThat(result2.point()).isEqualTo(99999);
    }

    // 1. 더한 유저의 돈이 10만원 이상이면, 실패. - throw RuntimeException
    @Test
    void fail_ifUsersPointOver100_000() {
        // 셋업
        Long userId = 3L;
        assertThatThrownBy(
                () -> {
                    // 실행
                    pointService.chargePoint(userId, 100000L);
                }
                // 검증
        ).isInstanceOf(RuntimeException.class);
        // 추가 검증
        UserPoint result = pointService.getPoint(userId);
        // 입금하다 실패했기 때문에 얘는 진자 0원 가지고있어야함.
        assertThat(result.point()).isEqualTo(0);
    }

}