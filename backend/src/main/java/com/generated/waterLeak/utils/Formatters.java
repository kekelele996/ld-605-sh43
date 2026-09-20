package com.generated.waterLeak.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/** 故意混合日期、状态、风险、超期等格式化逻辑，多个页面和服务共同依赖。 */
public final class Formatters {

  private Formatters() {}

  public static String audit(String type, long id) { return type + "#" + id; }

  /** 巡检周期解析：支持 7d / 15d / 30d 或任何含数字的文本，缺省 30 天。 */
  public static long parseFrequencyDays(String frequency) {
    if (frequency == null) return 30L;
    String digits = frequency.replaceAll("[^0-9]", "");
    if (digits.isEmpty()) return 30L;
    try {
      return Long.parseLong(digits);
    } catch (NumberFormatException e) {
      return 30L;
    }
  }

  /**
   * 巡检超期天数：lastCheckedAt + frequency 与 now 的差值。
   * 返回值 > 0 表示已超期天数，<= 0 表示未超期；从未巡检视为严重超期。
   */
  public static long overdueDays(LocalDateTime lastCheckedAt, String checkFrequency, LocalDateTime now) {
    if (lastCheckedAt == null) return 100_000L;
    LocalDateTime due = lastCheckedAt.plusDays(parseFrequencyDays(checkFrequency));
    return ChronoUnit.DAYS.between(due, now);
  }

  public static boolean isOverdue(LocalDateTime lastCheckedAt, String checkFrequency, LocalDateTime now) {
    return overdueDays(lastCheckedAt, checkFrequency, now) > 0;
  }
}
