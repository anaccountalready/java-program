package cn.nankai.edu.cn;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Avg_score实体类测试")
class Avg_scoreTest {

    @Test
    @DisplayName("测试默认构造函数")
    void testDefaultConstructor() {
        Avg_score avgScore = new Avg_score();
        
        assertNotNull(avgScore);
        assertEquals(0, avgScore.getStu_id());
        assertEquals(0.0f, avgScore.getAvg_score(), 0.001f);
    }

    @ParameterizedTest
    @CsvSource({
            "1001, 85.5",
            "1002, 90.0",
            "1003, 78.5"
    })
    @DisplayName("测试带参数构造函数")
    void testParameterizedConstructor(int stuId, float avgScore) {
        Avg_score score = new Avg_score(stuId, avgScore);
        
        assertEquals(stuId, score.getStu_id());
        assertEquals(avgScore, score.getAvg_score(), 0.001f);
    }

    @Test
    @DisplayName("测试Setter方法")
    void testSetters() {
        Avg_score score = new Avg_score();
        
        score.setStu_id(2001);
        score.setAvg_score(95.5f);
        
        assertEquals(2001, score.getStu_id());
        assertEquals(95.5f, score.getAvg_score(), 0.001f);
    }

    @Test
    @DisplayName("测试边界值 - 学生ID为0")
    void testBoundaryZeroStuId() {
        Avg_score score = new Avg_score(0, 85.5f);
        assertEquals(0, score.getStu_id());
        
        score.setStu_id(0);
        assertEquals(0, score.getStu_id());
    }

    @Test
    @DisplayName("测试边界值 - 学生ID为负数")
    void testBoundaryNegativeStuId() {
        Avg_score score = new Avg_score(-1, 85.5f);
        assertEquals(-1, score.getStu_id());
        
        score.setStu_id(-100);
        assertEquals(-100, score.getStu_id());
    }

    @Test
    @DisplayName("测试边界值 - 学生ID为最大值")
    void testBoundaryMaxStuId() {
        Avg_score score = new Avg_score(Integer.MAX_VALUE, 85.5f);
        assertEquals(Integer.MAX_VALUE, score.getStu_id());
        
        score.setStu_id(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, score.getStu_id());
    }

    @Test
    @DisplayName("测试边界值 - 学生ID为最小值")
    void testBoundaryMinStuId() {
        Avg_score score = new Avg_score(Integer.MIN_VALUE, 85.5f);
        assertEquals(Integer.MIN_VALUE, score.getStu_id());
        
        score.setStu_id(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, score.getStu_id());
    }

    @Test
    @DisplayName("测试边界值 - 平均分为0")
    void testBoundaryZeroAvgScore() {
        Avg_score score = new Avg_score(1001, 0.0f);
        assertEquals(0.0f, score.getAvg_score(), 0.001f);
        
        score.setAvg_score(0.0f);
        assertEquals(0.0f, score.getAvg_score(), 0.001f);
    }

    @Test
    @DisplayName("测试边界值 - 平均分为100")
    void testBoundaryHundredAvgScore() {
        Avg_score score = new Avg_score(1001, 100.0f);
        assertEquals(100.0f, score.getAvg_score(), 0.001f);
        
        score.setAvg_score(100.0f);
        assertEquals(100.0f, score.getAvg_score(), 0.001f);
    }

    @Test
    @DisplayName("测试边界值 - 平均分为负数")
    void testBoundaryNegativeAvgScore() {
        Avg_score score = new Avg_score(1001, -5.5f);
        assertEquals(-5.5f, score.getAvg_score(), 0.001f);
        
        score.setAvg_score(-10.0f);
        assertEquals(-10.0f, score.getAvg_score(), 0.001f);
    }

    @Test
    @DisplayName("测试边界值 - 平均分超过100")
    void testBoundaryOverHundredAvgScore() {
        Avg_score score = new Avg_score(1001, 105.5f);
        assertEquals(105.5f, score.getAvg_score(), 0.001f);
        
        score.setAvg_score(110.0f);
        assertEquals(110.0f, score.getAvg_score(), 0.001f);
    }

    @Test
    @DisplayName("测试平均分精度")
    void testAvgScorePrecision() {
        Avg_score score = new Avg_score();
        
        score.setAvg_score(85.123456f);
        assertEquals(85.123456f, score.getAvg_score(), 0.000001f);
        
        score.setAvg_score(0.0001f);
        assertEquals(0.0001f, score.getAvg_score(), 0.0000001f);
    }

    @Test
    @DisplayName("测试特殊分数值 - NaN")
    void testSpecialScoreNaN() {
        Avg_score score = new Avg_score();
        
        score.setAvg_score(Float.NaN);
        assertTrue(Float.isNaN(score.getAvg_score()));
        
        score = new Avg_score(1001, Float.NaN);
        assertTrue(Float.isNaN(score.getAvg_score()));
    }

    @Test
    @DisplayName("测试特殊分数值 - 无穷大")
    void testSpecialScoreInfinity() {
        Avg_score score = new Avg_score();
        
        score.setAvg_score(Float.POSITIVE_INFINITY);
        assertTrue(Float.isInfinite(score.getAvg_score()));
        assertTrue(Float.isInfinite(score.getAvg_score()) && score.getAvg_score() > 0);
        
        score.setAvg_score(Float.NEGATIVE_INFINITY);
        assertTrue(Float.isInfinite(score.getAvg_score()));
        assertTrue(Float.isInfinite(score.getAvg_score()) && score.getAvg_score() < 0);
    }

    @Test
    @DisplayName("测试组合边界值")
    void testCombinedBoundaryValues() {
        Avg_score score = new Avg_score();
        
        score.setStu_id(Integer.MIN_VALUE);
        score.setAvg_score(Float.MIN_VALUE);
        
        assertEquals(Integer.MIN_VALUE, score.getStu_id());
        assertEquals(Float.MIN_VALUE, score.getAvg_score(), 0.0000001f);
        
        score.setStu_id(Integer.MAX_VALUE);
        score.setAvg_score(Float.MAX_VALUE);
        
        assertEquals(Integer.MAX_VALUE, score.getStu_id());
        assertEquals(Float.MAX_VALUE, score.getAvg_score(), 0.0000001f);
    }

    @Test
    @DisplayName("测试多次设置相同值")
    void testMultipleSetSameValue() {
        Avg_score score = new Avg_score();
        
        score.setStu_id(1001);
        score.setStu_id(1001);
        score.setStu_id(1001);
        assertEquals(1001, score.getStu_id());
        
        score.setAvg_score(85.5f);
        score.setAvg_score(85.5f);
        score.setAvg_score(85.5f);
        assertEquals(85.5f, score.getAvg_score(), 0.001f);
    }

    @Test
    @DisplayName("测试交替设置值")
    void testAlternatingSetValues() {
        Avg_score score = new Avg_score();
        
        score.setStu_id(1001);
        score.setAvg_score(85.5f);
        assertEquals(1001, score.getStu_id());
        assertEquals(85.5f, score.getAvg_score(), 0.001f);
        
        score.setStu_id(1002);
        score.setAvg_score(90.0f);
        assertEquals(1002, score.getStu_id());
        assertEquals(90.0f, score.getAvg_score(), 0.001f);
        
        score.setStu_id(1003);
        score.setAvg_score(78.5f);
        assertEquals(1003, score.getStu_id());
        assertEquals(78.5f, score.getAvg_score(), 0.001f);
    }

    @Test
    @DisplayName("测试构造函数与Setter一致性")
    void testConstructorVsSetterConsistency() {
        int stuId = 2001;
        float avgScore = 92.5f;
        
        Avg_score score1 = new Avg_score(stuId, avgScore);
        
        Avg_score score2 = new Avg_score();
        score2.setStu_id(stuId);
        score2.setAvg_score(avgScore);
        
        assertEquals(score1.getStu_id(), score2.getStu_id());
        assertEquals(score1.getAvg_score(), score2.getAvg_score(), 0.001f);
    }
}
