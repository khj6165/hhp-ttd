import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.PointService;
import io.hhplus.tdd.point.UserPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class PointServiceUseTest {

    private PointService pointService;

    @Mock
    private UserPointTable userPointTable;

    @Mock
    private PointHistoryTable pointHistoryTable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pointService = new PointService(userPointTable, pointHistoryTable);
    }

    @Test
    void fail_insufficientPoints() {
        // Given
        long userId = 1L;
        long currentPoints = 50L;
        long amountToUse = 100L;
        when(userPointTable.selectById(userId)).thenReturn(new UserPoint(userId, currentPoints, System.currentTimeMillis()));

        // When
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pointService.usePoint(userId, amountToUse);
        });

        // Then
        assertEquals("Insufficient points", exception.getMessage());
    }

    @Test
    void testUsePoint_Success() {
        // Given
        long userId = 1L;
        long currentPoints = 1000L;
        long amountToUse = 50L;
        when(userPointTable.selectById(userId)).thenReturn(new UserPoint(userId, currentPoints, System.currentTimeMillis()));

        // When
        UserPoint result = pointService.usePoint(userId, amountToUse);

        // Then
        assertEquals(currentPoints - amountToUse, result.point());
    }
}
