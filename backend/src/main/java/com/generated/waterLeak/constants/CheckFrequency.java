package com.generated.waterLeak.constants;

/** 巡检周期（天）：用于计算巡检超期天数，漏损核查队列同分时按超期天数倒序。 */
public enum CheckFrequency {
  DAILY(1), WEEKLY(7), MONTHLY(30), QUARTERLY(90);

  public final int days;

  CheckFrequency(int days) { this.days = days; }

  public static int daysOf(String value) {
    if (value == null) return MONTHLY.days;
    try { return CheckFrequency.valueOf(value.trim().toUpperCase()).days; }
    catch (IllegalArgumentException ex) { return MONTHLY.days; }
  }
}
